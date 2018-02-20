package lv.st.sbogdano.popularmovies.utilities;

import android.content.Context;
import lv.st.sbogdano.popularmovies.AppExecutors;
import lv.st.sbogdano.popularmovies.data.MoviesRepository;
import lv.st.sbogdano.popularmovies.data.database.MoviesDatabase;
import lv.st.sbogdano.popularmovies.data.model.content.Movie;
import lv.st.sbogdano.popularmovies.data.network.MoviesNetworkDataSource;
import lv.st.sbogdano.popularmovies.ui.list.MainViewModelFactory;

/**
 * Provides static methods to inject the various classes needed for PopularMovies
 */
public class InjectorUtils {

    public static MoviesRepository provideRepository(Context context) {
        MoviesDatabase database = MoviesDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MoviesNetworkDataSource networkDataSource
                = MoviesNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return MoviesRepository.getInstance(database.moviesDao(), networkDataSource, executors);
    }

    public static MoviesNetworkDataSource provideNetworkDataSource(Context context) {
        // This call to provide repository is necessary if the app starts from a service - in this
        // case the repository will not exist unless it is specifically created.
        provideRepository(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return MoviesNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

//    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, Movie movie) {
//        MoviesRepository repository = provideRepository(context.getApplicationContext());
//        return new DetailViewModelFactory(repository, movie);
//    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        MoviesRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }
}
