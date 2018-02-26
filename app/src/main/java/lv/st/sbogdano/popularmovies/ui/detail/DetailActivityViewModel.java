package lv.st.sbogdano.popularmovies.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import lv.st.sbogdano.popularmovies.data.MoviesRepository;
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
    private final MoviesRepository mRepository;


    public DetailActivityViewModel(MoviesRepository repository, MovieEntry movie) {
        mRepository = repository;
        mMovie = movie;
    }

    public LiveData<List<ReviewEntry>> getReviews() {
       return mRepository.getReviews();
    }

    public LiveData<List<VideoEntry>> getVideos() {
       return mRepository.getVideos();
    }

    public void init(MovieEntry movie) {
        mRepository.init(movie);
    }

}
