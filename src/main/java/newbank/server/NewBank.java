package newbank.server;

import java.util.HashMap;
import java.util.Scanner;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String, Customer> customers;
	private HashMap<CustomerID, Customer> customersValues;

	private static final HashMap<String, String> customerAuthenticationDetails = new HashMap<String, String>(){
		{
			//key is Username, Value is password
			put("Bhagy", "bhagy");
			put("Christina", "christina");
			put("John", "john");
		}
	};
	
	private NewBank() {
		customers = new HashMap<>();
		addTestData();
		this.customersValues = initialCustomerValues();
	}

	private HashMap<CustomerID, Customer> initialCustomerValues() {
		HashMap<CustomerID, Customer> newBankCustomers = new HashMap<>();
		CustomerID bhagyCustomerId = new CustomerID("Bhagy");
		Customer bhagyCustomer = new Customer(bhagyCustomerId);
		bhagyCustomer.addAccount(new Account("Main", 1000.0));

		CustomerID christinaCustomerId = new CustomerID("Christina");
		Customer christinaCustomer = new Customer(christinaCustomerId);
		christinaCustomer.addAccount(new Account("Savings", 1500.0));

		CustomerID johnCustomerId = new CustomerID("John");
		Customer johnCustomer = new Customer(johnCustomerId);
		johnCustomer.addAccount(new Account("Checking", 250.0));

		newBankCustomers.put(bhagyCustomerId, bhagyCustomer);
		newBankCustomers.put(christinaCustomerId, christinaCustomer);
		newBankCustomers.put(johnCustomerId,johnCustomer);
		return newBankCustomers;
	}

	private void addTestData() {
		Customer bhagy = new Customer(new CustomerID("Bhagy"));
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer(new CustomerID("Christina"));
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);
		
		Customer john = new Customer(new CustomerID("John"));
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
	}

	public HashMap<CustomerID, Customer> getCustomersValues() {
		return customersValues;
	}

	public static NewBank getBank() {
		return bank;
	}
	
	public synchronized CustomerID checkLogInDetails(String userName, String password) {

//		if(customers.containsKey(userName)) {
//			return new CustomerID(userName);
//		}
		//Changing this logic to check password + userName match
		if (customerAuthenticationDetails.containsKey(userName) && customerAuthenticationDetails.get(userName).equals(password)){
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
    	public synchronized String processRequest(CustomerID customer, String request) {
		if(customersValues.keySet().stream().anyMatch(customerID -> customerID.getKey().equals(customer.getKey()))) {
			switch(request) {
			    case "SHOWMYACCOUNTS" :
			    	//return showMyAccounts(customer);
					return anotherShowMyAccounts(customer);
			    case "NEWACCOUNT" :
			        System.out.println("need to create NEWACCOUNT method");

					//return createNewAccount(customer);
					return anotherCreateNewAccount(customer);

			    case "MOVE" :
				    System.out.println("need to create MOVE method");
					//return moveCashBetweenAccounts(customer);
					return anotherMove(customer);
				case "PAY":
					System.out.println("need to create PAY method");
					return pay(customer);
					//return payAnotherUser(customer);
			    default : return "That command does not exist please try again";
			}
		}
		return "FAIL outside";
	}
	
	private String showMyAccounts(CustomerID customer) {
		return customers.get(customer.getKey()).getAccounts().toString();
	}

	// THIS IS THE ORIGINAL SHOWMYACCOUNT FUNCTION
	/*	private String showMyAccounts(CustomerID customer) {

		return (customers.get(customer.getKey())).accountsToString();
	}
	*/

	private String anotherShowMyAccounts(CustomerID customerId){
		Customer customerValue = getCustomersValues().values().stream().filter(customer -> customer.getCustomerId().getKey().equals(customerId.getKey()))
				.findAny().orElse(null);
		if (null != customerValue){
			return customerValue.getAccounts().toString();
		}else{
			return "No customer exists for this customer name " + customerId.getKey();
		}
	}

	private String anotherMove(CustomerID customerId){
		Scanner sc = new Scanner(System.in);
		Customer customerValue = getCustomersValues().values().stream().filter(customer -> customer.getCustomerId().getKey().equals(customerId.getKey()))
				.findAny().orElse(null);

		//Get move details
		System.out.println("Which account to do you want to move money to");
		String toAccountName = sc.next();
		Account toAccount = checkAccountExists(toAccountName, customerValue);
		System.out.println("Which account to do you want to move money from");
		String fromAccountName = sc.next();
		Account fromAccount = checkAccountExists(fromAccountName, customerValue);
		System.out.println("Enter amount you wish to transfer");
		double transferAmount = sc.nextDouble();

		//the move logic starts
		if (null != fromAccount && fromAccount.getBalance() > 0){
			assert toAccount != null;
			double amountToBeAdded = toAccount.getBalance() + transferAmount;
			toAccount.setBalance(amountToBeAdded);
			//then reduce the balance of the from account
			double amountToBeRemoved = fromAccount.getBalance() - transferAmount;
			fromAccount.setBalance(amountToBeRemoved);

			//then update newBank Customer values to reflect change
			assert customerValue != null;
			updateNewBankCustomerValues(customerValue, fromAccount);

			return toAccount.getAccountName() + " has been paid, Your Old " + fromAccountName +  " balance is now " + fromAccount.getBalance();

		}else {
			return "Insufficient Funds in your account";
		}

	}

	private String anotherCreateNewAccount(CustomerID customerId){
		Scanner sc = new Scanner(System.in);
		Customer customerValue = getCustomersValues().values().stream().filter(customer -> customer.getCustomerId().getKey().equals(customerId.getKey()))
				.findAny().orElse(null);

		//Get new account details
		System.out.println("Enter account name");
		String accountName = sc.next();
		if (checkAccountAlreadyExists(accountName, customerValue)){
			System.out.println("An account with the name " + accountName + " already exists");
		}else{
			System.out.println("Enter amount you want to open with");
		}
		double openingAmount = sc.nextDouble();
		if (null != customerValue){
			Account newAccount = new Account(accountName, openingAmount);
			//update customer values DB
			updateNewBankCustomerValues(customerValue, newAccount);
			return "A new account called " + accountName + " has been created for " + customerValue.getCustomerId().getKey();
		}else{
			return "Could not create new account";
		}
	}






	public boolean checkAccountAlreadyExists(String accountName, Customer customerValue) {
		return null != customerValue && customerValue.getAccounts().stream().anyMatch(account -> account.getAccountName().equals(accountName));
	}

	private String createNewAccount(CustomerID customer) {
		//Questions to get account type and opening balance

		//1- WHAT TYPE OF ACCOUNT DO YOU WANT SAVINGS / ISA or CURRENT and HOW MUCH DO YOU WANT TO DEPOSIT? e.g. SAVINGS 200.00
		//2- CAPTURE RESPONSE FROM COMMAND LINE
		//3- SEND RESPONSE TO METHOD TO CREATE THE ACCOUNT
		//4- PRINT ALL ACCOUNTS RELATED TO USER
		//customers.get(customer.getKey()).;
		Customer customerName = new Customer(customer);

		customerName.addAccount(new Account("SavingsTest", 1000.0));
		customerName.addAccount(new Account("SavingsTest2", 2000.0));
		//return customerName.accountsToString();
		customers.put(customer.getKey(), customerName);

		return customerName.accountsToString();

	}

	private String moveCashBetweenAccounts (CustomerID customer) {
		//Write the SUDO code here we need to know the accounts they want to move money from and to.
		//Micheal said it had to be from the command line but i assume we can give prompts, like list of valid accounts
		//1 - PRINT LIST OF ALL ACCOUNTS - SHOWMYACCOUNTS
		//2 - PRINT STATEMENT : PLEASE ENTER the AMOUNT of Money you want to move followed by Move FROM Account and Move TO Account e.g. <Amount> <From> <To>
		//3 - CAPTURE RESPONSE for Amount
		//4 - CAPTURE RESPONSE for (FROM) ACCOUNT
		//5 - CAPTURE RESPONSE for (TO) ACCOUNT
		//6 - CALCULATE THE UPDATED BALANCES (ignore check for available balance) - optional
		//7 - PRINT UPDATED SHOWMYACCOUNTS with updated balances

		Customer customerName = new Customer(customer);
		return customerName.accountsToString();

	}

	private String payAnotherUser (CustomerID customer) {
		//Write the SUDO code here we need to know the accounts they want to move money from and who to and account
		// .


		Customer customerName = new Customer(customer);
		return customerName.accountsToString();

	}

	private String pay(CustomerID customerId){
		Scanner sc = new Scanner(System.in);
		Customer fromCustomer = getCustomersValues().values().stream().filter(customer -> customer.getCustomerId().getKey().equals(customerId.getKey()))
				.findAny().orElse(null);

		//Get details about payment
		System.out.println("Enter customer name you want to pay to");
		String customerName = sc.next();
		Customer toCustomer =  checkCustomerExists(customerName);
		System.out.println("Enter customer account name you want to pay to");
		String toAccountName = sc.next();
		Account toAccount = checkAccountExists(toAccountName, toCustomer);
		System.out.println("Enter which of your account names you want to pay from");
		String fromAccountName = sc.next();
		//Customer fromCustomer = checkCustomerExists(customer.getCustomerId().getKey());
		Account fromAccount = checkAccountExists(fromAccountName, fromCustomer);
		System.out.println("Enter amount you wish to transfer");
		double transferAmount = sc.nextDouble();

		//the pay logic starts
		if (null != fromAccount && fromAccount.getBalance() > 0){
			assert toAccount != null;
			double amountToBeAdded = toAccount.getBalance() + transferAmount;
			toAccount.setBalance(amountToBeAdded);
			//then reduce the balance of the from account
			double amountToBeRemoved = fromAccount.getBalance() - transferAmount;
			fromAccount.setBalance(amountToBeRemoved);

			//then update newBank Customer values to reflect change
			updateNewBankCustomerValues(fromCustomer, fromAccount);
			updateNewBankCustomerValues(toCustomer, toAccount);

			return toAccount.getAccountName() + " has been paid, Your balance is now " + fromAccount.getBalance();

		}else {
			return "Insufficient Funds in your account";
		}
	}

	/**
	 * if account exists for customer update the account details else, add a new account for the customer
	 * @param customer
	 * @param account
	 */
	private void updateNewBankCustomerValues(Customer customer, Account account) {
		assert customer != null;
		if (customer.getAccounts().contains(account)){
			getCustomersValues().get(customer.getCustomerId()).updateAccount(account);
		}else{
			getCustomersValues().get(customer.getCustomerId()).addAccount(account);
		}

	}

	private Account checkAccountExists(String accountName, Customer customer) {
		if (null != customer && customer.getAccounts().stream().anyMatch(account -> account.getAccountName().equals(accountName))){
			return customer.getAccounts().stream().filter(account -> account.getAccountName().equals(accountName))
					.findAny().orElse(null);
		}else{
			System.out.println("Account with that name " + accountName + " doesnt exist");
			return null;
		}
	}

	private Customer checkCustomerExists(String customerName) {
		if (getCustomersValues().keySet().stream().anyMatch(custId -> custId.getKey().equals(customerName))){
			return getCustomersValues().values().stream().filter(customer -> customer.getCustomerId().getKey().equals(customerName)).findAny().orElse(null);
		}else{
			System.out.println("Customer with that name " + customerName + " doesnt exist");
			return null;
		}
	}


}
