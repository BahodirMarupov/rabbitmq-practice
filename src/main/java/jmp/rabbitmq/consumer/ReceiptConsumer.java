package jmp.rabbitmq.consumer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import jmp.rabbitmq.model.dto.ReceiptDto;
import jmp.rabbitmq.model.entity.FailedMessage;
import jmp.rabbitmq.model.enumuration.MessageStatus;
import lombok.extern.slf4j.Slf4j;
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
    AtomicInteger attempts = (AtomicInteger) message.getHeaders().get("deliveryAttempt");
    if (attempts.get() > 3) {
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
      throw new ImmediateAcknowledgeAmqpException("Failed after 4 attempts");
    }
    throw new RuntimeException();

  }
}
