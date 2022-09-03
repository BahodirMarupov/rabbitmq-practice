package jmp.rabbitmq.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import jmp.rabbitmq.model.enumuration.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FailedMessage {

  @Id
  @GeneratedValue
  private Long id;

  @Column
  private String payload;

  @Column
  @Enumerated(value = EnumType.STRING)
  private MessageStatus status;

  @Column
  private String originQueue;

  @Column
  private String reason;

  @Column
  private Long createdAt;

}
