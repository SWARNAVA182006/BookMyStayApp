import java.util.*;

// Represents a reservation
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String allocatedRoomId;

    public Reservation(String reservationId, String guestName, String roomType, String allocatedRoomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.allocatedRoomId = allocatedRoomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getAllocatedRoomId() {
        return allocatedRoomId;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Allocated Room ID: " + allocatedRoomId;
    }
}

// Manages room inventory
class RoomInventory {
    private Map<String, Integer> roomCount;

    public RoomInventory() {
        roomCount = new HashMap<>();
        roomCount.put("Standard", 5);
        roomCount.put("Deluxe", 3);
        roomCount.put("Suite", 2);
    }

    public boolean isAvailable(String roomType) {
        return roomCount.getOrDefault(roomType, 0) > 0;
    }

    public void allocateRoom(String roomType) {
        roomCount.put(roomType, roomCount.get(roomType) - 1);
    }

    public void releaseRoom(String roomType) {
        roomCount.put(roomType, roomCount.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (String type : roomCount.keySet()) {
            System.out.println("- " + type + ": " + roomCount.get(type) + " rooms available");
        }
    }
}

// Manages booking history and cancellations
class BookingManager {
    private Map<String, Reservation> activeBookings;
    private Stack<String> releasedRoomIds; // For rollback
    private RoomInventory inventory;

    public BookingManager(RoomInventory inventory) {
        activeBookings = new HashMap<>();
        releasedRoomIds = new Stack<>();
        this.inventory = inventory;
    }

    // Add a new reservation
    public void addBooking(Reservation r) {
        activeBookings.put(r.getReservationId(), r);
        inventory.allocateRoom(r.getRoomType());
        System.out.println("Booking confirmed: " + r);
    }

    // Cancel an existing reservation
    public void cancelBooking(String reservationId) {
        if (!activeBookings.containsKey(reservationId)) {
            System.out.println("Cancellation failed: Reservation " + reservationId + " does not exist or is already cancelled.");
            return;
        }

        Reservation r = activeBookings.remove(reservationId);
        // Record released room ID for rollback (LIFO)
        releasedRoomIds.push(r.getAllocatedRoomId());
        // Restore inventory immediately
        inventory.releaseRoom(r.getRoomType());

        System.out.println("Booking cancelled successfully: " + r.getReservationId() +
                ". Room " + r.getAllocatedRoomId() + " released back to inventory.");
    }

    public void displayActiveBookings() {
        if (activeBookings.isEmpty()) {
            System.out.println("No active bookings.");
            return;
        }

        System.out.println("Active Bookings:");
        for (Reservation r : activeBookings.values()) {
            System.out.println(r);
        }
    }

    public void displayReleasedRooms() {
        System.out.println("Recently Released Room IDs (LIFO): " + releasedRoomIds);
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        BookingManager manager = new BookingManager(inventory);

        // Example reservations
        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe", "D101");
        Reservation r2 = new Reservation("RES102", "Bob", "Standard", "S102");
        Reservation r3 = new Reservation("RES103", "Charlie", "Suite", "SU103");

        // Add bookings
        manager.addBooking(r1);
        manager.addBooking(r2);
        manager.addBooking(r3);

        inventory.displayInventory();
        manager.displayActiveBookings();

        System.out.println("\n--- Performing Cancellations ---\n");

        // Cancel bookings
        manager.cancelBooking("RES102"); // Bob
        manager.cancelBooking("RES104"); // Non-existent reservation

        inventory.displayInventory();
        manager.displayActiveBookings();
        manager.displayReleasedRooms();
    }
}