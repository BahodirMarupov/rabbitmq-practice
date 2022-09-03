package jmp.rabbitmq.controller;

import jmp.rabbitmq.model.dto.ReceiptDto;
import jmp.rabbitmq.publisher.Publisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

  private final Publisher publisher;

  public ReceiptController(Publisher publisher) {
    this.publisher = publisher;
  }

  @PostMapping
  public String sendReceipt(@RequestBody ReceiptDto receiptDto) {
    return publisher.publish(receiptDto);
  }

}
