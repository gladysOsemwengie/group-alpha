package newbank.server;

public class Account {
	
	private String accountName;
	private double balance;

	public Account(String accountName, double balance) {
		this.accountName = accountName;
		this.balance = balance;
	}
	
	public String toString() {
		return (accountName + " : " + balance);
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
