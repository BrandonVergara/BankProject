package domain;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Transaction {
	
	private Long id;
	private TransactionType type;
	private BigDecimal amount;
	private LocalDateTime timeStamp;
	private Long sourceId;
	private Long destinationId;
	private String description;
	
	public Transaction(Long id, TransactionType type, BigDecimal amount, LocalDateTime timeStamp,
			Long sourceId, Long destinationID, String description) {
		this.id = id;
		this.type = type;
		this.amount = amount;
		this.timeStamp = timeStamp;
		this.sourceId = sourceId;
		this.destinationId = destinationID;
		this.description = description;
	}
	
	public Long getId() {
		return id;
	}
	
	public TransactionType getType() {
		return type;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}
	
	public Long getSourceId() {
		return sourceId;
	}
	
	public Long getDestinationId() {
		return destinationId;
	}
	
	public String getDescription() {
		return description;
	}
	
}
