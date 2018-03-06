package lv.st.sbogdano.popularmovies.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import lv.st.sbogdano.popularmovies.AppDelegate;
import lv.st.sbogdano.popularmovies.data.model.MoviesType;

/**
 * Created by sbogdano.
 */
public class Preferences {

    public static final String SETTINGS_NAME = "movies_prefs";

    private static final String TYPE_KEY = "movies_type_key";

    public static final String POPULAR_MOVIE_TYPE = "popular";
    public static final String TOP_RATED_MOVIE_TYPE = "top_rated";
    public static final String FAVORITE_MOVIE_TYPE = "favorite";

    @NonNull
    public static MoviesType getMoviesType() {
        SharedPreferences prefs = getPrefs();
        if (!prefs.contains(TYPE_KEY)) {
            prefs.edit().putString(TYPE_KEY, POPULAR_MOVIE_TYPE).apply();
            return MoviesType.POPULAR;
        }

        String type = prefs.getString(TYPE_KEY, "");
        if (TextUtils.equals(type, TOP_RATED_MOVIE_TYPE)) {
            return MoviesType.TOP_RATED;
        } else if (TextUtils.equals(type, FAVORITE_MOVIE_TYPE)) {
            return MoviesType.FAVORITE;
        }
        return MoviesType.POPULAR;
    }

    @NonNull
    public static SharedPreferences getPrefs() {
        return AppDelegate.getAppContext()
                .getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    public static void setPrefs(String movieType) {
        SharedPreferences prefs = getPrefs();
        prefs.edit().putString(TYPE_KEY, movieType).apply();
    }



}
