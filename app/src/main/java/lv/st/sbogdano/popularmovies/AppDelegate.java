package lv.st.sbogdano.popularmovies;

import android.app.Application;
import android.content.ContentResolver;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;

public class AppDelegate extends Application {

    private static AppDelegate sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        Stetho.initializeWithDefaults(this);
    }

    public static AppDelegate getAppContext() {
        return sInstance;
    }

    @NonNull
    public static ContentResolver getDb() {
        return sInstance.getContentResolver();
    }

}
