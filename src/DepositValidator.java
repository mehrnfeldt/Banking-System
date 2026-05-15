public class DepositValidator {
	private Bank bank;

	public DepositValidator(Bank bank) {
		this.bank = bank;
	}

	private boolean validBalance(String balance) {
		try {
			double amount = Double.parseDouble(balance);
			if (amount < 0) {
				return false;
			} else {
				return true;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean validate(String command) {
		String[] parts = command.toLowerCase().split(" ");

		Account account = bank.getAccountById(parts[1]);

		if (parts[0].equals("deposit") && parts[1].matches("\\d{8}") && validBalance(parts[2])) {

			if (account == null) {
				return false;
			}

			return account.canDeposit(Double.parseDouble(parts[2]));
		}
		return false;

	}
}
