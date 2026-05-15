public class CommandValidator {
	private final CreateValidator createValidator;
	private final DepositValidator depositValidator;
	private final WithdrawValidator withdrawValidator;
	private final TransferValidator transferValidator;
	private final PassValidator passValidator;

	public CommandValidator(Bank bank) {
		this.createValidator = new CreateValidator(bank);
		this.depositValidator = new DepositValidator(bank);
		this.withdrawValidator = new WithdrawValidator(bank);
		this.transferValidator = new TransferValidator(bank);
		this.passValidator = new PassValidator();
	}

	public boolean validate(String command) {
		String[] parts = command.toLowerCase().split(" ");

		if (parts[0].equals("create")) {
			return createValidator.validate(command);
		}

		if (parts[0].equals("deposit")) {
			return depositValidator.validate(command);
		}

		if (parts[0].equals("withdraw")) {
			return withdrawValidator.validate(command);
		}

		if (parts[0].equals("transfer")) {
			return transferValidator.validate(command);
		}

		if (parts[0].equals("pass")) {
			return passValidator.validate(command);
		}

		return false;

	}

}
