import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SavingsTest {

	@Test
	void savings_balance_at_0() {
		Savings savings = new Savings(5.25, "12345678");
		double balance = savings.getBalance();

		assertEquals(0, balance);
	}

	@Test
	void savings_withdrawnThisMonth_is_false_at_start() {
		Savings savings = new Savings(5.25, "12345678");

		assertEquals(false, savings.hasWithdrawnThisMonth());
	}

	@Test
	void savings_withdrawnThisMonth_is_true_after_withdraw() {
		Savings savings = new Savings(5.25, "12345678");
		savings.deposit(500);
		savings.withdraw(50);

		assertEquals(true, savings.hasWithdrawnThisMonth());
	}
}