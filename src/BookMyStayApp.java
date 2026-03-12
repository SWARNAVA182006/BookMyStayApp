// Version 6.1

import java.util.*;

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decreaseAvailability(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Booking Service
class BookingService {

    private Queue<Reservation> bookingQueue;
    private RoomInventory inventory;

    // Track allocated room IDs
    private HashMap<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        bookingQueue = new LinkedList<>();
        allocatedRooms = new HashMap<>();
    }

    // Add booking request
    public void addReservation(Reservation reservation) {
        bookingQueue.add(reservation);
    }

    // Generate unique room ID
    private String generateRoomId(String roomType, int count) {
        String prefix = roomType.replace(" ", "").substring(0, 2).toUpperCase();
        return prefix + "-" + count;
    }

    // Process queue
    public void processBookings() {

        while (!bookingQueue.isEmpty()) {

            Reservation request = bookingQueue.poll();
            String roomType = request.getRoomType();

            System.out.println("\nProcessing request for " + request.getGuestName());

            if (inventory.getAvailability(roomType) > 0) {

                allocatedRooms.putIfAbsent(roomType, new HashSet<>());

                Set<String> rooms = allocatedRooms.get(roomType);

                String roomId = generateRoomId(roomType, rooms.size() + 1);

                rooms.add(roomId); // uniqueness enforced by Set
                inventory.decreaseAvailability(roomType);

                System.out.println("Reservation Confirmed!");
                System.out.println("Guest: " + request.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("Sorry! No rooms available for " + roomType);
            }
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App\n");

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Add booking requests
        bookingService.addReservation(new Reservation("Alice", "Single Room"));
        bookingService.addReservation(new Reservation("Bob", "Double Room"));
        bookingService.addReservation(new Reservation("Charlie", "Suite Room"));
        bookingService.addReservation(new Reservation("David", "Single Room"));

        // Process bookings
        bookingService.processBookings();

        System.out.println("\nFinal Inventory State:");
        inventory.displayInventory();
    }
}