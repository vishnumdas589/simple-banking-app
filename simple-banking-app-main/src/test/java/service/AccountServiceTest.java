package service;


import exception.AccountNotFoundException;
import exception.DuplicateAccountException;
import model.Account;
import repository.AccountRepository;
import java.math.BigDecimal;
import org.testng.Assert;
import org.testng.annotations.*;

@Test(groups = "accounts")
public class AccountServiceTest {
	private AccountRepository repo;
	private AccountService svc;

	@BeforeMethod
	public void setUp() {
		repo = new AccountRepository();
		svc = new AccountService(repo);
	}

	@AfterMethod
	public void tearDown() {
		repo = null;
		svc = null;
	}

	@Test(priority = 1)
	public void createAndGet() throws Exception {
		Account a = new Account("ACC1", "C1", Account.AccountType.SAVINGS, new BigDecimal("100.00"));
		svc.createAccount(a);
		Account got = svc.getAccount("ACC1");
		Assert.assertEquals(got.getBalance().compareTo(new BigDecimal("100.00")), 0);
	}

	@Test(priority = 2, expectedExceptions = DuplicateAccountException.class)
	public void duplicateThrows() throws Exception {
		Account a = new Account("ACC2", "C1", Account.AccountType.CHECKING, new BigDecimal("50.00"));
		svc.createAccount(a);
		svc.createAccount(a);
	}

	@Test(priority = 3, expectedExceptions = AccountNotFoundException.class)
	public void getNotFound() throws Exception {
		svc.getAccount("NOPE");
	}

	@Test(priority = 4)
	public void getBalancePrecision() throws Exception {
		Account a = new Account("ACC3", "C2", Account.AccountType.SAVINGS, new BigDecimal("250.00"));
		svc.createAccount(a);
		Assert.assertEquals(svc.getBalance("ACC3").compareTo(new BigDecimal("250.00")), 0);
	}

	@Test(priority = 5, expectedExceptions = DuplicateAccountException.class)
	public void onePerTypePerCustomer() throws Exception {
		Account s = new Account("S1", "CX", Account.AccountType.SAVINGS, new BigDecimal("10.00"));
		Account s2 = new Account("S2", "CX", Account.AccountType.SAVINGS, new BigDecimal("20.00"));
		svc.createAccount(s);
		svc.createAccount(s2);
	}
}
