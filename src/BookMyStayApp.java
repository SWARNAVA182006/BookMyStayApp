import java.util.*;
import java.util.concurrent.*;

// Represents a booking request
class BookingRequest {
    private String reservationId;
    private String guestName;
    private String roomType;

    public BookingRequest(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// Manages room inventory with thread-safe allocation
class RoomInventory {
    private Map<String, Integer> roomCount;

    public RoomInventory() {
        roomCount = new HashMap<>();
        roomCount.put("Standard", 5);
        roomCount.put("Deluxe", 3);
        roomCount.put("Suite", 2);
    }

    // Synchronized allocation to prevent double booking
    public synchronized boolean allocateRoom(String roomType) {
        int available = roomCount.getOrDefault(roomType, 0);
        if (available > 0) {
            roomCount.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    // Thread-safe inventory status
    public synchronized void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (String type : roomCount.keySet()) {
            System.out.println("- " + type + ": " + roomCount.get(type) + " rooms available");
        }
    }
}

// Manages confirmed bookings
class BookingManager {
    private Map<String, BookingRequest> confirmedBookings = new ConcurrentHashMap<>();

    // Thread-safe booking addition
    public void confirmBooking(BookingRequest request) {
        confirmedBookings.put(request.getReservationId(), request);
        System.out.println("Booking confirmed: " + request.getReservationId() +
                " for " + request.getGuestName() + " (" + request.getRoomType() + ")");
    }

    public void displayBookings() {
        System.out.println("\nConfirmed Bookings:");
        for (BookingRequest r : confirmedBookings.values()) {
            System.out.println("- " + r.getReservationId() + ": " + r.getGuestName() + " (" + r.getRoomType() + ")");
        }
    }
}

// Runnable task for processing booking requests concurrently
class BookingProcessor implements Runnable {
    private BookingRequest request;
    private RoomInventory inventory;
    private BookingManager manager;

    public BookingProcessor(BookingRequest request, RoomInventory inventory, BookingManager manager) {
        this.request = request;
        this.inventory = inventory;
        this.manager = manager;
    }

    @Override
    public void run() {
        // Critical section for allocation
        synchronized (inventory) {
            if (inventory.allocateRoom(request.getRoomType())) {
                manager.confirmBooking(request);
            } else {
                System.out.println("Booking failed for " + request.getGuestName() +
                        ": No " + request.getRoomType() + " rooms available.");
            }
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {

        RoomInventory inventory = new RoomInventory();
        BookingManager manager = new BookingManager();

        // Simulate multiple booking requests
        List<BookingRequest> requests = List.of(
                new BookingRequest("RES101", "Alice", "Deluxe"),
                new BookingRequest("RES102", "Bob", "Standard"),
                new BookingRequest("RES103", "Charlie", "Suite"),
                new BookingRequest("RES104", "Daisy", "Deluxe"),
                new BookingRequest("RES105", "Ethan", "Standard"),
                new BookingRequest("RES106", "Fiona", "Suite"),
                new BookingRequest("RES107", "George", "Standard") // may fail if Standard rooms exhausted
        );

        // Use thread pool to simulate concurrent booking
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (BookingRequest req : requests) {
            executor.submit(new BookingProcessor(req, inventory, manager));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\n--- Final Inventory and Bookings ---");
        inventory.displayInventory();
        manager.displayBookings();
    }
}