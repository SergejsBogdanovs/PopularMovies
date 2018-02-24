package lv.st.sbogdano.popularmovies.ui.list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import lv.st.sbogdano.popularmovies.data.MoviesRepositoryImpl;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link MoviesRepositoryImpl}
 */
public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final MoviesRepositoryImpl mRepository;

    public MainViewModelFactory(MoviesRepositoryImpl repository) {
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(mRepository);
    }
}
