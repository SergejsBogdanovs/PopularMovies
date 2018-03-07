package lv.st.sbogdano.popularmovies.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import lv.st.sbogdano.popularmovies.data.model.MoviesType;

/**
 * Defines the schema of a table in {@link android.arch.persistence.room.Room} for a single movie.
 */
@Entity(tableName = "movies")
public class MovieEntry implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int movieId;
    private String posterPath;
    private String overview;
    private String title;
    private String releasedDate;
    private double voteAverage;
    private String type;

    @Ignore
    boolean favorite;

    // Constructor used by Room to create MovieEntries
    public MovieEntry(int id,
                      int movieId,
                      String posterPath,
                      String overview,
                      String title,
                      String releasedDate,
                      double voteAverage,
                      String type) {
        this.id = id;
        this.movieId = movieId;
        this.posterPath = posterPath;
        this.overview = overview;
        this.title = title;
        this.releasedDate = releasedDate;
        this.voteAverage = voteAverage;
        this.type = type;
    }

    // Default constructor
    @Ignore
    public MovieEntry(int movieId,
                      String posterPath,
                      String overview,
                      String title,
                      String releasedDate,
                      double voteAverage,
                      String type) {
        this.movieId = movieId;
        this.posterPath = posterPath;
        this.overview = overview;
        this.title = title;
        this.releasedDate = releasedDate;
        this.voteAverage = voteAverage;
        this.type = type;
    }

    public MovieEntry(MovieEntry another) {
        this.movieId = another.getMovieId();
        this.posterPath = another.getPosterPath();
        this.overview = another.getOverview();
        this.title = another.getTitle();
        this.releasedDate = another.getReleasedDate();
        this.voteAverage = another.getVoteAverage();
        this.type = MoviesType.FAVORITE.name();
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.movieId);
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeString(this.title);
        dest.writeString(this.releasedDate);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.type);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
    }

    protected MovieEntry(Parcel in) {
        this.id = in.readInt();
        this.movieId = in.readInt();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.title = in.readString();
        this.releasedDate = in.readString();
        this.voteAverage = in.readDouble();
        this.type = in.readString();
        this.favorite = in.readByte() != 0;
    }

    public static final Creator<MovieEntry> CREATOR = new Creator<MovieEntry>() {
        @Override
        public MovieEntry createFromParcel(Parcel source) {
            return new MovieEntry(source);
        }

        @Override
        public MovieEntry[] newArray(int size) {
            return new MovieEntry[size];
        }
    };
}
