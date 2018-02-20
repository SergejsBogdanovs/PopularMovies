package lv.st.sbogdano.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import lv.st.sbogdano.popularmovies.AppExecutors;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.database.MoviesDao;
import lv.st.sbogdano.popularmovies.data.network.MoviesNetworkDataSource;

import static android.content.ContentValues.TAG;

/**
 * Handles data operations in PopularMovies. Acts as a mediator between {@link MoviesNetworkDataSource}
 * and {@link lv.st.sbogdano.popularmovies.data.database.MoviesDao}
 */
public class MoviesRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesRepository sInstance;
    private final MoviesDao mMoviesDao;
    private final MoviesNetworkDataSource mMoviesNetworkDataSource;
    private final AppExecutors mExecutors;

    private MoviesRepository(
            MoviesDao moviesDao,
            MoviesNetworkDataSource moviesNetworkDataSource,
            AppExecutors executors) {

        mMoviesDao = moviesDao;
        mMoviesNetworkDataSource = moviesNetworkDataSource;
        mExecutors = executors;

        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<MovieEntry[]> networkData = mMoviesNetworkDataSource.getMovies();
        networkData.observeForever(newMoviesFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                // delete old data
                mMoviesDao.deleteOldMovies();
                // Insert our new movies data into database
                mMoviesDao.bulkInsert(newMoviesFromNetwork);
            });
        });
    }

    public synchronized static MoviesRepository getInstance(
            MoviesDao moviesDao,
            MoviesNetworkDataSource moviesNetworkDataSource,
            AppExecutors executors) {

        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MoviesRepository(moviesDao, moviesNetworkDataSource, executors);
            }
        }
        return sInstance;
    }

    public LiveData<List<MovieEntry>> getMovies() {
        initializeData();
        return mMoviesDao.getMovies();
    }

    public synchronized void initializeData() {
        mMoviesNetworkDataSource.fetchMovies();
    }

    public LiveData<MovieEntry> getMovieDetails(int movieId) {
        return mMoviesDao.getMovieDetails(movieId);
    }
}
