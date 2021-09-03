package com.example.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.amplifyframework.core.Amplify;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public class TaskDetail extends AppCompatActivity {

    private static final String TAG ="TaskDetail" ;
    public URL url ;
    Handler handler;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Objects.requireNonNull(getSupportActionBar()).setDefaultDisplayHomeAsUpEnabled(true);

        String taskTitle = getIntent().getStringExtra(MainActivity.TASK_TITLE);
        TextView taskTitleID = findViewById(R.id.taskDetailTitle);
        taskTitleID.setText(taskTitle);

        String taskBody = getIntent().getStringExtra(MainActivity.TASK_BODY);
        TextView taskBodyID = findViewById(R.id.taskDetailDetail);
        taskBodyID.setText(taskBody);

        String taskState = getIntent().getStringExtra(MainActivity.TASK_STATUS);
        TextView taskStateID = findViewById(R.id.taskDetailStatus);
        taskStateID.setText(taskState);


        TextView taskAddressID = findViewById(R.id.taskDetailTextViewAddress);
        String taskAddress=getIntent().getStringExtra(MainActivity.TASK_ADDRESS);
        if (taskAddress!=null)taskAddressID.setText(taskAddress);

        Intent intent = getIntent();
        String fileName = intent.getExtras().get(MainActivity.TASK_FILE).toString();
        handler=new Handler();
        handler.postDelayed(()->{

            ImageView imageView = findViewById(R.id.taskDetailImageView);

            Amplify.Storage.downloadFile(
                    fileName,
                    new File(getApplicationContext().getFilesDir() + fileName),
                    result -> {
                        Log.i(TAG, "Successfully downloaded: " + result.getFile().getAbsoluteFile());
                        String type= null;
                        try {
                            type = Files.probeContentType(result.getFile().toPath());
                            Log.i(TAG, "onCreate: "+type.split("/")[0]);
                            if (type.split("/")[0].equals("image")){
                                imageView.setImageBitmap(BitmapFactory.decodeFile(result.getFile().getPath()));
                            }
                            else {
                                String linkedText = String.format("<a href=\"%s\">download File</a> ", url);

                                TextView test = findViewById(R.id.taskDetailLink);
                                test.setText(Html.fromHtml(linkedText));
                                test.setMovementMethod(LinkMovementMethod.getInstance());
                            }
                        } catch (IOException e) {
                            Log.i(TAG, "onCreate: "+e);
                            e.printStackTrace();
                        }

                        assert type != null;

                    },
                    error -> Log.e(TAG,  "Download Failure", error)
            );

        },9000);
        getURlFromS3Storage(fileName);


    }

    private void getURlFromS3Storage(String key) {

        Amplify.Storage.getUrl(
                key,
                result -> {
                    Log.i(TAG, "Successfully generated: " + result.getUrl());
                    Log.i(TAG, "getFileFromS3Storage: " +key);
                    url= result.getUrl();
                },
                error -> Log.e(TAG, "URL generation failure", error)
        );
    }
}