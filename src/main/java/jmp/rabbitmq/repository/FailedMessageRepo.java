package jmp.rabbitmq.repository;

import java.util.List;
import jmp.rabbitmq.model.entity.FailedMessage;
import org.springframework.data.repository.CrudRepository;

public interface FailedMessageRepo extends CrudRepository<FailedMessage, Long> {

  List<FailedMessage> findAll();

}
