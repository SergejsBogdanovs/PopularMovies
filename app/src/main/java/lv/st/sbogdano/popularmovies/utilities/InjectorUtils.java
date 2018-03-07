package lv.st.sbogdano.popularmovies.utilities;

import android.content.Context;

import lv.st.sbogdano.popularmovies.AppExecutors;
import lv.st.sbogdano.popularmovies.data.MoviesRepository;
import lv.st.sbogdano.popularmovies.data.MoviesRepositoryImpl;
import lv.st.sbogdano.popularmovies.data.database.MoviesDatabase;
import lv.st.sbogdano.popularmovies.ui.detail.DetailViewModelFactory;
import lv.st.sbogdano.popularmovies.ui.list.MainViewModelFactory;

/**
 * Provides static methods to inject the various classes needed for PopularMovies
 */
public class InjectorUtils {

    public static MoviesRepository provideRepository(Context context) {
        MoviesDatabase database = MoviesDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return MoviesRepositoryImpl.getInstance(
                database.moviesDao(),
                database.reviewDao(),
                database.videoDao(),
                executors);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context) {
        MoviesRepository repository = provideRepository(context.getApplicationContext());
        return new DetailViewModelFactory(repository);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        MoviesRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }
}
