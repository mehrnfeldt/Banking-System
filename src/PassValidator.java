public class PassValidator {

	public PassValidator() {
	}

	private boolean validAmount(String months) {
		try {
			int amount = Integer.parseInt(months);
			if (amount < 0 || amount > 60) {
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

		if (parts[0].equals("pass") && validAmount(parts[1])) {
			return true;
		}

		return false;
	}

}
