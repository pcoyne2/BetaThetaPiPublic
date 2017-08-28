package com.solutions.coyne.betathetapi.music;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick Coyne on 8/27/2017.
 */

public class SongBox {
    private static final String TAG = "SongBox";


    private static final String SOUNDS_FOLDER = "songs";
    private static final int MAX_SOUNDS = 1;

    private AssetManager mAssets;
    private List<Song> mSongs = new ArrayList<>();
//    private SoundPool mSoundPool;
    private MediaPlayer mediaPlayer;


    public SongBox(Context context){
        mAssets = context.getAssets();
        //This old constructor is deprecated but need for compatibility
//        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        mediaPlayer = new MediaPlayer();
        LoadSongs();
    }

    public void play(Song song){
//        Integer songId = song.getSongId();
//        if(songId == null){
//            return;
//        }
        if(mediaPlayer.isPlaying()){
//            mediaPlayer.pause();
            mediaPlayer.reset();
        }
        try {
            AssetFileDescriptor descriptor = mAssets.openFd(song.getAssetPath());
            long start = descriptor.getStartOffset();
            long end = descriptor.getLength();
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        mSoundPool.play(songId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void release(){
//        mSoundPool.release();
        mediaPlayer.release();
    }

    private void LoadSongs(){
        String[] songNames;
        try {
            songNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + songNames.length + " songs");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
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

//    private void load(Song song) throws IOException{
//        AssetFileDescriptor afd = mAssets.openFd(song.getAssetPath());
//        int songId = mSoundPool.load(afd, 1);
//        song.setSongId(songId);
//    }

    public List<Song> getSongs() {
        return mSongs;
    }
}
