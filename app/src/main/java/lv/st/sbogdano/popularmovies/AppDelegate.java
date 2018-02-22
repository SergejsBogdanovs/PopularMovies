package lv.st.sbogdano.popularmovies;

import android.app.Application;

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
