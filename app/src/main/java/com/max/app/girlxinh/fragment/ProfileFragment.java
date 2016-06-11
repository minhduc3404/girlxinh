package com.max.app.girlxinh.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.max.app.girlxinh.R;
import com.max.app.girlxinh.bus.BusProvider;
import com.max.app.girlxinh.bus.SettingEvent;

/**
 * Created by SnowDark on 1/24/2016.
 */
public class ProfileFragment extends PreferenceFragment {

    private static final String TAG = "ProfileFragment";

    public static ProfileFragment newInstance() {

        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.registerOnSharedPreferenceChangeListener(listener);
        addPreferencesFromResource(R.xml.setting_preference_screen);
    }

    @Override
    public void onResume() {
        super.onResume();
        sendPreference();
    }

    private void sendPreference() {
        Boolean autoWallpaper = prefs.getBoolean(SettingEvent.WALLPAPER, false);
        BusProvider.getInstance().post(new SettingEvent(SettingEvent.WALLPAPER, String.valueOf(autoWallpaper)));
        boolean lValue = prefs.getBoolean(SettingEvent.MUSIC, true);
        BusProvider.getInstance().post(new SettingEvent(SettingEvent.MUSIC, String.valueOf(lValue)));
        String value = prefs.getString(SettingEvent.QUALITY, "1");
        BusProvider.getInstance().post(new SettingEvent(SettingEvent.QUALITY, value));
        int time = prefs.getInt(SettingEvent.TIME, 5);
        BusProvider.getInstance().post(new SettingEvent(SettingEvent.TIME, time+""));
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener = new
            SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {

                    switch (key) {
                        case SettingEvent.WALLPAPER:
                            Boolean autoWallpaper = prefs.getBoolean(SettingEvent.WALLPAPER, false);
                            BusProvider.getInstance().post(new SettingEvent(SettingEvent.WALLPAPER, String.valueOf(autoWallpaper)));
                            Log.d(TAG, autoWallpaper + "");
                            break;
                        case SettingEvent.MUSIC:
                            boolean lValue = prefs.getBoolean(SettingEvent.MUSIC, true);
                            BusProvider.getInstance().post(new SettingEvent(SettingEvent.MUSIC, String.valueOf(lValue)));
                            Log.d(TAG, lValue + "");
                            break;
                        case SettingEvent.QUALITY:
                            String value = prefs.getString(SettingEvent.QUALITY, "1");
                            BusProvider.getInstance().post(new SettingEvent(SettingEvent.QUALITY, value));
                            Log.d(TAG, value);
                            break;
                        case SettingEvent.TIME:
                            int time = prefs.getInt(SettingEvent.TIME, 5);
                            BusProvider.getInstance().post(new SettingEvent(SettingEvent.TIME, time + ""));
                            Log.d(TAG, time + "");
                            break;
                    }
                }
            };

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}
