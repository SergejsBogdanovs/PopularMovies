package lv.st.sbogdano.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import lv.st.sbogdano.popularmovies.data.MoviesRepository;

/**
 * Created by sbogdano on 20/02/2018.
 */
public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final MoviesRepository mRepository;
    private final int mMovieId;

    public DetailViewModelFactory(MoviesRepository repository, int movieId) {
        mRepository = repository;
        mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailActivityViewModel(mRepository, mMovieId);
    }
}
