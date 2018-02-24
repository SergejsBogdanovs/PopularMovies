package lv.st.sbogdano.popularmovies.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.database.ReviewEntry;
import lv.st.sbogdano.popularmovies.data.database.VideoEntry;
import lv.st.sbogdano.popularmovies.utilities.MoviesTypeProvider;

public interface MoviesRepository {

    LiveData<List<MovieEntry>> getMovies(MoviesTypeProvider type);

    LiveData<List<ReviewEntry>> getReviews(MovieEntry movie);

    LiveData<List<VideoEntry>> getVideos(MovieEntry movie);

    LiveData<Boolean> addToFavorite(MovieEntry movie);

    LiveData<Boolean> removeFromFavorite(MovieEntry movie);
}
