package newbank.server;

import java.util.*;

import static java.util.Arrays.asList;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String, List<Customer>> customers;

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
	}
	
	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", Collections.singletonList(bhagy));
		
		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", Collections.singletonList(christina));
		
		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", Collections.singletonList(john));
	}

	//todo -this returns the customer and password details
	public static Map<String, String> getCustomerAuthenticationDetails(){
		return Collections.unmodifiableMap(customerAuthenticationDetails);
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
	//todo -- i dont think this should return string -- this should be a void
    	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
			    case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
			    case "NEWACCOUNT" :
			        System.out.println("need to create NEWACCOUNT method");
					//customers.get(customer.getKey()).addAccount(new Account("Saving Acc", 555.0));

					return createNewAccount(customer);

			    case "MOVE" :
				    System.out.println("need to create MOVE method");
					return moveCashBetweenAccounts(customer);
				case "PAY":
					System.out.println("need to create PAY method");
					return payAnotherUser(customer);
			    default : return "That command does not exist please try again";
			}
		}
		return "FAIL outside";
	}
	
	private String showMyAccounts(CustomerID customer) {
		List<String> accountstemp = new ArrayList<>();
		customers.get(customer.getKey()).forEach(customer1 -> {
			accountstemp.add(customer1.accountsToString());
		});
		return accountstemp.toString();
	}

	// THIS IS THE ORIGINAL SHOWMYACCOUNT FUNCTION
	/*	private String showMyAccounts(CustomerID customer) {

		return (customers.get(customer.getKey())).accountsToString();
	}
	*/
	private String createNewAccount(CustomerID customer) {
		//Questions to get account type and opening balance

		//1- WHAT TYPE OF ACCOUNT DO YOU WANT SAVINGS / ISA or CURRENT and HOW MUCH DO YOU WANT TO DEPOSIT? e.g. SAVINGS 200.00
		//2- CAPTURE RESPONSE FROM COMMAND LINE
		//3- SEND RESPONSE TO METHOD TO CREATE THE ACCOUNT
		//4- PRINT ALL ACCOUNTS RELATED TO USER
		//customers.get(customer.getKey()).;
		Customer customerName = new Customer();

		customerName.addAccount(new Account("SavingsTest", 1000.0));
		customerName.addAccount(new Account("SavingsTest2", 2000.0));
		return customerName.accountsToString();
		//customerName.addAccount("Fred", 44.44);
		//customers.put(customer.getKey(),asList(customerName));
		//customers.put(customer.getKey(), Collections.singletonList(customerName));
		//customers.get(customer.getKey()).addAccount(new Account("Savings", 555.0));
		//return showMyAccounts(customer);
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

		Customer customerName = new Customer();
		return customerName.accountsToString();

	}

	private String payAnotherUser (CustomerID customer) {
		//Write the SUDO code here we need to know the accounts they want to move money from and who to and account
		// .


		Customer customerName = new Customer();
		return customerName.accountsToString();

	}


}
