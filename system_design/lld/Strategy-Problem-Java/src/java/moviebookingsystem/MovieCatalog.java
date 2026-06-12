package moviebookingsystem;

import java.util.HashMap;
import java.util.Map;

public class MovieCatalog {
    private final Map<String, Movie> movies;

    public MovieCatalog() {
        this.movies = new HashMap<>();
    }

    public void addMovie(Movie movie) {
        movies.put(movie.getId(), movie);
    }

    public void removeMovie(String movieId) {
        movies.remove(movieId);
    }

    public Movie getMovie(String movieId) {
        return movies.get(movieId);
    }
}
