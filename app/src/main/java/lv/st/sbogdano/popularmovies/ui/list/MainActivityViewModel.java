package lv.st.sbogdano.popularmovies.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import lv.st.sbogdano.popularmovies.data.MoviesRepository;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.content.Movie;
import lv.st.sbogdano.popularmovies.data.model.response.MoviesResponse;

import static android.content.ContentValues.TAG;

/**
 * {@link ViewModel} for {@link MainActivity}
 */
public class MainActivityViewModel extends ViewModel {

    private final MoviesRepository mRepository;
    private final LiveData<List<MovieEntry>> mMovies;

    public MainActivityViewModel(MoviesRepository repository) {
        this.mRepository = repository;
        this.mMovies = mRepository.getMovies();
    }

    public LiveData<List<MovieEntry>> getMovies() {
        return mMovies;
    }
}
