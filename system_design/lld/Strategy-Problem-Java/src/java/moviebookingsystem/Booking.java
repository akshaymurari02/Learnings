package moviebookingsystem;

public class Booking {
    private final String id;
    private final String userId;
    private final Movie movie;
    private final int numSeats;

    public Booking(String id, String userId, Movie movie, int numSeats) {
        this.id = id;
        this.userId = userId;
        this.movie = movie;
        this.numSeats = numSeats;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getNumSeats() {
        return numSeats;
    }
}
