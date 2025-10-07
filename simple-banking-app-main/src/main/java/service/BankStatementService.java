package service;

import model.Transaction;
import repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.List;

public class BankStatementService {
	private final TransactionRepository txRepo;

	public BankStatementService(TransactionRepository txRepo) {
		this.txRepo = txRepo;
	}

	public List<Transaction> generateStatement(String accNo, LocalDateTime from, LocalDateTime to) {
		return txRepo.findByAccAndRange(accNo, from, to);
	}
}
