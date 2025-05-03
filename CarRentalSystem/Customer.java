package CarRentalSystem;

public class Customer {
    //declaring fields
    private int customerID;
    private String customerName;

    //constructor declared
    public Customer(int customerID, String customerName){
        this.customerID = customerID;
        this.customerName = customerName;
    }

    //getter for customer's id
    public int getCustomerID(){
        return customerID;
    }

    //getter for customer's name
    public String getCustomerName(){
        return customerName;
    }

    //setter for customer's name
    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }
}
