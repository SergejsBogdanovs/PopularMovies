package lv.st.sbogdano.popularmovies.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Maybe;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.Resource;
import lv.st.sbogdano.popularmovies.data.model.MoviesType;
import lv.st.sbogdano.popularmovies.data.model.content.Review;
import lv.st.sbogdano.popularmovies.data.model.content.Video;

public interface MoviesRepository {

    LiveData<Resource<List<MovieEntry>>> loadMovies(MoviesType type);

    Maybe<MovieEntry> getFavoriteMovie(int movieId);

    LiveData<Resource<List<Review>>> getReviews(MovieEntry movieEntry);

    LiveData<Resource<List<Video>>> getVideos(MovieEntry movieEntry);

    void addToFavorite(MovieEntry movie);

    void removeFromFavorite(MovieEntry movie);

}
