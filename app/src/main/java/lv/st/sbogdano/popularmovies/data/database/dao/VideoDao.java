package lv.st.sbogdano.popularmovies.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import lv.st.sbogdano.popularmovies.data.database.VideoEntry;

/**
 * Created by sbogdano on 04/03/2018.
 */
@Dao
public interface VideoDao {

    /**
     * Select all entries.
     * @return {@LiveData} list of all videos.
     */
    @Query("SELECT * FROM videos")
    LiveData<List<VideoEntry>> getVideos();

    /**
     * Inserts a list of {@link VideoEntry} into the movies table.
     * @param videoEntries
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllVideos(VideoEntry... videoEntries);

    /**
     * Deletes all movies.
     */
    @Query("DELETE FROM videos")
    void deleteOldVideos();

}
