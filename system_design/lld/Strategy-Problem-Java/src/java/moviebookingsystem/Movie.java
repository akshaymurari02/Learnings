package moviebookingsystem;

public class Movie {
    private final String id;
    private final String title;
    private final String genre;
    private final int duration; // in minutes
    private final String director;

    public Movie(String id, String title, String genre, int duration, String director) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.director = director;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public String getDirector() {
        return director;
    }
}
