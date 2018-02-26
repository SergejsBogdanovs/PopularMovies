package lv.st.sbogdano.popularmovies.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import lv.st.sbogdano.popularmovies.AppExecutors;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.database.MoviesDao;
import lv.st.sbogdano.popularmovies.data.database.ReviewEntry;
import lv.st.sbogdano.popularmovies.data.database.VideoEntry;
import lv.st.sbogdano.popularmovies.data.model.content.Video;
import lv.st.sbogdano.popularmovies.data.network.MoviesNetworkDataSource;
import lv.st.sbogdano.popularmovies.utilities.MoviesTypeProvider;

/**
 * Handles data operations in PopularMovies. Acts as a mediator between {@link MoviesNetworkDataSource}
 * and {@link lv.st.sbogdano.popularmovies.data.database.MoviesDao}
 */
public class MoviesRepositoryImpl implements MoviesRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesRepositoryImpl sInstance;
    private final MoviesDao mMoviesDao;
    private final MoviesNetworkDataSource mMoviesNetworkDataSource;
    private final AppExecutors mExecutors;

    private MoviesRepositoryImpl(MoviesDao moviesDao,
                                 MoviesNetworkDataSource moviesNetworkDataSource,
                                 AppExecutors executors) {

        mMoviesDao = moviesDao;
        mMoviesNetworkDataSource = moviesNetworkDataSource;
        mExecutors = executors;

        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<MovieEntry[]> movieData = mMoviesNetworkDataSource.getMovies();
        movieData.observeForever(newMoviesFromNetwork -> mExecutors.diskIO().execute(() -> {
            // delete old data
            mMoviesDao.deleteOldMovies();
            // Insert our new movies data into database
            mMoviesDao.insertAllMovies(newMoviesFromNetwork);
        }));
    }

    public synchronized static MoviesRepositoryImpl getInstance(
            MoviesDao moviesDao,
            MoviesNetworkDataSource moviesNetworkDataSource,
            AppExecutors executors) {

        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MoviesRepositoryImpl(moviesDao, moviesNetworkDataSource, executors);
            }
        }
        return sInstance;
    }

    @Override
    public LiveData<List<MovieEntry>> getMovies(MoviesTypeProvider type) {
        initializeData(type);
        return mMoviesDao.getMovies();
    }

    @Override
    public LiveData<List<ReviewEntry>> getReviews() {
        return mMoviesDao.getReviews();
    }

    @Override
    public LiveData<List<VideoEntry>> getVideos() {
        return mMoviesDao.getVideos();
    }

    @Override
    public LiveData<Boolean> addToFavorite(MovieEntry movie) {
        return null;
    }

    @Override
    public LiveData<Boolean> removeFromFavorite(MovieEntry movie) {
        return null;
    }

    @Override
    public void init(MovieEntry movie) {
        LiveData<ReviewEntry[]> reviewData = mMoviesNetworkDataSource.getReviews(movie);
        reviewData.observeForever(newReviewsFromNetwork -> mExecutors.diskIO().execute(() -> {
            // delete old data
            mMoviesDao.deleteOldReviews();
            // Insert our new reviews data into database
            mMoviesDao.insertAllReviews(newReviewsFromNetwork);
        }));

        LiveData<VideoEntry[]> videoData = mMoviesNetworkDataSource.getVideos(movie);
        videoData.observeForever(newVideoFromNetwork -> mExecutors.diskIO().execute(() -> {
            // delete old data
            mMoviesDao.deleteOldVideos();
            // Insert our new movies data into database
            mMoviesDao.insertAllVideos(newVideoFromNetwork);
        }));
    }

    public synchronized void initializeData(MoviesTypeProvider type) {
        mMoviesNetworkDataSource.fetchMovies(type);
    }

}
