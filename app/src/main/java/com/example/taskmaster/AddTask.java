package com.example.taskmaster;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddTask extends AppCompatActivity {
    private static final String TAG = "addTask" ;
    List<Team> teams;
    List<String> list;
    public Spinner spinner;
    ArrayAdapter<String> adapter;
    String uploadedFileType;
    File uploadFile;
    String fileName;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teams = new ArrayList<>();
        gettingTeams();
        list = new ArrayList<>();
        list.add("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Task");

        setContentView(R.layout.activity_add_task);
        spinner = findViewById(R.id.spinner);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddTask.this, "fffff", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        Button upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFileFromDevice();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri=data.getData();
        uploadedFileType=getContentResolver().getType(uri);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult: returned from file explorer");
            Log.i(TAG, "onActivityResult: File Type -> "+uploadedFileType);
            Log.i(TAG, "onActivityResult: => " + data.getData());

             uploadFile = new File(getApplicationContext().getFilesDir(), "uploadFile");

             fileName=new SimpleDateFormat("yyMMddHHmmssZ").format(new Date())+"."+uploadedFileType.split("/")[1];

            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                FileUtils.copy(inputStream, new FileOutputStream(uploadFile));
                Log.i(TAG, "onActivityResult: ---->"+"entered");
            } catch (Exception exception) {
                Log.e(TAG, "onActivityResult: file upload failed" + exception.toString());
            }


        }}
    public void addLabel(View view) {
        TextView titleText = findViewById(R.id.add_task_db);
        String title = titleText.getText().toString();
        TextView bodyText = findViewById(R.id.add_task_discrption);
        String body = bodyText.getText().toString();
        String status = "new";
        int teamName =  spinner.getSelectedItemPosition();
        Team team=teams.get(teamName-1);


        uploadFile();

        Task item = Task.builder()
                .team(team)
                .title(title)
                .body(body)
                .state(status)
                .attachedFile(fileName)
                .build();
        Amplify.API.mutate(
                ModelMutation.create(item),
                success -> Log.i("Amplify", "Saved item: " + success.getData().getTitle()),
                error -> Log.e("Amplify", "Could not save item to DataStore", error)
        );



        TextView text = findViewById(R.id.submitted);
        text.setVisibility(View.VISIBLE);

        Intent detailsIntent = new Intent(this, MainActivity.class);
        startActivity(detailsIntent);
    }


    public void gettingTeams() {


        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {

                    for (Team team : response.getData()) {
                        Log.i("MyAmplifyApp", team.getName());
                        teams.add(team);
                        list.add(team.getName());
                    }
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );
    }

    private void getFileFromDevice() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
//        chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{".jpg", ".png",".jpeg"});
        chooseFile = Intent.createChooser(chooseFile, "Choose a File");
        startActivityForResult(chooseFile, 999); // deprecated
    }

    public String uploadFile(){
        Amplify.Storage.uploadFile(
                fileName,
                uploadFile,
                success -> {
                    Log.i(TAG, "uploadFileToS3: succeeded " + success.getKey());
                },
                error -> {
                    Log.e(TAG, "uploadFileToS3: failed " + error.toString());
                }
        );
        return fileName;
    }
}