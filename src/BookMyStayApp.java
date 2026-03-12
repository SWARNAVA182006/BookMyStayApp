import java.util.*;

// Custom exception for invalid booking scenarios
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Represents a reservation
class Reservation {
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

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Nights: " + nights +
                ", Total: ₹" + totalCost;
    }
}

// Validates booking input
class InvalidBookingValidator {
    private static final Set<String> validRoomTypes = Set.of("Standard", "Deluxe", "Suite");

    // Validate room type
    public static void validateRoomType(String roomType) throws InvalidBookingException {
        if (!validRoomTypes.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType + ". Must be Standard, Deluxe, or Suite.");
        }
    }

    // Validate number of nights
    public static void validateNights(int nights) throws InvalidBookingException {
        if (nights <= 0) {
            throw new InvalidBookingException("Number of nights must be positive. Provided: " + nights);
        }
    }

    // Validate total cost
    public static void validateTotalCost(double totalCost) throws InvalidBookingException {
        if (totalCost < 0) {
            throw new InvalidBookingException("Total cost cannot be negative. Provided: " + totalCost);
        }
    }
}

// Main class demonstrating validation
public class BookMyStayApp {

    public static void main(String[] args) {
        List<Reservation> bookings = new ArrayList<>();

        // Example input data (some invalid intentionally)
        Object[][] inputData = {
                {"RES101", "Alice", "Deluxe", 3, 4500.0},
                {"RES102", "Bob", "Premium", 2, 3000.0},     // Invalid room type
                {"RES103", "Charlie", "Suite", -2, 12000.0}, // Invalid nights
                {"RES104", "Daisy", "Standard", 2, -500.0}   // Invalid cost
        };

        for (Object[] data : inputData) {
            String id = (String) data[0];
            String guest = (String) data[1];
            String roomType = (String) data[2];
            int nights = (int) data[3];
            double cost = (double) data[4];

            try {
                // Validate input
                InvalidBookingValidator.validateRoomType(roomType);
                InvalidBookingValidator.validateNights(nights);
                InvalidBookingValidator.validateTotalCost(cost);

                // If valid, create reservation
                Reservation r = new Reservation(id, guest, roomType, nights, cost);
                bookings.add(r);
                System.out.println("Booking confirmed: " + r);

            } catch (InvalidBookingException e) {
                // Handle validation failure gracefully
                System.out.println("Booking failed for " + guest + ": " + e.getMessage());
            }
        }

        System.out.println("\nTotal valid bookings: " + bookings.size());
    }
}