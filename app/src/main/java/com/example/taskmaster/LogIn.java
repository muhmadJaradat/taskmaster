package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        findViewById(R.id.LoginButton).setOnClickListener(v -> {
            String username =  ((EditText)findViewById(R.id.LoginInUserName)).getText().toString();
            String password = ((EditText)findViewById(R.id.loginPassword)).getText().toString();
            Amplify.Auth.signIn(
                    username,
                    password,
                    result -> {
                        Intent main = new Intent(this, MainActivity.class);

                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor userValues = sp.edit();
                        userValues.putString("name",username);
                        userValues.apply();
                        startActivity(main);
                        Log.i("singin", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                    },
                    error -> Log.e("singin", "Error in signing in")
            );


        });
    }
    }
