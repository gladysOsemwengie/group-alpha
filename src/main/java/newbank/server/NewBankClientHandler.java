package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread{
	
	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	
	
	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}
	
	public void run() {
		// keep getting requests from the client and processing them
		try {
			// ask for user name
			out.println("Enter Username");
			String userName = in.readLine();
			// ask for password
			out.println("Enter Password");
			String password = in.readLine();
			out.println("Checking Details...");
			// authenticate user and get customer ID token from bank for use in subsequent requests
			CustomerID customer = bank.checkLogInDetails(userName, password);
			// if the user is authenticated then get requests from the user and process them 
			if(customer != null) {
				out.println("Log In Successful. What do you want to do?");
				while(true) {
					String request = in.readLine();
					if (request.equals("hello")){
					    out.println("Customer typed hello and I'm echoing it back");
                    }
					else {
                        System.out.println("Request from " + customer.getKey());
                        String responce = bank.processRequest(customer, request);
                        out.println(responce);
                    }
				}
			}
			else {
				out.println("Log In Failed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

    private String anotherCreateNewAccountSC(CustomerID customerId){

        out.println("Enter account name");
        String accountName = sc.next();
        if (bank.checkAccountAlreadyExists(accountName, customerValue)){
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


}
