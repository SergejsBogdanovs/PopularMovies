package lv.st.sbogdano.popularmovies.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.database.MoviesDatabase;
import lv.st.sbogdano.popularmovies.data.model.content.Movie;

/**
 * {@link android.arch.persistence.room.Dao} which provides an api for all data operations with the {@link MoviesDatabase}
 */
@Dao
public interface MoviesDao {

    /**
     * Select all entries from movies.
     * @return {@LiveData} list of all movies.
     */
    @Query("SELECT * FROM movies WHERE type = :movieType")
    LiveData<List<MovieEntry>> getMovies(String movieType);

    /**
     * Inserts a list of {@link Movie} into the movies table.
     * @param movieEntries
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMovies(List<MovieEntry> movieEntries);

    /**
     * Delete favorite movie.
     */
    @Query("DELETE FROM movies WHERE movieId = :id AND type = :type")
    void deleteMovieFromFavorite(int id, String type);

    /**
     * Deletes movies.
     */
    @Query("DELETE FROM movies WHERE type = :type")
    void deleteAllMovies(String type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(MovieEntry movie);

    @Query("SELECT * FROM movies WHERE movieId = :movieId AND type = :type")
    Maybe<MovieEntry> getFavoriteMovie(int movieId, String type);

}
