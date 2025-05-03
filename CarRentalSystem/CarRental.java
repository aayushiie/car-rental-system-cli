package CarRentalSystem;

import java.util.Scanner;
import java.io.IOException;
import java.sql.*;

public class CarRental {
    // fields declared
    private Customer customer;
    private Car car;
    public int daysToRent;

    // constructor declared
    public CarRental() {
        // database details
        try {
            DBConfig config = new DBConfig("config.properties");

            // getting customer's name
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String name = sc.nextLine();

            // customer name setter
            this.customer = new Customer(0, name);
            customer.setCustomerName(name);

            // connecting to driver manager
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }

            try {
                // connection to database
                Connection conn = DriverManager.getConnection(config.getUrl(), config.getUsername(),
                        config.getPassword());
                Statement stmt = conn.createStatement();

                // menu to either rent or return
                while (true) {
                    System.out.println("----CAR RENTAL----");
                    System.out.println("1. Rent a car");
                    System.out.println("2. Return a car");
                    System.out.println("3. Exit");
                    int menuChoice = sc.nextInt();

                    switch (menuChoice) {
                        case 1:
                            System.out.println("----RENTING A CAR----");
                            // inserting details of customer into the database
                            String query1 = String.format("INSERT INTO CUSTOMER(customerName) values('%s')", name);
                            int rowsAffected = stmt.executeUpdate(query1);
                            if (rowsAffected > 0) {
                                System.out.println("You've been registered.");
                            } else {
                                System.out.println("Registration failed.");
                            }

                            // displaying available car from db
                            String query2 = "SELECT * FROM CAR";
                            ResultSet resultSet1 = stmt.executeQuery(query2);
                            System.out.println("\nCarID\t\tBrand\t\tModel\t\tBase Price");
                            while (resultSet1.next()) {
                                String carID = resultSet1.getString("carid");
                                String brand = resultSet1.getString("brand");
                                String model = resultSet1.getString("model");
                                int basePrice = resultSet1.getInt("basePrice");
                                boolean available = resultSet1.getInt("isAvailable") == 1;
                                System.out.println(carID + "\t" + brand + "\t\t" + model + "\t\t" + basePrice);
                            }

                            // selecting car to rent
                            sc.nextLine();
                            System.out.print("\nEnter the car ID to rent: ");
                            String carChoice = sc.nextLine();
                            this.car = new Car(carChoice);
                            car.setCarID(carChoice);

                            // testing purposes
                            // System.out.println(car.getCarID());
                            // System.out.println(car.isAvailable());

                            // getting details of selected car from db
                            String query3 = "SELECT * FROM CAR WHERE CARID = ?";
                            PreparedStatement pstmt1 = conn.prepareStatement(query3);
                            pstmt1.setString(1, carChoice);
                            ResultSet resultSet2 = pstmt1.executeQuery();

                            if (resultSet2.next()) {
                                String brand = resultSet2.getString("brand");
                                String model = resultSet2.getString("model");
                                double basePrice = resultSet2.getDouble("basePrice");
                                int availability = resultSet2.getInt("isAvailable");

                                car.setBrand(brand);
                                car.setModel(model);
                                car.setBasePrice(basePrice);
                                car.setAvailability(availability == 1);

                                if (!car.isAvailable()) {
                                    System.out.println("This car is already rented.");
                                    break;
                                }

                                // taking no. of days to rent and calculating price
                                System.out.print("Enter the number of days to rent the car: ");
                                daysToRent = sc.nextInt();
                                sc.nextLine();
                                double rentalPrice = car.calculateRentalPrice(daysToRent);

                                // receipt
                                System.out.println("\nCar has successfully been rented.\n");
                                System.out.println("---Receipt---");
                                System.out.println("Customer Name: " + name);
                                System.out.println("Car: " + car.getBrand() + " " + car.getModel());
                                System.out.println("Days renting: " + daysToRent);
                                System.out.println("Price:" + rentalPrice);

                                // changing availability status
                                // car.rentCar(); // not required but kept for future reference
                                String query4 = String.format("UPDATE CAR SET ISAVAILABLE = %d WHERE CARID = '%s'", 0,
                                        carChoice);
                                int rowsAffected2 = stmt.executeUpdate(query4);
                                if (rowsAffected2 > 0) {
                                    System.out.println("Car has been rented successfully.\n");
                                } else {
                                    System.out.println("Car could not be rented. Try again.\n");
                                }
                            } else {
                                System.out.println("Car with ID " + carChoice + " not found.");
                            }
                            break;

                        case 2:
                            System.out.println("----RETURNING A CAR----");
                            // taking car's ID
                            System.out.print("Enter car ID: ");
                            sc.nextLine();
                            String carId = sc.nextLine();

                            // linking db to memory
                            Car car = new Car(carId);

                            // fetching the car details from db
                            String query4 = "SELECT * FROM CAR WHERE CARID = ?";
                            PreparedStatement pstmt2 = conn.prepareStatement(query4);
                            pstmt2.setString(1, carId);
                            ResultSet resultSet3 = pstmt2.executeQuery();

                            if (resultSet3.next()) {
                                String brand = resultSet3.getString("brand");
                                String model = resultSet3.getString("model");
                                double basePrice = resultSet3.getDouble("basePrice");
                                int availability = resultSet3.getInt("isAvailable");

                                car.setBrand(brand);
                                car.setModel(model);
                                car.setBasePrice(basePrice);
                                car.setAvailability(availability == 0);

                                if (!car.isAvailable()) {
                                    System.out.println("Car is not rented.");
                                    break;
                                }
                                System.out.println("Thank you for returning the car.");

                                // changing availability of car
                                String query5 = String.format("UPDATE CAR SET ISAVAILABLE = %d WHERE CARID = '%s'", 1,
                                        carId);
                                int rowsAffected3 = pstmt2.executeUpdate(query5);
                                if (rowsAffected3 > 0) {
                                    System.out.println("Car has been returned successfully\n");
                                } else {
                                    System.out.println("Car could not be returned. Try again.\n");
                                }
                            } else {
                                System.out.println("Car with id " + carId + " not found.");
                            }
                            break;

                        case 3:
                            System.out.println("\nThank you.\nExiting...");
                            return;

                        default:
                            System.out.println("Invalid choice.\n");
                            break;
                    }

                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            sc.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
