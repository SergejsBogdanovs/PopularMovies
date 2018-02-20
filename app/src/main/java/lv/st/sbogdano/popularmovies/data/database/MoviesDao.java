package lv.st.sbogdano.popularmovies.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import lv.st.sbogdano.popularmovies.data.model.content.Movie;

/**
 * {@link android.arch.persistence.room.Dao} which provides an api for all data operations with the {@link MoviesDatabase}
 */
@Dao
public interface MoviesDao {

    /**
     * Select all entries.
     * @return {@LiveData} list of all movies.
     */
    @Query("SELECT * FROM movies")
    LiveData<List<MovieEntry>> getMovies();

    /**
     * Get the single movie by it id
     * @param movieId the movie id
     * @return {@LiveData}  the movie.
     */
    @Query("SELECT * FROM movies WHERE movieId = :movieId")
    LiveData<MovieEntry> getMovieDetails(int movieId);

    /**
     * Inserts a list of {@link MovieEntry} into the movies table.
     * @param movieEntries
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(MovieEntry... movieEntries);

    /**
     * Deletes all movies.
     */
    @Query("DELETE FROM movies")
    void deleteOldMovies();

}
