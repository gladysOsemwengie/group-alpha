package newbank.server;

import java.util.ArrayList;

public class Customer {
	
	private final ArrayList<Account> accounts;
	private CustomerID customerId;
	
	public Customer(CustomerID customerId) {
		this.customerId = customerId;
		accounts = new ArrayList<>();
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public CustomerID getCustomerId() {
		return customerId;
	}

	public void setCustomerId(CustomerID customerId) {
		this.customerId = customerId;
	}

	public void updateAccount(Account account){
		for (Account a : accounts){
			if (a.getAccountName().equals(account.getAccountName())){
				a.setBalance(account.getBalance());
			}
		}
	}

}
