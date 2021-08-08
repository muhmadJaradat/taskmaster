package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button button = findViewById(R.id.new_task_btn);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                addLabel();
            }
        });
    }
    protected void addLabel(){
        TextView text = findViewById(R.id.submitted);
        text.setVisibility(View.VISIBLE);
    }
}