package api;

import domain.Account;
import service.AccountService;
import java.util.Scanner;

public class BankRepl {
	
	private final AccountService service;
	private final Scanner in = new Scanner(System.in);
	private Account user = null;
	
	
	public BankRepl(AccountService service) {
		this.service = service;
	}
	
	public void run() {
		System.out.println("Please enter a command: ");
		System.out.println("login - Log in to your account");
		System.out.println("register - Create a new account");
		System.out.println("exit - Close banking session");
		
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
		long id = readLong("Account ID: ");
		System.out.println("Pin ");
		String pin = in.nextLine().trim();
		
		try {
			user = service.login(id, pin);
			System.out.println("Welcome back");
		} catch(IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
		
	}
	
	private void register() {
		System.out.println("Pin: ");
		String pin = in.nextLine().trim();
		
		try {
			user = service.register(pin);
			System.out.println("Account created successfully.");
		} catch(IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	private long readLong(String prompt) {
		System.out.print(prompt);
		return Long.parseLong(in.nextLine().trim());
	}
	
	private void logHandle(String command) {
		switch(command) {
		case "help" -> printHelp();
		default -> System.out.println("Unknown command");
		}
	}
	
	

	private void printHelp() {
		System.out.println("Available commands:");
		System.out.println("balance - Check available balance");
		System.out.println("transaction - Make Transaction");
		System.out.println("history - Check recent transaction history");
		System.out.println("update - Update your account");
		System.out.println("delete - Delete your account");
		System.out.println("help - Show this help message");
		System.out.println("logout - Log out of account");
		System.out.println("exit - Close banking session");
		
	}
}
