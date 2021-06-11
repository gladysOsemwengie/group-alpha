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
		//questions to get account type and opening balance
		//WHAT SORT OF ACCOUNT DO YOU WANT SAVINGS ISA DEPOSIT
		// CAPTURE ANSWER INTO

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
