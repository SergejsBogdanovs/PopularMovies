package lv.st.sbogdano.popularmovies.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

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
     * Select all entries.
     * @return {@LiveData} list of all reviews.
     */
    @Query("SELECT * FROM reviews")
    LiveData<List<ReviewEntry>> getReviews();

    /**
     * Select all entries.
     * @return {@LiveData} list of all videos.
     */
    @Query("SELECT * FROM videos")
    LiveData<List<VideoEntry>> getVideos();

    /**
     * Inserts a list of {@link MovieEntry} into the movies table.
     * @param movieEntries
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMovies(MovieEntry... movieEntries);

    /**
     * Inserts a list of {@link ReviewEntry} into the reviews table.
     * @param reviewEntries
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllReviews(ReviewEntry... reviewEntries);

    /**
     * Inserts a list of {@link VideoEntry} into the movies table.
     * @param videoEntries
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllVideos(VideoEntry... videoEntries);

    /**
     * Deletes all movies.
     */
    @Query("DELETE FROM movies")
    void deleteOldMovies();

    /**
     * Deletes all movies.
     */
    @Query("DELETE FROM reviews")
    void deleteOldReviews();

    /**
     * Deletes all movies.
     */
    @Query("DELETE FROM videos")
    void deleteOldVideos();
}
