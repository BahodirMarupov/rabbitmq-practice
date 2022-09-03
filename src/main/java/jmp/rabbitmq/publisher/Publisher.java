package jmp.rabbitmq.publisher;

import jmp.rabbitmq.model.dto.ReceiptDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class Publisher {

  @Autowired
  StreamBridge streamBridge;

  private static final String ROUTING_KEY_HEADER = "myRoutingKey";

  public String publish(ReceiptDto receiptDto){
    streamBridge.send("source-out-0",
        MessageBuilder
            .withPayload(receiptDto)
            .setHeader(ROUTING_KEY_HEADER, "routing-queue1")
            .build());
    streamBridge.send("source-out-0",
        MessageBuilder
            .withPayload(receiptDto)
            .setHeader(ROUTING_KEY_HEADER, "routing-queue2")
            .build());
    return "Message sent";
  }

}
