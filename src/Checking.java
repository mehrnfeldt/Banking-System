public class Checking extends Account {

	public Checking(double apr, String accountNumber) {
		super(apr, accountNumber);
	}

	@Override
	public boolean canDeposit(double amount) {
		return amount <= 1000 && amount >= 0;
	}
}
