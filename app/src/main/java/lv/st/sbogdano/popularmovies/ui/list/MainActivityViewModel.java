package lv.st.sbogdano.popularmovies.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.annotations.Nullable;
import lv.st.sbogdano.popularmovies.data.MoviesRepository;
import lv.st.sbogdano.popularmovies.data.MoviesRepositoryImpl;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.ui.settings.Preferences;
import lv.st.sbogdano.popularmovies.utilities.MoviesTypeProvider;

/**
 * {@link ViewModel} for {@link MainActivity}
 */
public class MainActivityViewModel extends ViewModel {

    private MoviesTypeProvider mType;

    private final MoviesRepository mRepository;
    private LiveData<List<MovieEntry>> mMovies;


    public MainActivityViewModel(@Nullable MoviesRepository repository) {
        mRepository = repository;
    }

    public void init() {
        mType = Preferences.getMoviesType();
        loadMovies();
    }

    private void loadMovies() {
        mMovies = mRepository.getMovies(mType);
    }

    public LiveData<List<MovieEntry>> getMovies() {
        return mMovies;
    }

    public void onResume() {
        MoviesTypeProvider type = Preferences.getMoviesType();
        if (mType != type) {
            mType = type;
            loadMovies();
        }
    }
}
