package com.solutions.coyne.betathetapi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.solutions.coyne.betathetapi.music.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Patrick Coyne on 8/30/2017.
 */

public class SongService extends Service {

    private static final String PLAY_SONG = "play_song";
    private static final String SOUNDS_FOLDER = "songs";

    private AssetManager mAssets;
    private List<Song> mSongs = new ArrayList<>();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String action = intent.getAction();
            if(action.equals(PLAY_SONG)){
                mAssets = this.getAssets();
                LoadSongs();
                MediaPlayer mediaPlayer = new MediaPlayer();
                if(mediaPlayer.isPlaying()){
//            mediaPlayer.pause();
                    mediaPlayer.reset();
                }
                try {
                    Random rand = new Random();
                    int result = rand.nextInt(mSongs.size()-1);
                    AssetFileDescriptor descriptor = mAssets.openFd(mSongs.get(result).getAssetPath());
                    long start = descriptor.getStartOffset();
                    long end = descriptor.getLength();
                    mediaPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
                    mediaPlayer.prepare();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                            stopSelf();
                        }
                    });
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return START_NOT_STICKY;
    }

    private void LoadSongs(){
        String[] songNames;
        try {
            songNames = mAssets.list(SOUNDS_FOLDER);
//            Log.i(TAG, "Found " + songNames.length + " songs");
        } catch (IOException ioe) {
//            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        for (String filename : songNames) {
//            try {
            String assetPath = SOUNDS_FOLDER + "/" + filename;
            Song song = new Song(assetPath);
//                load(song);
            mSongs.add(song);
//            }catch (IOException ioe){
//                Log.e(TAG, "Could not load song "+ filename, ioe);
//            }
        }
    }

    public static Intent createIntentPlaySong(Context context){
        Intent i = new Intent(context, SongService.class);
        i.setAction(PLAY_SONG);

        return i;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
