package lv.st.sbogdano.popularmovies;

import android.app.Application;

/**
 * Created by sbogdano on 21/02/2018.
 */

public class AppDelegate extends Application {

    private static AppDelegate sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static AppDelegate getAppContext() {
        return sInstance;
    }
}
