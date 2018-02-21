package lv.st.sbogdano.popularmovies.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import io.reactivex.annotations.Nullable;
import lv.st.sbogdano.popularmovies.data.MoviesRepository;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.content.Movie;
import lv.st.sbogdano.popularmovies.data.model.response.MoviesResponse;
import lv.st.sbogdano.popularmovies.utilities.MoviesTypeProvider;

import static android.content.ContentValues.TAG;

/**
 * {@link ViewModel} for {@link MainActivity}
 */
public class MainActivityViewModel extends ViewModel {

    private final MoviesRepository mRepository;
    private LiveData<List<MovieEntry>> mMovies;


    public MainActivityViewModel(@Nullable MoviesRepository repository) {
        mRepository = repository;
    }

    public LiveData<List<MovieEntry>> getMovies(MoviesTypeProvider type) {
        mMovies = mRepository.getMovies(type);
        return mMovies;
    }
}
