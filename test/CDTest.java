import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CDTest {
	CD account;

	@BeforeEach
	void setUp() {
		account = new CD(1000, 5.25, "12345678");
	}

	@Test
	void cd_balance_at_initial() {
		assertEquals(1000, account.getBalance());
	}

	@Test
	void accrue_interest_works_as_expected() {
		account.accrueInterest();

		assertEquals(1017.6, account.getBalance());
	}

	@Test
	void accrue_interest_twice_works_as_expected() {
		account.accrueInterest();
		account.accrueInterest();

		assertEquals(1035.52, account.getBalance());
	}

	@Test
	void cd_age_at_initial() {
		assertEquals(0, account.getMonthsPassed());
	}

	@Test
	void cd_increment_months_works_as_expected() {
		account.incrementMonthsPassed();
		assertEquals(1, account.getMonthsPassed());
	}

	@Test
	void cd_withdraw_works_as_expected() {
		for (int i = 0; i < 12; i++) {
			account.incrementMonthsPassed();
		}
		account.withdraw(1000);
		assertEquals(0, account.getBalance());
	}

	@Test
	void cd_cant_withdraw_works_less_than_12() {
		account.incrementMonthsPassed();
		account.withdraw(1000);
		assertEquals(1000, account.getBalance());
	}

}
