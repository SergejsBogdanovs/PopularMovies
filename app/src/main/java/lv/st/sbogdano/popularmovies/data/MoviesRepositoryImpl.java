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
import lv.st.sbogdano.popularmovies.data.database.dao.ReviewDao;
import lv.st.sbogdano.popularmovies.data.database.dao.VideoDao;
import lv.st.sbogdano.popularmovies.data.model.Resource;
import lv.st.sbogdano.popularmovies.data.model.content.Review;
import lv.st.sbogdano.popularmovies.data.model.content.Video;
import lv.st.sbogdano.popularmovies.data.model.response.MoviesResponse;
import lv.st.sbogdano.popularmovies.data.model.response.ReviewsResponse;
import lv.st.sbogdano.popularmovies.data.model.response.VideosResponse;
import lv.st.sbogdano.popularmovies.utilities.MoviesTransformer;
import lv.st.sbogdano.popularmovies.data.model.MoviesType;

/**
 * Handles data operations in PopularMovies. Acts as a mediator between {@link MovieService}
 * and {@link MoviesDao}
 */
public class MoviesRepositoryImpl implements MoviesRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesRepositoryImpl sInstance;
    private final MoviesDao mMoviesDao;
    private final ReviewDao mReviewDao;
    private final VideoDao mVideoDao;
    private final AppExecutors mExecutors;
    private final MovieService mMovieService = ServiceGenerator.createService(MovieService.class);

    private MoviesRepositoryImpl(MoviesDao moviesDao,
                                 ReviewDao reviewDao,
                                 VideoDao videoDao,
                                 AppExecutors executors) {
        mMoviesDao = moviesDao;
        mReviewDao = reviewDao;
        mVideoDao = videoDao;
        mExecutors = executors;
    }

    public synchronized static MoviesRepositoryImpl getInstance(
            MoviesDao moviesDao,
            ReviewDao reviewDao,
            VideoDao videoDao,
            AppExecutors executors) {

        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MoviesRepositoryImpl(
                        moviesDao,
                        reviewDao,
                        videoDao,
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
    public LiveData<Resource<List<Review>>> getReviews(MovieEntry movie) {

        return new NetworkBoundResource<List<Review>, ReviewsResponse>(mExecutors) {
            @Override
            protected void saveCallResult(@NonNull ReviewsResponse item) {
                mReviewDao.deleteOldReviews();
                mReviewDao.insertAllReviews(item.getReviews());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Review> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Review>> loadFromDb() {
                return mReviewDao.getReviews();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ReviewsResponse>> createCall() {
                return mMovieService.getReviews(movie.getMovieId());
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<List<Video>>> getVideos(MovieEntry movie) {

        return new NetworkBoundResource<List<Video>, VideosResponse>(mExecutors) {
            @Override
            protected void saveCallResult(@NonNull VideosResponse item) {
                mVideoDao.deleteOldVideos();
                mVideoDao.insertAllVideos(item.getVideos());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Video> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Video>> loadFromDb() {
                return mVideoDao.getVideos();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<VideosResponse>> createCall() {
                return mMovieService.getVideos(movie.getMovieId());
            }
        }.asLiveData();
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
    public Maybe<MovieEntry> getFavoriteMovie(int movieId) {
        return mMoviesDao.getFavoriteMovie(movieId, MoviesType.FAVORITE.name());
    }
}
