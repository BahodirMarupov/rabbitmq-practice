package jmp.rabbitmq.model.dto;

public class ReceiptDto {
  private Long id;
  private Double sum;
  private Long createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getSum() {
    return sum;
  }

  public void setSum(Double sum) {
    this.sum = sum;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "ReceiptDto{" +
        "id=" + id +
        ", sum=" + sum +
        ", createdAt=" + createdAt +
        '}';
  }
}
