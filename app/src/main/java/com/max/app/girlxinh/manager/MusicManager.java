package com.max.app.girlxinh.manager;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.max.app.girlxinh.bus.BusProvider;
import com.max.app.girlxinh.bus.SettingEvent;
import com.max.app.girlxinh.main.MainApp;
import com.max.app.girlxinh.module.Song;
import com.max.app.girlxinh.util.converts.StreamToJson;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created by Forev on 4/6/2016.
 */
public class MusicManager implements MediaPlayer.OnCompletionListener {
    private static final String TAG = "MusicManager";
    static MusicManager instance;
    public Song[] songs;
    public int cur_song = 0;

    private MainApp mainApp = MainApp.getInstance();
    private MediaPlayer mPlayer;

    public static MusicManager getInstance(MusicLoadCallback callback) {
        if (instance == null) {
            instance = new MusicManager(callback);
        }

        return instance;
    }

    public MusicManager(MusicLoadCallback callback) {
        synchronized (this) {
            new LoadMusicTask(callback).execute();
        }
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void settingAvailable(SettingEvent event) {
        // TODO: React to the event somehow!
        if (event.getType().equals(SettingEvent.MUSIC)) {
            Log.d(TAG, event.getValue());
            if (mPlayer != null) {
                Boolean value = Boolean.valueOf(event.getValue());
                if (value)
                {
                    if(!mPlayer.isPlaying())
                        mPlayer.start();
                }else
                {
                    if(mPlayer.isPlaying())
                        mPlayer.stop();
                }
            }
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
        cur_song++;
        if (cur_song + 1 >= songs.length)
            cur_song = 0;
        playSong(this);
    }


    public class LoadMusicTask extends AsyncTask {
        MusicLoadCallback musicLoadCallback;

        public LoadMusicTask(MusicLoadCallback callback) {
            this.musicLoadCallback = callback;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            onLoadSong();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (musicLoadCallback != null)
                musicLoadCallback.onLoadMusicCompleted();
        }
    }

    public void onLoadSong() {
        InputStream is = null;
        try {
            is = MainApp.getInstance().getAssets().open("json/sound");
            String js = StreamToJson.StreamToJson(is);
            Gson gs = new Gson();
            Type arrType = Song[].class;
            songs = gs.fromJson(js, arrType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface MusicLoadCallback {
        void onLoadMusicCompleted();
    }

    public void playSong(MediaPlayer.OnCompletionListener listener) {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(listener);
        try {
            AssetFileDescriptor file = mainApp.getAssets().openFd(songs[cur_song].path);
            Log.d(TAG, "Music is play: " + songs[cur_song].path);
            mPlayer.setDataSource(file.getFileDescriptor());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playAuto() {
        playSong(this);
    }
}

