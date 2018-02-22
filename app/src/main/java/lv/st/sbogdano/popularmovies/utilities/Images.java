package lv.st.sbogdano.popularmovies.utilities;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import lv.st.sbogdano.popularmovies.AppDelegate;
import lv.st.sbogdano.popularmovies.BuildConfig;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;

public final class Images {
    public static final String WIDTH_185 = "w185";
    public static final String WIDTH_780 = "w780";

    private Images() {
    }


    public static void loadMovie(@NonNull ImageView imageView, @NonNull MovieEntry movie,
                                 @NonNull String size) {
        loadMovie(imageView, movie.getPosterPath(), size);
    }

    public static void loadMovie(@NonNull ImageView imageView, @NonNull String posterPath,
                                 @NonNull String size) {
        String url = BuildConfig.IMAGES_BASE_URL + size + posterPath;
        Picasso.with(imageView.getContext())
                .load(url)
                .noFade()
                .into(imageView);
    }

    public static void fetch(@NonNull String posterPath, @NonNull String size) {
        String url = BuildConfig.IMAGES_BASE_URL + size + posterPath;
        Picasso.with(AppDelegate.getAppContext())
                .load(url)
                .fetch();
    }

    public static void loadImageIntoAppBar(ImageView imageView, MovieEntry movie) {
    }
}
