import java.io.*;
import java.util.*;

// Represents a reservation (serializable for persistence)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;
    private double totalCost;

    public Reservation(String reservationId, String guestName, String roomType, int nights, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.totalCost = totalCost;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNights() { return nights; }
    public double getTotalCost() { return totalCost; }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Nights: " + nights +
                ", Total: ₹" + totalCost;
    }
}

// Inventory class (serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> roomCount;

    public RoomInventory() {
        roomCount = new HashMap<>();
        roomCount.put("Standard", 5);
        roomCount.put("Deluxe", 3);
        roomCount.put("Suite", 2);
    }

    public void allocateRoom(String roomType) {
        roomCount.put(roomType, roomCount.get(roomType) - 1);
    }

    public void releaseRoom(String roomType) {
        roomCount.put(roomType, roomCount.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("Room Inventory:");
        for (String type : roomCount.keySet()) {
            System.out.println("- " + type + ": " + roomCount.get(type) + " rooms available");
        }
    }

    public Map<String, Integer> getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(Map<String, Integer> roomCount) {
        this.roomCount = roomCount;
    }
}

// Handles persistence of booking and inventory data
class PersistenceService {
    private static final String BOOKING_FILE = "bookings.dat";
    private static final String INVENTORY_FILE = "inventory.dat";

    public static void saveBookingHistory(List<Reservation> bookings) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKING_FILE))) {
            oos.writeObject(bookings);
            System.out.println("Booking history saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving booking history: " + e.getMessage());
        }
    }

    public static List<Reservation> loadBookingHistory() {
        File file = new File(BOOKING_FILE);
        if (!file.exists()) {
            System.out.println("No booking history found. Starting fresh.");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Reservation>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading booking history: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveInventory(RoomInventory inventory) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {
            oos.writeObject(inventory);
            System.out.println("Inventory state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public static RoomInventory loadInventory() {
        File file = new File(INVENTORY_FILE);
        if (!file.exists()) {
            System.out.println("No inventory file found. Starting with default inventory.");
            return new RoomInventory();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (RoomInventory) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
            return new RoomInventory();
        }
    }
}

// Main class demonstrating persistence and recovery
public class BookMyStayApp {

    public static void main(String[] args) {

        // Load persisted data
        List<Reservation> bookings = PersistenceService.loadBookingHistory();
        RoomInventory inventory = PersistenceService.loadInventory();

        System.out.println("\n--- Current System State After Recovery ---");
        inventory.displayInventory();
        if (bookings.isEmpty()) {
            System.out.println("No previous bookings.");
        } else {
            System.out.println("Recovered Booking History:");
            for (Reservation r : bookings) {
                System.out.println("- " + r);
            }
        }

        // Simulate new bookings
        System.out.println("\n--- Adding New Bookings ---");
        Reservation r1 = new Reservation("RES201", "Alice", "Deluxe", 3, 4500);
        Reservation r2 = new Reservation("RES202", "Bob", "Standard", 2, 3000);

        bookings.add(r1);
        bookings.add(r2);

        // Use getters for private roomType
        inventory.allocateRoom(r1.getRoomType());
        inventory.allocateRoom(r2.getRoomType());

        System.out.println("New bookings added.");
        inventory.displayInventory();

        // Persist updated state
        PersistenceService.saveBookingHistory(bookings);
        PersistenceService.saveInventory(inventory);

        System.out.println("\nSystem shutdown simulation complete. State persisted.");
    }
}