package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity {
    EditText etName;
    EditText etBio1;
    EditText etUsername;
    EditText etPass;

    TextView tvSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        tvSignup = findViewById(R.id.tvSignUp);
        etName = findViewById(R.id.etName);

        etBio1 = findViewById(R.id.etBio1);
        etUsername = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPass);
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPass.getText().toString().isEmpty() || etBio1.getText().toString().isEmpty()
                        || etName.getText().toString().isEmpty() || etUsername.getText().toString().isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Fill everything out!", Toast.LENGTH_SHORT).show();
                } else {
                    String password = etPass.getText().toString();
                    String bio = etBio1.getText().toString();
                    String name = etName.getText().toString();
                    String un = etUsername.getText().toString();


                    ParseUser user = new ParseUser();

                    user.setUsername(un);
                    user.setPassword(password);
                    user.put("biography", bio);
                    user.put("name", name);
                    List<String> likedPosts = new ArrayList<>();
                    user.put("likedPosts", likedPosts);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(" ERROR ", e.toString());
                            } else {
                                ParseUser.logInInBackground(un, password, new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {
                                        if (e!= null) {
                                            Log.e("LOGIN", e.toString());
                                            Toast.makeText(SignupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        Intent i = new Intent(SignupActivity.this, FeedActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                            }

                        }
                    });
                }
            }
        });
    }
}