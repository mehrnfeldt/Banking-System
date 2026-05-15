import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankTest {

	public static final String SAVINGS_ACCOUNT_NUMBER = "12345678";
	public static final String CHECKING_ACCOUNT_NUMBER = "23456789";
	public static final String CD_ACCOUNT_NUMBER = "24242424";
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		Account savings = new Savings(5.3, SAVINGS_ACCOUNT_NUMBER);
		Account checking = new Checking(3.4, CHECKING_ACCOUNT_NUMBER);
		Account cd = new CD(1000, 5.3, CD_ACCOUNT_NUMBER);
		bank.addAccount(savings);
		bank.addAccount(checking);
		bank.addAccount(cd);
	}

	@Test
	public void no_accounts_when_bank_is_created() {
		Bank newBank = new Bank();
		assertEquals(0, newBank.getAccounts().size());
	}

	@Test
	public void one_account_in_bank_when_account_is_added() {
		Account newSavings = new Savings(5.3, "12341234");
		bank.addAccount(newSavings);

		assertEquals(4, bank.getAccounts().size());
	}

	@Test
	public void two_accounts_in_bank_when_two_accounts_are_added() {
		Account savings1 = new Savings(5.3, SAVINGS_ACCOUNT_NUMBER);
		Account savings2 = new Savings(5.3, SAVINGS_ACCOUNT_NUMBER);
		bank.addAccount(savings1);
		bank.addAccount(savings2);
		assertEquals(5, bank.getAccounts().size());
	}

	@Test
	public void correct_account_is_retrieved() {
		Account account1 = bank.getAccountById(SAVINGS_ACCOUNT_NUMBER);
		assertEquals(account1, bank.getAccountById(SAVINGS_ACCOUNT_NUMBER));
	}

	@Test
	public void depositing_money_through_ID_goes_to_correct_account() {
		bank.deposit(SAVINGS_ACCOUNT_NUMBER, 100);

		assertEquals(100, bank.getAccountById(SAVINGS_ACCOUNT_NUMBER).getBalance());
		assertEquals(0, bank.getAccountById(CHECKING_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void withdrawing_money_through_ID_goes_to_correct_account() {
		bank.deposit(SAVINGS_ACCOUNT_NUMBER, 150);
		bank.withdraw(SAVINGS_ACCOUNT_NUMBER, 100);

		assertEquals(50, bank.getAccountById(SAVINGS_ACCOUNT_NUMBER).getBalance());
		assertEquals(0, bank.getAccountById(CHECKING_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void depositing_twice_through_ID_works_as_expected() {
		bank.deposit(SAVINGS_ACCOUNT_NUMBER, 100);
		bank.deposit(SAVINGS_ACCOUNT_NUMBER, 100);

		assertEquals(200, bank.getAccountById(SAVINGS_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void withdrawing_twice_through_ID_works_as_expected_for_checking() {
		bank.deposit(CHECKING_ACCOUNT_NUMBER, 300);
		bank.withdraw(CHECKING_ACCOUNT_NUMBER, 100);
		bank.withdraw(CHECKING_ACCOUNT_NUMBER, 150);

		assertEquals(50, bank.getAccountById(CHECKING_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void withdrawing_twice_through_ID_works_as_expected_for_savings() {
		bank.deposit(SAVINGS_ACCOUNT_NUMBER, 300);
		bank.withdraw(SAVINGS_ACCOUNT_NUMBER, 100);
		bank.passTime(1);
		bank.withdraw(SAVINGS_ACCOUNT_NUMBER, 150);

		assertEquals(50.88, bank.getAccountById(SAVINGS_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void transfer_works_as_expected_between_two_savings() {
		Account account1 = new Savings(5.3, "11111111");
		bank.addAccount(account1);
		bank.deposit("11111111", 500);
		Account account2 = new Savings(5.3, "22222222");
		bank.addAccount(account2);
		bank.transfer("11111111", "22222222", 400);
		assertEquals(100, bank.getAccountById("11111111").getBalance());
		assertEquals(400, bank.getAccountById("22222222").getBalance());
	}

	@Test
	public void transfer_works_as_expected_between_two_checking() {
		Account account1 = new Checking(5.3, "11111111");
		bank.addAccount(account1);
		bank.deposit("11111111", 500);
		Account account2 = new Checking(5.3, "22222222");
		bank.addAccount(account2);
		bank.transfer("11111111", "22222222", 400);
		assertEquals(100, bank.getAccountById("11111111").getBalance());
		assertEquals(400, bank.getAccountById("22222222").getBalance());
	}

	@Test
	public void transfer_works_as_expected_between_checking_to_saving() {
		Account account1 = new Checking(5.3, "11111111");
		bank.addAccount(account1);
		bank.deposit("11111111", 500);
		Account account2 = new Savings(5.3, "22222222");
		bank.addAccount(account2);
		bank.transfer("11111111", "22222222", 400);
		assertEquals(100, bank.getAccountById("11111111").getBalance());
		assertEquals(400, bank.getAccountById("22222222").getBalance());
	}

	@Test
	public void transfer_works_as_expected_between_saving_to_checking() {
		Account account1 = new Savings(5.3, "11111111");
		bank.addAccount(account1);
		bank.deposit("11111111", 500);
		Account account2 = new Checking(5.3, "22222222");
		bank.addAccount(account2);
		bank.transfer("11111111", "22222222", 400);
		assertEquals(100, bank.getAccountById("11111111").getBalance());
		assertEquals(400, bank.getAccountById("22222222").getBalance());
	}

	@Test
	public void transfer_twice_works_as_expected() {
		Account account1 = new Savings(5.3, "11111111");
		bank.addAccount(account1);
		bank.deposit("11111111", 500);
		Account account2 = new Savings(5.3, "22222222");
		bank.addAccount(account2);
		bank.transfer("11111111", "22222222", 200);
		bank.passTime(1);
		bank.transfer("11111111", "22222222", 200);
		assertEquals(101.32, bank.getAccountById("11111111").getBalance());
		assertEquals(400.88, bank.getAccountById("22222222").getBalance());
	}

	@Test
	public void transfer_amount_is_higher_than_balance() {
		Account account1 = new Savings(5.3, "11111111");
		bank.addAccount(account1);
		bank.deposit("11111111", 500);
		Account account2 = new Savings(5.3, "22222222");
		bank.addAccount(account2);
		bank.transfer("11111111", "22222222", 600);
		assertEquals(0, bank.getAccountById("11111111").getBalance());
		assertEquals(500, bank.getAccountById("22222222").getBalance());
	}

	@Test
	public void pass_one_month_checking_account_correct_interest() {
		bank.deposit(CHECKING_ACCOUNT_NUMBER, 600);
		bank.passTime(1);
		assertEquals(601.7, bank.getAccountById(CHECKING_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void pass_two_months_checking_account_correct_interest() {
		bank.deposit(CHECKING_ACCOUNT_NUMBER, 600);
		bank.passTime(2);
		assertEquals(603.4, bank.getAccountById(CHECKING_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void pass_one_month_checking_account_correct_withdraw_when_under_100() {
		bank.deposit(CHECKING_ACCOUNT_NUMBER, 99);
		bank.passTime(1);
		assertEquals(74.2, bank.getAccountById(CHECKING_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void pass_two_month_checking_account_correct_withdraw_when_under_100() {
		bank.deposit(CHECKING_ACCOUNT_NUMBER, 99);
		bank.passTime(2);
		assertEquals(49.33, bank.getAccountById(CHECKING_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void pass_one_month_checking_account_removed_balance_0() {
		bank.passTime(1);
		assertEquals(1, bank.getAccounts().size());
	}

	@Test
	public void pass_four_months_checking_account_removed_when_balance_reaches_0() {
		bank.deposit(CHECKING_ACCOUNT_NUMBER, 99);
		bank.passTime(5);
		assertEquals(1, bank.getAccounts().size());
	}

	@Test
	public void pass_one_month_cd_account_correct_interest() {
		bank.passTime(1);
		assertEquals(1017.76, bank.getAccountById(CD_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void pass_two_months_cd_account_correct_interest() {
		bank.passTime(2);
		assertEquals(1035.83, bank.getAccountById(CD_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void pass_one_month_cd_account_removed_balance_0() {
		bank.passTime(12);
		bank.withdraw(CD_ACCOUNT_NUMBER, 1);
		bank.passTime(1);

		assertEquals(0, bank.getAccounts().size());
	}

	@Test
	public void pass_one_month_savings_account_correct_interest() {
		bank.deposit(SAVINGS_ACCOUNT_NUMBER, 600);
		bank.passTime(1);
		assertEquals(602.65, bank.getAccountById(SAVINGS_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void pass_two_months_savings_account_correct_interest() {
		bank.deposit(SAVINGS_ACCOUNT_NUMBER, 600);
		bank.passTime(2);
		assertEquals(605.3, bank.getAccountById(SAVINGS_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void pass_one_month_savings_account_correct_withdraw_when_under_100() {
		bank.deposit(SAVINGS_ACCOUNT_NUMBER, 99);
		bank.passTime(1);
		assertEquals(74.31, bank.getAccountById(SAVINGS_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void pass_two_month_savings_account_correct_withdraw_when_under_100() {
		bank.deposit(SAVINGS_ACCOUNT_NUMBER, 99);
		bank.passTime(2);
		assertEquals(49.52, bank.getAccountById(SAVINGS_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void pass_one_month_savings_account_removed_balance_0() {
		bank.passTime(1);
		assertEquals(1, bank.getAccounts().size());
	}

	@Test
	public void bank_with_multiple_accounts_all_correct_interest_one_month() {
		bank.deposit(SAVINGS_ACCOUNT_NUMBER, 600);
		bank.deposit(CHECKING_ACCOUNT_NUMBER, 600);
		bank.passTime(1);
		assertEquals(602.65, bank.getAccountById(SAVINGS_ACCOUNT_NUMBER).getBalance());
		assertEquals(601.7, bank.getAccountById(CHECKING_ACCOUNT_NUMBER).getBalance());
		assertEquals(1017.76, bank.getAccountById(CD_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void bank_with_multiple_accounts_all_correct_interest_two_months() {
		bank.deposit(SAVINGS_ACCOUNT_NUMBER, 600);
		bank.deposit(CHECKING_ACCOUNT_NUMBER, 600);
		bank.passTime(2);
		assertEquals(605.3, bank.getAccountById(SAVINGS_ACCOUNT_NUMBER).getBalance());
		assertEquals(603.4, bank.getAccountById(CHECKING_ACCOUNT_NUMBER).getBalance());
		assertEquals(1035.83, bank.getAccountById(CD_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void bank_with_multiple_accounts_withdraw_and_remove_works_as_expected() {
		bank.deposit(SAVINGS_ACCOUNT_NUMBER, 99);
		bank.deposit(CHECKING_ACCOUNT_NUMBER, 99);
		bank.passTime(5);
		assertEquals(1, bank.getAccounts().size());
	}

	@Test
	public void cd_account_is_one_month_old_after_pass_one() {
		bank.passTime(1);
		assertEquals(1, ((CD) bank.getAccountById(CD_ACCOUNT_NUMBER)).getMonthsPassed());
	}

	@Test
	public void cd_account_is_one_month_old_after_pass_12() {
		bank.passTime(12);
		assertEquals(12, ((CD) bank.getAccountById(CD_ACCOUNT_NUMBER)).getMonthsPassed());
	}

	@Test
	public void withdrawing_through_ID_works_as_expected_for_mature_cd() {
		bank.passTime(12);
		bank.withdraw(CD_ACCOUNT_NUMBER, 1000);

		assertEquals(0, bank.getAccountById(CD_ACCOUNT_NUMBER).getBalance());
	}

	@Test
	public void withdrawing_through_ID_works_as_expected_for_immature_cd() {
		bank.passTime(1);
		bank.withdraw(CD_ACCOUNT_NUMBER, 1000);

		assertEquals(1017.76, bank.getAccountById(CD_ACCOUNT_NUMBER).getBalance());
	}
}