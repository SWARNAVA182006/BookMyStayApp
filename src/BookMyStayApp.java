import java.util.*;

// Represents a confirmed reservation
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

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    public double getTotalCost() {
        return totalCost;
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

// Maintains booking history
class BookingHistory {
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    // Add a confirmed reservation to history
    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(confirmedBookings); // prevent modification
    }
}

// Generates reports from booking history
class BookingReportService {

    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Display all reservations
    public void displayAllReservations() {
        List<Reservation> reservations = bookingHistory.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("=== Booking History ===");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary: total bookings and total revenue
    public void generateSummaryReport() {
        List<Reservation> reservations = bookingHistory.getAllReservations();
        int totalBookings = reservations.size();
        double totalRevenue = 0;

        for (Reservation r : reservations) {
            totalRevenue += r.getTotalCost();
        }

        System.out.println("=== Booking Summary Report ===");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        // Simulate confirmed reservations
        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe", 3, 4500);
        Reservation r2 = new Reservation("RES102", "Bob", "Standard", 2, 3000);
        Reservation r3 = new Reservation("RES103", "Charlie", "Suite", 5, 12500);

        // Add to booking history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Reporting service
        BookingReportService reportService = new BookingReportService(history);

        // Display all reservations
        reportService.displayAllReservations();

        // Generate summary report
        reportService.generateSummaryReport();
    }
}