package lv.st.sbogdano.popularmovies.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.database.ReviewEntry;
import lv.st.sbogdano.popularmovies.data.database.VideoEntry;
import lv.st.sbogdano.popularmovies.utilities.MoviesTypeProvider;

public interface MoviesRepository {

    LiveData<List<MovieEntry>> getMovies(MoviesTypeProvider type);

    LiveData<List<ReviewEntry>> getReviews();

    LiveData<List<VideoEntry>> getVideos();

    LiveData<Boolean> addToFavorite(MovieEntry movie);

    LiveData<Boolean> removeFromFavorite(MovieEntry movie);

    void init(MovieEntry movie);

}
