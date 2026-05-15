import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CheckingTest {

	@Test
	void savings_balance_at_0() {
		Checking checking = new Checking(5.25, "12345678");
		double balance = checking.getBalance();

		assertEquals(0, balance);
	}
}