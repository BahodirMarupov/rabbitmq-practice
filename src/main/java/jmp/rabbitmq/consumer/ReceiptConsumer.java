package jmp.rabbitmq.consumer;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import jmp.rabbitmq.model.dto.ReceiptDto;
import jmp.rabbitmq.model.entity.FailedMessage;
import jmp.rabbitmq.model.enumuration.MessageStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Slf4j
public class ReceiptConsumer implements Consumer<Message<ReceiptDto>> {

  private final String consumerName;
  private final StreamBridge streamBridge;

  private static final String ROUTING_KEY_HEADER = "myRoutingKey";

  public ReceiptConsumer(String consumerName, StreamBridge streamBridge) {
    this.consumerName = consumerName;
    this.streamBridge = streamBridge;
  }

  @Override
  public void accept(Message<ReceiptDto> message) {
    log.info("{} , payload={} , headers={}", consumerName, message.getPayload(), message.getHeaders());

    var deathHeader = message.getHeaders().get("x-death", List.class);
    var death = deathHeader != null && deathHeader.size() > 0
        ? (Map<String, Object>)deathHeader.get(0)
        : null;
    if (death != null && (long) death.get("count") > 2) {
      ReceiptDto receiptDto = message.getPayload();
      FailedMessage failedMessage = FailedMessage.builder()
          .payload(receiptDto.toString())
          .status(MessageStatus.ARCHIVED)
          .originQueue(message.getHeaders().get(ROUTING_KEY_HEADER).toString())
          .reason("Failed after 3 attempts -> throw RuntimeException!!")
          .build();
      streamBridge.send("failed-out-0",
          MessageBuilder
              .withPayload(failedMessage)
              .build());
      throw new ImmediateAcknowledgeAmqpException("Failed after 3 attempts");
    }
    throw new AmqpRejectAndDontRequeueException("failed");
  }
}
