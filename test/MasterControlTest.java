import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MasterControlTest {
	MasterControl masterControl;
	List<String> input;
	Bank bank;

	@BeforeEach
	void setUp() {
		input = new ArrayList<>();
		bank = new Bank();
		masterControl = new MasterControl(new CommandValidator(bank), new CommandProcessor(bank), new CommandStorage(),
				bank);
	}

	@Test
	void typo_in_create_command_is_invalid() {
		input.add("creat checking 12345678 1.0");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("creat checking 12345678 1.0", actual);
	}

	@Test
	void typo_in_deposit_command_is_invalid() {
		input.add("depositt 12345678 100");

		List<String> actual = masterControl.start(input);
		assertSingleCommand("depositt 12345678 100", actual);
	}

	private void assertSingleCommand(String command, List<String> actual) {
		assertEquals(1, actual.size());
		assertEquals(command, actual.get(0));
	}

	@Test
	void two_typo_commands_both_invalid() {
		input.add("creat checking 12345678 1.0");
		input.add("depositt 12345678 100");

		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertEquals("creat checking 12345678 1.0", actual.get(0));
		assertEquals("depositt 12345678 100", actual.get(1));

	}

	@Test
	void invalid_to_create_accounts_with_same_ID() {
		input.add("create checking 12345678 1.0");
		input.add("create checking 12345678 1.0");

		List<String> actual = masterControl.start(input);
		System.out.print(actual);
		assertEquals(2, actual.size());
		assertEquals("checking 12345678 0.00 1.00", actual.get(0));
		assertEquals("create checking 12345678 1.0", actual.get(1));

	}

	@Test
	void valid_create_command_is_processed_correctly() {
		input.add("create savings 12345678 0.6");
		List<String> actual = masterControl.start(input);
		assertEquals(1, actual.size());
		assertTrue(actual.get(0).contains("savings 12345678 0.00 0.60"));
	}

	@Test
	void valid_deposit_to_savings_account() {
		input.add("create savings 12345678 0.6");
		input.add("deposit 12345678 500");
		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertTrue(actual.get(1).contains("deposit 12345678 500"));
	}

	@Test
	void valid_deposit_to_checking_account() {
		input.add("create checking 12345678 0.6");
		input.add("deposit 12345678 500");
		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertTrue(actual.get(1).contains("deposit 12345678 500"));
	}

	@Test
	void invalid_deposit_to_cd_account() {
		input.add("create cd 12345678 0.6 2000");
		input.add("deposit 12345678 500");
		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertTrue(actual.get(1).contains("deposit 12345678 500"));
	}

	@Test
	void passTime_removes_empty_account() {
		input.add("create savings 12345678 0.6");
		input.add("pass 3");
		List<String> actual = masterControl.start(input);
		assertEquals(0, actual.size());
	}

	@Test
	void valid_withdraw_to_savings_account() {
		input.add("create savings 12345678 0.6");
		input.add("deposit 12345678 500");
		input.add("withdraw 12345678 200");
		List<String> actual = masterControl.start(input);
		assertEquals(3, actual.size());
		assertTrue(actual.get(2).contains("withdraw 12345678 200"));
	}

	@Test
	void valid_transfer_to_savings_account() {
		input.add("create savings 12345678 0.6");
		input.add("create savings 12345677 5.6");
		input.add("deposit 12345678 500");
		input.add("transfer 12345678 12345677 200");
		List<String> actual = masterControl.start(input);
		assertEquals(5, actual.size());
		assertTrue(actual.get(4).contains("transfer 12345678 12345677 200"));
	}

	@Test
	void invalid_withdraw_to_savings_account() {
		input.add("create savings 12345678 0.6");
		input.add("deposit 12345678 500");
		input.add("withdra 12345678 200");
		List<String> actual = masterControl.start(input);
		assertEquals(3, actual.size());
		assertTrue(actual.get(2).contains("withdra 12345678 200"));
	}

	@Test
	void invalid_transfer_to_savings_account() {
		input.add("create savings 12345678 0.6");
		input.add("create savings 12345677 5.6");
		input.add("deposit 12345678 500");
		input.add("transfe 12345678 12345677 200");
		List<String> actual = masterControl.start(input);
		assertEquals(4, actual.size());
		assertTrue(actual.get(3).contains("transfe 12345678 12345677 200"));
	}

	@Test
	void valid_passTime_works_as_expected() {
		input.add("create savings 12345678 0.6");
		input.add("deposit 12345678 500");
		input.add("pass 3");
		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertTrue(actual.get(0).contains("savings 12345678 500.75 0.60"));
	}

	@Test
	void invalid_withdraw_before_12_months_cd() {
		input.add("create cd 12345678 0.6 2000");
		input.add("pass 3");
		input.add("withdraw 12345678 5000");
		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertTrue(actual.get(0).contains("cd 12345678 2012.00 0.60"));
	}

	@Test
	void valid_withdraw_after_12_months_worked_as_expected_cd() {
		input.add("create cd 12345678 0.6 2000");
		input.add("pass 12");
		input.add("withdraw 12345678 5000");
		input.add("pass 1");
		List<String> actual = masterControl.start(input);
		assertEquals(0, actual.size());

	}

}
