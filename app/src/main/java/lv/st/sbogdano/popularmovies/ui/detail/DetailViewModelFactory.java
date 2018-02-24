package lv.st.sbogdano.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import lv.st.sbogdano.popularmovies.data.MoviesRepositoryImpl;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final MoviesRepositoryImpl mRepository;
    private final MovieEntry mMovie;

    public DetailViewModelFactory(MoviesRepositoryImpl repository, MovieEntry movie) {
        mRepository = repository;
        mMovie = movie;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailActivityViewModel(mRepository, mMovie);
    }
}
