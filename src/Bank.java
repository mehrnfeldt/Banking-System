import java.util.ArrayList;
import java.util.List;

public class Bank {

	private List<Account> accounts;

	public Bank() {
		this.accounts = new ArrayList<>();
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void addAccount(Account account) {
		accounts.add(account);
	}

	public Account getAccountById(String accountNumber) {
		for (Account account : accounts) {
			if (account.getAccountNumber().equals(accountNumber)) {
				return account;
			}
		}
		return null;
	}

	public void deposit(String accountNumber, int depositAmount) {
		Account account = this.getAccountById(accountNumber);
		account.deposit(depositAmount);
	}

	public void withdraw(String accountNumber, double withdrawAmount) {
		Account account = this.getAccountById(accountNumber);
		account.withdraw(withdrawAmount);
	}

	public void transfer(String fromID, String toID, int amount) {
		Account fromAccount = this.getAccountById(fromID);
		Account toAccount = this.getAccountById(toID);

		if (amount > fromAccount.getBalance()) {
			amount = (int) fromAccount.getBalance();
		}

		fromAccount.withdraw(amount);
		toAccount.deposit(amount);
	}

	public void passTime(int months) {
		for (int i = 0; i < months; i++) {
			List<Account> toRemove = new ArrayList<>();
			for (Account account : accounts) {
				if (account instanceof Savings) {
					((Savings) account).setWithdrawnThisMonth(false);
				}
				if (account instanceof CD) {
					((CD) account).incrementMonthsPassed();
				}
				if (account.getBalance() == 0) {
					toRemove.add(account);
				}
				if (account.getBalance() < 100) {
					account.withdraw(25);
				}

				account.accrueInterest();

			}
			accounts.removeAll(toRemove);
		}
	}

}