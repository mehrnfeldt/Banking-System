import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateValidatorTest {

	CreateValidator createValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		createValidator = new CreateValidator(bank);
	}

	@Test
	void valid_creation_of_savings() {
		boolean actual = createValidator.validate("create savings 12345678 .90");
		assertTrue(actual);
	}

	@Test
	void valid_creation_of_checking() {
		boolean actual = createValidator.validate("create checking 12345678 .90");
		assertTrue(actual);
	}

	@Test
	void valid_creation_of_cd() {
		boolean actual = createValidator.validate("create cd 12345678 .90 2000");
		assertTrue(actual);
	}

	@Test
	void invalid_creation_of_two_accounts_with_same_id() {
		Checking account = new Checking(5, "12345678");
		bank.addAccount(account);
		boolean actual = createValidator.validate("create checking 12345678 .90");
		assertFalse(actual);
	}

	@Test
	void invalid_id_has_nine_digits() {
		boolean actual1 = createValidator.validate("create checking 123456789 .90");
		boolean actual2 = createValidator.validate("create cd 123456789 .90 3000");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_id_has_seven_digits() {
		boolean actual1 = createValidator.validate("create checking 1234567 .90");
		boolean actual2 = createValidator.validate("create cd 1234567 .90 1500");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_apr_is_negative() {
		boolean actual1 = createValidator.validate("create checking 12345678 -7.8");
		boolean actual2 = createValidator.validate("create cd 12345678 -7.8 2000");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_apr_is_too_high() {
		boolean actual1 = createValidator.validate("create checking 12345678 17.8");
		boolean actual2 = createValidator.validate("create cd 12345678 17.8 1900");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void valid_apr_is_minimum() {
		boolean actual = createValidator.validate("create checking 12345678 0");
		assertTrue(actual);
	}

	@Test
	void valid_apr_is_maximum() {
		boolean actual1 = createValidator.validate("create checking 12345678 10");
		assertTrue(actual1);
	}

	@Test
	void invalid_apr_is_not_digits() {
		boolean actual1 = createValidator.validate("create checking 12345678 abc");
		boolean actual2 = createValidator.validate("create cd 12345678 abc 1000");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_id_is_not_digits() {
		boolean actual1 = createValidator.validate("create checking 1234n678 5");
		boolean actual2 = createValidator.validate("create cd 1234n678 5 1000");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_extra_parameter_for_savings_and_checking() {
		boolean actual1 = createValidator.validate("create savings 12345678 5 100");
		boolean actual2 = createValidator.validate("create checking 12345678 5 100");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_one_less_parameter_for_savings_and_checking() {
		boolean actual1 = createValidator.validate("create savings 12345678");
		boolean actual2 = createValidator.validate("create checking 12345678");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_one_less_parameter_for_cd() {
		boolean actual = createValidator.validate("create cd 12345678 4.0");
		assertFalse(actual);
	}

	@Test
	void invalid_one_more_parameter_for_cd() {
		boolean actual = createValidator.validate("create cd 12345678 4.0 5005 2");
		assertFalse(actual);
	}

	@Test
	void invalid_unsupportedAccountType() {
		boolean actual = createValidator.validate("create account 12345678 5");
		assertFalse(actual);
	}

	@Test
	void valid_balance_of_cd_at_minimum() {
		boolean actual = createValidator.validate("create cd 12345678 5 1000");
		assertTrue(actual);
	}

	@Test
	void valid_balance_of_cd_at_maximum() {
		boolean actual = createValidator.validate("create cd 12345678 5 10000");
		assertTrue(actual);
	}

	@Test
	void invalid_balance_of_cd_below_minimum() {
		boolean actual = createValidator.validate("create cd 12345678 5 999");
		assertFalse(actual);
	}

	@Test
	void invalid_balance_of_cd_above_maximum() {
		boolean actual = createValidator.validate("create cd 12345678 5 10001");
		assertFalse(actual);
	}

	@Test
	void valid_cd_with_minimum_apr() {
		boolean actual = createValidator.validate("create cd 12345678 0.0 7500");
		assertTrue(actual);
	}

	@Test
	void valid_cd_with_maximum_apr() {
		boolean actual = createValidator.validate("create cd 12345678 10.0 7500");
		assertTrue(actual);
	}

	@Test
	void invalid_command_format_is_incorrect_cd() {
		boolean actual = createValidator.validate("create cd 12345678 5000 10.0");
		assertFalse(actual);
	}

	@Test
	void invalid_command_format_is_incorrect_savings_and_checking() {
		boolean actual1 = createValidator.validate("create checking 10.0 12345678");
		boolean actual2 = createValidator.validate("create savings 10.0 12345679");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void valid_bank_can_validate_three_diff_accounts() {
		boolean actual1 = createValidator.validate("create savings 12345678 5");
		boolean actual2 = createValidator.validate("create checking 12345679 6 ");
		boolean actual3 = createValidator.validate("create cd 12345673 6 4000");
		assertTrue(actual1);
		assertTrue(actual2);
		assertTrue(actual3);
	}

	@Test
	void valid_bank_can_validate_numerous_accounts() {
		boolean actual1 = createValidator.validate("create savings 12345678 5");
		boolean actual2 = createValidator.validate("create checking 12345679 6 ");
		boolean actual3 = createValidator.validate("create cd 12345673 6 4000");
		boolean actual4 = createValidator.validate("create savings 12345672 5");
		boolean actual5 = createValidator.validate("create checking 12345629 6 ");
		boolean actual6 = createValidator.validate("create cd 12345373 6 4000");
		assertTrue(actual1);
		assertTrue(actual2);
		assertTrue(actual3);
		assertTrue(actual4);
		assertTrue(actual5);
		assertTrue(actual6);

	}

}
