package lv.st.sbogdano.popularmovies.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import lv.st.sbogdano.popularmovies.data.database.ReviewEntry;

/**
 * Created by sbogdano on 04/03/2018.
 */
@Dao
public interface ReviewDao {
    /**
     * Select all entries.
     * @return {@LiveData} list of all reviews.
     */
    @Query("SELECT * FROM reviews")
    LiveData<List<ReviewEntry>> getReviews();

    /**
     * Inserts a list of {@link ReviewEntry} into the reviews table.
     * @param reviewEntries
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllReviews(ReviewEntry... reviewEntries);

    /**
     * Deletes all movies.
     */
    @Query("DELETE FROM reviews")
    void deleteOldReviews();

}
