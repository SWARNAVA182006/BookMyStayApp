import java.util.*;

// Represents an Add-On Service
class Service {
    private String serviceName;
    private double price;

    public Service(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }
}

// Manages add-on services for reservations
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<Service>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Add a service to a reservation
    public void addService(String reservationId, Service service) {
        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        List<Service> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Add-On Services for Reservation " + reservationId + ":");
        for (Service s : services) {
            System.out.println("- " + s.getServiceName() + " : ₹" + s.getPrice());
        }
    }

    // Calculate total cost of services
    public double calculateTotalServiceCost(String reservationId) {
        List<Service> services = reservationServices.get(reservationId);

        double total = 0;

        if (services != null) {
            for (Service s : services) {
                total += s.getPrice();
            }
        }

        return total;
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();

        // Example reservation
        String reservationId = "RES101";

        // Guest selects add-on services
        Service breakfast = new Service("Breakfast", 500);
        Service airportPickup = new Service("Airport Pickup", 1200);
        Service spa = new Service("Spa Access", 2000);

        // Add services to reservation
        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, airportPickup);
        manager.addService(reservationId, spa);

        // Display selected services
        manager.displayServices(reservationId);

        // Calculate additional cost
        double totalCost = manager.calculateTotalServiceCost(reservationId);

        System.out.println("Total Add-On Cost: ₹" + totalCost);

        // Core booking / inventory remains unchanged
        System.out.println("Room allocation and inventory remain unchanged.");
    }
}