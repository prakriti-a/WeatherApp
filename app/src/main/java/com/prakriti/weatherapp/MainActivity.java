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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
// weather app - fire api based on user input location

//    private static final int LOCATION_REQ_CODE = 33;
    private String apiKey = "66f3cfea2d52f0f35c01a1b9b39d2ff5";
    private String inputCity;
//    private FusedLocationProviderClient client;

    private TextView txtForecast, txtTemp, txtCity, txtTime, txtCountry, txtHumidity, txtMinTemp, txtMaxTemp, txtSunrise, txtSunset;
    private EditText edtLocation;
    private Button btnClear, btnGetWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtLocation = findViewById(R.id.edtLocation);

        txtForecast = findViewById(R.id.txtForecast);
        txtTemp = findViewById(R.id.txtTemp);
        txtTime = findViewById(R.id.txtTime);
        txtCity = findViewById(R.id.txtCity);
        txtCountry = findViewById(R.id.txtCountry);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtMinTemp = findViewById(R.id.txtMinTemp);
        txtMaxTemp = findViewById(R.id.txtMaxTemp);
        txtSunrise = findViewById(R.id.txtSunrise);
        txtSunset = findViewById(R.id.txtSunset);

        btnGetWeather = findViewById(R.id.btnGetWeather);
        btnClear = findViewById(R.id.btnClear);

        btnGetWeather.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGetWeather:
                if(edtLocation.getText().toString().trim().equals("")) {
                    edtLocation.setError("This field cannot be empty");
                    return;
                }
                inputCity = edtLocation.getText().toString().trim();
                getWeatherInfo(inputCity);
                break;
            case R.id.btnClear:
                edtLocation.setText("");
                txtForecast.setText(R.string.forecast);
                txtTemp.setText(R.string.temp);
                txtTime.setText(R.string.time);
                txtCity.setText(R.string.city);
                txtCountry.setText(R.string.country);
                txtHumidity.setText(R.string.humidity);
                txtMinTemp.setText(R.string.min_temp);
                txtMaxTemp.setText(R.string.max_temp);
                txtSunrise.setText(R.string.sunrise);
                txtSunset.setText(R.string.sunset);
                break;
        }
    }


    private void getWeatherInfo(String inputCity) {
        // clear all textviews
        onClick(btnClear);

        // create Volley request
        RequestQueue myQueue = Volley.newRequestQueue(this);

        JsonObjectRequest myjson = new JsonObjectRequest(Request.Method.GET, "https://api.openweathermap.org/data/2.5/weather?q=" + inputCity
                + "&units=metric&appid=" + apiKey, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main = response.getJSONObject("main");
                    JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                    JSONObject sys = response.getJSONObject("sys");

                    String cityName = response.getString("name");
                    Long updatedAt = response.getLong("dt");
                    String updatedAtText = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));

                    String temperature = main.getString("temp");
                    String humidity = main.getString("humidity");
                    String temp_min = main.getString("temp_min");
                    String temp_max = main.getString("temp_max");

                    String cast = weather.getString("description");

                    String countryName = sys.getString("country");
                    Long rise = sys.getLong("sunrise");
                    String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));
                    Long set = sys.getLong("sunset");
                    String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));

                    // display values
                    txtCity.setText(txtCity.getText() + cityName);
                    txtCountry.setText(txtCountry.getText() + countryName);
                    txtTime.setText(txtTime.getText() + updatedAtText);
                    txtTemp.setText(txtTemp.getText() + temperature + " °C");
                    txtForecast.setText(txtForecast.getText() + cast);
                    txtHumidity.setText(txtHumidity.getText() + humidity);
                    txtMinTemp.setText(txtMinTemp.getText() + temp_min);
                    txtMaxTemp.setText(txtMaxTemp.getText() + temp_max);
                    txtSunrise.setText(txtSunrise.getText() + sunrise);
                    txtSunset.setText(txtSunset.getText() + sunset);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Error occured\nPlease try again", Toast.LENGTH_SHORT).show();
            }
        });

        myQueue.add(myjson);
    }


    /*
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

     */
}