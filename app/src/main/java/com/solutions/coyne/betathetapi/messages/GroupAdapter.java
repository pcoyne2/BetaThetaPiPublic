package com.solutions.coyne.betathetapi.messages;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solutions.coyne.betathetapi.Main2Activity;
import com.solutions.coyne.betathetapi.R;

import java.util.List;

/**
 * Created by Patrick Coyne on 8/25/2017.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.PhotoHolder> {

    List<String> groupNames;
    private Context mContext;

    public GroupAdapter(Context context, List<String> groupNames) {
        mContext = context;
        this.groupNames = groupNames;
    }

    @Override
    public GroupAdapter.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.song_list_item, parent, false);
        GroupAdapter.PhotoHolder ph = new GroupAdapter.PhotoHolder(v);
        return ph;
    }

    @Override
    public void onBindViewHolder(GroupAdapter.PhotoHolder holder, int position) {
        holder.title.setText(groupNames.get(position));
    }

    @Override
    public int getItemCount() {
        return groupNames.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2
        private TextView title;

        //4
        public PhotoHolder(View v) {
            super(v);

            title = (TextView)v.findViewById(R.id.song_title);
            v.setOnClickListener(this);
        }

        //5
        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!"+getAdapterPosition());
            Log.d("RecyclerView", "CLICK!"+groupNames.get(getAdapterPosition()));
            ((Main2Activity)mContext).openChatRoom(groupNames.get(getAdapterPosition()));
        }
    }
}
