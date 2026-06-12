package moviebookingsystem;

import java.util.HashMap;
import java.util.Map;

public class BookingManager {
    private final Map<String, Booking> bookings;

    public BookingManager() {
        this.bookings = new HashMap<>();
    }

    public Booking bookTicket(String userId, Movie movie, int numSeats) throws Exception {
        String bookingId = generateBookingId();
        Booking booking = new Booking(bookingId, userId, movie, numSeats);
        bookings.put(bookingId, booking);
        return booking;
    }

    public void cancelBooking(String bookingId) throws Exception {
        if (!bookings.containsKey(bookingId)) {
            throw new Exception("Booking not found");
        }
        bookings.remove(bookingId);
    }

    private String generateBookingId() {
        return "BKG" + System.currentTimeMillis();
    }
}
