package com.solutions.coyne.betathetapi.messages;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by Patrick Coyne on 8/25/2017.
 */

public class EventListenerClass implements ChildEventListener {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.d("TAG", "onchildAdded");
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
}
