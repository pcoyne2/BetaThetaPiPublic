package com.solutions.coyne.betathetapi.messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.solutions.coyne.betathetapi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick Coyne on 8/25/2017.
 */

public class ChatRoomFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference messageRef;
    private ChildEventListener mChildEventListener;


    public static Fragment newInstance(String room){
        ChatRoomFragment fragment = new ChatRoomFragment();

        Bundle args = new Bundle();
        args.putString("ROOM", room);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        mFireBaseDatabase = FirebaseDatabase.getInstance();
        messageRef = mFireBaseDatabase.getReference().child("Eta Delta").child(args.getString("ROOM"));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_room, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.message_recyclerview);
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        adapter = new MessageAdapter(friendlyMessages);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener(){
        if(mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    adapter.messages.add(friendlyMessage);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d("TAG", s);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            messageRef.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener(){
        if(mChildEventListener != null) {
            messageRef.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


}
