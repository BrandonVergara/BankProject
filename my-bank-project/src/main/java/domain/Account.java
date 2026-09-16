package domain;

public class Account {
	
	private int id;
	private String pin;
	private String accountType;
	private double balance;
	
	public Account(int id, String pin, String accountType, double balance) {
		this.id = id;
		this.pin = pin;
		this.accountType = accountType;
		this.balance = balance;
	}
	
	public int getId() {
		return id;
	}
	
	public String getPin() {
		return pin;
	}
	
	public String getAccountType() {
		return accountType;
	}
	
	public double getBalance() {
		return balance;
	}

}
