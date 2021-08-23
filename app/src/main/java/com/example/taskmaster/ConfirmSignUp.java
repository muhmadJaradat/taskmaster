package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;

public class ConfirmSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_sign_up);

        findViewById(R.id.confirmSignUpButton).setOnClickListener(v -> {
            String code = ((EditText)findViewById(R.id.confirmCode)).getText().toString();
            String name = getIntent().getStringExtra("name");

            Amplify.Auth.confirmSignUp(
                    name,
                    code,
                    result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
                    error -> Log.e("AuthQuickstart", error.toString())
            );

            Intent intent=new Intent(this,LogIn.class);
            startActivity(intent);
        });
    }
    }
