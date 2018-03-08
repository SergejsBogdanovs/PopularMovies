package lv.st.sbogdano.popularmovies.data.database.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import lv.st.sbogdano.popularmovies.AppDelegate;
import lv.st.sbogdano.popularmovies.data.database.room.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.MoviesType;

/**
 * Utility class for favorite movies db operations.
 */
public class FavoriteMoviesUtils {

    private static MoviesType mType = MoviesType.FAVORITE;

    private static final Uri URI = FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;

    public static Cursor getFavoriteMovies() {
        return AppDelegate.getDb().query(URI, null, null, null, null);
    }

    public static Cursor getFavoriteMovie(int movieId) {
        Uri uri = FavoriteMoviesContract.FavoriteMoviesEntry.buildFavoriteMovieUri(movieId);
        return AppDelegate.getDb().query(uri, null, null, null, null);
    }

    public static void insert(@NonNull MovieEntry movie) {
        AppDelegate.getDb().insert(URI, toContentValues(movie));
    }

    public static void delete(@NonNull MovieEntry movie) {
        String where = FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID + "=?";
        AppDelegate.getDb().delete(URI, where, new String[]{String.valueOf(movie.getMovieId())});
    }


    @NonNull
    public static ContentValues toContentValues(@NonNull MovieEntry movie) {
        ContentValues values = new ContentValues();
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry._ID, movie.getId());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID, movie.getMovieId());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.POSTER_PATH, movie.getPosterPath());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.OVERVIEW, movie.getOverview());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.TITLE, movie.getTitle());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.RELEASED_DATE, movie.getReleasedDate());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.VOTE_AVERAGE, Double.toString(movie.getVoteAverage()));
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.TYPE, mType.name());
        return values;
    }

    @NonNull
    public static MovieEntry fromCursor(@NonNull Cursor cursor) {
        int movieId = safeIntFromCursor(cursor, FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID);
        String posterPath = safeStringFromCursor(cursor, FavoriteMoviesContract.FavoriteMoviesEntry.POSTER_PATH);
        String overview = safeStringFromCursor(cursor, FavoriteMoviesContract.FavoriteMoviesEntry.OVERVIEW);
        String title = safeStringFromCursor(cursor, FavoriteMoviesContract.FavoriteMoviesEntry.TITLE);
        String releasedDate = safeStringFromCursor(cursor, FavoriteMoviesContract.FavoriteMoviesEntry.RELEASED_DATE);
        double voteAverage = Double.parseDouble(safeStringFromCursor(cursor, FavoriteMoviesContract.FavoriteMoviesEntry.VOTE_AVERAGE));
        return new MovieEntry(movieId, posterPath, overview, title, releasedDate, voteAverage, mType.name());
    }

   private static String safeStringFromCursor(@NonNull Cursor cursor, @NonNull String column) {
        try {
            return cursor.getString(cursor.getColumnIndex(column));
        } catch (Exception e) {
            return "";
        }
    }

    private static int safeIntFromCursor(@NonNull Cursor cursor, @NonNull String column) {
        try {
            return cursor.getInt(cursor.getColumnIndex(column));
        } catch (Exception e) {
            return 0;
        }
    }

}
