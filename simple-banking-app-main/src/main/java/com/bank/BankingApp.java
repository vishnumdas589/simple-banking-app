package com.bank;

import exception.*;

import model.Account;
import model.Account.AccountType;
import model.Customer;
import repository.AccountRepository;
import repository.TransactionRepository;
import service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.List;

public class App {
	private final AccountService accountService;
	private final TransactionService txService;
	private final BankStatementService stmtService;
	private final CustomerService custService;
	private final Scanner sc = new Scanner(System.in);

	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public App() {
		AccountRepository ar = new AccountRepository();
		TransactionRepository tr = new TransactionRepository();
		this.accountService = new AccountService(ar);
		this.txService = new TransactionService(ar, tr);
		this.stmtService = new BankStatementService(tr);
		this.custService = new CustomerService();
	}

	private void menu() {
		System.out.println("\n=== Simple Banking Application ===");
		System.out.println("1) Create Customer");
		System.out.println("2) Create Account");
		System.out.println("3) Deposit");
		System.out.println("4) Withdraw");
		System.out.println("5) Transfer");
		System.out.println("6) Statement");
		System.out.println("7) Delete Account");
		System.out.println("8) View Customer");
		System.out.println("9) Exit");
		System.out.print("Choose an option: ");
	}

	public void start() {
		while (true) {
			menu();
			String choice = sc.nextLine().trim();
			try {
				switch (choice) {
				case "1": createCustomer(); break;
				case "2": createAccount(); break;
				case "3": deposit(); break;
				case "4": withdraw(); break;
				case "5": transfer(); break;
				case "6": statement(); break;
				case "7": deleteAccount(); break;
				case "8": viewCustomer(); break;
				case "9":
					out("Thanks for visiting Simple Banking");
					return;
				default:
					out("Invalid choice !");
				}
			} catch (Exception e) {
				err(e.getMessage());
			}
		}
	}

	private void createCustomer() {
		System.out.print("Cust ID: ");
		String cid = sc.nextLine().trim();
		System.out.print("Name: ");
		String name = sc.nextLine().trim();
		System.out.print("Email: ");
		String email = sc.nextLine().trim();
		System.out.print("Phone: ");
		String phone = sc.nextLine().trim();

		custService.createCustomer(new Customer(cid, name, email, phone));
		out("Customer created successfully! ID: " + cid);
	}

	private void createAccount() {
		try {
			System.out.print("Cust ID: ");
			String cid = sc.nextLine().trim();
			System.out.print("Type (SAVINGS/CHECKING): ");
			AccountType type = AccountType.valueOf(sc.nextLine().trim().toUpperCase());
			System.out.print("Initial amount: ");
			BigDecimal init = new BigDecimal(sc.nextLine().trim());

			String accNo = "ACC" + System.currentTimeMillis();
			accountService.createAccount(new Account(accNo, cid, type, init));

			out("Account created successfully! Account No: " + accNo);
			BigDecimal current = accountService.getBalance(accNo);
			out("Current balance: " + current);
		} catch (DuplicateAccountException e) {
			err(e.getMessage());
		} catch (IllegalArgumentException e) {
			err("Bad input (type or number).");
		} catch (Exception e) {
			err(e.getMessage());
		}
	}

	private void deposit() {
		try {
			System.out.print("Acc No: ");
			String acc = sc.nextLine().trim();
			System.out.print("Amount: ");
			BigDecimal amt = new BigDecimal(sc.nextLine().trim());

			txService.deposit(acc, amt);
			BigDecimal current = accountService.getBalance(acc);
			out("Deposited successfully! Current balance: " + current);
		} catch (AccountNotFoundException | InvalidAmountException e) {
			err(e.getMessage());
		} catch (NumberFormatException e) {
			err("Bad number format.");
		}
	}

	private void withdraw() {
		try {
			System.out.print("Acc No: ");
			String acc = sc.nextLine().trim();
			System.out.print("Amount: ");
			BigDecimal amt = new BigDecimal(sc.nextLine().trim());

			txService.withdraw(acc, amt);
			BigDecimal current = accountService.getBalance(acc);
			out("Withdraw successful! Current balance: " + current);
		} catch (AccountNotFoundException | InvalidAmountException | InsufficientFundsException e) {
			err(e.getMessage());
		} catch (NumberFormatException e) {
			err("Bad number format.");
		}
	}

	private void transfer() {
		try {
			System.out.print("From Acc: ");
			String from = sc.nextLine().trim();
			System.out.print("To Acc: ");
			String to = sc.nextLine().trim();
			System.out.print("Amount: ");
			BigDecimal amt = new BigDecimal(sc.nextLine().trim());

			txService.transfer(from, to, amt);

			BigDecimal fromBal = accountService.getBalance(from);

			out("Transfer successful!");
			out("Current balance (From " + from + "): " + fromBal);
		} catch (AccountNotFoundException | InvalidAmountException | InsufficientFundsException e) {
			err(e.getMessage());
		} catch (NumberFormatException e) {
			err("Bad number format.");
		}
	}

	private void statement() {
		System.out.print("Acc No: ");
		String acc = sc.nextLine().trim();

		try {
			accountService.getAccount(acc); // validate exists
		} catch (AccountNotFoundException e) {
			err(e.getMessage());
			return;
		}

		System.out.println("Enter date/time in format yyyy-MM-dd HH:mm:ss (dates are required; no defaults)");
		LocalDateTime from = readDateRequired("From: ");
		LocalDateTime to = readDateRequired("To  : ");

		if (from.isAfter(to)) {
			err("From date must be before To date.");
			return;
		}

		LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
		if (from.isBefore(sixMonthsAgo)) {
			err("Cannot fetch data older than 6 months.");
			return;
		}

		var list = stmtService.generateStatement(acc, from, to);
		if (list.isEmpty()) {
			out("No transactions found in this range.");
		} else {
			for (var t : list) {
				System.out.printf("%s | %s | %s | rel:%s%n",
						t.getTime().format(DF),
						t.getType(),
						t.getAmount(),
						t.getRelatedAcc() == null ? "-" : t.getRelatedAcc());
			}
			out("Statement generated successfully!");
		}
	}

	private void deleteAccount() {
		System.out.print("Acc No: ");
		String acc = sc.nextLine().trim();
		try {
			accountService.deleteAccount(acc);
			out("Account deleted successfully! Account No: " + acc);
		} catch (AccountNotFoundException e) {
			err(e.getMessage());
		}
	}

	
	private void viewCustomer() {
		System.out.print("Cust ID: ");
		String id = sc.nextLine().trim();
		var c = custService.getCustomer(id);
		if (c == null) {
			out("Customer not found.");
			return;
		}

		
		String header = "[" + ts() + "]";
		System.out.println(header + " Id: " + c.getCustomerId());
		System.out.println("       Name: " + c.getName());
		System.out.println("       Email: " + c.getEmail());
		System.out.println("       Phone: " + c.getPhone());

		System.out.print("Show which account type? (SAVINGS/CHECKING/ALL) [ALL]: ");
		String sel = sc.nextLine().trim();
		if (sel.isEmpty()) sel = "ALL";
		sel = sel.toUpperCase();

		switch (sel) {
		case "SAVINGS":
		case "CHECKING":
			try {
				AccountType type = AccountType.valueOf(sel);
				Account a = accountService.getAccountForCustomerAndType(id, type);
				System.out.println("       Account Number (" + type + "): " + a.getAccountNumber());
				System.out.println("       Current balance (" + type + "): " + a.getBalance());
			} catch (IllegalArgumentException e) {
				System.out.println("       Invalid type. Showing ALL.");
				List<Account> accounts = accountService.getAccountsForCustomer(id);
				if (accounts == null || accounts.isEmpty()) {
					System.out.println("       Account Numbers: -");
				} else {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < accounts.size(); i++) {
						if (i > 0) sb.append(", ");
						sb.append(accounts.get(i).getAccountNumber())
						  .append(" (").append(accounts.get(i).getType()).append(")");
					}
					System.out.println("       Account Numbers: " + sb);
				}
			} catch (AccountNotFoundException e) {
				System.out.println("       Account not found or deleted for type: " + sel);
			}
			break;
		case "ALL":
			{
				List<Account> accounts = accountService.getAccountsForCustomer(id);
				if (accounts == null || accounts.isEmpty()) {
					System.out.println("       Account Numbers: -");
				} else {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < accounts.size(); i++) {
						if (i > 0) sb.append(", ");
						sb.append(accounts.get(i).getAccountNumber())
						  .append(" (").append(accounts.get(i).getType()).append(")");
					}
					System.out.println("       Account Numbers: " + sb);
				}
			}
			break;
		default:
			System.out.println("       Invalid selection. Showing ALL.");
			{
				List<Account> accounts = accountService.getAccountsForCustomer(id);
				if (accounts == null || accounts.isEmpty()) {
					System.out.println("       Account Numbers: -");
				} else {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < accounts.size(); i++) {
						if (i > 0) sb.append(", ");
						sb.append(accounts.get(i).getAccountNumber())
						  .append(" (").append(accounts.get(i).getType()).append(")");
					}
					System.out.println("       Account Numbers: " + sb);
				}
			}
		}
	}


	private void showAllAccounts(String customerId) {
		List<Account> accounts = accountService.getAccountsForCustomer(customerId);
		if (accounts == null || accounts.isEmpty()) {
			out("Account Numbers: -");
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < accounts.size(); i++) {
				if (i > 0) sb.append(", ");
				sb.append(accounts.get(i).getAccountNumber())
				.append(" (").append(accounts.get(i).getType()).append(")");
			}
			out("Account Numbers: " + sb);
		}
	}

	private LocalDateTime readDateRequired(String prompt) {
		while (true) {
			System.out.print(prompt);
			String in = sc.nextLine().trim();
			if (in.isEmpty()) {
				out("This field is required. Please enter a date/time like 2025-09-20 00:34:00");
				continue;
			}
			try {
				return LocalDateTime.parse(in, DF);
			} catch (DateTimeParseException e) {
				err("Bad date format. Use yyyy-MM-dd HH:mm:ss (e.g., 2025-09-20 00:34:00)");
			}
		}
	}

	private String ts() { return LocalDateTime.now().format(DF); }
	private void out(String msg) { System.out.println("[" + ts() + "] " + msg); }
	private void err(String msg) { System.out.println("[" + ts() + "] ERROR: " + msg); }

	public static void main(String[] args) {
		new App().start();
	}
}
