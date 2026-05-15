public class CD extends Account {
	private int monthsPassed;

	public CD(double balance, double apr, String accountNumber) {
		super(balance, apr, accountNumber);
		this.monthsPassed = 0;
	}

	@Override
	public void accrueInterest() {
		double rate = (getApr() / 100) / 12;
		for (int i = 0; i < 4; i++) {
			this.setBalance(getBalance() + (getBalance() * rate));
		}
		this.setBalance(Math.floor(getBalance() * 100) / 100);
	}

	public boolean canWithdraw() {
		if (monthsPassed < 12) {
			return false;
		}
		return true;
	}

	public void incrementMonthsPassed() {
		monthsPassed++;
	}

	public int getMonthsPassed() {
		return monthsPassed;
	}

	@Override
	public void withdraw(double amount) {
		if (canWithdraw()) {
			super.withdraw(this.getBalance());
		}
	}
}
