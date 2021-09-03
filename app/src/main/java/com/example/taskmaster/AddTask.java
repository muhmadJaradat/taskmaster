package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Looper;
import android.provider.Settings;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddTask extends AppCompatActivity {
    private static final String TAG = "addTask" ;
    List<Team> teams;
    List<String> list;
    public Spinner spinner;
    ArrayAdapter<String> adapter;
    String uploadedFileType;
    File uploadFile;
    String fileName;
    FusedLocationProviderClient mFusedLocationClient;
    Geocoder geocoder;
    String address;
    private double latitude;
    private double longitude;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
                Log.i(TAG, "onCreate: type is text");
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
                Log.i(TAG, "onCreate: type is image");
            }
        }




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
                .address(address)
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

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            uploadFile= new File(imageUri.getPath());
        }
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
//                            latitudeTextView.setText(location.getLatitude() + "");
//                            longitTextView.setText(location.getLongitude() + "");

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            address=""+latitude+","+longitude;

//                            googleMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(latitude, longitude))
//                                    .title("Marker"));
                            Log.i(TAG, "onCreate: latitude => "+ latitude);
                            Log.i(TAG, "onCreate: longitude => "+ longitude);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }



    private boolean checkPermissions() {
        return ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat
                        .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
//            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
//            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 44);
    }
}