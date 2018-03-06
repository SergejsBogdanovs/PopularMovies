package lv.st.sbogdano.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Maybe;
import lv.st.sbogdano.popularmovies.AppExecutors;
import lv.st.sbogdano.popularmovies.data.api.ApiResponse;
import lv.st.sbogdano.popularmovies.data.api.MovieService;
import lv.st.sbogdano.popularmovies.data.api.ServiceGenerator;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.database.dao.MoviesDao;
import lv.st.sbogdano.popularmovies.data.database.ReviewEntry;
import lv.st.sbogdano.popularmovies.data.database.VideoEntry;
import lv.st.sbogdano.popularmovies.data.database.dao.ReviewDao;
import lv.st.sbogdano.popularmovies.data.database.dao.VideoDao;
import lv.st.sbogdano.popularmovies.data.model.Resource;
import lv.st.sbogdano.popularmovies.data.model.response.MoviesResponse;
import lv.st.sbogdano.popularmovies.data.network.MoviesNetworkDataSource;
import lv.st.sbogdano.popularmovies.utilities.MoviesTransformer;
import lv.st.sbogdano.popularmovies.data.model.MoviesType;

/**
 * Handles data operations in PopularMovies. Acts as a mediator between {@link MoviesNetworkDataSource}
 * and {@link MoviesDao}
 */
public class MoviesRepositoryImpl implements MoviesRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesRepositoryImpl sInstance;
    private final MoviesDao mMoviesDao;
    private final ReviewDao mReviewDao;
    private final VideoDao mVideoDao;
    private final MoviesNetworkDataSource mMoviesNetworkDataSource;
    private final AppExecutors mExecutors;
    private MovieService mMovieService = ServiceGenerator.createService(MovieService.class);

    private MoviesRepositoryImpl(MoviesDao moviesDao,
                                 ReviewDao reviewDao,
                                 VideoDao videoDao,
                                 MoviesNetworkDataSource moviesNetworkDataSource,
                                 AppExecutors executors) {
        mMoviesDao = moviesDao;
        mReviewDao = reviewDao;
        mVideoDao = videoDao;
        mMoviesNetworkDataSource = moviesNetworkDataSource;
        mExecutors = executors;
    }

    public synchronized static MoviesRepositoryImpl getInstance(
            MoviesDao moviesDao,
            ReviewDao reviewDao,
            VideoDao videoDao,
            MoviesNetworkDataSource moviesNetworkDataSource,
            AppExecutors executors) {

        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MoviesRepositoryImpl(
                        moviesDao,
                        reviewDao,
                        videoDao,
                        moviesNetworkDataSource,
                        executors);
            }
        }
        return sInstance;
    }

    @Override
    public LiveData<Resource<List<MovieEntry>>> loadMovies(MoviesType type) {

        return new NetworkBoundResource<List<MovieEntry>, MoviesResponse>(mExecutors) {
            @Override
            protected void saveCallResult(@NonNull MoviesResponse item) {
                List<MovieEntry> movieEntries = MoviesTransformer.transformMovies(item.getMovies(), type);
                mMoviesDao.insertAllMovies(movieEntries);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<MovieEntry> data) {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<MovieEntry>> loadFromDb() {
                return mMoviesDao.getMovies(type.name());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MoviesResponse>> createCall() {
                return mMovieService.getMovies(type.name().toLowerCase());
            }
        }.asLiveData();
    }

    @Override
    public LiveData<List<ReviewEntry>> getReviews() {
        return mReviewDao.getReviews();
    }

    @Override
    public LiveData<List<VideoEntry>> getVideos() {
        return mVideoDao.getVideos();
    }

    @Override
    public void addToFavorite(MovieEntry movie) {
        mExecutors.diskIO().execute(() -> {
            MovieEntry favoriteMovie = new MovieEntry(movie);
            mMoviesDao.insertFavorite(favoriteMovie);
        });
    }

    @Override
    public void removeFromFavorite(MovieEntry movie) {
        mExecutors.diskIO().execute(() ->
                mMoviesDao.deleteMovieFromFavorite(movie.getMovieId(), MoviesType.FAVORITE.name()));
    }

    @Override
    public void loadMovieDetails(MovieEntry movie) {
        LiveData<ReviewEntry[]> reviewData = mMoviesNetworkDataSource.getReviews(movie);
        reviewData.observeForever(newReviewsFromNetwork -> mExecutors.diskIO().execute(() -> {
            // delete old data
            mReviewDao.deleteOldReviews();
            // Insert our new reviews data into database
            mReviewDao.insertAllReviews(newReviewsFromNetwork);
        }));

        LiveData<VideoEntry[]> videoData = mMoviesNetworkDataSource.getVideos(movie);
        videoData.observeForever(newVideoFromNetwork -> mExecutors.diskIO().execute(() -> {
            // delete old data
            mVideoDao.deleteOldVideos();
            // Insert our new movies data into database
            mVideoDao.insertAllVideos(newVideoFromNetwork);
        }));
    }

    @Override
    public Maybe<MovieEntry> getFavoriteMovie(int movieId) {
        return mMoviesDao.getFavoriteMovie(movieId, MoviesType.FAVORITE.name());
    }
}
