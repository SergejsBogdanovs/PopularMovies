package lv.st.sbogdano.popularmovies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import lv.st.sbogdano.popularmovies.R;

public final class MoviesPreferences {

    public static String getPreferredMoviesType(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForMoviesType = context.getString(R.string.settings_movies_type_key);
        String defaultMoviesType = context.getString(R.string.settings_movie_default_type);

        return sp.getString(keyForMoviesType, defaultMoviesType);
    }
}
