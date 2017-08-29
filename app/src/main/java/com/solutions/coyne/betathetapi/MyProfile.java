package com.solutions.coyne.betathetapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MyProfile extends AppCompatActivity {

    private TextView name;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        user = UserSingleton.getInstance().getUser();

        name = (TextView)findViewById(R.id.user_name);
        name.setText(user.getName());
    }
}
