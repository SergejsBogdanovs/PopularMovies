package lv.st.sbogdano.popularmovies.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.annotations.Nullable;
import lv.st.sbogdano.popularmovies.data.MoviesRepository;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.Resource;
import lv.st.sbogdano.popularmovies.ui.settings.Preferences;
import lv.st.sbogdano.popularmovies.data.model.MoviesType;

/**
 * {@link ViewModel} for {@link MainActivity}
 */
public class MainActivityViewModel extends ViewModel {

    private MoviesType mType;
    private final MoviesRepository mRepository;
    private LiveData<Resource<List<MovieEntry>>> mMovies;


    public MainActivityViewModel(@Nullable MoviesRepository repository) {
        mRepository = repository;
    }

    public void init() {
        mType = Preferences.getMoviesType();
        load();
    }

    private void load() {
        mMovies = mRepository.loadMovies(mType);
    }

    public LiveData<Resource<List<MovieEntry>>> getMovies() {
        return mMovies;
    }

    public void onResume() {
        MoviesType type = Preferences.getMoviesType();
        if (mType != type || mType == MoviesType.FAVORITE) {
            mType = type;
            load();
        }
    }
}
