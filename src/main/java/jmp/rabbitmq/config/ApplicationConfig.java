package jmp.rabbitmq.config;

import java.util.function.Consumer;
import jmp.rabbitmq.consumer.DeadLetterConsumer;
import jmp.rabbitmq.consumer.FailedMessageConsumer;
import jmp.rabbitmq.consumer.ReceiptConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DeclarableCustomizer;
import org.springframework.amqp.core.Queue;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ApplicationConfig {

  @Bean
  public Consumer queue1Sink(StreamBridge streamBridge) {
    return in -> safeSleep(20000);
//    return new ReceiptConsumer("Consumer1", streamBridge);
  }

  @Bean
  public ReceiptConsumer queue2Sink(StreamBridge streamBridge) {
    return new ReceiptConsumer("Consumer2", streamBridge);
  }

  @Bean
  public FailedMessageConsumer failedSink(StreamBridge streamBridge) {
    return new FailedMessageConsumer("FM_Consumer", streamBridge);
  }

  @Bean
  public DeadLetterConsumer deadLetterSink(StreamBridge streamBridge) {
    return new DeadLetterConsumer("DL_CONSUMER", streamBridge);
  }

  @Bean
  public DeclarableCustomizer declarableCustomizer() {
    return declarable -> {
      if (declarable instanceof Queue) {
        var queue = (Queue) declarable;
        if (queue.getName().equals("demo-queue1")
            || queue.getName().equals("demo-queue2")) {
          queue.removeArgument("x-dead-letter-exchange");
          queue.removeArgument("x-dead-letter-routing-key");

          queue.addArgument("x-dead-letter-exchange", "deadLetter-exchange");
        }
      }
      return declarable;
    };
  }

  private static void safeSleep(int duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException ignore) {
      log.error(ignore.getMessage());
    }
  }
}
