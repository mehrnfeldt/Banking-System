public class CommandProcessor {

	private Bank bank;

	public CommandProcessor(Bank bank) {
		this.bank = bank;
	}

	public Account createSavings(String command) {
		String[] parts = command.toLowerCase().split(" ");
		Account account = new Savings(Double.parseDouble(parts[3]), parts[2]);
		bank.addAccount(account);
		return bank.getAccountById(parts[2]);
	}

	public Account createChecking(String command) {
		String[] parts = command.toLowerCase().split(" ");
		Account account = new Checking(Double.parseDouble(parts[3]), parts[2]);
		bank.addAccount(account);
		return bank.getAccountById(parts[2]);
	}

	public Account createCD(String command) {
		String[] parts = command.toLowerCase().split(" ");
		Account account = new CD(Double.parseDouble(parts[4]), Double.parseDouble(parts[3]), parts[2]);
		bank.addAccount(account);
		return bank.getAccountById(parts[2]);
	}

	public Account depositSavingsOrChecking(String command) {
		String[] parts = command.toLowerCase().split(" ");
		bank.deposit(parts[1], Integer.parseInt(parts[2]));
		bank.getAccountById(parts[1]).recordTransaction(command);
		return bank.getAccountById(parts[1]);
	}

	public Account withdrawAccount(String command) {
		String[] parts = command.toLowerCase().split(" ");
		bank.withdraw(parts[1], Double.parseDouble(parts[2]));
		bank.getAccountById(parts[1]).recordTransaction(command);
		return bank.getAccountById(parts[1]);
	}

	public Account transferAccount(String command) {
		String[] parts = command.toLowerCase().split(" ");
		bank.transfer(parts[1], parts[2], Integer.parseInt(parts[3]));
		bank.getAccountById(parts[1]).recordTransaction(command);
		bank.getAccountById(parts[2]).recordTransaction(command);
		return bank.getAccountById(parts[1]);
	}

	private void passTimeAccount(String command) {
		String[] parts = command.toLowerCase().split(" ");
		bank.passTime(Integer.parseInt(parts[1]));
	}

	public Account process(String command) {
		String[] parts = command.toLowerCase().split(" ");

		if (parts[1].equals("savings")) {
			return createSavings(command);
		}

		if (parts[1].equals("checking")) {
			return createChecking(command);
		}

		if (parts[1].equals("cd")) {
			return createCD(command);
		}

		if (parts[0].equals("deposit")) {
			return depositSavingsOrChecking(command);
		}

		if (parts[0].equals("withdraw")) {
			return withdrawAccount(command);
		}

		if (parts[0].equals("transfer")) {
			return transferAccount(command);
		}

		if (parts[0].equals("pass")) {
			passTimeAccount(command);
		}

		return null;
	}

}
