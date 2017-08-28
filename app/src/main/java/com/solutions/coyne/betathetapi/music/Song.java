package com.solutions.coyne.betathetapi.music;

/**
 * Created by Patrick Coyne on 8/27/2017.
 */

public class Song {
    
    private String assetPath;
    private String name;
    private Integer songId;

    public Song(String assetPath) {
        this.assetPath = assetPath;
        String[] components = assetPath.split("/");
        String filename = components[components.length-1];
        name = filename.replace(".mp3", "");
    }

    public String getAssetPath() {
        return assetPath;
    }

    public String getName() {
        return name;
    }

    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }
}
