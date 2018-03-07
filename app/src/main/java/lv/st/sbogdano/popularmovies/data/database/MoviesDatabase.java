package lv.st.sbogdano.popularmovies.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import lv.st.sbogdano.popularmovies.data.database.dao.MoviesDao;
import lv.st.sbogdano.popularmovies.data.database.dao.ReviewDao;
import lv.st.sbogdano.popularmovies.data.database.dao.VideoDao;
import lv.st.sbogdano.popularmovies.data.model.content.Review;
import lv.st.sbogdano.popularmovies.data.model.content.Video;

/**
 * {@link MoviesDatabase} database for the application including a table for {@link MovieEntry}
 * with the DAO {@link MoviesDao}.
 */
@Database(entities = {MovieEntry.class, Review.class, Video.class}, version = 1)
public abstract class MoviesDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "movies";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesDatabase sInstance;

    public static MoviesDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        MoviesDatabase.class,
                        MoviesDatabase.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract MoviesDao moviesDao();

    public abstract ReviewDao reviewDao();

    public abstract VideoDao videoDao();
}
