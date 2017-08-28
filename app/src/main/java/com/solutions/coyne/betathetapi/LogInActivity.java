package com.solutions.coyne.betathetapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

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
