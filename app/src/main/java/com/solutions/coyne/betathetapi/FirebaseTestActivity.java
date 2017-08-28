package com.solutions.coyne.betathetapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FirebaseTestActivity extends AppCompatActivity {

    EditText childList;
    Button send;
    String text;
    Button exists;

    private FirebaseDatabase mFireBaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test);

        mFireBaseDatabase = FirebaseDatabase.getInstance();

        final User user = new User();
        user.setEmail("patrick.coyne.ud@gmail.com");
        user.setKey("9DOf3JPe95NnWVzQODgdAuUV5tc2");
        user.setName("Patrick Coyne");
        user.setSchool("Dayton - Eta Delta");


        childList = (EditText)findViewById(R.id.child_list);
        childList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        send = (Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = text.split("_");
                DatabaseReference dbRef = mFireBaseDatabase.getReference();
                for(int i=0; i<array.length;i++){
                    dbRef = dbRef.child(array[i]);
                }
                HashMap<String, Object> users = new HashMap<String, Object>();
                users.put(user.getKey(), user);
                dbRef.updateChildren(users);
            }
        });

        exists = (Button)findViewById(R.id.check_exists);
        exists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = text.split("_");
                DatabaseReference dbRef = mFireBaseDatabase.getReference();
                for(int i=0; i<array.length;i++){
                    dbRef = dbRef.child(array[i]);
                }
            }
        });

        DatabaseReference membersDbRef = mFireBaseDatabase.getReference().child("Eta Delta").child("users");
        membersDbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
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
        });
    }
}
