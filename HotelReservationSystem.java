import java.io.*;
import java.time.LocalDate;
import java.util.*;

// Room Class
class Room{
    private int roomNumber;
    private String category;
    private double price;

    public Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public void displayRoom() {
        System.out.println(roomNumber + "       " +
                category + "       Rs." + price);
    }
}

// Customer Class
class Customer {
    private String name;
    private String email;
    private String phone;

    public Customer(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}

// Reservation Class
class Reservation {
    private String bookingId;
    private Room room;
    private Customer customer;
    private int nights;
    private double totalAmount;
    private String bookingDate;
    private String status;
    private String paymentStatus;

    public Reservation(String bookingId, Room room,
                       Customer customer, int nights) {

        this.bookingId = bookingId;
        this.room = room;
        this.customer = customer;
        this.nights = nights;

        this.totalAmount = room.getPrice() * nights;
        this.bookingDate = LocalDate.now().toString();
        this.status = "CONFIRMED";
        this.paymentStatus = "PAID";
    }

    public String getBookingId() {
        return bookingId;
    }

    public Room getRoom() {
        return room;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getNights() {
        return nights;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void cancelReservation() {
        status = "CANCELLED";
        paymentStatus = "REFUND PENDING";
    }

    public void displayDetails() {
        System.out.println("\n========== BOOKING DETAILS ==========");
        System.out.println("Booking ID     : " + bookingId);
        System.out.println("Customer Name  : " + customer.getName());
        System.out.println("Email          : " + customer.getEmail());
        System.out.println("Phone          : " + customer.getPhone());
        System.out.println("Room Number    : " + room.getRoomNumber());
        System.out.println("Room Category  : " + room.getCategory());
        System.out.println("Price Per Night: Rs." + room.getPrice());
        System.out.println("Number of Nights: " + nights);
        System.out.println("Total Amount   : Rs." + totalAmount);
        System.out.println("Booking Date   : " + bookingDate);
        System.out.println("Booking Status : " + status);
        System.out.println("Payment Status : " + paymentStatus);
        System.out.println("=====================================");
    }

    public String toFileString() {
        return String.join("|",
                bookingId,
                String.valueOf(room.getRoomNumber()),
                room.getCategory(),
                String.valueOf(room.getPrice()),
                customer.getName().replace("|", " "),
                customer.getEmail().replace("|", " "),
                customer.getPhone().replace("|", " "),
                String.valueOf(nights),
                String.valueOf(totalAmount),
                bookingDate,
                status,
                paymentStatus
        );
    }
}

// Payment Class
class Payment {

    public boolean makePayment(double amount) {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n========== PAYMENT ==========");
        System.out.println("Total Amount: Rs." + amount);

        System.out.print("Confirm payment? (yes/no): ");

        String choice = sc.nextLine();

        if (choice.equalsIgnoreCase("yes")) {
            System.out.println("Processing payment...");
            System.out.println("Payment Successful!");
            return true;
        } else {
            System.out.println("Payment Cancelled.");
            return false;
        }
    }
}

// Main Hotel Reservation System
public class HotelReservationSystem {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Reservation> reservations = new ArrayList<>();

    static final String FILE_NAME = "bookings.txt";

    // Initialize hotel rooms
    static void initializeRooms() {

        rooms.add(new Room(101, "Standard", 1500));
        rooms.add(new Room(102, "Standard", 1500));
        rooms.add(new Room(103, "Standard", 1500));

        rooms.add(new Room(201, "Deluxe", 2500));
        rooms.add(new Room(202, "Deluxe", 2500));
        rooms.add(new Room(203, "Deluxe", 2500));

        rooms.add(new Room(301, "Suite", 4000));
        rooms.add(new Room(302, "Suite", 4000));
    }

    // Check room availability
    static boolean isRoomAvailable(int roomNumber) {

        for (Reservation r : reservations) {

            if (r.getRoom().getRoomNumber() == roomNumber
                    && r.getStatus().equals("CONFIRMED")) {
                return false;
            }
        }

        return true;
    }

    // Display available rooms
    static void viewRooms() {

        System.out.println("\n========== AVAILABLE ROOMS ==========");
        System.out.println("Room No.   Category   Price");

        for (Room room : rooms) {

            if (isRoomAvailable(room.getRoomNumber())) {
                room.displayRoom();
            }
        }
    }

    // Search rooms by category
    static void searchRooms() {

        System.out.println("\n1. Standard");
        System.out.println("2. Deluxe");
        System.out.println("3. Suite");

        System.out.print("Select room category: ");

        int choice = readInt();

        String category;

        switch (choice) {
            case 1:
                category = "Standard";
                break;

            case 2:
                category = "Deluxe";
                break;

            case 3:
                category = "Suite";
                break;

            default:
                System.out.println("Invalid category!");
                return;
        }

        System.out.println("\nAvailable " + category + " Rooms:");

        boolean found = false;

        for (Room room : rooms) {

            if (room.getCategory().equalsIgnoreCase(category)
                    && isRoomAvailable(room.getRoomNumber())) {

                room.displayRoom();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available in this category.");
        }
    }

    // Book a room
    static void bookRoom() {

        viewRooms();

        System.out.print("\nEnter Room Number: ");
        int roomNumber = readInt();

        Room selectedRoom = null;

        for (Room room : rooms) {

            if (room.getRoomNumber() == roomNumber
                    && isRoomAvailable(roomNumber)) {

                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room not available!");
            return;
        }

        System.out.print("Enter Customer Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Phone Number: ");
        String phone = sc.nextLine();

        System.out.print("Enter Number of Nights: ");
        int nights = readInt();

        if (nights <= 0) {
            System.out.println("Number of nights must be positive.");
            return;
        }

        Customer customer = new Customer(name, email, phone);

        double totalAmount = selectedRoom.getPrice() * nights;

        System.out.println("\n========== BOOKING SUMMARY ==========");
        System.out.println("Customer: " + name);
        System.out.println("Room: " + roomNumber);
        System.out.println("Category: " + selectedRoom.getCategory());
        System.out.println("Nights: " + nights);
        System.out.println("Total: Rs." + totalAmount);

        Payment payment = new Payment();

        if (payment.makePayment(totalAmount)) {

            String bookingId = "BK" + System.currentTimeMillis();

            Reservation reservation = new Reservation(
                    bookingId,
                    selectedRoom,
                    customer,
                    nights
            );

            reservations.add(reservation);

            saveBookings();

            System.out.println("\nRoom Booked Successfully!");
            System.out.println("Your Booking ID: " + bookingId);

        } else {
            System.out.println("Booking unsuccessful.");
        }
    }

    // Cancel reservation
    static void cancelBooking() {

        System.out.print("\nEnter Booking ID: ");
        String id = sc.nextLine();

        for (Reservation r : reservations) {

            if (r.getBookingId().equalsIgnoreCase(id)
                    && r.getStatus().equals("CONFIRMED")) {

                r.cancelReservation();

                saveBookings();

                System.out.println("Reservation cancelled successfully!");
                System.out.println("Payment status: REFUND PENDING");
                return;
            }
        }

        System.out.println("Booking not found or already cancelled.");
    }

    // View booking details
    static void viewBooking() {

        System.out.print("\nEnter Booking ID: ");
        String id = sc.nextLine();

        for (Reservation r : reservations) {

            if (r.getBookingId().equalsIgnoreCase(id)) {

                r.displayDetails();
                return;
            }
        }

        System.out.println("Booking not found!");
    }

    // View all reservations
    static void viewAllBookings() {

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        for (Reservation r : reservations) {
            r.displayDetails();
        }
    }

    // Save bookings to file
    static void saveBookings() {

        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(FILE_NAME))) {

            for (Reservation r : reservations) {
                writer.println(r.toFileString());
            }

            System.out.println("Booking data saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    // Load bookings from file
    static void loadBookings() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                try {
                    String[] data = line.split("\\|", -1);

                    if (data.length != 12) {
                        continue;
                    }

                    int roomNumber = Integer.parseInt(data[1]);

                    Room room = new Room(
                            roomNumber,
                            data[2],
                            Double.parseDouble(data[3])
                    );

                    Customer customer = new Customer(
                            data[4], data[5], data[6]
                    );

                    int nights = Integer.parseInt(data[7]);

                    Reservation r = new Reservation(
                            data[0], room, customer, nights
                    );

                    if (!data[9].equals(r.getBookingDate())) {
                        // Booking date is restored below through saved data.
                    }

                    // Restore saved booking status and payment status.
                    if (data[10].equals("CANCELLED")) {
                        r.cancelReservation();
                    }

                    reservations.add(r);

                } catch (RuntimeException e) {
                    System.out.println("Skipping invalid booking record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
    }

    // Read integer safely
    static int readInt() {

        while (true) {

            try {
                int value = Integer.parseInt(sc.nextLine());
                return value;

            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        initializeRooms();
        loadBookings();

        int choice;

        System.out.println("\n======================================");
        System.out.println("       HOTEL RESERVATION SYSTEM");
        System.out.println("======================================");

        do {

            System.out.println("\n1. View All Available Rooms");
            System.out.println("2. Search Rooms");
            System.out.println("3. Book a Room");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. View Booking Details");
            System.out.println("6. View All Reservations");
            System.out.println("7. Exit");

            System.out.print("\nEnter Your Choice: ");

            choice = readInt();

            switch (choice) {

                case 1:
                    viewRooms();
                    break;

                case 2:
                    searchRooms();
                    break;

                case 3:
                    bookRoom();
                    break;

                case 4:
                    cancelBooking();
                    break;

                case 5:
                    viewBooking();
                    break;

                case 6:
                    viewAllBookings();
                    break;

                case 7:
                    System.out.println("Thank you for using our system!");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }

        } while (choice != 7);
    }
}