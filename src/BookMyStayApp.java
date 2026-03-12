// Version 4.1

import java.util.*;

// Abstract Room class
abstract class Room {
    protected String type;
    protected double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type + " | Price per night: ₹" + price);
    }
}

// Room Types
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 2500);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 4000);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 8000);
    }
}

// Inventory class (centralized state holder)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 10);
        inventory.put("Double Room", 5);
        inventory.put("Suite Room", 0); // Example unavailable room
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Set<String> getRoomTypes() {
        return inventory.keySet();
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App\n");

        RoomInventory inventory = new RoomInventory();

        // Room domain objects
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        System.out.println("Available Rooms:\n");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getType());

            // Defensive validation: show only rooms with availability > 0
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available Count: " + available);
                System.out.println();
            }
        }

        System.out.println("Search completed. Inventory state unchanged.");
    }
}
