package lv.st.sbogdano.popularmovies.data.model.content;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "reviews")
public class Review {

    @PrimaryKey(autoGenerate = true)
    private int reviewId;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    // Constructor used by Room to create ReviewEntries
    public Review(int reviewId, String author, String content) {
        this.reviewId = reviewId;
        this.author = author;
        this.content = content;
    }

    @Ignore
    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
