package newbank.server;

public class Loan {
    private CustomerID customerID;
    private int loanID;
    private double loanInterest;
    private double loanAmount;
    private int loanYears;

    // Constructer

    public Loan(CustomerID customerID, int loanID, double loanInterest, double loanAmount, int loanYears) {
        this.customerID = customerID;
        this.loanID = loanID;
        this.loanInterest = loanInterest;
        this.loanAmount = loanAmount;
        this.loanYears = loanYears;

    }

    // Getters
    public CustomerID getCustomerID() {
        return customerID;
    }

    public int getloanID() {
        return loanID;
    }

    public double getLoanInterest() {
        return loanInterest;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public int loanYears() {
        return loanYears;
    }

    public double getTotalRepaymentAmount() {
        double amount = loanAmount;

        for (int i = 0; i < loanYears-1; i++) {
            double toAdd = (amount / 100) * loanInterest;
            amount += toAdd;
        }

        return amount;

    }

    // Setters

    public void setCustomerID (CustomerID customerID) {
        this.customerID = customerID;
    }

    // Created as a possible setter, but not sure this should be used
    public void setloanID (int loanID) {
        this.loanID = loanID;
    }

    public void setLoanInterest (double loanInterest) {
        this.loanInterest =loanInterest;
    }

    public void setLoanAmount (double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setLoanYears (int loanYears) {
        this.loanYears = loanYears;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "customerID=" + customerID +
                ", loanID=" + loanID +
                ", loanInterest=" + loanInterest +
                ", loanAmount=" + loanAmount +
                ", loanYears=" + loanYears +
                '}';
    }
}
