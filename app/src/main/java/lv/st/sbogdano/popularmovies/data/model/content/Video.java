package lv.st.sbogdano.popularmovies.data.model.content;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "videos")
public class Video implements Parcelable{

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.videoId);
        dest.writeString(this.key);
        dest.writeString(this.name);
    }

    protected Video(Parcel in) {
        this.videoId = in.readInt();
        this.key = in.readString();
        this.name = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
