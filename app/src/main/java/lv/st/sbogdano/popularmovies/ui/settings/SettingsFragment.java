package lv.st.sbogdano.popularmovies.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import lv.st.sbogdano.popularmovies.R;

/**
 *
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getPreferenceManager().setSharedPreferencesName(Preferences.SETTINGS_NAME);
        addPreferencesFromResource(R.xml.settings);
    }
}
