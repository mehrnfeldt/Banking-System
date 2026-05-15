import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransferValidatorTest {
	TransferValidator transferValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		transferValidator = new TransferValidator(bank);
	}

	@Test
	void valid_transfer_between_savings() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = transferValidator.validate("transfer 12345678 12345677 50");
		assertTrue(actual);
	}

	@Test
	void valid_transfer_between_checking() {
		Account fromAccount = new Checking(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Checking(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = transferValidator.validate("transfer 12345678 12345677 50");
		assertTrue(actual);
	}

	@Test
	void valid_transfer_between_checking_and_saving() {
		Account fromAccount = new Checking(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = transferValidator.validate("transfer 12345678 12345677 50");
		assertTrue(actual);
	}

	@Test
	void valid_transfer_between_saving_and_checking() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Checking(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = transferValidator.validate("transfer 12345678 12345677 50");
		assertTrue(actual);
	}

	@Test
	void invalid_transfer_mispelled() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = transferValidator.validate("transfe 12345678 12345677 50");
		assertFalse(actual);
	}

	@Test
	void invalid_id_has_nine_digits() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = transferValidator.validate("transfer 123456789 12345677 50");
		assertFalse(actual);
	}

	@Test
	void invalid_id_has_seven_digits() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = transferValidator.validate("transfer 12345678 1234567 50");
		assertFalse(actual);
	}

	@Test
	void invalid_amount_is_not_number() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = transferValidator.validate("transfer 12345678 12345677 abc");
		assertFalse(actual);
	}

	@Test
	void invalid_transfer_into_nonexistent_account() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		boolean actual = transferValidator.validate("transfer 12345678 12345677 50");
		assertFalse(actual);
	}

	@Test
	void invalid_transfer_with_CD() {
		Account fromAccount = new CD(1000, 5, "12345678");
		bank.addAccount(fromAccount);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = transferValidator.validate("transfer 12345678 12345677 abc");
		assertFalse(actual);
	}

	@Test
	void invalid_transfer_with_invalid_withdraw() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 2400);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = transferValidator.validate("transfer 12345678 12345677 1600");
		assertFalse(actual);
	}

	@Test
	void invalid_transfer_with_invalid_deposit() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Checking(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = transferValidator.validate("transfer 12345678 12345677 1050");
		assertFalse(actual);
	}

}
