package lv.st.sbogdano.popularmovies.data.api;

import android.support.annotation.NonNull;

import lv.st.sbogdano.popularmovies.data.model.response.MoviesResponse;
import lv.st.sbogdano.popularmovies.data.model.response.ReviewsResponse;
import lv.st.sbogdano.popularmovies.data.model.response.VideosResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("{moviesType}/")
    Call<MoviesResponse> getMovies(@Path("moviesType") String type, @Query("api_key") String apiKey);

    @GET("{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") @NonNull String id, @Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<VideosResponse> getVideo(@Path("id") @NonNull String id, @Query("api_key") String apiKey);


}
