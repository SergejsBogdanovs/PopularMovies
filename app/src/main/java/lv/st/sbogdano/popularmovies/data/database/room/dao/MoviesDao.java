package lv.st.sbogdano.popularmovies.data.database.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import lv.st.sbogdano.popularmovies.data.database.room.MovieEntry;
import lv.st.sbogdano.popularmovies.data.database.room.MoviesDatabase;

/**
 * {@link android.arch.persistence.room.Dao}
 * which provides an api for all data operations with the {@link MoviesDatabase}
 */
@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movies WHERE type = :movieType")
    LiveData<List<MovieEntry>> getMovies(String movieType);

    @Query("SELECT * FROM movies WHERE movieId = :movieId AND type = :type")
    Maybe<MovieEntry> getFavoriteMovie(int movieId, String type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMovies(List<MovieEntry> movieEntries);

    @Query("DELETE FROM movies WHERE movieId = :id AND type = :type")
    void deleteMovieFromFavorite(int id, String type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(MovieEntry movie);
}
