package lv.st.sbogdano.popularmovies.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import io.reactivex.FlowableSubscriber;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lv.st.sbogdano.popularmovies.AppExecutors;
import lv.st.sbogdano.popularmovies.BuildConfig;
import lv.st.sbogdano.popularmovies.R;
import lv.st.sbogdano.popularmovies.data.api.MovieService;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.content.Movie;
import lv.st.sbogdano.popularmovies.data.model.response.MoviesResponse;
import lv.st.sbogdano.popularmovies.utilities.MoviesParser;
import lv.st.sbogdano.popularmovies.utilities.MoviesPreferences;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Provides an API for doing all operations with the server data
 */
public class MoviesNetworkDataSource {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesNetworkDataSource sInstance;
    private final Context mContext;

    private final MutableLiveData<MovieEntry[]> mDownloadedMovies;
    private final AppExecutors mExecutors;

    private MoviesNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedMovies = new MutableLiveData<>();
    }

    public static MoviesNetworkDataSource getInstance(Context context, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MoviesNetworkDataSource(context.getApplicationContext(), executors);
            }
        }
        return sInstance;
    }

    public LiveData<MovieEntry[]> getMovies() {
        return mDownloadedMovies;
    }

    public void fetchMovies() {

        String moviesType = MoviesPreferences.getPreferredMoviesType(mContext);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        MovieService client = retrofit.create(MovieService.class);

        Call<MoviesResponse> clientMovies = client.getMovies(moviesType, BuildConfig.API_KEY);

        mExecutors.networkIO().execute(() -> {

            try {
                MoviesResponse response = clientMovies.execute().body();
                MovieEntry[] movieEntries = MoviesParser.parse(response.getMovies());
                mDownloadedMovies.postValue(movieEntries);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
