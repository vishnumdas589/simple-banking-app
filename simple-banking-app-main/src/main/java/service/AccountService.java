package service;

import exception.*;
import model.Account;
import repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {
	private final AccountRepository repo;

	public AccountService(AccountRepository repo) { this.repo = repo; }

	public void createAccount(Account acc) throws DuplicateAccountException {
		if (repo.exists(acc.getAccountNumber())) {
			throw new DuplicateAccountException("Acc already exists: " + acc.getAccountNumber());
		}
		int current = repo.countByCustomer(acc.getCustomerId());
		if (current >= 2) {
			throw new DuplicateAccountException("Customer already has max accounts (2): " + acc.getCustomerId());
		}
		if (repo.customerHasAccountOfType(acc.getCustomerId(), acc.getType())) {
			throw new DuplicateAccountException("Customer already has " + acc.getType() + ": " + acc.getCustomerId());
		}
		repo.save(acc);
	}

	public Account getAccount(String accNo) throws AccountNotFoundException {
		Account a = repo.find(accNo);
		if (a == null) throw new AccountNotFoundException("Acc not found: " + accNo);
		return a;
	}

	public void deleteAccount(String accNo) throws AccountNotFoundException {
		if (!repo.exists(accNo)) throw new AccountNotFoundException("Acc not found: " + accNo);
		repo.delete(accNo);
	}

	public BigDecimal getBalance(String accNo) throws AccountNotFoundException {
		return getAccount(accNo).getBalance();
	}

	public List<Account> getAccountsForCustomer(String custId) {
		return repo.findByCustomer(custId);
	}

	public Account getAccountForCustomerAndType(String custId, Account.AccountType type)
			throws AccountNotFoundException {
		Account a = repo.findByCustomerAndType(custId, type);
		if (a == null) throw new AccountNotFoundException("No " + type + " account for: " + custId);
		return a;
	}
}
