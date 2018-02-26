package lv.st.sbogdano.popularmovies.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import lv.st.sbogdano.popularmovies.data.MoviesRepositoryImpl;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.database.ReviewEntry;
import lv.st.sbogdano.popularmovies.data.database.VideoEntry;
import lv.st.sbogdano.popularmovies.data.model.content.Review;

/**
 * {@link ViewModel} for {@link DetailActivity}
 */
public class DetailActivityViewModel extends ViewModel {

    private MovieEntry mMovie;
    private final MoviesRepositoryImpl mRepository;


    public DetailActivityViewModel(MoviesRepositoryImpl repository, MovieEntry movie) {
        mRepository = repository;
        mMovie = movie;
    }

    public LiveData<List<ReviewEntry>> getReviews(MovieEntry movie) {
       return mRepository.getReviews(movie);
    }

    public LiveData<List<VideoEntry>> getVideos(MovieEntry movie) {
       return mRepository.getVideos(movie);
    }


}
