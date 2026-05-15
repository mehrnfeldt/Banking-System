import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WithdrawValidatorTest {

	WithdrawValidator withdrawValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		withdrawValidator = new WithdrawValidator(bank);
	}

	@Test
	void valid_withdraw_in_savings() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = withdrawValidator.validate("withdraw 12345678 50");
		assertTrue(actual);
	}

	@Test
	void valid_withdraw_in_checking() {
		Account account = new Checking(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = withdrawValidator.validate("withdraw 12345678 50");
		assertTrue(actual);
	}

	@Test
	void invalid_withdraw_misspelled() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = withdrawValidator.validate("withdra 12345678 50");
		assertFalse(actual);
	}

	@Test
	void invalid_id_has_nine_digits() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = withdrawValidator.validate("withdraw 123456789 50");
		assertFalse(actual);
	}

	@Test
	void invalid_id_has_seven_digits() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = withdrawValidator.validate("withdraw 1234567 50");
		assertFalse(actual);
	}

	@Test
	void invalid_amount_is_negative() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = withdrawValidator.validate("withdraw 12345678 -20");
		assertFalse(actual);
	}

	@Test
	void invalid_amount_is_not_number() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = withdrawValidator.validate("withdraw 12345678 abc");
		assertFalse(actual);
	}

	@Test
	void valid_amount_is_zero() {
		Account account1 = new Savings(5, "12345678");
		Account account2 = new Checking(5, "12345677");
		bank.addAccount(account1);
		bank.addAccount(account2);
		boolean actual1 = withdrawValidator.validate("withdraw 12345678 0");
		boolean actual2 = withdrawValidator.validate("withdraw 12345677 0");
		assertTrue(actual1);
		assertTrue(actual2);
	}

	@Test
	void valid_amount_at_max_savings() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 2000);
		boolean actual = withdrawValidator.validate("withdraw 12345678 1000");
		assertTrue(actual);
	}

	@Test
	void invalid_amount_higher_than_max_savings() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 2000);
		boolean actual = withdrawValidator.validate("withdraw 12345678 1001");
		assertFalse(actual);
	}

	@Test
	void valid_amount_at_max_checking() {
		Account account = new Checking(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 1000);
		boolean actual = withdrawValidator.validate("withdraw 12345678 400");
		assertTrue(actual);
	}

	@Test
	void invalid_amount_higher_than_max_checking() {
		Account account = new Checking(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 1000);
		boolean actual = withdrawValidator.validate("withdraw 12345678 401");
		assertFalse(actual);
	}

	@Test
	void invalid_withdraw_into_nonexistent_account() {
		boolean actual = withdrawValidator.validate("withdraw 12345678 0");
		assertFalse(actual);
	}

	@Test
	void invalid_withdraw_twice() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 1000);
		boolean actual = withdrawValidator.validate("withdraw 12345678 400");
		((Savings) account).setWithdrawnThisMonth(true);
		boolean actual1 = withdrawValidator.validate("withdraw 12345678 400");
		assertFalse(actual1);
	}

	// 12 months on cd

	@Test
	void invalid_withdraw_in_cd_immature() {
		CD account = new CD(1000, 5, "12345678");
		bank.addAccount(account);
		boolean actual = withdrawValidator.validate("withdraw 12345678 1000");
		assertFalse(actual);
	}

	@Test
	void valid_withdraw_in_cd_mature_exact_amount() {
		Account account = new CD(1000, 5, "12345678");
		bank.addAccount(account);
		bank.passTime(12);
		boolean actual = withdrawValidator.validate("withdraw 12345678 1220.55");
		assertTrue(actual);
	}

	@Test
	void valid_withdraw_of_more_cd() {
		CD account = new CD(1000, 5, "12345678");
		bank.addAccount(account);
		bank.passTime(12);
		boolean actual = withdrawValidator.validate("withdraw 12345678 1500");
		assertTrue(actual);
	}

	@Test
	void invalid_withdraw_too_less_cd() {
		CD account = new CD(1000, 5, "12345678");
		bank.addAccount(account);
		bank.passTime(12);
		boolean actual = withdrawValidator.validate("withdraw 12345678 1000");
		assertFalse(actual);
	}

}
