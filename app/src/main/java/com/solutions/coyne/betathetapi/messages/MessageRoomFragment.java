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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.solutions.coyne.betathetapi.R;
import com.solutions.coyne.betathetapi.User;
import com.solutions.coyne.betathetapi.UserSingleton;
import com.solutions.coyne.betathetapi.messages.EventListenerClass;
import com.solutions.coyne.betathetapi.messages.FriendlyMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Patrick Coyne on 8/23/2017.
 */

public class MessageRoomFragment extends Fragment {

    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mRoomDatabaseReference;
    private DatabaseReference groupMessages;
    private ChildEventListener mChildEventListener;

    List<String> groups;
    List<DatabaseReference> dbReferences;
    EventListenerClass eventListenerClass;
    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_rooms, container, false);

        mFireBaseDatabase = FirebaseDatabase.getInstance();
        groups = new ArrayList<>();
        dbReferences = new ArrayList<>();

        UserSingleton userSingleton = UserSingleton.getInstance();
        List<String> groupList = userSingleton.getUser().getGroupsStrings();

        mRoomDatabaseReference = mFireBaseDatabase.getReference().child(userSingleton.getUser().getSchool()).child("Groups");
        groupMessages = mFireBaseDatabase.getReference().child(userSingleton.getUser().getSchool());

        eventListenerClass = new EventListenerClass();

        recyclerView = (RecyclerView)view.findViewById(R.id.room_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        groupAdapter = new GroupAdapter(getActivity(), groupList);
        recyclerView.setAdapter(groupAdapter);

//        Map<String, Object> room = new HashMap<>();
//        room.put("Groups", groupList);
//        groupMessages.updateChildren(room);

//        for(int i=0; i<groupList.size(); i++){
//            DatabaseReference ref = groupMessages.child(groupList.get(i));
//            ref.addChildEventListener(eventListenerClass);
//            dbReferences.add(ref);
//        }

//        for(int i=0; i<groupList.size(); i++) {
//            for (int j = 0; j < 3; j++) {
//                groupMessages.child(groupList.get(i))
//                        .push().setValue(new FriendlyMessage("this is text", "Patrick Coyne", null));
//            }
//        }

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
                    Log.d("TAG", "onchildAdded");
                    String group = dataSnapshot.getValue().toString();
                    groups.add(group);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d("TAG", "onchildAdded");

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d("TAG", "onchildAdded");

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.d("TAG", "onchildAdded");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mRoomDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener(){
        if(mChildEventListener != null) {
            mRoomDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}
