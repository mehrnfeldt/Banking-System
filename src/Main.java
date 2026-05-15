import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		Bank bank = new Bank();
		CommandValidator commandValidator = new CommandValidator(bank);
		CommandProcessor commandProcessor = new CommandProcessor(bank);
		CommandStorage commandStorage = new CommandStorage();
		MasterControl masterControl = new MasterControl(commandValidator, commandProcessor, commandStorage, bank);

		List<String> input = Arrays.asList("Create savings 12345678 0.6", "Deposit 12345678 700",
				"Deposit 12345678 5000", "creAte cHecKing 98765432 0.01", "Deposit 98765432 300",
				"Transfer 98765432 12345678 300", "Pass 1", "Create cd 23456789 1.2 2000");

		List<String> output = masterControl.start(input);

		for (String line : output) {
			System.out.println(line);
		}
	}
}
