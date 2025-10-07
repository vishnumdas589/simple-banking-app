package model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
	public enum TxType { DEPOSIT, WITHDRAWAL, TRANSFER }

	private final String id;
	private final String accNo;
	private final TxType type;
	private final BigDecimal amount;
	private final LocalDateTime time;
	private final String relatedAcc;

	public Transaction(String id, String accNo, TxType type, BigDecimal amt, String relatedAcc) {
		this.id = id;
		this.accNo = accNo;
		this.type = type;
		this.amount = amt;
		this.time = LocalDateTime.now();
		this.relatedAcc = relatedAcc;
	}

	public String getId() {
		return id;
	}
	public String getAccNo() {
		return accNo; 
	}
	public TxType getType() {
		return type;
	}
	public BigDecimal getAmount() { 
		return amount; 
	}
	public LocalDateTime getTime() { 
		return time;
	}
	public String getRelatedAcc() { 
		return relatedAcc;
	}
}
