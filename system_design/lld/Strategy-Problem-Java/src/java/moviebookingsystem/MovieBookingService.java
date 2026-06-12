package moviebookingsystem;

public class MovieBookingService
{
    private final MovieCatalog movieCatalog;
    private final BookingManager bookingManager;

    public MovieBookingService(MovieCatalog movieCatalog, BookingManager bookingManager) {
        this.movieCatalog = movieCatalog;
        this.bookingManager = bookingManager;
    }

    public void addMovie(Movie movie) {
        movieCatalog.addMovie(movie);
    }

    public void removeMovie(String movieId) {
        movieCatalog.removeMovie(movieId);
    }

    public Movie getMovie(String movieId) {
        return movieCatalog.getMovie(movieId);
    }

    public Booking bookTicket(String userId, String movieId, int numSeats) throws Exception {
        Movie movie = getMovie(movieId);
        if (movie == null) {
            throw new Exception("Movie not found");
        }
        return bookingManager.bookTicket(userId, movie, numSeats);
    }

    public void cancelBooking(String bookingId) throws Exception {
        bookingManager.cancelBooking(bookingId);
    }
}
