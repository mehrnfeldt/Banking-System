import java.util.Set;

public class CreateValidator {

	private Bank bank;

	public CreateValidator(Bank bank) {
		this.bank = bank;
	}

	public boolean validNumber(String number) {
		return number.matches("\\d{8}") && bank.getAccountById(number) == null;
	}

	public boolean validCreate(String create) {
		return (create.equals("create"));
	}

	public boolean validAccount(String account) {
		Set<String> accountTypes = Set.of("savings", "checking", "cd");
		return (accountTypes.contains(account));
	}

	public boolean validAPR(String apr) {
		try {
			double aprValue = Double.parseDouble(apr);
			return aprValue >= 0 && aprValue <= 10;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean validBalance(String balance) {
		try {
			double cdBalance = Double.parseDouble(balance);
			if (cdBalance < 1000 || cdBalance > 10000) {
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

		if ((parts[1].equals("cd") && parts.length != 5) || (!parts[1].equals("cd") && (parts.length != 4))) {
			return false;
		}

		if (validCreate(parts[0]) && validAccount(parts[1]) && validNumber(parts[2]) && validAPR(parts[3])) {
			if (parts[1].equals("cd")) {
				if (!validBalance(parts[4])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
