package com.prakriti.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

public class MainActivity extends AppCompatActivity {
// weather app - fire api based on user input location

    private static final int LOCATION_REQ_CODE = 33;
    private String inputLocation;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        client = new FusedLocationProviderClient();

        EditText edtLocation = findViewById(R.id.edtLocation);
        TextView txtWeatherInfo = findViewById(R.id.txtWeatherInfo);
        Button btnGetWeather = findViewById(R.id.btnGetWeather);

        btnGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtLocation.getText().toString().trim().equals("")) {
                    edtLocation.setError("This field cannot be empty");
                    return;
                }
                inputLocation = edtLocation.getText().toString().trim();
            }
        });
    }

    public void checkLocationPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
        {
            // request permission
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQ_CODE);
        }
        else { // granted
            getUsersLocationInput();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_REQ_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUsersLocationInput();
            }
            else {
                Toast.makeText(this, "User Denied Location Access", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getUsersLocationInput() {

    }
}