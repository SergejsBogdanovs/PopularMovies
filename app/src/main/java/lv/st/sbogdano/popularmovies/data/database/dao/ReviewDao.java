package lv.st.sbogdano.popularmovies.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import lv.st.sbogdano.popularmovies.data.model.content.Review;

@Dao
public interface ReviewDao {

    @Query("SELECT * FROM reviews")
    LiveData<List<Review>> getReviews();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllReviews(List<Review> reviews);

    @Query("DELETE FROM reviews")
    void deleteOldReviews();
}
