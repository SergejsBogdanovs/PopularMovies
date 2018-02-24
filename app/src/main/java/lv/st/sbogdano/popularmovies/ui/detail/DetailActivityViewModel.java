package lv.st.sbogdano.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModel;

import lv.st.sbogdano.popularmovies.data.MoviesRepositoryImpl;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;

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

}
