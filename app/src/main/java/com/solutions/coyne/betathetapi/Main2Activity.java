package com.solutions.coyne.betathetapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.solutions.coyne.betathetapi.messages.ChatRoomFragment;
import com.solutions.coyne.betathetapi.messages.GroupAdapter;
import com.solutions.coyne.betathetapi.messages.MessageRoomFragment;
import com.solutions.coyne.betathetapi.music.SongsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    public static final int RC_SIGN_IN = 1;

    private String mUsername;
    private boolean isLoggingIn;

    private FirebaseDatabase mFireBaseDatabase;
//    private DatabaseReference mMessagesDatabaseReference;
//    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    List<User> users;
    UserSingleton userSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isLoggingIn = false;

        mUsername = "ANONYMOUS";
        users = new ArrayList<>();

        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        userSingleton = UserSingleton.getInstance();

//        if(userSingleton.getUser() != null) {
//        mMessagesDatabaseReference = mFireBaseDatabase.getReference().child("Alpha").child("users");
//        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_holder);
        if(fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            Fragment frag = new DirectoryFragment();
            ft.add(R.id.fragment_holder, frag);
            ft.commit();
        }

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user  = firebaseAuth.getCurrentUser();
                if(user != null){
                    //User is signed in
                    //Toast.makeText(MainActivity.this, "You're now signed in. Welcome to FriendlyChat.", Toast.LENGTH_SHORT).show();
                    userSingleton.setUser(new User(user.getDisplayName(), user.getUid(), "Eta Delta", user.getEmail()));
                    onSignInInitialize(user.getDisplayName());
                    DirectoryFragment fragment = (DirectoryFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_holder);
                    if(fragment != null){
                        fragment.reattachDbListener();
                    }
                }else{
                    //User is signed out
                    //Give ability for pledge, possible new members, etc to browse unconfidential parts
                    // like calendar, members (not all data), etc
                    if(!isLoggingIn) {
                        startActivityForResult(new Intent(Main2Activity.this, LogInActivity.class), 3);
                    }
//                    onSignedOutCleanup();
//                    startActivityForResult(
//                            AuthUI.getInstance()
//                                    .createSignInIntentBuilder()
//                                    .setIsSmartLockEnabled(false)
//                                    .setAvailableProviders(
//                                            Arrays.asList(
//                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
//                                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
//                                            )
//                                    )
//                                    .build(),
//                            RC_SIGN_IN);
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                mFireBaseDatabase.getReference().child("users").push().setValue(mFirebaseAuth.getCurrentUser().getUid());
//                mMessagesDatabaseReference = mFireBaseDatabase.getReference().child(mFirebaseAuth.getCurrentUser().getUid()).child("messages");
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else if(requestCode ==3 & resultCode == RESULT_OK){
            isLoggingIn = true;
            final String emial = data.getStringExtra("EMAIL");
            String pass = data.getStringExtra("PASSWORD");
            final String school = data.getStringExtra("SCHOOL");

            mFirebaseAuth.signInWithEmailAndPassword(emial, pass).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Map<String, Object> user = new HashMap<>();
                                UserSingleton userSingleton = UserSingleton.getInstance();
                                userSingleton.setUser(new User("Patrick Coyne", mFirebaseAuth.getCurrentUser().getUid(), school, emial));
                                user.put(mFirebaseAuth.getCurrentUser().getUid(),
                                        userSingleton.getUser());
                                mFireBaseDatabase.getReference().child(school).child("users").updateChildren(user);
//                                mMessagesDatabaseReference = mFireBaseDatabase.getReference().child(userSingleton.getUser().getSchool()).child("users");
                                Log.d("TAG", "onComplete");
                            }else{
                                Log.d("TAG", "onFail");
                                startActivityForResult(new Intent(Main2Activity.this, LogInActivity.class), 3);
                            }
                            isLoggingIn = false;
                        }
                    });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void SignoutInitialized(){
        mFirebaseAuth.signOut();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fm = getSupportFragmentManager();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(fm.getBackStackEntryCount() >0){
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.sign_out){
            mFirebaseAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment frag = new DirectoryFragment();
            ft.replace(R.id.fragment_holder, frag).addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_gallery) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment frag = new MessageRoomFragment();
            ft.replace(R.id.fragment_holder, frag).addToBackStack(null);
            ft.commit();
//        } else if (id == R.id.nav_slideshow) {
//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            Fragment frag = ChatRoomFragment.newInstance("Pledge");
//            ft.replace(R.id.fragment_holder, frag).addToBackStack(null);
//            ft.commit();
        } else if (id == R.id.nav_manage) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment frag = new SongsFragment();
            ft.replace(R.id.fragment_holder, frag).addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onSignInInitialize(String displayName) {
        mUsername = displayName;
//        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener(){
//        if(mChildEventListener == null) {
//            mChildEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    User user = dataSnapshot.getValue(User.class);
////                    mMessageAdapter.add(friendlyMessage);
//                    UserSingleton userSingleton = UserSingleton.getInstance();
//                    if(user.getKey().equals(userSingleton.getUser().getKey())) {
//                        userSingleton.setUser(user);
//                    }
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            };
//            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
//        }
    }

    private void onSignedOutCleanup(){
        mUsername = "ANONYMOUS";
//        mMessageAdapter.clear();
    }

    private void detachDatabaseReadListener(){
//        if(mChildEventListener != null) {
//            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
//            mChildEventListener = null;
//        }
    }

    private void checkPreferences(){
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String UUID = sharedPreferences.getString(getString(R.string.UUID), "");
        String School = sharedPreferences.getString(getString(R.string.School), "");
    }

    private void savePreferences(){
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
    }

    public void openChatRoom(String name){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frag = ChatRoomFragment.newInstance(name);
        ft.replace(R.id.fragment_holder, frag).addToBackStack(null);
        ft.commit();
    }

}
