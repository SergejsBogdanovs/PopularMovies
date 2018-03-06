package lv.st.sbogdano.popularmovies.utilities;

import java.util.ArrayList;
import java.util.List;

import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.database.ReviewEntry;
import lv.st.sbogdano.popularmovies.data.database.VideoEntry;
import lv.st.sbogdano.popularmovies.data.model.MoviesType;
import lv.st.sbogdano.popularmovies.data.model.content.Movie;
import lv.st.sbogdano.popularmovies.data.model.content.Review;
import lv.st.sbogdano.popularmovies.data.model.content.Video;

public class MoviesTransformer {

    public static List<MovieEntry> transformMovies(List<Movie> movies, MoviesType type) {

        List<MovieEntry> movieEntries = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            int movieId = movies.get(i).getId();
            String posterPath = movies.get(i).getPosterPath();
            String overview = movies.get(i).getOverview();
            String title = movies.get(i).getOriginalTitle();
            String releasedDate = movies.get(i).getReleaseDate();
            double voteAverage = movies.get(i).getVoteAverage();

            movieEntries.add(
                    new MovieEntry(movieId, posterPath, overview, title, releasedDate, voteAverage, type.name()));
        }

        return movieEntries;
    }

    public static ReviewEntry[] transformReviews(List<Review> reviews) {
        int responseSize = reviews.size();

        ReviewEntry[] reviewEntries = new ReviewEntry[responseSize];

        for (int i = 0; i < responseSize; i++) {
            String author = reviews.get(i).getAuthor();
            String content = reviews.get(i).getContent();
            reviewEntries[i] = new ReviewEntry(author, content);
        }

        return reviewEntries;
    }

    public static VideoEntry[] transformVideos(List<Video> videos) {
        int responseSize = videos.size();

        VideoEntry[] videoEntries = new VideoEntry[responseSize];

        for (int i = 0; i < responseSize; i++) {
            String key = videos.get(i).getKey();
            String name = videos.get(i).getName();
            videoEntries[i] = new VideoEntry(key, name);
        }

        return videoEntries;
    }
}
