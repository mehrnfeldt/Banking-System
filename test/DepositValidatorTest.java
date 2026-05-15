import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepositValidatorTest {

	DepositValidator depositValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		depositValidator = new DepositValidator(bank);
	}

	@Test
	void valid_deposit_in_savings() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = depositValidator.validate("deposit 12345678 500");
		assertTrue(actual);
	}

	@Test
	void valid_deposit_in_checking() {
		Checking account = new Checking(5, "12345678");
		bank.addAccount(account);
		boolean actual = depositValidator.validate("deposit 12345678 500");
		assertTrue(actual);
	}

	@Test
	void invalid_deposit_mispelled() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = depositValidator.validate("deposi 12345678 500");
		assertFalse(actual);
	}

	@Test
	void invalid_id_has_nine_digits() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = depositValidator.validate("deposit 123456789 500");
		assertFalse(actual);
	}

	@Test
	void invalid_id_has_seven_digits() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = depositValidator.validate("deposit 1234567 500");
		assertFalse(actual);
	}

	@Test
	void invalid_amount_is_negative() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = depositValidator.validate("deposit 12345678 -8");
		assertFalse(actual);
	}

	@Test
	void valid_amount_is_zero() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = depositValidator.validate("deposit 12345678 0");
		assertTrue(actual);
	}

	@Test
	void valid_amount_is_max_savings() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = depositValidator.validate("deposit 12345678 2500");
		assertTrue(actual);
	}

	@Test
	void valid_amount_is_max_checking() {
		Checking account = new Checking(5, "12345678");
		bank.addAccount(account);
		boolean actual = depositValidator.validate("deposit 12345678 1000");
		assertTrue(actual);
	}

	@Test
	void invalid_amount_too_high_savings() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = depositValidator.validate("deposit 12345678 2501");
		assertFalse(actual);
	}

	@Test
	void invalid_amount_too_high_checking() {
		Checking account = new Checking(5, "12345678");
		bank.addAccount(account);
		boolean actual = depositValidator.validate("deposit 12345678 1001");
		assertFalse(actual);
	}

	@Test
	void invalid_deposit_into_cd() {
		CD account = new CD(1000, 5, "12345678");
		bank.addAccount(account);
		boolean actual = depositValidator.validate("deposit 12345678 500");
		assertFalse(actual);
	}

	@Test
	void invalid_deposit_into_nonexistent_account() {
		boolean actual = depositValidator.validate("deposit 12345678 500");
		assertFalse(actual);
	}

}
