import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandStorageTest {

	private CommandStorage commandStorage;

	@BeforeEach
	void setUp() {
		commandStorage = new CommandStorage();
	}

	@Test
	void add_invalid_command() {
		commandStorage.addInvalidCommand("invalid command 1");
		List<String> commandList = commandStorage.getInvalidCommands();

		assertEquals(1, commandList.size());
		assertEquals("invalid command 1", commandList.get(0));
	}

	@Test
	void add_multiple_invalid_commands() {
		commandStorage.addInvalidCommand("invalid command 1");
		commandStorage.addInvalidCommand("invalid command 2");

		List<String> commandList = commandStorage.getInvalidCommands();

		assertEquals(2, commandList.size());
		assertEquals("invalid command 1", commandList.get(0));
		assertEquals("invalid command 2", commandList.get(1));
	}

	@Test
	void invalid_commands_is_empty_initially() {
		List<String> commandlist = commandStorage.getInvalidCommands();
		assertTrue(commandlist.isEmpty());
	}
}
