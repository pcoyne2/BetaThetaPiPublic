package com.solutions.coyne.betathetapi.music;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.solutions.coyne.betathetapi.R;
import com.solutions.coyne.betathetapi.databinding.SongListItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick Coyne on 8/23/2017.
 */

public class SongsFragment extends Fragment {

    RecyclerView songRecyclerView;
    private SongBox songBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);

        songBox = new SongBox(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        songRecyclerView = (RecyclerView)view.findViewById(R.id.songs_recyclerview);
        songRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        songRecyclerView.setAdapter(new SoundAdapter(songBox.getSongs()));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        songBox.release();
    }

    private class SoundHolder extends RecyclerView.ViewHolder{
        private SongListItemBinding mBinding;
        public SoundHolder(SongListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setViewModel(new SongViewModel(songBox));
        }

        public void bind(Song song){
            mBinding.getViewModel().setSong(song);
            mBinding.executePendingBindings();
        }
    }

    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder>{
        private List<Song>songs;


        public SoundAdapter(List<Song> songs) {
            this.songs = songs;
        }

        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            SongListItemBinding binding = DataBindingUtil
                    .inflate(inflater, R.layout.song_list_item, parent, false);
            return new SoundHolder(binding);
        }

        @Override
        public void onBindViewHolder(SoundHolder holder, int position) {
            Song sound = songs.get(position);
            holder.bind(sound);
        }

        @Override
        public int getItemCount() {
            return songs.size();
        }
    }
}

