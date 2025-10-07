package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {
    public enum AccountType { SAVINGS, CHECKING
    	}

    private final String accountNumber;
    private final String customerId;
    private final AccountType type;
    private BigDecimal balance;
    private final LocalDateTime openedOn;

    public Account(String accNo, String custId, AccountType type, BigDecimal initBal) {
        this.accountNumber = accNo;
        this.customerId = custId;
        this.type = type;
        this.balance = (initBal != null) ? initBal : BigDecimal.ZERO;
        this.openedOn = LocalDateTime.now();
    }

    public String getAccountNumber() { 
    	return accountNumber;
    	}
    public String getCustomerId() { 
    	return customerId;
    }
    public AccountType getType() { 
    	return type; 
    }
    public LocalDateTime getOpenedOn() { 
    	return openedOn;
    	}

    public BigDecimal getBalance() {
    	return balance;
    	}
    public void setBalance(BigDecimal bal) {
    	this.balance = bal;
    	}
}
