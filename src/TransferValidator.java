public class TransferValidator {
	private final DepositValidator depositValidator;
	private final WithdrawValidator withdrawValidator;
	private Bank bank;

	public TransferValidator(Bank bank) {
		this.bank = bank;
		this.depositValidator = new DepositValidator(bank);
		this.withdrawValidator = new WithdrawValidator(bank);
	}

	private boolean validAmount(String balance) {
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

		Account fromAccount = bank.getAccountById(parts[1]);
		Account toAccount = bank.getAccountById(parts[2]);

		if (parts[0].equals("transfer") && parts[1].matches("\\d{8}") && parts[2].matches("\\d{8}")
				&& validAmount(parts[3])) {
			if (fromAccount == null || toAccount == null) {
				return false;
			}

			if (fromAccount.getClass().equals(CD.class) || toAccount.getClass().equals(CD.class)) {
				return false;
			}

			String withdraw = "withdraw " + parts[1] + " " + parts[3];
			String deposit = "deposit " + parts[2] + " " + parts[3];
			if (!withdrawValidator.validate(withdraw) || !depositValidator.validate(deposit)) {
				return false;
			}

			return true;

		}

		return false;
	}
}