package lv.st.sbogdano.popularmovies.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Maybe;
import lv.st.sbogdano.popularmovies.data.MoviesRepository;
import lv.st.sbogdano.popularmovies.data.MoviesRepositoryImpl;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.database.ReviewEntry;
import lv.st.sbogdano.popularmovies.data.database.VideoEntry;
import lv.st.sbogdano.popularmovies.data.model.Resource;
import lv.st.sbogdano.popularmovies.data.model.content.Review;

/**
 * {@link ViewModel} for {@link DetailActivity}
 */
public class DetailActivityViewModel extends ViewModel {

    private final MoviesRepository mRepository;

    DetailActivityViewModel(MoviesRepository repository) {
        mRepository = repository;
    }

    void init(MovieEntry movie) {
        mRepository.loadMovieDetails(movie);
    }

    public LiveData<List<ReviewEntry>> getReviews() {
       return mRepository.getReviews();
    }

    LiveData<List<VideoEntry>> getVideos() {
       return mRepository.getVideos();
    }

    public void removeFromFavorite(MovieEntry movie) {
        mRepository.removeFromFavorite(movie);
    }

    public void addToFavorite(MovieEntry movie) {
        mRepository.addToFavorite(movie);
    }

    public Maybe<MovieEntry> getFavoriteMovie(int movieId) {
        return mRepository.getFavoriteMovie(movieId);
    }
}
