package jmp.rabbitmq.service;

import java.util.List;
import jmp.rabbitmq.model.entity.FailedMessage;
import jmp.rabbitmq.model.enumuration.MessageStatus;
import jmp.rabbitmq.repository.FailedMessageRepo;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class MessageTriageService {

  private final FailedMessageRepo failedMessageRepo;
  private final StreamBridge streamBridge;
  private static final String ROUTING_KEY_HEADER = "myRoutingKey";

  public MessageTriageService(FailedMessageRepo failedMessageRepo, StreamBridge streamBridge) {
    this.failedMessageRepo = failedMessageRepo;
    this.streamBridge = streamBridge;
  }

  public List<FailedMessage> getAll() {
    return failedMessageRepo.findAll();
  }

  public String returnFailedMessage(Long id) {
    FailedMessage failedMessage = failedMessageRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Record not found!"));
    failedMessage.setStatus(MessageStatus.RETURNED);
    streamBridge.send("source-out-0",
        MessageBuilder
            .withPayload(failedMessage)
            .setHeader(ROUTING_KEY_HEADER, failedMessage.getOriginQueue())
            .build());
    failedMessageRepo.save(failedMessage);
    return "returned";
  }

  public void delete(Long id) {
    FailedMessage failedMessage = failedMessageRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Record not found!"));
    failedMessage.setStatus(MessageStatus.DISCARDED);
    failedMessageRepo.save(failedMessage);
  }
}
