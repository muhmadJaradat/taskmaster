package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        TextView taskTitleDetail = findViewById(R.id.taskTitleDetail);
        String title = getIntent().getStringExtra("title");
        taskTitleDetail.setText(title);
    }
}