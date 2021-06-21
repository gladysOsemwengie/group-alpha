package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


public class NewBank {

    private static final NewBank bank = new NewBank();
    private HashMap<String, Customer> customers;
    private HashMap<CustomerID, Customer> customersValues;
    private HashMap<CustomerID, Loan> loanDB;

    private static final HashMap<String, String> customerAuthenticationDetails = new HashMap<String, String>() {
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
        this.loanDB = loanDB();
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
        newBankCustomers.put(johnCustomerId, johnCustomer);
        return newBankCustomers;
    }

    private HashMap<CustomerID, Loan> loanDB() {
        HashMap<CustomerID, Loan> loanDatabase = new HashMap<>();

        CustomerID bhagyCustomerId = new CustomerID("Bhagy");
        Loan bhagyloan = new Loan(bhagyCustomerId, 1, 0.03, 1200, 3);

        CustomerID christinaCustomerId = new CustomerID("Christina");
        Loan christinaLoan  = new Loan(christinaCustomerId, 2, 0.05, 1500, 4);

        CustomerID loraineCustomerId = new CustomerID("Loraine");
        Loan loraineloan = new Loan(loraineCustomerId, 3, 0.1, 2000, 5);

        CustomerID alwynCustomerId = new CustomerID("Alwyn");
        Loan alwynloan = new Loan(alwynCustomerId, 4, 0.15, 3500, 6);

        CustomerID stephenCustomerId = new CustomerID("Stephen");
        Loan stephenloan = new Loan(stephenCustomerId, 5, 0.2, 4000, 7);

        loanDatabase.put(bhagyCustomerId, bhagyloan);
        loanDatabase.put(christinaCustomerId, christinaLoan);
        loanDatabase.put(loraineCustomerId, loraineloan);
        loanDatabase.put(alwynCustomerId, alwynloan);
        loanDatabase.put(stephenCustomerId, stephenloan);

        return loanDatabase;
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
        //Changing this logic to check password + userName match
        if (customerAuthenticationDetails.containsKey(userName) && customerAuthenticationDetails.get(userName).equals(password)) {
            return new CustomerID(userName);
        }
        return null;
    }

    // commands from the NewBank customer are processed in this method
    public synchronized String processRequest(CustomerID customer, String request, BufferedReader in, PrintWriter out) throws IOException {
        if (customersValues.keySet().stream().anyMatch(customerID -> customerID.getKey().equals(customer.getKey()))) {
            switch (request) {
                case "SHOWMYACCOUNTS":
                    return showMyAccounts(customer);
                case "NEWACCOUNT":
                    return createNewAccount(customer, in, out);

                case "MOVE":

                    return move(customer, in, out);
                case "PAY":

                    return pay(customer, in, out);

                default:
                    return "That command does not exist please try again";
            }
        }
        return "FAIL outside";
    }


    private String showMyAccounts(CustomerID customerId) {
        Customer customerValue = getCustomersValues().values().stream().filter(customer -> customer.getCustomerId().getKey().equals(customerId.getKey()))
                .findAny().orElse(null);
        if (null != customerValue) {
            return customerValue.getAccounts().toString();
        } else {
            return "No customer exists for this customer name " + customerId.getKey();
        }
    }

    private String move(CustomerID customerId, BufferedReader in, PrintWriter out) throws IOException {
        Customer customerValue = getCustomersValues().values().stream().filter(customer -> customer.getCustomerId().getKey().equals(customerId.getKey()))
                .findAny().orElse(null);

        //Get move details
        out.println("Which account to do you want to move money to");
        String toAccountName = in.readLine();
        Account toAccount = checkAccountExists(toAccountName, customerValue);
        out.println("Which account to do you want to move money from");
        String fromAccountName = in.readLine();
        Account fromAccount = checkAccountExists(fromAccountName, customerValue);
        out.println("Enter amount you wish to transfer");
        double transferAmount = Double.parseDouble(in.readLine());

        //the move logic starts
        if (null != fromAccount && fromAccount.getBalance() > 0) {
            assert toAccount != null;
            double amountToBeAdded = toAccount.getBalance() + transferAmount;
            toAccount.setBalance(amountToBeAdded);
            //then reduce the balance of the from account
            double amountToBeRemoved = fromAccount.getBalance() - transferAmount;
            fromAccount.setBalance(amountToBeRemoved);

            //then update newBank Customer values to reflect change
            assert customerValue != null;
            updateNewBankCustomerValues(customerValue, fromAccount);

            return toAccount.getAccountName() + " has been paid, Your Old " + fromAccountName + " balance is now " + fromAccount.getBalance();

        } else {
            return "Insufficient Funds in your account";
        }

    }

    private String createNewAccount(CustomerID customerId, BufferedReader in, PrintWriter out) throws IOException {
        Customer customerValue = getCustomersValues().values().stream().filter(customer -> customer.getCustomerId().getKey().equals(customerId.getKey()))
                .findAny().orElse(null);

        //Get new account details
        out.println("Enter account name");
        String accountName = in.readLine();
        if (checkAccountAlreadyExists(accountName, customerValue)) {
            out.println("An account with the name " + accountName + " already exists");
        } else {
            out.println("Enter amount you want to open with");
        }
        double openingAmount = Double.parseDouble(in.readLine());
        if (null != customerValue) {
            Account newAccount = new Account(accountName, openingAmount);
            //update customer values DB
            updateNewBankCustomerValues(customerValue, newAccount);
            return "A new account called " + accountName + " has been created for " + customerValue.getCustomerId().getKey();
        } else {
            return "Could not create new account";
        }
    }


    public boolean checkAccountAlreadyExists(String accountName, Customer customerValue) {
        return null != customerValue && customerValue.getAccounts().stream().anyMatch(account -> account.getAccountName().equals(accountName));
    }


    private String pay(CustomerID customerId, BufferedReader in, PrintWriter out) throws IOException {
        Customer fromCustomer = getCustomersValues().values().stream().filter(customer -> customer.getCustomerId().getKey().equals(customerId.getKey()))
                .findAny().orElse(null);

        //Get details about payment
        out.println("Enter customer name you want to pay to");
        String customerName = in.readLine();
        Customer toCustomer = checkCustomerExists(customerName);
        out.println("Enter customer account name you want to pay to");
        String toAccountName = in.readLine();
        Account toAccount = checkAccountExists(toAccountName, toCustomer);
        out.println("Enter which of your account names you want to pay from");
        String fromAccountName = in.readLine();
        //Customer fromCustomer = checkCustomerExists(customer.getCustomerId().getKey());
        Account fromAccount = checkAccountExists(fromAccountName, fromCustomer);
        out.println("Enter amount you wish to transfer");
        double transferAmount = Double.parseDouble(in.readLine());

        //the pay logic starts
        if (null != fromAccount && fromAccount.getBalance() > 0) {
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

        } else {
            return "Insufficient Funds in your account";
        }
    }

    /**
     * if account exists for customer update the account details else, add a new account for the customer
     *
     * @param customer
     * @param account
     */
    private void updateNewBankCustomerValues(Customer customer, Account account) {
        assert customer != null;
        if (customer.getAccounts().contains(account)) {
            getCustomersValues().get(customer.getCustomerId()).updateAccount(account);
        } else {
            getCustomersValues().get(customer.getCustomerId()).addAccount(account);
        }

    }

    private Account checkAccountExists(String accountName, Customer customer) {
        if (null != customer && customer.getAccounts().stream().anyMatch(account -> account.getAccountName().equals(accountName))) {
            return customer.getAccounts().stream().filter(account -> account.getAccountName().equals(accountName))
                    .findAny().orElse(null);
        } else {
            System.out.println("Account with that name " + accountName + " doesnt exist");
            return null;
        }
    }

    private Customer checkCustomerExists(String customerName) {
        if (getCustomersValues().keySet().stream().anyMatch(custId -> custId.getKey().equals(customerName))) {
            return getCustomersValues().values().stream().filter(customer -> customer.getCustomerId().getKey().equals(customerName)).findAny().orElse(null);
        } else {
            System.out.println("Customer with that name " + customerName + " doesnt exist");
            return null;
        }
    }


    public HashMap<CustomerID, Loan> getLoanDB() {
        return loanDB;
    }
}
