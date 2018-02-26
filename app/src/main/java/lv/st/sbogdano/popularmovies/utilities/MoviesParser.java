package lv.st.sbogdano.popularmovies.utilities;

import java.util.ArrayList;
import java.util.List;

import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.content.Movie;

public class MoviesParser {

    public static MovieEntry[] parse(List<Movie> movies) {

        int responseSize = movies.size();

        MovieEntry[] movieEntries = new MovieEntry[responseSize];

        for (int i = 0; i < responseSize; i++) {
            int movieId = movies.get(i).getId();
            String posterPath = movies.get(i).getPosterPath();
            String overview = movies.get(i).getOverview();
            String title = movies.get(i).getOriginalTitle();
            String releasedDate = movies.get(i).getReleaseDate();
            double voteAverage = movies.get(i).getVoteAverage();

            movieEntries[i] = new MovieEntry(movieId, posterPath, overview, title, releasedDate, voteAverage);
        }

        return movieEntries;

    }

}
