package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        findViewById(R.id.sign_up_button).setOnClickListener(v ->{
            String userName = ((EditText)findViewById(R.id.AuthPersonName)).getText().toString();
            String password = ((EditText)findViewById(R.id.AuthTextPassword)).getText().toString();
            String email = ((EditText)findViewById(R.id.AuthTextEmailAddress)).getText().toString();

            AuthSignUpOptions options = AuthSignUpOptions.builder()
                    .userAttribute(AuthUserAttributeKey.email(), email)
                    .build();
            Amplify.Auth.signUp(userName, password, options,
                    result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                    error -> Log.e("AuthQuickStart", "Sign up failed", error)
            );

            Intent confirm = new Intent(this, ConfirmSignUp.class);
            confirm.putExtra("name",userName);
            startActivity(confirm);
        });
    }
}