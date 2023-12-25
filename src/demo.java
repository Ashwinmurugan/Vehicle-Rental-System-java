import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class demo {
    static String admin_role = "administrator";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3307/rental_car_project";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "UR PASSWORD";
	private static final int BIKE_SECURITY_DEPOSIT = 10000;
	private static final int CAR_SECURITY_DEPOSIT = 30000;
	private static List<String> checkoutCart = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n<!------- Welcome To Car Rental Service ------!>\n");
        while (true) {
            System.out.println("1. Login as Borrower");
            System.out.println("2. Login as Administrator");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    registerUser("user_login");
                    break;
                case 2:
                    registerUser("admin_info");
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    //register User
    private static void registerUser(String tableName) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(); // Add a blank line for better separation

        if (tableName.equals("user_login")) {
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                String sql = "INSERT INTO " + tableName + " (userName, userPassword) VALUES (?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Registration successful.");
                        userMenu();
                        
                    } else {
                        System.out.println("Failed to register. Please try again.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                String sql = "INSERT INTO " + tableName + " (AdminName, AdminPassword) VALUES (?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Registration successful.");
                        adminMenu();
                    } else {
                        System.out.println("Failed to register. Please try again.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    
    //Admin menu
    private static void adminMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Modify Vehicle Specifications");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");

            int adminChoice = sc.nextInt();

            switch (adminChoice) {
                case 1:
                    addVehicle();
                    break;
                case 2:
                    modifyVehicle();
                    break;
                case 3:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    
    public static void addVehicle() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(); // Add a blank line for better separation
        System.out.print("Enter Vehicle Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Vehicle Type (Car/Bike): ");
        String type = scanner.nextLine();

        System.out.print("Enter Vehicle Number Plate: ");
        String numberPlate = scanner.nextLine();

        System.out.print("Enter Available Count: ");
        int availableCount = scanner.nextInt();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO vehicles (name, type, number_plate, available_count) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, type);
                preparedStatement.setString(3, numberPlate);
                preparedStatement.setInt(4, availableCount);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Vehicle added successfully.");
                } else {
                    System.out.println("Failed to add the vehicle.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static void modifyVehicle() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(); // Add a blank line for better separation
        System.out.print("Enter Vehicle Name to modify: ");
        String name = scanner.nextLine();

        System.out.println("Choose modification option:");
        System.out.println("1. Modify Available Count");
        System.out.println("2. Delete Vehicle");
        System.out.print("Enter your choice: ");
        int modificationChoice = scanner.nextInt();

        switch (modificationChoice) {
            case 1:
                modifyAvailableCount(name);
                break;
            case 2:
                deleteVehicle(name);
                break;
            default:
                System.out.println("Invalid choice. No modifications made.");
        }
    }

    
    private static void modifyAvailableCount(String name) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter new Available Count: ");
        int newAvailableCount = scanner.nextInt();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "UPDATE vehicles SET available_count = ? WHERE name = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, newAvailableCount);
                preparedStatement.setString(2, name);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Available Count modified successfully.");
                } else {
                    System.out.println("Vehicle not found. No modifications made.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteVehicle(String name) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "DELETE FROM vehicles WHERE name = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Vehicle deleted successfully.");
                } else {
                    System.out.println("Vehicle not found. No deletions made.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    //usermenu
    private static void userMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\nRenter Menu:");
            System.out.println("1. View Available Vehicles");
            System.out.println("2. Add/Remove Vehicle from Checkout Cart");
            System.out.println("3. Checkout Vehicles");
            System.out.println("4. Return to Main Menu");
            System.out.println("5. Return Vehicle");
            System.out.print("Enter your choice: ");
            
            int renterChoice = sc.nextInt();

            switch (renterChoice) {
            case 1:
                viewAvailableVehicles();
                break;
            case 2:
            	 modifyCheckoutCart(checkoutCart );    
            	break;
            case 3:
            	checkoutVehicles(checkoutCart);
                break;
            case 4:
                System.out.println("Returning to the Main Menu...");
                return;
            case 5:
                returnVehicle();
                break;  
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        }
    }

    private static void viewAvailableVehicles() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT * FROM vehicles WHERE available_count > 0";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No available vehicles at the moment.");
                } else {
                    System.out.println("\nAvailable Vehicles:");
                    System.out.printf("%-20s%-15s%-15s%-15s\n", "Name", "Type", "Number Plate", "Available Count");
                    while (resultSet.next()) {
                        System.out.printf("%-20s%-15s%-15s%-15s\n",
                                resultSet.getString("name"),
                                resultSet.getString("type"),
                                resultSet.getString("number_plate"),
                                resultSet.getInt("available_count"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void modifyCheckoutCart(List<String> checkoutCart) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nCheckout Cart:");
        for (int i = 0; i < checkoutCart.size(); i++) {
            System.out.println((i + 1) + ". " + checkoutCart.get(i));
        }

        System.out.println("1. Add Vehicle to Cart");
        System.out.println("2. Remove Vehicle from Cart");
        System.out.println("3. Go Back");
        System.out.print("Enter your choice: ");

        int cartChoice = scanner.nextInt();

        switch (cartChoice) {
            case 1:
                addVehicleToCart(checkoutCart);
                break;
            case 2:
                removeVehicleFromCart(checkoutCart);
                break;
            case 3:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    private static void addVehicleToCart(List<String> checkoutCart) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter the name of the vehicle to add to the cart: ");
        String vehicleName = scanner.nextLine();

        if (!checkoutCart.contains(vehicleName)) {
            checkoutCart.add(vehicleName);
            System.out.println("Vehicle added to the cart.");
        } else {
            System.out.println("Vehicle is already in the cart.");
        }
    }

    private static void removeVehicleFromCart(List<String> checkoutCart) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter the name of the vehicle to remove from the cart: ");
        String vehicleName = scanner.nextLine();

        if (checkoutCart.contains(vehicleName)) {
            checkoutCart.remove(vehicleName);
            System.out.println("Vehicle removed from the cart.");
        } else {
            System.out.println("Vehicle is not in the cart.");
        }
    }

    private static int getSecurityDeposit(String vehicleName) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String getSecurityDepositSql = "SELECT type FROM vehicles WHERE name = ?";
            try (PreparedStatement getSecurityDepositStatement = connection.prepareStatement(getSecurityDepositSql)) {
                getSecurityDepositStatement.setString(1, vehicleName);
                ResultSet resultSet = getSecurityDepositStatement.executeQuery();

                if (resultSet.next()) {
                    String vehicleType = resultSet.getString("type");
                    return "Bike".equalsIgnoreCase(vehicleType) ? BIKE_SECURITY_DEPOSIT : CAR_SECURITY_DEPOSIT;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
    private static void checkoutVehicles(List<String> checkoutCart) {
        if (checkoutCart.isEmpty()) {
            System.out.println("Checkout cart is empty. Add vehicles before proceeding to checkout.");
            return;
        }

        int totalSecurityDeposit = 0;
        for (String vehicleName : checkoutCart) {
            totalSecurityDeposit += getSecurityDeposit(vehicleName);
        }
        if (totalSecurityDeposit < BIKE_SECURITY_DEPOSIT || totalSecurityDeposit < CAR_SECURITY_DEPOSIT) {
            System.out.println("Insufficient security deposit. Please add more vehicles or remove some from the cart.");
            return;
        }
        System.out.println("Deposit Done Successful!");
        System.out.println("Checkout successful! Enjoy your ride!");
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            for (String vehicleName : checkoutCart) {
                String updateAvailabilitySql = "UPDATE vehicles SET available_count = available_count - 1 WHERE name = ?";
                try (PreparedStatement updateAvailabilityStatement = connection.prepareStatement(updateAvailabilitySql)) {
                    updateAvailabilityStatement.setString(1, vehicleName);
                    updateAvailabilityStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        checkoutCart.clear();
    }
    private static void returnVehicle() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter the name of the vehicle to return: ");
        String vehicleName = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String updateAvailabilitySql = "UPDATE vehicles SET available_count = available_count + 1 WHERE name = ?";
            try (PreparedStatement updateAvailabilityStatement = connection.prepareStatement(updateAvailabilitySql)) {
                updateAvailabilityStatement.setString(1, vehicleName);
                int rowsAffected = updateAvailabilityStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Vehicle returned successfully.");

                    // Ask for vehicle condition
                    System.out.print("Enter the condition of the vehicle (Good/Damaged): ");
                    String condition = scanner.nextLine();

                    if ("Good".equalsIgnoreCase(condition)) {
                        System.out.println("Deposit returned successfully.");
                    } else if ("Damaged".equalsIgnoreCase(condition)) {
                        System.out.println("Condition: Damaged. Fine of ₹10,000 applied.");
                    } else {
                        System.out.println("Invalid condition. Please enter 'Good' or 'Damaged'.");
                    }
                } else {
                    System.out.println("Vehicle not found. Return process failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}