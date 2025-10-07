package repository;

import model.Account;
import java.math.BigDecimal;
import org.testng.Assert;
import org.testng.annotations.*;

@Test(groups = "repository")
public class AccountRepositoryTest {
	private AccountRepository repo;
	private Account acc;

	@BeforeMethod
	public void setUp() {
		repo = new AccountRepository();
		acc = new Account("ACC001", "CUST001", Account.AccountType.SAVINGS, new BigDecimal("1000.00"));
	}

	@AfterMethod
	public void tearDown() {
		repo = null;
		acc = null;
	}

	@Test
	public void saveAndFind() {
		repo.save(acc);
		Account found = repo.find("ACC001");
		Assert.assertNotNull(found);
		Assert.assertEquals(found.getBalance().compareTo(new BigDecimal("1000.00")), 0);
	}

	@Test
	public void existsAndDelete() {
		repo.save(acc);
		Assert.assertTrue(repo.exists("ACC001"));
		repo.delete("ACC001");
		Assert.assertFalse(repo.exists("ACC001"));
		Assert.assertNull(repo.find("ACC001"));
	}
}
