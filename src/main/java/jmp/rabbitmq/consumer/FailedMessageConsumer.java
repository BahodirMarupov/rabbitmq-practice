package jmp.rabbitmq.consumer;

import java.util.function.Consumer;
import jmp.rabbitmq.model.entity.FailedMessage;
import jmp.rabbitmq.repository.FailedMessageRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;

@Slf4j
public class FailedMessageConsumer implements Consumer<Message<FailedMessage>> {

  @Autowired
  private FailedMessageRepo failedMessageRepo;

  private final String consumerName;
  private final StreamBridge streamBridge;

  private static final String ROUTING_KEY_HEADER = "myRoutingKey";

  public FailedMessageConsumer(String consumerName, StreamBridge streamBridge) {
    this.consumerName = consumerName;
    this.streamBridge = streamBridge;
  }

  @Override
  public void accept(Message<FailedMessage> message) {
    log.info("{} , payload={} , headers={}", consumerName, message.getPayload(), message.getHeaders());
    FailedMessage failedMessageToSave = message.getPayload();
    failedMessageRepo.save(failedMessageToSave);
  }
}
