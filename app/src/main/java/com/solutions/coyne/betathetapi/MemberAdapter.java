package com.solutions.coyne.betathetapi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Patrick Coyne on 8/25/2017.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.PhotoHolder> {

    List<String> songTitles;
    private Context context;

    public MemberAdapter(List<String> songTitles, Context mContext) {
        this.songTitles = songTitles;
        context = mContext;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.member_list_item, parent, false);
        PhotoHolder ph = new PhotoHolder(v);
        return ph;
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        holder.title.setText(songTitles.get(position));
        Picasso.with(context)
                .load("https://upload.wikimedia.org/wikipedia/en/2/29/Beta_Theta_Pi_seal.png")
                .placeholder(R.drawable.btp_coat_of_arms)
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return songTitles.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2
        private TextView title;
        private ImageView photo;

        //4
        public PhotoHolder(View v) {
            super(v);
            photo = (ImageView)v.findViewById(R.id.profile_photo);
            title = (TextView)v.findViewById(R.id.member_name);
            v.setOnClickListener(this);
        }

        //5
        @Override
        public void onClick(View v) {
            //Open Dialog with view pager members or in tablet open second page with members viewpager
            Log.d("RecyclerView", "CLICK!");
        }
    }
}
