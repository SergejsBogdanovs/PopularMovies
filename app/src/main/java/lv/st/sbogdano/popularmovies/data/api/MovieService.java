package lv.st.sbogdano.popularmovies.data.api;

import lv.st.sbogdano.popularmovies.data.model.response.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("{moviesType}/")
    Call<MoviesResponse> getMovies(@Path("moviesType") String type, @Query("api_key") String apiKey);

}
