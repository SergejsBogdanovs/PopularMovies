package lv.st.sbogdano.popularmovies.data.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "reviews")
public class ReviewEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String author;
    private String content;

    // Constructor used by Room to create ReviewEntries
    public ReviewEntry(int id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    @Ignore
    public ReviewEntry(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
