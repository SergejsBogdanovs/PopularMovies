package lv.st.sbogdano.popularmovies.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lv.st.sbogdano.popularmovies.AppExecutors;
import lv.st.sbogdano.popularmovies.data.api.ApiResponse;
import lv.st.sbogdano.popularmovies.data.api.MovieService;
import lv.st.sbogdano.popularmovies.data.api.ServiceGenerator;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.database.ReviewEntry;
import lv.st.sbogdano.popularmovies.data.database.VideoEntry;
import lv.st.sbogdano.popularmovies.data.model.response.ReviewsResponse;
import lv.st.sbogdano.popularmovies.data.model.response.VideosResponse;
import lv.st.sbogdano.popularmovies.utilities.MoviesTransformer;
import retrofit2.Call;

/**
 * Provides an API for doing all operations with the server data
 */
public class MoviesNetworkDataSource {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesNetworkDataSource sInstance;
    private final Context mContext;

    private final MutableLiveData<ApiResponse<List<MovieEntry>>> mDownloadedMovies;
    private final MutableLiveData<ReviewEntry[]> mDownloadedReviews;
    private final MutableLiveData<VideoEntry[]> mDownloadedVideos;

    private final AppExecutors mExecutors;

    private MoviesNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedMovies = new MutableLiveData<>();
        mDownloadedReviews = new MutableLiveData<>();
        mDownloadedVideos = new MutableLiveData<>();
    }

    public static MoviesNetworkDataSource getInstance(Context context, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MoviesNetworkDataSource(context.getApplicationContext(), executors);
            }
        }
        return sInstance;
    }

    public LiveData<ApiResponse<List<MovieEntry>>> getMovies() {
        return mDownloadedMovies;
    }

//    public void fetchMovies(MoviesType type) {
//
//        MovieService client = ServiceGenerator.createService(MovieService.class);
//
//        Call<MoviesResponse> clientMovies = client.getMovies(type.name().toLowerCase());
//
//        mExecutors.networkIO().execute(() -> {
//            try {
//                MoviesResponse response = clientMovies.execute().body();
//                ApiResponse<List<MovieEntry>> movieEntries = MoviesTransformer.transformMovies(response.getMovies(), type);
//                mDownloadedMovies.postValue(movieEntries);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }

    public LiveData<ReviewEntry[]> getReviews(MovieEntry movieEntry) {
        MovieService client = ServiceGenerator.createService(MovieService.class);
        Call<ReviewsResponse> clientReviews = client.getReviews(movieEntry.getMovieId());
        mExecutors.networkIO().execute(() -> {
            try {
                ReviewsResponse response = clientReviews.execute().body();
                ReviewEntry[] reviewEntries = MoviesTransformer.transformReviews(
                        response != null ? response.getReviews() : new ArrayList<>());
                mDownloadedReviews.postValue(reviewEntries);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return mDownloadedReviews;
    }

    public LiveData<VideoEntry[]> getVideos(MovieEntry movieEntry) {
        MovieService client = ServiceGenerator.createService(MovieService.class);
        Call<VideosResponse> clientVideos = client.getVideos(movieEntry.getMovieId());
        mExecutors.networkIO().execute(() -> {
            try {
                VideosResponse response = clientVideos.execute().body();
                VideoEntry[] videoEntries = MoviesTransformer.transformVideos(
                        response != null ? response.getVideos() : new ArrayList<>());
                mDownloadedVideos.postValue(videoEntries);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return mDownloadedVideos;
    }
}
