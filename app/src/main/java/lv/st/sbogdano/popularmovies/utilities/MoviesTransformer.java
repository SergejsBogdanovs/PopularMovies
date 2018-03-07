package lv.st.sbogdano.popularmovies.utilities;

import java.util.ArrayList;
import java.util.List;

import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.MoviesType;
import lv.st.sbogdano.popularmovies.data.model.content.Movie;

public class MoviesTransformer {

    public static List<MovieEntry> transformMovies(List<Movie> movies, MoviesType type) {

        List<MovieEntry> movieEntries = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            int movieId = movies.get(i).getId();
            String posterPath = movies.get(i).getPosterPath();
            String overview = movies.get(i).getOverview();
            String title = movies.get(i).getOriginalTitle();
            String releasedDate = movies.get(i).getReleaseDate();
            double voteAverage = movies.get(i).getVoteAverage();

            movieEntries.add(
                    new MovieEntry(movieId, posterPath, overview, title, releasedDate, voteAverage, type.name()));
        }

        return movieEntries;
    }
}
