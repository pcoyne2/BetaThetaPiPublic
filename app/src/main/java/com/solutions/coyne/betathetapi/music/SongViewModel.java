package com.solutions.coyne.betathetapi.music;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by Patrick Coyne on 8/27/2017.
 */

public class SongViewModel extends BaseObservable{

    private Song song;
    private SongBox songBox;

    public SongViewModel(SongBox songBox){
        this.songBox = songBox;
    }


    @Bindable
    public String getTitle(){
        return song.getName();
    }

    public Song getSong(){
        return song;
    }

    public void setSong(Song song){
        this.song = song;
        notifyChange();
    }

    public void onButtonClicked(){
        songBox.play(song);
    }
}
