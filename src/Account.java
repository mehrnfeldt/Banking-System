import java.util.ArrayList;
import java.util.List;

public abstract class Account {
	private double apr;
	private double balance;
	private String accountNumber;
	private List<String> transactionHistory = new ArrayList<>();

	public Account(double apr, String accountNumber) {
		this.accountNumber = accountNumber;
		this.apr = apr;
		this.balance = 0;
	}

	public Account(double balance, double apr, String accountNumber) {
		this.balance = balance;
		this.accountNumber = accountNumber;
		this.apr = apr;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = truncateToTwoDecimals(balance);
	}

	public double getApr() {
		return apr;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void deposit(double depositAmount) {
		this.setBalance(this.balance + depositAmount);
		this.setBalance(truncateToTwoDecimals(this.balance));
	}

	public void withdraw(double withdrawAmount) {
		if (withdrawAmount > this.balance) {
			this.setBalance(0);
		} else {
			this.setBalance(this.balance - withdrawAmount);
			this.setBalance(truncateToTwoDecimals(this.balance));
		}
	}

	public void accrueInterest() {
		double rate = (apr / 100) / 12;
		this.balance = (this.balance * rate) + this.balance;
		this.setBalance(truncateToTwoDecimals(this.balance));
	}

	private double truncateToTwoDecimals(double value) {
		return Math.floor(value * 100) / 100.0;
	}

	public List<String> getTransactionHistory() {
		return transactionHistory;
	}

	public void recordTransaction(String command) {
		transactionHistory.add(command);
	}

	public boolean canDeposit(double amount) {
		return false;
	}
}