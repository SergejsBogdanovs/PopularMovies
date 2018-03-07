package lv.st.sbogdano.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import lv.st.sbogdano.popularmovies.data.MoviesRepository;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final MoviesRepository mRepository;

    public DetailViewModelFactory(MoviesRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailActivityViewModel(mRepository);
    }
}
