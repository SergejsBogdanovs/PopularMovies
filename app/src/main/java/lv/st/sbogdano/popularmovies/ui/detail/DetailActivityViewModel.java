package lv.st.sbogdano.popularmovies.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import lv.st.sbogdano.popularmovies.data.MoviesRepository;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;

/**
 * {@link ViewModel} for {@link DetailActivity}
 */
public class DetailActivityViewModel extends ViewModel{

    private final LiveData<MovieEntry> mMovie;
    private final MoviesRepository mRepository;
    private final int mMovieId;

    public DetailActivityViewModel(MoviesRepository repository, int movieId) {
        mRepository = repository;
        mMovieId = movieId;
        mMovie = mRepository.getMovieDetails(mMovieId);
    }

    public LiveData<MovieEntry> getMovie() {
        return mMovie;
    }
}
