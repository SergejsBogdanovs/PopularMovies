package lv.st.sbogdano.popularmovies;

import android.app.Application;
import android.content.ContentResolver;
import android.support.annotation.NonNull;

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

    @NonNull
    public static ContentResolver getDb() {
        return sInstance.getContentResolver();
    }

}
