package jmp.rabbitmq.consumer;

import static org.springframework.cloud.stream.binder.rabbit.RabbitExpressionEvaluatingInterceptor.ROUTING_KEY_HEADER;

import java.util.function.Consumer;
import jmp.rabbitmq.model.dto.ReceiptDto;
import jmp.rabbitmq.model.entity.FailedMessage;
import jmp.rabbitmq.model.enumuration.MessageStatus;
import jmp.rabbitmq.repository.FailedMessageRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;

@Slf4j
public class DeadLetterConsumer implements Consumer<Message<ReceiptDto>> {

  @Autowired
  private FailedMessageRepo failedMessageRepo;

  private final String consumerName;
  private final StreamBridge streamBridge;

  public DeadLetterConsumer(String consumerName, StreamBridge streamBridge) {
    this.consumerName = consumerName;
    this.streamBridge = streamBridge;
  }

  @Override
  public void accept(Message<ReceiptDto> message) {
    log.info("{} , payload={} , headers={}", consumerName, message.getPayload(), message.getHeaders());
    FailedMessage failedMessage = FailedMessage.builder()
        .payload(message.getPayload().toString())
        .status(MessageStatus.ARCHIVED)
        .originQueue(message.getHeaders().computeIfAbsent(ROUTING_KEY_HEADER, s -> "").toString())
        .reason("Consumed from dead letter queue!!!")
        .build();
    failedMessageRepo.save(failedMessage);
  }
}
