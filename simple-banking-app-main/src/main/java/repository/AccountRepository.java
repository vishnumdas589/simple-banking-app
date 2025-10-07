package repository;

import model.Account;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class AccountRepository {
	private final Map<String, Account> accounts = new HashMap<>();

	public void save(Account acc) {
		accounts.put(acc.getAccountNumber(), acc);
	}

	public Account find(String accNo) {
		return accounts.get(accNo);
	}

	public boolean exists(String accNo) {
		return accounts.containsKey(accNo);
	}

	public void delete(String accNo) {
		accounts.remove(accNo);
	}

	public List<Account> findByCustomer(String custId) {
		List<Account> res = new ArrayList<>();
		for (Account a : accounts.values()) {
			if (a.getCustomerId().equals(custId)) {
				res.add(a);
			}
		}
		return res;
	}

	public int countByCustomer(String custId) {
		int count = 0;
		for (Account a : accounts.values()) {
			if (a.getCustomerId().equals(custId)) {
				count++;
			}
		}
		return count;
	}

	public boolean customerHasAccountOfType(String custId, Account.AccountType type) {
		for (Account a : accounts.values()) {
			if (a.getCustomerId().equals(custId) && a.getType() == type) {
				return true;
			}
		}
		return false;
	}

	public Account findByCustomerAndType(String custId, Account.AccountType type) {
		for (Account a : accounts.values()) {
			if (a.getCustomerId().equals(custId) && a.getType() == type) {
				return a;
			}
		}
		return null;
	}
}
