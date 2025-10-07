package service;

import exception.*;
import model.Account;
import repository.AccountRepository;
import repository.TransactionRepository;
import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;

@Test(groups = "transactions")
public class TransactionServiceTest {
	private AccountRepository ar;
	private TransactionRepository tr;
	private TransactionService tx;
	private AccountService accSvc;

	@BeforeMethod
	public void setUp() throws Exception {
		ar = new AccountRepository();
		tr = new TransactionRepository();
		tx = new TransactionService(ar, tr);
		accSvc = new AccountService(ar);

		accSvc.createAccount(new Account("A1", "C1", Account.AccountType.SAVINGS,  new BigDecimal("1000.00")));
		accSvc.createAccount(new Account("A2", "C2", Account.AccountType.CHECKING, new BigDecimal("500.00")));
	}

	@AfterMethod
	public void tearDown() {
		ar = null; tr = null; tx = null; accSvc = null;
	}

	@DataProvider(name = "depositData")
	public Object[][] depositData() {
		return new Object[][]{
			{ "A1", new BigDecimal("100.00"), true  },
			{ "A1", new BigDecimal("0.00"),   false },
			{ "A1", new BigDecimal("-5.00"),  false }
		};
	}

	@Test(dataProvider = "depositData", priority = 1)
	public void deposit_DataDriven(String acc, BigDecimal amt, boolean ok) {
		BigDecimal before = ar.find(acc).getBalance();
		try {
			tx.deposit(acc, amt);
			if (!ok) Assert.fail("Expected InvalidAmountException");
			BigDecimal after = ar.find(acc).getBalance();
			Assert.assertEquals(after.compareTo(before.add(amt)), 0);
		} catch (InvalidAmountException | AccountNotFoundException e) {
			if (ok) Assert.fail("Unexpected: " + e.getMessage());
		}
	}

	@Test(expectedExceptions = AccountNotFoundException.class, priority = 2)
	public void deposit_AccountNotFound() throws Exception {
		tx.deposit("NOPE", new BigDecimal("10.00"));
	}


	@DataProvider(name = "withdrawData")
	public Object[][] withdrawData() {
		return new Object[][]{
			{ "A1", new BigDecimal("100.00"),  true  },
			{ "A1", new BigDecimal("1000.00"), true  },   
			{ "A1", new BigDecimal("1000.01"), false },
			{ "A2", new BigDecimal("0.00"),    false }
		};
	}

	@Test(dataProvider = "withdrawData", priority = 3, dependsOnMethods = { "deposit_DataDriven" })
	public void withdraw_DataDriven(String acc, BigDecimal amt, boolean ok) {
		BigDecimal before = ar.find(acc).getBalance();
		try {
			tx.withdraw(acc, amt);
			if (!ok) Assert.fail("Expected exception");
			BigDecimal after = ar.find(acc).getBalance();
			Assert.assertEquals(after.compareTo(before.subtract(amt)), 0);
		} catch (InvalidAmountException | InsufficientFundsException | AccountNotFoundException e) {
			if (ok) Assert.fail("Unexpected: " + e.getMessage());
		}
	}

	@Test(expectedExceptions = InsufficientFundsException.class, priority = 4)
	public void withdraw_Insufficient() throws Exception {
		tx.withdraw("A2", new BigDecimal("1000.00"));
	}

	@Test(expectedExceptions = AccountNotFoundException.class, priority = 5)
	public void withdraw_AccountNotFound() throws Exception {
		tx.withdraw("NOPE", new BigDecimal("10.00"));
	}

	@DataProvider(name = "transferData")
	public Object[][] transferData() {
		return new Object[][]{
			{ "A1", "A2", new BigDecimal("300.00"), true  },
			{ "A2", "A1", new BigDecimal("1000.00"), false }, 
			{ "A1", "A1", new BigDecimal("10.00"),   false }, 
			{ "NOPE", "A1", new BigDecimal("10.00"), false }, 
			{ "A1", "NOPE", new BigDecimal("10.00"), false }, 
			{ "A1", "A2", new BigDecimal("0.00"),    false }  
		};
	}

	@Test(dataProvider = "transferData", priority = 6, dependsOnMethods = { "withdraw_DataDriven" })
	public void transfer_DataDriven(String from, String to, BigDecimal amt, boolean ok) {
		BigDecimal fromBefore = (ar.find(from) != null) ? ar.find(from).getBalance() : null;
		BigDecimal toBefore   = (ar.find(to)   != null) ? ar.find(to).getBalance()   : null;
		try {
			tx.transfer(from, to, amt);
			if (!ok) Assert.fail("Expected exception");
			BigDecimal fromAfter = ar.find(from).getBalance();
			BigDecimal toAfter   = ar.find(to).getBalance();
			Assert.assertEquals(fromAfter.compareTo(fromBefore.subtract(amt)), 0, "From must decrease exactly");
			Assert.assertEquals(toAfter.compareTo(toBefore.add(amt)), 0,       "To must increase exactly");
		} catch (InvalidAmountException | InsufficientFundsException | AccountNotFoundException e) {
			if (ok) Assert.fail("Unexpected: " + e.getMessage());
		}
	}

	@Test(
			expectedExceptions = InvalidAmountException.class,
			expectedExceptionsMessageRegExp = ".*same account.*|.*Can't transfer to same account.*",
			priority = 7
			)
	public void transfer_SameAccount_MessageCheck() throws Exception {
		accSvc.createAccount(new Account("SAME1", "C3", Account.AccountType.SAVINGS, new BigDecimal("100.00")));
		tx.transfer("SAME1", "SAME1", new BigDecimal("10.00"));
	}
}
