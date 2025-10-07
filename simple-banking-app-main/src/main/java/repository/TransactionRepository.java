package repository;


import model.Transaction;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionRepository {
	private final List<Transaction> txs = new ArrayList<>();

	public void save(Transaction tx) {
		txs.add(tx);
	}

	public List<Transaction> findByAcc(String accNo) {
		List<Transaction> res = new ArrayList<>();
		for (Transaction t : txs) {
			if (t.getAccNo().equals(accNo)) res.add(t);
		}
		return res;
	}

	public List<Transaction> findByAccAndRange(String accNo, LocalDateTime from, LocalDateTime to) {
		List<Transaction> res = new ArrayList<>();
		for (Transaction t : txs) {
			if (t.getAccNo().equals(accNo) &&
					!t.getTime().isBefore(from) &&
					!t.getTime().isAfter(to)) {
				res.add(t);
			}
		}
		return res;
	}

	public void clear() {
		txs.clear();
	}
}
