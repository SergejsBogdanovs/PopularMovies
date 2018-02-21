package lv.st.sbogdano.popularmovies.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import lv.st.sbogdano.popularmovies.AppDelegate;
import lv.st.sbogdano.popularmovies.utilities.MoviesTypeProvider;

/**
 * Created by sbogdano.
 */
public class Preferences {

    public static final String SETTINGS_NAME = "movies_prefs";

    private static final String TYPE_KEY = "movies_type_key";

    private static final String POPULAR_MOVIE_TYPE = "popular";
    private static final String TOP_RATED_MOVIE_TYPE = "top_rated";

    @NonNull
    public static MoviesTypeProvider getMoviesType() {
        SharedPreferences prefs = getPrefs();
        if (!prefs.contains(TYPE_KEY)) {
            prefs.edit().putString(TYPE_KEY, POPULAR_MOVIE_TYPE).apply();
            return MoviesTypeProvider.POPULAR;
        }

        String type = prefs.getString(TYPE_KEY, "");
        if (TextUtils.equals(type, TOP_RATED_MOVIE_TYPE)) {
            return MoviesTypeProvider.TOP_RATED;
        }
        return MoviesTypeProvider.POPULAR;
    }

    @NonNull
    public static SharedPreferences getPrefs() {
        return AppDelegate.getAppContext()
                .getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

}
