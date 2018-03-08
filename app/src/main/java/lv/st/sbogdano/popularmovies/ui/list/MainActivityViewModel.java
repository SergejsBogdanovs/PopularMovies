package lv.st.sbogdano.popularmovies.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.TypedValue;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import lv.st.sbogdano.popularmovies.data.MoviesRepository;
import lv.st.sbogdano.popularmovies.data.database.room.MovieEntry;
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
    private Observable<List<MovieEntry>> mFavoriteMovies;


    public MainActivityViewModel(@Nullable MoviesRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<List<MovieEntry>>> getMovies() {
        return mMovies;
    }

    public Observable<List<MovieEntry>> getFavoriteMovies() {
        return mFavoriteMovies;
    }

    public void init() {
        mType = Preferences.getMoviesType();
        if (mType != MoviesType.FAVORITE) {
            load();
        } else {
            loadFavorite();
        }
    }

    private void loadFavorite() {
        mFavoriteMovies = mRepository.getFavoriteMovies();
    }

    private void load() {
        mMovies = mRepository.loadMovies(mType);
    }

    public void onResume() {
        MoviesType type = Preferences.getMoviesType();
        if (mType != type) {
            switch (type) {
                case FAVORITE:
                    loadFavorite();
                    break;
                default:
                    mType = type;
                    load();
            }
        }
    }
}
