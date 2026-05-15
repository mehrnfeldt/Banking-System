import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MasterControl {
	private CommandValidator commandValidator;
	private CommandProcessor commandProcessor;
	private CommandStorage commandStorage;
	private Bank bank;

	public MasterControl(CommandValidator commandValidator, CommandProcessor commandProcessor,
			CommandStorage commandStorage, Bank bank) {
		this.commandValidator = commandValidator;
		this.commandProcessor = commandProcessor;
		this.commandStorage = commandStorage;
		this.bank = bank;
	}

	public List<String> start(List<String> input) {
		for (String command : input) {
			if (commandValidator.validate(command)) {
				commandProcessor.process(command);
			} else {
				commandStorage.addInvalidCommand(command);
			}

		}

		List<String> output = new ArrayList<>();
		List<Account> accounts = bank.getAccounts();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		decimalFormat.setRoundingMode(RoundingMode.FLOOR);

		for (Account account : accounts) {
			String accountState = String.format("%s %s %s %s", account.getClass().getSimpleName().toLowerCase(),
					account.getAccountNumber(), decimalFormat.format(account.getBalance()),
					decimalFormat.format(account.getApr()));
			output.add(accountState);
			output.addAll(account.getTransactionHistory());
		}

		output.addAll(commandStorage.getInvalidCommands());

		return output;
	}

}
