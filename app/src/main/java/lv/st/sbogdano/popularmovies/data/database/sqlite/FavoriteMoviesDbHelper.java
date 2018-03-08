package lv.st.sbogdano.popularmovies.data.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for favorite movies data.
 */
public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorite_movies.db";
    public static final int DATABASE_VERSION = 1;

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE IF NOT EXISTS " +
                FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME + " (" +

                FavoriteMoviesContract.FavoriteMoviesEntry._ID + " INTEGER, " +
                FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID + " INTEGER, " +
                FavoriteMoviesContract.FavoriteMoviesEntry.POSTER_PATH + " VARCHAR(50), " +
                FavoriteMoviesContract.FavoriteMoviesEntry.OVERVIEW + " TEXT, " +
                FavoriteMoviesContract.FavoriteMoviesEntry.TITLE + " VARCHAR(50), " +
                FavoriteMoviesContract.FavoriteMoviesEntry.RELEASED_DATE + " VARCHAR(20), " +
                FavoriteMoviesContract.FavoriteMoviesEntry.VOTE_AVERAGE + " VARCHAR(6), " +
                FavoriteMoviesContract.FavoriteMoviesEntry.TYPE + " VARCHAR(20), " +
                "PRIMARY KEY (" + FavoriteMoviesContract.FavoriteMoviesEntry._ID + "));";
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase
                .execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
