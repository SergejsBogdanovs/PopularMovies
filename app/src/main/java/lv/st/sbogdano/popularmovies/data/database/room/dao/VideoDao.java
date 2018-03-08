package lv.st.sbogdano.popularmovies.data.database.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import lv.st.sbogdano.popularmovies.data.model.content.Video;

@Dao
public interface VideoDao {

    @Query("SELECT * FROM videos")
    LiveData<List<Video>> getVideos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllVideos(List<Video> videos);

    @Query("DELETE FROM videos")
    void deleteOldVideos();
}
