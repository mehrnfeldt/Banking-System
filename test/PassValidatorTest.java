import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassValidatorTest {
	PassValidator passValidator;

	@BeforeEach
	void setUp() {
		passValidator = new PassValidator();
	}

	@Test
	void valid_pass_command() {
		boolean actual = passValidator.validate("Pass 5");
		assertTrue(actual);
	}

	@Test
	void valid_pass_command_at_max() {
		boolean actual = passValidator.validate("pass 60");
		assertTrue(actual);
	}

	@Test
	void valid_pass_command_at_minimum() {
		boolean actual = passValidator.validate("pass 0");
		assertTrue(actual);
	}

	@Test
	void invalid_pass_command_below_minimum() {
		boolean actual = passValidator.validate("pass -1");
		assertFalse(actual);
	}

	@Test
	void invalid_pass_command_above_maximum() {
		boolean actual = passValidator.validate("pass 61");
		assertFalse(actual);
	}

	@Test
	void invalid_amount_is_not_number() {
		boolean actual = passValidator.validate("pass abc");
		assertFalse(actual);
	}

	@Test
	void invalid_command_misspelled() {
		boolean actual = passValidator.validate("pas 9");
		assertFalse(actual);
	}

}
