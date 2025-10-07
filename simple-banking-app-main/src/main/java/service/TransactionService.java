package service;

import exception.*;
import model.Account;
import model.Transaction;
import model.Transaction.TxType;
import repository.AccountRepository;
import repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionService {
	private final AccountRepository repo;
	private final TransactionRepository txRepo;

	public TransactionService(AccountRepository repo, TransactionRepository txRepo) {
		this.repo = repo;
		this.txRepo = txRepo;
	}

	private static boolean isPositive(BigDecimal amt) {
		return amt != null && amt.compareTo(BigDecimal.ZERO) > 0;
	}

	private Account mustGet(String accNo) throws AccountNotFoundException {
		Account a = repo.find(accNo);
		if (a == null) throw new AccountNotFoundException("Acc not found: " + accNo);
		return a;
	}

	public void deposit(String accNo, BigDecimal amt)
			throws InvalidAmountException, AccountNotFoundException {
		if (!isPositive(amt)) throw new InvalidAmountException("Deposit must be > 0");
		Account a = mustGet(accNo);
		a.setBalance(a.getBalance().add(amt));
		repo.save(a);
		txRepo.save(new Transaction(UUID.randomUUID().toString(), accNo, TxType.DEPOSIT, amt, null));
	}

	public void withdraw(String accNo, BigDecimal amt)
			throws InvalidAmountException, InsufficientFundsException, AccountNotFoundException {
		if (!isPositive(amt)) throw new InvalidAmountException("Withdraw must be > 0");
		Account a = mustGet(accNo);
		if (a.getBalance().compareTo(amt) < 0) throw new InsufficientFundsException("Not enough funds");
		a.setBalance(a.getBalance().subtract(amt));
		repo.save(a);
		txRepo.save(new Transaction(UUID.randomUUID().toString(), accNo, TxType.WITHDRAWAL, amt, null));
	}

	public void transfer(String from, String to, BigDecimal amt)
			throws InvalidAmountException, InsufficientFundsException, AccountNotFoundException {
		if (!isPositive(amt)) throw new InvalidAmountException("Transfer must be > 0");
		if (from.equals(to)) throw new InvalidAmountException("Can't transfer to same account");

		Account f = mustGet(from);
		Account t = mustGet(to);

		if (f.getBalance().compareTo(amt) < 0) throw new InsufficientFundsException("Not enough funds");

		
		f.setBalance(f.getBalance().subtract(amt));
		t.setBalance(t.getBalance().add(amt));
		repo.save(f);
		repo.save(t);

		txRepo.save(new Transaction(UUID.randomUUID().toString(), from, TxType.TRANSFER, amt, to));
		txRepo.save(new Transaction(UUID.randomUUID().toString(), to,   TxType.TRANSFER, amt, from));
	}
}
