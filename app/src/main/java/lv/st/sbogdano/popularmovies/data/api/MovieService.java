package lv.st.sbogdano.popularmovies.data.api;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import lv.st.sbogdano.popularmovies.data.model.response.MoviesResponse;
import lv.st.sbogdano.popularmovies.data.model.response.ReviewsResponse;
import lv.st.sbogdano.popularmovies.data.model.response.VideosResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieService {

    @GET("{moviesType}/")
    LiveData<ApiResponse<MoviesResponse>> getMovies(@Path("moviesType") String type);

    @GET("{id}/reviews")
    LiveData<ApiResponse<ReviewsResponse>> getReviews(@Path("id") @NonNull int id);

    @GET("{id}/videos")
    LiveData<ApiResponse<VideosResponse>> getVideos(@Path("id") @NonNull int id);

}
