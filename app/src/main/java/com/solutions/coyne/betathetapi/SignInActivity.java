package com.solutions.coyne.betathetapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Patrick Coyne on 8/23/2017.
 */

public class SignInActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_sign_in);

        final EditText email = (EditText)findViewById(R.id.email_signin);
        final EditText password = (EditText)findViewById(R.id.password_signin);
        final EditText school = (EditText)findViewById(R.id.school);

        Button signIn = (Button)findViewById(R.id.sign_in_button);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("EMAIL", email.getText().toString());
                intent.putExtra("PASSWORD", password.getText().toString());
                intent.putExtra("SCHOOL", school.getText().toString());
                setResult(3, intent);
                finish();
            }
        });
    }
}
