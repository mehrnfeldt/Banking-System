import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandValidatorTest {
	CommandValidator commandValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		commandValidator = new CommandValidator(bank);
	}

	@Test
	void valid_creation_of_savings() {
		boolean actual = commandValidator.validate("create savings 12345678 .90");
		assertTrue(actual);
	}

	@Test
	void valid_creation_of_checking() {
		boolean actual = commandValidator.validate("create checking 12345678 .90");
		assertTrue(actual);
	}

	@Test
	void valid_creation_of_cd() {
		boolean actual = commandValidator.validate("create cd 12345678 .90 2000");
		assertTrue(actual);
	}

	@Test
	void invalid_creation_of_two_accounts_with_same_id() {
		Checking account = new Checking(5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("create checking 12345678 .90");
		assertFalse(actual);
	}

	@Test
	void invalid_id_has_nine_digits() {
		boolean actual1 = commandValidator.validate("create checking 123456789 .90");
		boolean actual2 = commandValidator.validate("create cd 123456789 .90 3000");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_id_has_seven_digits() {
		boolean actual1 = commandValidator.validate("create checking 1234567 .90");
		boolean actual2 = commandValidator.validate("create cd 1234567 .90 1500");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_apr_is_negative() {
		boolean actual1 = commandValidator.validate("create checking 12345678 -7.8");
		boolean actual2 = commandValidator.validate("create cd 12345678 -7.8 2000");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_apr_is_too_high() {
		boolean actual1 = commandValidator.validate("create checking 12345678 17.8");
		boolean actual2 = commandValidator.validate("create cd 12345678 17.8 1900");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void valid_apr_is_minimum() {
		boolean actual = commandValidator.validate("create checking 12345678 0");
		assertTrue(actual);
	}

	@Test
	void valid_apr_is_maximum() {
		boolean actual1 = commandValidator.validate("create checking 12345678 10");
		assertTrue(actual1);
	}

	@Test
	void invalid_apr_is_not_digits() {
		boolean actual1 = commandValidator.validate("create checking 12345678 abc");
		boolean actual2 = commandValidator.validate("create cd 12345678 abc 1000");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_id_is_not_digits() {
		boolean actual1 = commandValidator.validate("create checking 1234n678 5");
		boolean actual2 = commandValidator.validate("create cd 1234n678 5 1000");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_extra_parameter_for_savings_and_checking() {
		boolean actual1 = commandValidator.validate("create savings 12345678 5 100");
		boolean actual2 = commandValidator.validate("create checking 12345678 5 100");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_one_less_parameter_for_savings_and_checking() {
		boolean actual1 = commandValidator.validate("create savings 12345678");
		boolean actual2 = commandValidator.validate("create checking 12345678");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void invalid_one_less_parameter_for_cd() {
		boolean actual = commandValidator.validate("create cd 12345678 4.0");
		assertFalse(actual);
	}

	@Test
	void invalid_one_more_parameter_for_cd() {
		boolean actual = commandValidator.validate("create cd 12345678 4.0 5005 2");
		assertFalse(actual);
	}

	@Test
	void invalid_unsupportedAccountType() {
		boolean actual = commandValidator.validate("create account 12345678 5");
		assertFalse(actual);
	}

	@Test
	void valid_balance_of_cd_at_minimum() {
		boolean actual = commandValidator.validate("create cd 12345678 5 1000");
		assertTrue(actual);
	}

	@Test
	void valid_balance_of_cd_at_maximum() {
		boolean actual = commandValidator.validate("create cd 12345678 5 10000");
		assertTrue(actual);
	}

	@Test
	void invalid_balance_of_cd_below_minimum() {
		boolean actual = commandValidator.validate("create cd 12345678 5 999");
		assertFalse(actual);
	}

	@Test
	void invalid_balance_of_cd_above_maximum() {
		boolean actual = commandValidator.validate("create cd 12345678 5 10001");
		assertFalse(actual);
	}

	@Test
	void valid_cd_with_minimum_apr() {
		boolean actual = commandValidator.validate("create cd 12345678 0.0 7500");
		assertTrue(actual);
	}

	@Test
	void valid_cd_with_maximum_apr() {
		boolean actual = commandValidator.validate("create cd 12345678 10.0 7500");
		assertTrue(actual);
	}

	@Test
	void invalid_command_format_is_incorrect_cd() {
		boolean actual = commandValidator.validate("create cd 12345678 5000 10.0");
		assertFalse(actual);
	}

	@Test
	void invalid_command_format_is_incorrect_savings_and_checking() {
		boolean actual1 = commandValidator.validate("create checking 10.0 12345678");
		boolean actual2 = commandValidator.validate("create savings 10.0 12345679");
		assertFalse(actual1);
		assertFalse(actual2);
	}

	@Test
	void valid_bank_can_validate_three_diff_accounts() {
		boolean actual1 = commandValidator.validate("create savings 12345678 5");
		boolean actual2 = commandValidator.validate("create checking 12345679 6 ");
		boolean actual3 = commandValidator.validate("create cd 12345673 6 4000");
		assertTrue(actual1);
		assertTrue(actual2);
		assertTrue(actual3);
	}

	@Test
	void valid_bank_can_validate_numerous_accounts() {
		boolean actual1 = commandValidator.validate("create savings 12345678 5");
		boolean actual2 = commandValidator.validate("create checking 12345679 6 ");
		boolean actual3 = commandValidator.validate("create cd 12345673 6 4000");
		boolean actual4 = commandValidator.validate("create savings 12345672 5");
		boolean actual5 = commandValidator.validate("create checking 12345629 6 ");
		boolean actual6 = commandValidator.validate("create cd 12345373 6 4000");
		assertTrue(actual1);
		assertTrue(actual2);
		assertTrue(actual3);
		assertTrue(actual4);
		assertTrue(actual5);
		assertTrue(actual6);

	}

	@Test
	void valid_deposit_in_savings() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("deposit 12345678 500");
		assertTrue(actual);
	}

	@Test
	void valid_deposit_in_checking() {
		Checking account = new Checking(5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("deposit 12345678 500");
		assertTrue(actual);
	}

	@Test
	void invalid_deposit_mispelled() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("deposi 12345678 500");
		assertFalse(actual);
	}

	@Test
	void invalid_id_has_nine_digits_deposit() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("deposit 123456789 500");
		assertFalse(actual);
	}

	@Test
	void invalid_id_has_seven_digits_deposit() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("deposit 1234567 500");
		assertFalse(actual);
	}

	@Test
	void invalid_amount_is_negative_deposit() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("deposit 12345678 -8");
		assertFalse(actual);
	}

	@Test
	void valid_amount_is_zero_depsit() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("deposit 12345678 0");
		assertTrue(actual);
	}

	@Test
	void valid_amount_is_max_savings_deposit() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("deposit 12345678 2500");
		assertTrue(actual);
	}

	@Test
	void valid_amount_is_max_checking_deposit() {
		Checking account = new Checking(5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("deposit 12345678 1000");
		assertTrue(actual);
	}

	@Test
	void invalid_amount_too_high_savings_deposit() {
		Savings account = new Savings(5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("deposit 12345678 2501");
		assertFalse(actual);
	}

	@Test
	void invalid_amount_too_high_checking_deposit() {
		Checking account = new Checking(5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("deposit 12345678 1001");
		assertFalse(actual);
	}

	@Test
	void invalid_deposit_into_cd_deposit() {
		CD account = new CD(1000, 5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("deposit 12345678 500");
		assertFalse(actual);
	}

	@Test
	void invalid_deposit_into_nonexistent_account_deposit() {
		boolean actual = commandValidator.validate("deposit 12345678 500");
		assertFalse(actual);
	}

	@Test
	void valid_withdraw_in_savings_withdraw() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = commandValidator.validate("withdraw 12345678 50");
		assertTrue(actual);
	}

	@Test
	void valid_withdraw_in_checking_withdraw() {
		Account account = new Checking(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = commandValidator.validate("withdraw 12345678 50");
		assertTrue(actual);
	}

	@Test
	void invalid_deposit_mispelled_withdraw() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = commandValidator.validate("withdra 12345678 50");
		assertFalse(actual);
	}

	@Test
	void invalid_id_has_nine_digits_withdraw() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = commandValidator.validate("withdraw 123456789 50");
		assertFalse(actual);
	}

	@Test
	void invalid_id_has_seven_digits_withdraw() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = commandValidator.validate("withdraw 1234567 50");
		assertFalse(actual);
	}

	@Test
	void invalid_amount_is_negative_withdraw() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = commandValidator.validate("withdraw 12345678 -20");
		assertFalse(actual);
	}

	@Test
	void invalid_amount_is_not_number_withdraw() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 500);
		boolean actual = commandValidator.validate("withdraw 12345678 abc");
		assertFalse(actual);
	}

	@Test
	void valid_amount_is_zero_withdraw() {
		Account account1 = new Savings(5, "12345678");
		Account account2 = new Checking(5, "12345677");
		bank.addAccount(account1);
		bank.addAccount(account2);
		boolean actual1 = commandValidator.validate("withdraw 12345678 0");
		boolean actual2 = commandValidator.validate("withdraw 12345677 0");
		assertTrue(actual1);
		assertTrue(actual2);
	}

	@Test
	void valid_amount_at_max_savings_withdraw() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 2000);
		boolean actual = commandValidator.validate("withdraw 12345678 1000");
		assertTrue(actual);
	}

	@Test
	void invalid_amount_higher_than_max_savings_withdraw() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 2000);
		boolean actual = commandValidator.validate("withdraw 12345678 1001");
		assertFalse(actual);
	}

	@Test
	void valid_amount_at_max_checking_withdraw() {
		Account account = new Checking(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 1000);
		boolean actual = commandValidator.validate("withdraw 12345678 400");
		assertTrue(actual);
	}

	@Test
	void invalid_amount_higher_than_max_checking_withdraw() {
		Account account = new Checking(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 1000);
		boolean actual = commandValidator.validate("withdraw 12345678 401");
		assertFalse(actual);
	}

	@Test
	void invalid_withdraw_into_nonexistent_account() {
		boolean actual = commandValidator.validate("withdraw 12345678 0");
		assertFalse(actual);
	}

	@Test
	void invalid_withdraw_twice_savings() {
		Account account = new Savings(5, "12345678");
		bank.addAccount(account);
		bank.deposit("12345678", 1000);
		boolean actual = commandValidator.validate("withdraw 12345678 400");
		((Savings) account).setWithdrawnThisMonth(true);
		boolean actual1 = commandValidator.validate("withdraw 12345678 400");
		assertFalse(actual1);
	}

	@Test
	void invalid_withdraw_in_cd_immature() {
		CD account = new CD(1000, 5, "12345678");
		bank.addAccount(account);
		boolean actual = commandValidator.validate("withdraw 12345678 1000");
		assertFalse(actual);
	}

	@Test
	void valid_withdraw_in_cd_mature_exact_amount() {
		Account account = new CD(1000, 5, "12345678");
		bank.addAccount(account);
		bank.passTime(12);
		boolean actual = commandValidator.validate("withdraw 12345678 1220.55");
		assertTrue(actual);
	}

	@Test
	void valid_withdraw_of_more_cd() {
		CD account = new CD(1000, 5, "12345678");
		bank.addAccount(account);
		bank.passTime(12);
		boolean actual = commandValidator.validate("withdraw 12345678 1500");
		assertTrue(actual);
	}

	@Test
	void invalid_withdraw_too_less_cd() {
		CD account = new CD(1000, 5, "12345678");
		bank.addAccount(account);
		bank.passTime(12);
		boolean actual = commandValidator.validate("withdraw 12345678 1000");
		assertFalse(actual);
	}

	@Test
	void valid_transfer_between_savings() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = commandValidator.validate("transfer 12345678 12345677 50");
		assertTrue(actual);
	}

	@Test
	void valid_transfer_between_checking() {
		Account fromAccount = new Checking(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Checking(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = commandValidator.validate("transfer 12345678 12345677 50");
		assertTrue(actual);
	}

	@Test
	void valid_transfer_between_checking_and_saving() {
		Account fromAccount = new Checking(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = commandValidator.validate("transfer 12345678 12345677 50");
		assertTrue(actual);
	}

	@Test
	void valid_transfer_between_saving_and_checking() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Checking(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = commandValidator.validate("transfer 12345678 12345677 50");
		assertTrue(actual);
	}

	@Test
	void invalid_transfer_misspelled() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = commandValidator.validate("transfe 12345678 12345677 50");
		assertFalse(actual);
	}

	@Test
	void invalid_transfer_id_has_nine_digits() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = commandValidator.validate("transfer 123456789 12345677 50");
		assertFalse(actual);
	}

	@Test
	void invalid_transfer_id_has_seven_digits() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = commandValidator.validate("transfer 12345678 1234567 50");
		assertFalse(actual);
	}

	@Test
	void invalid_transfer_amount_is_not_number() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = commandValidator.validate("transfer 12345678 12345677 abc");
		assertFalse(actual);
	}

	@Test
	void invalid_transfer_into_nonexistent_account() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		boolean actual = commandValidator.validate("transfer 12345678 12345677 50");
		assertFalse(actual);
	}

	@Test
	void invalid_transfer_with_CD() {
		Account fromAccount = new CD(1000, 5, "12345678");
		bank.addAccount(fromAccount);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = commandValidator.validate("transfer 12345678 12345677 abc");
		assertFalse(actual);
	}

	@Test
	void invalid_transfer_with_invalid_withdraw() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 2400);
		Account toAccount = new Savings(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = commandValidator.validate("transfer 12345678 12345677 1600");
		assertFalse(actual);
	}

	@Test
	void invalid_transfer_with_invalid_deposit() {
		Account fromAccount = new Savings(5, "12345678");
		bank.addAccount(fromAccount);
		bank.deposit("12345678", 500);
		Account toAccount = new Checking(5, "12345677");
		bank.addAccount(toAccount);
		boolean actual = commandValidator.validate("transfer 12345678 12345677 1050");
		assertFalse(actual);
	}

	@Test
	void valid_pass_command() {
		boolean actual = commandValidator.validate("Pass 5");
		assertTrue(actual);
	}

	@Test
	void valid_pass_command_at_max() {
		boolean actual = commandValidator.validate("pass 60");
		assertTrue(actual);
	}

	@Test
	void valid_pass_command_at_minimum() {
		boolean actual = commandValidator.validate("pass 0");
		assertTrue(actual);
	}

	@Test
	void invalid_pass_command_below_minimum() {
		boolean actual = commandValidator.validate("pass -1");
		assertFalse(actual);
	}

	@Test
	void invalid_pass_command_above_maximum() {
		boolean actual = commandValidator.validate("pass 61");
		assertFalse(actual);
	}

	@Test
	void invalid_pass_amount_is_not_number() {
		boolean actual = commandValidator.validate("pass abc");
		assertFalse(actual);
	}

	@Test
	void invalid_pass_command_misspelled() {
		boolean actual = commandValidator.validate("pas 9");
		assertFalse(actual);
	}

	@Test
	void invalid_pass_decimal_months() {
		boolean actual = commandValidator.validate("pass 9.0");
		assertFalse(actual);
	}

}
