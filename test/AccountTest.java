import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {
	public static final double APR = 9.14;
	public static final String ACCOUNT_NUMBER = "987654321";
	Checking account;

	@BeforeEach
	void setUp() {
		account = new Checking(APR, ACCOUNT_NUMBER);
	}

	@Test
	void account_apr_is_supplied() {
		assertEquals(APR, account.getApr());
	}

	@Test
	void account_account_number_is_supplied() {
		assertEquals(ACCOUNT_NUMBER, account.getAccountNumber());
	}

	@Test
	void deposit_increases_the_balance_by_correct_amount() {
		account.deposit(100);

		assertEquals(100, account.getBalance());
	}

	@Test
	void withdrawing_decreases_the_balance_by_correct_amount() {
		account.deposit(150);
		account.withdraw(100);

		assertEquals(50, account.getBalance());
	}

	@Test
	void withdrawing_cant_cause_negative_balance() {
		account.deposit(100);
		account.withdraw(2000);
		assertEquals(0, account.getBalance());
	}

	@Test
	void depositing_twice_works_as_expected() {
		account.deposit(75);
		account.deposit(135);

		assertEquals(210, account.getBalance());
	}

	@Test
	void withdrawing_twice_works_as_expected() {
		account.deposit(75);
		account.withdraw(15);
		account.withdraw(45);

		assertEquals(15, account.getBalance());
	}

	@Test
	void accrue_interest_works_as_expected() {
		account.deposit(100);
		account.accrueInterest();

		assertEquals(100.76, account.getBalance());
	}

	@Test
	void accrue_interest_twice_works_as_expected() {
		account.deposit(100);
		account.accrueInterest();
		account.accrueInterest();

		assertEquals(101.52, account.getBalance());
	}

	@Test
	void accrue_interest_works_as_expected_on_account_with_0_balance() {
		account.accrueInterest();

		assertEquals(0, account.getBalance());
	}

	@Test
	void savings_withdrawnThisMonth_is_false_at_start() {
		Account savings = new Savings(5.25, "12345678");

		assertEquals(false, ((Savings) savings).hasWithdrawnThisMonth());
	}

	@Test
	void savings_withdrawnThisMonth_is_true_after_withdraw() {
		Account savings = new Savings(5.25, "12345678");
		savings.deposit(500);
		savings.withdraw(50);

		assertEquals(true, ((Savings) savings).hasWithdrawnThisMonth());
	}

	@Test
	void cd_age_is_0_cant_withdraw() {
		Account cd = new CD(1000, 5.25, "12345678");
		assertEquals(false, ((CD) cd).canWithdraw());
	}

	@Test
	void cd_age_is_12_can_withdraw() {
		Account cd = new CD(1000, 5.25, "12345678");
		for (int i = 0; i < 12; i++) {
			((CD) cd).incrementMonthsPassed();
		}
		assertEquals(true, ((CD) cd).canWithdraw());
	}

}
