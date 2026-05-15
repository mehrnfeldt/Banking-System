import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandProcessorTest {

	CommandProcessor commandProcessor;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		commandProcessor = new CommandProcessor(bank);
	}

	@Test
	void creation_of_savings() {
		Account account = commandProcessor.process("create savings 12345678 1.0");

		assertEquals("12345678", account.getAccountNumber());
		assertEquals(1.0, account.getApr());

	}

	@Test
	void creation_of_checking() {
		Account account = commandProcessor.process("create checking 12345678 1.0");
		assertEquals("12345678", account.getAccountNumber());
		assertEquals(1.0, account.getApr());
	}

	@Test
	void creation_of_cd() {
		Account account = commandProcessor.process("create cd 12345678 1.0 2500");
		assertEquals("12345678", account.getAccountNumber());
		assertEquals(1.0, account.getApr());
		assertEquals(2500, account.getBalance());

	}

	@Test
	void deposit_into_savings_zero() {
		Account account1 = commandProcessor.process("create savings 12345678 1.0");
		Account account2 = commandProcessor.process("deposit 12345678 500");
		assertEquals(500, account2.getBalance());
	}

	@Test
	void deposit_into_checking_zero() {
		Account account = commandProcessor.process("create checking 12345678 1.0");
		Account account1 = commandProcessor.process("deposit 12345678 500");

		assertEquals(500, account1.getBalance());
	}

	@Test
	void deposit_into_savings_with_money() {
		Account account1 = commandProcessor.process("create savings 12345678 1.0");
		Account account2 = commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("deposit 12345678 500");
		assertEquals(1000, account2.getBalance());
	}

	@Test
	void deposit_into_checking_with_money() {
		Account account1 = commandProcessor.process("create checking 12345678 1.0");
		Account account2 = commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("deposit 12345678 500");
		assertEquals(1000, account2.getBalance());
	}

	@Test
	void withdraw_savings_zero() {
		Account account = commandProcessor.process("create savings 12345678 1.0");
		commandProcessor.process("withdraw 12345678 0");
		assertEquals(0, account.getBalance());
	}

	@Test
	void withdraw_checking_zero() {
		Account account = commandProcessor.process("create checking 12345678 1.0");
		commandProcessor.process("withdraw 12345678 0");

		assertEquals(0, account.getBalance());
	}

	@Test
	void withdraw_cd_balance() {
		Account account = commandProcessor.process("create cd 12345678 1.0 1000");
		commandProcessor.process("pass 12");
		commandProcessor.process("withdraw 12345678 1040.52");

		assertEquals(0, account.getBalance());
	}

	@Test
	void withdraw_cd_balance_higher() {
		Account account = commandProcessor.process("create cd 12345678 1.0 1000");
		commandProcessor.process("pass 12");
		commandProcessor.process("withdraw 12345678 1050");

		assertEquals(0, account.getBalance());
	}

	@Test
	void withdraw_into_savings_with_money() {
		Account account = commandProcessor.process("create savings 12345678 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("withdraw 12345678 50");
		assertEquals(450, account.getBalance());
	}

	@Test
	void withdraw_into_savings_twice_send_statement_doesnt_work() {
		Account account = commandProcessor.process("create savings 12345678 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("withdraw 12345678 50");
		commandProcessor.process("withdraw 12345678 50");
		assertEquals(450, account.getBalance());
	}

	@Test
	void withdraw_into_checking_twice_works() {
		Account account = commandProcessor.process("create checking 12345678 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("withdraw 12345678 50");
		commandProcessor.process("withdraw 12345678 50");
		assertEquals(400, account.getBalance());
	}

	@Test
	void withdraw_checking_with_money() {
		Account account = commandProcessor.process("create checking 12345678 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("withdraw 12345678 50");
		assertEquals(450, account.getBalance());
	}

	@Test
	void transfer_zero_savings_to_savings() {
		Account account1 = commandProcessor.process("create savings 12345678 1.0");
		Account account2 = commandProcessor.process("create savings 12345677 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("transfer 12345678 12345677 0");

		assertEquals(500, account1.getBalance());
		assertEquals(0, account2.getBalance());
	}

	@Test
	void transfer_money_savings_to_savings() {
		Account account1 = commandProcessor.process("create savings 12345678 1.0");
		Account account2 = commandProcessor.process("create savings 12345677 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("transfer 12345678 12345677 200");

		assertEquals(300, account1.getBalance());
		assertEquals(200, account2.getBalance());
	}

	@Test
	void transfer_zero_checking_to_checking() {
		Account account1 = commandProcessor.process("create checking 12345678 1.0");
		Account account2 = commandProcessor.process("create checking 12345677 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("transfer 12345678 12345677 0");

		assertEquals(500, account1.getBalance());
		assertEquals(0, account2.getBalance());
	}

	@Test
	void transfer_money_checking_to_checking() {
		Account account1 = commandProcessor.process("create checking 12345678 1.0");
		Account account2 = commandProcessor.process("create checking 12345677 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("transfer 12345678 12345677 200");

		assertEquals(300, account1.getBalance());
		assertEquals(200, account2.getBalance());
	}

	@Test
	void transfer_zero_savings_to_checking() {
		Account account1 = commandProcessor.process("create savings 12345678 1.0");
		Account account2 = commandProcessor.process("create checking 12345677 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("transfer 12345678 12345677 0");

		assertEquals(500, account1.getBalance());
		assertEquals(0, account2.getBalance());
	}

	@Test
	void transfer_money_savings_to_checking() {
		Account account1 = commandProcessor.process("create savings 12345678 1.0");
		Account account2 = commandProcessor.process("create checking 12345677 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("transfer 12345678 12345677 200");

		assertEquals(300, account1.getBalance());
		assertEquals(200, account2.getBalance());
	}

	@Test
	void transfer_zero_checking_to_savings() {
		Account account1 = commandProcessor.process("create checking 12345678 1.0");
		Account account2 = commandProcessor.process("create savings 12345677 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("transfer 12345678 12345677 0");

		assertEquals(500, account1.getBalance());
		assertEquals(0, account2.getBalance());
	}

	@Test
	void transfer_money_checking_to_savings() {
		Account account1 = commandProcessor.process("create checking 12345678 1.0");
		Account account2 = commandProcessor.process("create savings 12345677 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("transfer 12345678 12345677 200");

		assertEquals(300, account1.getBalance());
		assertEquals(200, account2.getBalance());
	}

	@Test
	void passTime_interest_correct_one_month() {
		Account account1 = commandProcessor.process("create checking 12345678 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("pass 1");

		assertEquals(500.41, account1.getBalance());
	}

	@Test
	void passTime_withdraw_correct() {
		Account account1 = commandProcessor.process("create checking 12345678 1.0");
		commandProcessor.process("deposit 12345678 50");
		commandProcessor.process("pass 1");

		assertEquals(25.02, account1.getBalance());
	}

	@Test
	void passTime_interest_correct_two_months() {
		Account account1 = commandProcessor.process("create checking 12345678 1.0");
		commandProcessor.process("deposit 12345678 500");
		commandProcessor.process("pass 2");

		assertEquals(500.82, account1.getBalance());
	}

	@Test
	void passTime_removes_account_when_balance_0() {
		Account account1 = commandProcessor.process("create checking 12345678 1.0");
		commandProcessor.process("pass 1");

		assertEquals(0, bank.getAccounts().size());
	}

	@Test
	void passTime_interest_correct_one_month_cd() {
		Account account1 = commandProcessor.process("create cd 12345678 1.0 2000");
		commandProcessor.process("pass 1");

		assertEquals(2006.65, account1.getBalance());
	}

	@Test
	void passTime_interest_correct_two_months_cd() {
		Account account1 = commandProcessor.process("create cd 12345678 1.0 2000");
		commandProcessor.process("pass 2");

		assertEquals(2013.33, account1.getBalance());
	}

}
