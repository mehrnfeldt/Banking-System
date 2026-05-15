public class Savings extends Account {
	private boolean withdrawnThisMonth = false;

	public Savings(double apr, String accountNumber) {
		super(apr, accountNumber);

	}

	public boolean hasWithdrawnThisMonth() {
		return withdrawnThisMonth;
	}

	public void setWithdrawnThisMonth(boolean withdrawn) {
		this.withdrawnThisMonth = withdrawn;
	}

	@Override
	public void withdraw(double withdrawAmount) {
		if (!withdrawnThisMonth) {
			setWithdrawnThisMonth(true);
			if (withdrawAmount > getBalance()) {
				this.setBalance(0);
			} else {
				this.setBalance(getBalance() - withdrawAmount);
			}
		}
	}

	@Override
	public boolean canDeposit(double amount) {
		return amount <= 2500 && amount >= 0;
	}

}