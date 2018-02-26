package lv.st.sbogdano.popularmovies.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "videos")
public class VideoEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String key;
    private String name;

    // Used by Room
    public VideoEntry(int id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    @Ignore
    public VideoEntry(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
