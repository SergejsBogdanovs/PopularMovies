package lv.st.sbogdano.popularmovies.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Maybe;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.database.ReviewEntry;
import lv.st.sbogdano.popularmovies.data.database.VideoEntry;
import lv.st.sbogdano.popularmovies.data.model.Resource;
import lv.st.sbogdano.popularmovies.data.model.MoviesType;

public interface MoviesRepository {

    LiveData<Resource<List<MovieEntry>>> loadMovies(MoviesType type);

    LiveData<List<ReviewEntry>> getReviews();

    LiveData<List<VideoEntry>> getVideos();

    void addToFavorite(MovieEntry movie);

    void removeFromFavorite(MovieEntry movie);

    void loadMovieDetails(MovieEntry movie);

    Maybe<MovieEntry> getFavoriteMovie(int movieId);
}
