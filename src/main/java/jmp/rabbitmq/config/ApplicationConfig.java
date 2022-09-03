package jmp.rabbitmq.config;

import jmp.rabbitmq.consumer.FailedMessageConsumer;
import jmp.rabbitmq.consumer.ReceiptConsumer;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean
  public ReceiptConsumer queue1Sink(StreamBridge streamBridge) {
    return new ReceiptConsumer("Consumer1", streamBridge);
  }

  @Bean
  public ReceiptConsumer queue2Sink(StreamBridge streamBridge) {
    return new ReceiptConsumer("Consumer2", streamBridge);
  }

  @Bean
  public FailedMessageConsumer failedSink(StreamBridge streamBridge) {
    return new FailedMessageConsumer("FM_Consumer", streamBridge);
  }

}
