package domain;

import java.time.LocalDateTime;

public class Transaction {
	
	private int id;
	private String type;
	private double amount;
	private LocalDateTime timeStamp;
	private int sourceId;
	private int destinationId;
	private String description;
	
	public Transaction(int id, String type, double amount, LocalDateTime timeStamp,
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
	
	public String getType() {
		return type;
	}
	
	public double getAmount() {
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
