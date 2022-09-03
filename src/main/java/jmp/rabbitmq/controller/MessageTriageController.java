package jmp.rabbitmq.controller;

import java.util.List;
import jmp.rabbitmq.model.entity.FailedMessage;
import jmp.rabbitmq.service.MessageTriageService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/failed-messages")
public class MessageTriageController {
  private final MessageTriageService triageService;

  public MessageTriageController(MessageTriageService triageService) {
    this.triageService = triageService;
  }

  @GetMapping
  public List<FailedMessage> getAll() {
    return triageService.getAll();
  }

  @PostMapping
  public String returnFailedMessage(@RequestParam Long id) {
    return triageService.returnFailedMessage(id);
  }

  @DeleteMapping
  public void delete(@RequestParam Long id){
    triageService.delete(id);
  }

}
