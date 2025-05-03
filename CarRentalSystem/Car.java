package CarRentalSystem;

public class Car {
    //fields declared
    private String carID;
    private String brand;
    private String model;
    private double basePrice;
    private boolean isAvailable;

    //constructor for carID
    public Car(String carID){
        this.carID = carID;
        // this.isAvailable = true;
    }

    //getter for car's id
    public String getCarID(){
        return carID;
    }

    //setter for car's id
    public void setCarID(String carID){
        this.carID = carID;
    }
    
    //getter for car's brand
    public String getBrand(){
        return brand;
    }

    //setter for car's model
    public void setBrand(String brand){
        this.brand = brand;
    }

    //getter for car's model
    public String getModel(){
        return model;
    }

    //setter for car's model
    public void setModel(String model){
        this.model = model;
    }

    //setting base price
    public void setBasePrice(double basePrice){
        this.basePrice = basePrice;
    }


    //method to calculate rent price
    public double calculateRentalPrice(int rentalDays){
        return basePrice * rentalDays;
    }

    //method to change availability status
    public boolean isAvailable(){
        return isAvailable;
    }

    //setter for isAvailable
    public void setAvailability(boolean isAvailable){
        this.isAvailable = isAvailable;
    }

    //method to change rent status
    public void rentCar(){
        isAvailable = false;
    }

    //method to change returned car's status
    public void returnCar(){
        isAvailable = true;
    }
}
