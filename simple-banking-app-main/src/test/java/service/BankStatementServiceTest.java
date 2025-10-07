package service;

import model.Account;
import model.Transaction;
import repository.AccountRepository;
import repository.TransactionRepository;
import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Test(groups = "statements")
public class BankStatementServiceTest {
	private AccountRepository ar;
	private TransactionRepository tr;
	private TransactionService tx;
	private BankStatementService stmtSvc;
	private AccountService accSvc;

	@BeforeMethod
	public void setUp() throws Exception {
		ar = new AccountRepository();
		tr = new TransactionRepository();
		tx = new TransactionService(ar, tr);
		stmtSvc = new BankStatementService(tr);
		accSvc = new AccountService(ar);

		accSvc.createAccount(new Account("ST1", "C1", Account.AccountType.SAVINGS, new BigDecimal("1000.00")));
	}

	@AfterMethod
	public void tearDown() {
		tr.clear();
		ar = null; tr = null; tx = null; stmtSvc = null; accSvc = null;
	}

	@Test
	public void statementContainsAll() throws Exception {
		tx.deposit("ST1", new BigDecimal("100.00"));
		tx.withdraw("ST1", new BigDecimal("50.00"));
		tx.deposit("ST1", new BigDecimal("25.00"));

		List<Transaction> list = stmtSvc.generateStatement("ST1",
				LocalDateTime.now().minusDays(1),
				LocalDateTime.now().plusDays(1));
		Assert.assertEquals(list.size(), 3);
	}

	@Test
	public void dateBoundsIncludeNow() throws Exception {
		tx.deposit("ST1", new BigDecimal("200.00"));
		LocalDateTime now = LocalDateTime.now();
		List<Transaction> list = stmtSvc.generateStatement("ST1",
				now.minusHours(1), now.plusHours(1));
		Assert.assertTrue(list.size() >= 1);
	}
}
