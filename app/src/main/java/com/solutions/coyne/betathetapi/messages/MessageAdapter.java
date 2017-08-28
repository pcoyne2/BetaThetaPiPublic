package com.solutions.coyne.betathetapi.messages;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.solutions.coyne.betathetapi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    List<FriendlyMessage> messages;

    public MessageAdapter(List<FriendlyMessage> messages) {
        this.messages = messages;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_message, parent, false);
        MessageAdapter.ViewHolder ph = new MessageAdapter.ViewHolder(v);
        return ph;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {
        FriendlyMessage message = messages.get(position);
        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            holder.messageTextView.setVisibility(View.GONE);
            holder.photoImageView.setVisibility(View.VISIBLE);
            Picasso.with(holder.photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(holder.photoImageView);
        } else {
            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.photoImageView.setVisibility(View.GONE);
            holder.messageTextView.setText(message.getText());
        }
        holder.authorTextView.setText(message.getName());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2
        private TextView messageTextView;
        private ImageView photoImageView;
        private TextView authorTextView;
        //4
        public ViewHolder(View v) {
            super(v);

            photoImageView = (ImageView) v.findViewById(R.id.photoImageView);
            messageTextView = (TextView) v.findViewById(R.id.messageTextView);
            authorTextView = (TextView) v.findViewById(R.id.nameTextView);
            v.setOnClickListener(this);
        }

        //5
        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");
        }
    }
}
