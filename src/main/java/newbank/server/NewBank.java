package newbank.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;

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
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);
		
		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
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
					customers.get(customer.getKey()).addAccount(new Account("Steve", 555.0));
			        return "This is a test to prove we can add a second account, we'll want to call a func in reality";

			    case "MOVE" :
				    System.out.println("need to create MOVE method");
					return "Dear client we need to create MOVE method";
			    case "PAY" :
					System.out.println("need to create PAY method");
					return "Dear client we need to create the PAY method";
			    default : return "That command does not exist please try again";
			}
		}
		return "FAIL outside";
	}
	
	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

}
