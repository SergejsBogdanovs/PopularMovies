package lv.st.sbogdano.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class AppDelegate extends Application {

    private static AppDelegate sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // Stetho
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }

    public static AppDelegate getAppContext() {
        return sInstance;
    }


}
