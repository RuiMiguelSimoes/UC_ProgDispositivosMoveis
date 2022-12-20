package com.example.happens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class addEvnt extends AppCompatActivity {
    DBCon objectDatabaseCon;

    private ImageView img;
    private static final int PICK_IMAGE_REQUEST = 100;
    private Uri imageFilePath;
    private Bitmap imageToStore;

    private EditText invtName;
    private EditText info;

    private SharedPreferences userInf;

    private Switch geoSwitch;
    private LocationRequest locationRequest;
    private TextView locationText;
    private String Location;

    private Switch officialSwitch;
    private EditText emailBox;

    int actCode = (int) ((Math.random() * (99999999 - 10000000)) + 10000000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_evnt);

        objectDatabaseCon = new DBCon(this);
        img = findViewById(R.id.eventImage);
        invtName = findViewById(R.id.invtName);
        info = findViewById(R.id.info);
        userInf = getSharedPreferences("userInf", MODE_PRIVATE);
        geoSwitch = findViewById(R.id.geoSwitch);
        locationText = findViewById(R.id.locationText);
        officialSwitch = findViewById(R.id.officialSwitch);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        emailBox = findViewById(R.id.emailInf);

        geoSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geo();
            }
        });

        officialSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEmailVisible();
            }
        });
    }

    private void geo(){
        if(geoSwitch.isChecked() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(addEvnt.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if(isGPSEnabled()){
                    LocationServices.getFusedLocationProviderClient(addEvnt.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);

                            if(locationResult != null && locationResult.getLocations().size() > 0){
                                int index = locationResult.getLocations().size() - 1;
                                double latitude = locationResult.getLocations().get(index).getLatitude();
                                double longitude = locationResult.getLocations().get(index).getLongitude();

                                System.out.println("Latitude - " + latitude + "\nLongitude: " + longitude);

                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                try {
                                    List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    if(null!=listAddresses&&listAddresses.size()>0){
                                        Location = listAddresses.get(0).getAddressLine(0);
                                        locationText.setText(Location);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, Looper.getMainLooper());
                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            locationText.setText("");
        }
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(addEvnt.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                resolvableApiException.startResolutionForResult(addEvnt.this,2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    public void createEvent(View view){
        try {
            if(!invtName.getText().toString().isEmpty() && !info.getText().toString().isEmpty()){
                int id = objectDatabaseCon.getUserId(userInf.getString("username", null));
                String name = invtName.getText().toString();
                String inf = info.getText().toString();
                String actCodeStr = String.valueOf(actCode);
                int isActive = 0;
                if(officialSwitch.isChecked())
                    isActive = 1;

                if(imageToStore == null)
                    Toast.makeText(addEvnt.this, "Must add image to event", Toast.LENGTH_SHORT).show();
                else{
                    sendMail();
                    System.out.println(objectDatabaseCon.createEvent(id, name, inf, actCodeStr, new ModelClass(imageToStore), Location, isActive));
                    startActivity(new Intent(addEvnt.this, mainScreen.class));
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void chooseImg(View objectView){
        try {
            Intent intent = new Intent();
            intent.setType("image/*");

            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData() != null){
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                img.setImageBitmap(imageToStore);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean isGPSEnabled(){
        LocationManager locationManager = null;
        boolean isAvailable = false;

        if(locationManager==null){
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isAvailable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isAvailable;
    }

    private void sendMail() {
        String recipient = "ubitesterpdm@gmail.com";

        String subject = "Autorization Request";
        String message = "This is the request info: \n" + info.getText().toString() + "\nIn case you you want to turn this official please send the following code to " + emailBox.getText().toString() + ".\nCode: " + actCode;

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, recipient, subject, message);

        javaMailAPI.execute();
    }

    public void setEmailVisible(){
        if(emailBox.getVisibility() == View.INVISIBLE)
            emailBox.setVisibility(View.VISIBLE);
        else
            emailBox.setVisibility(View.INVISIBLE);
    }
}