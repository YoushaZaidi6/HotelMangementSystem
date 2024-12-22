import java.util.HashMap;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

abstract class HotelService {
    public abstract void execute(Scanner scanner);
    Scanner scanner = new Scanner(System.in);
    public void returnToDashboard() {
        System.out.println("\nPress Enter to return to the dashboard...");
        scanner.nextLine(); 
    }
}

class BookingDetails {
    String customerName;
    String roomType;
    int nights;
    int costPerNight;
    int roomNumber;

    public BookingDetails(String customerName, String roomType, int nights, int costPerNight, int roomNumber) {
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
        this.costPerNight = costPerNight;
        this.roomNumber = roomNumber;
    }

    public String toFileFormat() {
        return customerName + "\n" + roomType + "\n" + "Nights:" + nights + "\n" + "costPerNight:" + costPerNight + "$" + "\n" + "Room Number:" + roomNumber + "\n";
    }
}

class RoomBooking extends HotelService {
    private HashMap<Integer, BookingDetails> bookings;
    private HashMap<Integer, Boolean> roomAvailability;

    public RoomBooking(HashMap<Integer, BookingDetails> bookings, HashMap<Integer, Boolean> roomAvailability) {
        this.bookings = bookings;
        this.roomAvailability = roomAvailability;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n===ROOM BOOKING===\n");
        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine();

        System.out.println("Select Room Type:");
        System.out.println("1. Single Room (1000$/night)");
        System.out.println("2. Double Room (2000$/night)");
        System.out.println("3. Suite (5000$/night)");
        System.out.print("Enter your choice: ");
        int roomTypeChoice = scanner.nextInt();

        String roomType;
        int costPerNight;
        switch (roomTypeChoice) {
            case 1:
                roomType = "Single Room";
                costPerNight = 1000;
                break;
            case 2:
                roomType = "Double Room";
                costPerNight = 2000;
                break;
            case 3:
                roomType = "Suite";
                costPerNight = 5000;
                break;
            default:
                System.out.println("Invalid room type. Booking failed.");
                return;
        }

        System.out.print("Enter Number of Nights: ");
        int nights = scanner.nextInt();
        
        System.out.println("Current Room Availability:");

        int roomCount = 1;
        
        System.out.println("\n===Single Room Availability===\n");
        for (int i = 1; i <= 3; i++) {
            String status = roomAvailability.get(roomCount) ? "Available" : "Booked";
            System.out.println("Single Room " + roomCount + ": " + status);
            roomCount++;
        }
        
        System.out.println("\n===Double Room Availability===\n");
        for (int i = 1; i <= 3; i++) {
            String status = roomAvailability.get(roomCount) ? "Available" : "Booked";
            System.out.println("Double Room " + roomCount + ": " + status);
            roomCount++;
        }
        
        System.out.println("\n===Suite Availability===\n");
        for (int i = 1; i <= 3; i++) {
            String status = roomAvailability.get(roomCount) ? "Available" : "Booked";
            System.out.println("Suite " + roomCount + ": " + status);
            roomCount++;
        }
        
        System.out.print("\nEnter Room Number to book: ");
        int roomNumber = scanner.nextInt();
        
        if (!roomAvailability.containsKey(roomNumber) || !roomAvailability.get(roomNumber)) {
            System.out.println("Sorry, Room " + roomNumber + " is already booked. Please choose another room.");
            return;
        }

        roomAvailability.put(roomNumber, false);

        BookingDetails booking = new BookingDetails(customerName, roomType, nights, costPerNight, roomNumber);
        bookings.put(customerId, booking);

        System.out.println("\n*****BOOKING SUMMARY*****\n");
        System.out.println("Customer ID: " + customerId);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Room Type: " + roomType);
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Number of Nights: " + nights);
        System.out.println("Status: Booking Confirmed!");
        System.out.println("==============================\n");

        try (FileWriter writer = new FileWriter("C:\\Users\\ACC\\Desktop\\HotelMangementSystem\\bookings.txt", true)) {
            writer.write("*****BOOKING DETAILS*****\n");
            writer.write("Customer ID: " + customerId + "\n");
            writer.write("Room Number: " + roomNumber + "\n");
            writer.write("Customer Details: " + booking.toFileFormat() + "\n");
            System.out.println("Booking details saved to bookings.txt!");
        } catch (IOException e) {
            System.out.println("Error saving booking details: " + e.getMessage());
        }

        returnToDashboard();
    }

    public void setRoomAvailable(int roomNumber) {
        roomAvailability.put(roomNumber, true);
    }
}

class CheckOut extends HotelService {
    private HashMap<Integer, BookingDetails> bookings;
    private RoomBooking roomBooking; 

    public CheckOut(HashMap<Integer, BookingDetails> bookings, RoomBooking roomBooking) {
        this.bookings = bookings;
        this.roomBooking = roomBooking;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n===CUSTOMER CHECK-OUT===\n");
        System.out.print("Enter Customer ID for Check-Out: ");
        int customerId = scanner.nextInt();

        BookingDetails booking = bookings.get(customerId);
        if (booking == null) {
            System.out.println("No booking found for Customer ID: " + customerId);
            return;
        }

        System.out.println("\n===Booking Details===\n");
        System.out.println("Customer Name: " + booking.customerName);
        System.out.println("Room Type: " + booking.roomType);
        System.out.println("Number of Nights: " + booking.nights);
        System.out.println("Room Number: " + booking.roomNumber);

        System.out.println("Cost per Night: $" + booking.costPerNight);

        int totalBill = booking.costPerNight * booking.nights;
        System.out.println("\n*****BILL SUMMARY*****\n");
        System.out.println("Total Bill: $" + totalBill);
        System.out.println("\n=================================\n");
        System.out.print("Enter Room Number for Check-Out: ");
        int roomNumber = scanner.nextInt();
        roomBooking.setRoomAvailable(roomNumber);
        System.out.println("\n=================================\n");
        System.out.println("Check-Out successful! Thank you for staying with us!");
        System.out.println("\n===============================\n");


        try (FileWriter writer = new FileWriter("C:\\Users\\ACC\\Desktop\\HotelMangementSystem\\Bills.txt", true)) {
            writer.write("\n===CHECK-OUT DETAILS===\n");
            writer.write("\nCustomer ID: " + customerId + "\n");
            writer.write("Customer Name: " + booking.customerName + "\n");
            writer.write("Room Type: " + booking.roomType + "\n");
            writer.write("Room Number: " + booking.roomNumber + "\n");
            writer.write("Number of Nights: " + booking.nights + "\n");
            writer.write("Total Bill: $" + totalBill + "\n");
            writer.write("=================================\n");
            System.out.println("Check-out details saved to Bills.txt!");
        } catch (IOException e) {
            System.out.println("Error saving check-out details: " + e.getMessage());
        }
        bookings.remove(customerId); 
        returnToDashboard();
    }
}

class ModifyBooking extends HotelService {
    private HashMap<Integer, BookingDetails> bookings;

    public ModifyBooking(HashMap<Integer, BookingDetails> bookings) {
        this.bookings = bookings;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n===MODIFY BOOKING===");
        System.out.print("Enter Customer ID to Modify Booking: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); 
        
        BookingDetails booking = bookings.get(customerId);
        if (booking == null) {
            System.out.println("No booking found for Customer ID: " + customerId);
            return;
        }

        System.out.println("\nCurrent Booking Details:\n");
        System.out.println("Customer Name: " + booking.customerName);
        System.out.println("Room Type: " + booking.roomType);
        System.out.println("Number of Nights: " + booking.nights);
        System.out.println("Cost per Night: $" + booking.costPerNight);

        System.out.println("\nChoose option to modify:\n");
        System.out.println("1. Customer Name");
        System.out.println("2. Room Type");
        System.out.println("3. Number of Nights");
        System.out.print("Enter your choice: ");
        int modifyChoice = scanner.nextInt();
        scanner.nextLine(); 

        switch (modifyChoice) {
            case 1:
                System.out.print("Enter new Customer Name: ");
                String newName = scanner.nextLine();
                booking.customerName = newName;
                break;

            case 2:
                System.out.println("Select Room Type to change:");
                System.out.println("1. Single Room (1000$/night)");
                System.out.println("2. Double Room (2000$/night)");
                System.out.println("3. Suite (5000$/night)");
                System.out.print("Enter your choice: ");
                int newRoomTypeChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (newRoomTypeChoice) {
                    case 1:
                        booking.roomType = "Single Room";
                        booking.costPerNight = 1000;
                        break;
                    case 2:
                        booking.roomType = "Double Room";
                        booking.costPerNight = 2000;
                        break;
                    case 3:
                        booking.roomType = "Suite";
                        booking.costPerNight = 5000;
                        break;
                    default:
                        System.out.println("Invalid room type.");
                        return;
                }
                break;

            case 3:
                System.out.print("Enter new Number of Nights: ");
                int newNights = scanner.nextInt();
                booking.nights = newNights;
                break;

            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.println("\nBooking Modified Successfully!\n");
        System.out.println("Updated Booking Details:");
        System.out.println("Customer Name: " + booking.customerName);
        System.out.println("Room Type: " + booking.roomType);
        System.out.println("Number of Nights: " + booking.nights);
        System.out.println("Cost per Night: $" + booking.costPerNight);

        try (FileWriter writer = new FileWriter("C:\\Users\\ACC\\Desktop\\HotelMangementSystem\\bookings.txt", true)) {
            writer.write("Updated Booking - " + booking.toFileFormat() + "\n");
            System.out.println("Modified booking saved to bookings.txt!");
        } catch (IOException e) {
            System.out.println("Error saving modified booking details: " + e.getMessage());
        }

        returnToDashboard();
    }
}

class CheckBookings extends HotelService {

    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n===ALL BOOKINGS===");

        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\ACC\\Desktop\\HotelMangementSystem\\bookings.txt"))) {
            String line;
            boolean bookingsExist = false;

            while ((line = reader.readLine()) != null) {
                // Display the saved booking data
                System.out.println(line);
                bookingsExist = true;
            }

            if (!bookingsExist) {
                System.out.println("No bookings found in the system.");
            }

        } catch (IOException e) {
            System.out.println("Error reading bookings: " + e.getMessage());
        }

        System.out.println("==============================\n");
    }
}

class CheckRoomStatus extends HotelService {
    private HashMap<Integer, Boolean> roomAvailability;

    public CheckRoomStatus(HashMap<Integer, Boolean> roomAvailability) {
        this.roomAvailability = roomAvailability;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n===ROOM STATUS===");

        int roomCount = 1;
        
        System.out.println("\n===Single Room Availability===\n");
        for (int i = 1; i <= 3; i++) {
            String status = roomAvailability.get(roomCount) ? "Available" : "Booked";
            System.out.println("Single Room " + roomCount + ": " + status);
            roomCount++;
        }
        
        System.out.println("\n===Double Room Availability===\n");
        for (int i = 1; i <= 3; i++) {
            String status = roomAvailability.get(roomCount) ? "Available" : "Booked";
            System.out.println("Double Room " + roomCount + ": " + status);
            roomCount++;
        }
        
        System.out.println("\n===Suite Availability===\n");
        for (int i = 1; i <= 3; i++) {
            String status = roomAvailability.get(roomCount) ? "Available" : "Booked";
            System.out.println("Suite " + roomCount + ": " + status);
            roomCount++;
        }

        System.out.println("==============================\n");
        returnToDashboard();
    }
}



// Main Class
public class HotelManagementSystem {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "1234";

    // Admin Login
    public static boolean adminLogin(Scanner scanner) {
        System.out.println("\n====RECEPTION LOGIN===");
        int attempts = 3; // Allow 3 attempts to login

        while (attempts > 0) {
            System.out.print("Enter Username: ");
            String username = scanner.next();
            System.out.print("Enter Password: ");
            String password = scanner.next();

            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                System.out.println("\nLogin Successful!");
                return true;
            } else {
                attempts--;
                System.out.println("Incorrect credentials. Attempts remaining: " + attempts);
            }
        }
        System.out.println("\nToo many failed attempts. Exiting the system.");
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashMap<Integer, BookingDetails> bookings = new HashMap<>();
    
        HashMap<Integer, Boolean> roomAvailability = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            roomAvailability.put(i, true);
        }
    
        if (!adminLogin(scanner)) {
            scanner.close();
            return;
        }
    
        HotelService[] services = {
            new RoomBooking(bookings, roomAvailability), 
            new CheckOut(bookings, new RoomBooking(bookings, roomAvailability)),
            new ModifyBooking(bookings),
            new CheckBookings(),
            new CheckRoomStatus(roomAvailability) 
        };
        
        System.out.println("\n * * * * * * * * * * WELCOME TO YZ HOTEL! * * * * * * * * * * ");
    
        while (true) {
            System.out.println("\n ===DASHBOARD=== \n");
            System.out.println("1. Book a Room");
            System.out.println("2. Check-Out");
            System.out.println("3. Modify Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. Check Room Availability");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
    
            if (choice == 6) {
                System.out.println("Exiting... Thank you for stay in this hotel!");
                break;
            } else if (choice > 0 && choice <= services.length) {
                services[choice - 1].execute(scanner);
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }    
}
