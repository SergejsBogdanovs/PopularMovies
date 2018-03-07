package lv.st.sbogdano.popularmovies.data.model.content;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "videos")
public class Video {

    @PrimaryKey(autoGenerate = true)
    private int videoId;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    // Used by Room
    public Video(int videoId, String key, String name) {
        this.videoId = videoId;
        this.key = key;
        this.name = name;
    }

    @Ignore
    public Video(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
