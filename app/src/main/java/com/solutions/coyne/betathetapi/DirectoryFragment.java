package com.solutions.coyne.betathetapi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick Coyne on 8/23/2017.
 */

public class DirectoryFragment extends Fragment {

    private FirebaseDatabase mFireBaseDatabase;
    private ChildEventListener mChildEventListener;

    private RecyclerView recyclerView;
    private MemberAdapter adapter;
    private DatabaseReference membersDbRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFireBaseDatabase = FirebaseDatabase.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directory, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.member_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        List<String> memberNames = new ArrayList<>();
//        for(int i=0; i<30; i++){
//            memberNames.add("Member "+ i);
//        }
        adapter = new MemberAdapter(memberNames, getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String school = prefs.getString(getString(R.string.School), "Eta Delta");
        membersDbRef = mFireBaseDatabase.getReference().child(school).child("users");
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.songTitles.clear();
        if(mChildEventListener != null) {
            membersDbRef.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    public void reattachDbListener(){
        detachDatabaseReadListener();
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User user = dataSnapshot.getValue(User.class);
                    adapter.songTitles.add(user.getName());
                    adapter.notifyDataSetChanged();
                    Log.d("TAG", "childAdded");
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    User user = dataSnapshot.getValue(User.class);
                    Log.d("TAG", "childChanged");

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d("TAG", "childRemoved");

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.d("TAG", "childMoved");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("TAG", "cancelled");

                }
            };
            membersDbRef.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener(){
        if(mChildEventListener != null) {
            membersDbRef.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}
