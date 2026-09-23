package domain;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Transaction {
	
	private int id;
	private TransactionType type;
	private BigDecimal amount;
	private LocalDateTime timeStamp;
	private int sourceId;
	private int destinationId;
	private String description;
	
	public Transaction(int id, TransactionType type, BigDecimal amount, LocalDateTime timeStamp,
			int sourceId, int destinationID, String description) {
		this.id = id;
		this.type = type;
		this.amount = amount;
		this.timeStamp = timeStamp;
		this.sourceId = sourceId;
		this.destinationId = destinationID;
		this.description = description;
	}
	
	public int getId() {
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
	
	public int getSourceId() {
		return sourceId;
	}
	
	public int getDestinationID() {
		return destinationId;
	}
	
	public String getDescription() {
		return description;
	}
	
}
