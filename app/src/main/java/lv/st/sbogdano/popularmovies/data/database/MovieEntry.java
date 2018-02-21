package lv.st.sbogdano.popularmovies.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Defines the schema of a table in {@link android.arch.persistence.room.Room} for a single movie.
 */
@Entity(tableName = "movies", indices = {@Index(value = {"movieId"}, unique = true)})
public class MovieEntry{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int movieId;
    private String posterPath;
    private String overview;
    private String title;
    private String releasedDate;
    private double voteAverage;

    // Constructor used by Room to create MovieEntries
    public MovieEntry(int id,
                      int movieId,
                      String posterPath,
                      String overview,
                      String title,
                      String releasedDate,
                      double voteAverage) {
        this.id = id;
        this.movieId = movieId;
        this.posterPath = posterPath;
        this.overview = overview;
        this.title = title;
        this.releasedDate = releasedDate;
        this.voteAverage = voteAverage;
    }

    // Constructor used by Room to create MovieEntries
    @Ignore
    public MovieEntry(int movieId,
                      String posterPath,
                      String overview,
                      String title,
                      String releasedDate,
                      double voteAverage) {
        this.movieId = movieId;
        this.posterPath = posterPath;
        this.overview = overview;
        this.title = title;
        this.releasedDate = releasedDate;
        this.voteAverage = voteAverage;
    }

    public int getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getTitle() {
        return title;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

}
