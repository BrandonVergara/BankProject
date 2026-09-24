package api;

import domain.Account;
import domain.Transaction;
import service.AccountService;
import service.TransactionService;
import java.util.Scanner;
import java.util.List;
import java.math.BigDecimal;

public class BankRepl {
	
	private final AccountService accService;
	private final TransactionService traService;
	private final Scanner in = new Scanner(System.in);
	private Account user = null;
	
	
	public BankRepl(AccountService accService, TransactionService traService) {
		this.accService = accService;
		this.traService = traService;
	}
	
	public void run() {
		logInHelp();
		
		while(true) {
			System.out.print("> ");
		
			String command = in.nextLine().trim();
		
			if(command.equals("exit")) {
				return;
			}
		
			try {
				handle(command);
			}catch(IllegalArgumentException e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}
	
	private void handle(String command) {
		if(user == null) {
			logInHandle(command);
		}
		if(user != null) {
			logHandle(command);
		}
	}
	
	private void logInHandle(String command) {
		switch(command){
			case "login" -> login();
			case "register" -> register();
			default -> System.out.println("Unknown command");
		}
	}
	
	private void login() {
		long id = readLong("Account Id: ");
		System.out.print("Pin: ");
		String pin = in.nextLine().trim();
		
		try {
			user = accService.login(id, pin);
			System.out.println("Welcome back");
			System.out.println();
			printHelp();
		} catch(IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
		
	}
	
	private void register() {
		System.out.print("Pin: ");
		String pin = in.nextLine().trim();
		
		try {
			user = accService.register(pin);
			System.out.println("Account created successfully.");
		} catch(IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	private void logHandle(String command) {
		switch(command) {
		case "balance" -> checkBalance();
		case "transaction" -> makeTransaction();
		case "history" -> getHistory();
		case "delete" -> deleteAccount();
		case "help" -> printHelp();
		case "logout" -> logOut();
		default -> System.out.println("Unknown command");
		}
	}
	
	private void checkBalance() {
		Account current = accService.findAccount(user.getId());
		System.out.println("Balance: $" + current.getBalance().setScale(2));
	}
	
	private void makeTransaction() {
		System.out.println("Enter what kind of transaction you would like to make");
		System.out.println("deposit - Deposit money into account");
		System.out.println("withdraw - Withdraw money from account");
		System.out.println("transfer - Transfer money into another account");
		String command = in.nextLine().trim();
		long id = user.getId();
		switch(command){
			case "deposit" -> makeDeposit(id);
			case "withdraw" -> makeWithdraw(id);
			case "transfer" -> makeTransfer(id);
			default -> System.out.println("Unknown transaction");
		}
	}
	
	private void makeDeposit(long id) {
		BigDecimal amount = readAmount("Deposit amount: ");
		
		try {
			traService.makeDeposit(id, amount);
			System.out.println("Deposit successful.");
		} catch(IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	private void makeWithdraw(long id) {
		BigDecimal amount = readAmount("Withdraw amount: ");
		
		try {
			traService.makeWithdraw(id, amount);
			System.out.println("Withdraw successful.");
		} catch(IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	private void makeTransfer(long id) {
		
		long desId = readLong("Transfer to account: ");
		BigDecimal amount = readAmount("Transfer amount: ");
		
		try {
			traService.makeTransfer(id, desId, amount);
			System.out.println("Transfer successful.");
		} catch(IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	
	private void getHistory() {
		List<Transaction> transactions = traService.getHistory(user.getId());
		
		if(transactions.isEmpty()) {
			System.out.println("No transactions made.");
			return;
		}
		transactions.forEach(this::printTransaction);
	}
	
	private void printTransaction(Transaction t) {
		String base = t.getTimeStamp() + " | " + t.getType() + " | $" + t.getAmount() + " | " + t.getDescription();
		switch(t.getType()) {
		case TRANSFER -> System.out.println(base + " | From: " + t.getSourceId() + " To: " + t.getDestinationId());
		default -> System.out.println(base);
		}
	}
	
	private void deleteAccount() {
		try {
			accService.deleteAccount(user.getId());
			System.out.println("Account deleted succesfully");
			user = null;
			logInHelp();
		} catch(IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	private void logOut() {
		user = null;
		logInHelp();
	}
	
	private void logInHelp() {
		System.out.println("Please enter a command: ");
		System.out.println("login - Log in to your account");
		System.out.println("register - Create a new account");
		System.out.println("exit - Close banking session");
	}
	
	private void printHelp() {
		System.out.println("Available commands:");
		System.out.println("balance - Check available balance");
		System.out.println("transaction - Make Transaction");
		System.out.println("history - Check recent transaction history");
		System.out.println("delete - Delete your account");
		System.out.println("help - Show this help message");
		System.out.println("logout - Log out of account");
		System.out.println("exit - Close banking session");
		
	}
	
	private BigDecimal readAmount(String prompt) {
		System.out.print(prompt);
		return new BigDecimal(in.nextLine().trim());
	}
	
	private long readLong(String prompt) {
		System.out.print(prompt);
		return Long.parseLong(in.nextLine().trim());
	}
}
