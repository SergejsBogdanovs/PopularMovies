package lv.st.sbogdano.popularmovies.data.database.sqlite;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the favorite movies database.
 */
public class FavoriteMoviesContract {

    public static final String CONTENT_AUTHORITY = "lv.st.sbogdano.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";

    public static final class FavoriteMoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIES)
                .build();

        public static final String TABLE_NAME = "favorite_movies";

        // Columns
        public static final String MOVIE_ID = "movieId";
        public static final String POSTER_PATH = "posterPath";
        public static final String OVERVIEW = "overview";
        public static final String TITLE = "title";
        public static final String RELEASED_DATE = "releasedDate";
        public static final String VOTE_AVERAGE = "voteAverage";
        public static final String TYPE = "type";

        public static Uri buildFavoriteMovieUri(int movieId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(movieId))
                    .build();
        }
    }
}
