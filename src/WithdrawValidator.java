public class WithdrawValidator {
	private Bank bank;

	public WithdrawValidator(Bank bank) {
		this.bank = bank;
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

		Account account = bank.getAccountById(parts[1]);

		if (parts[0].equals("withdraw") && parts[1].matches("\\d{8}") && validAmount(parts[2])) {

			if (account == null) {
				return false;
			}

			if (account.getClass().equals(CD.class)) {
				if (Double.parseDouble(parts[2]) < bank.getAccountById(parts[1]).getBalance()
						|| !((CD) account).canWithdraw()) {
					return false;
				}

			}

			if (account.getClass().equals(Savings.class)) {
				if (Double.parseDouble(parts[2]) > 1000 || ((Savings) account).hasWithdrawnThisMonth()) {
					return false;
				}
			}

			if (account.getClass().equals(Checking.class)) {
				if (Double.parseDouble(parts[2]) > 400) {
					return false;
				}
			}

			return true;
		}
		return false;

	}
}
