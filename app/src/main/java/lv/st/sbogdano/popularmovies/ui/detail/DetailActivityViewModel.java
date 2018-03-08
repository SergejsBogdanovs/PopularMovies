package lv.st.sbogdano.popularmovies.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Observable;
import lv.st.sbogdano.popularmovies.data.MoviesRepository;
import lv.st.sbogdano.popularmovies.data.database.room.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.Resource;
import lv.st.sbogdano.popularmovies.data.model.content.Review;
import lv.st.sbogdano.popularmovies.data.model.content.Video;

/**
 * {@link ViewModel} for {@link DetailActivity}
 */
class DetailActivityViewModel extends ViewModel {

    private final MoviesRepository mRepository;

    DetailActivityViewModel(MoviesRepository repository) {
        mRepository = repository;
    }


    LiveData<Resource<List<Review>>> getReviews(MovieEntry movieEntry) {
       return mRepository.getReviews(movieEntry);
    }

    LiveData<Resource<List<Video>>> getVideos(MovieEntry movieEntry) {
       return mRepository.getVideos(movieEntry);
    }

    void removeFromFavorite(MovieEntry movie) {
        mRepository.removeFromFavorite(movie);
    }

    void addToFavorite(MovieEntry movie) {
        mRepository.addToFavorite(movie);
    }

    Observable<List<MovieEntry>> getFavoriteMovie(int movieId) {
        return mRepository.getFavoriteMovie(movieId);
    }
}
