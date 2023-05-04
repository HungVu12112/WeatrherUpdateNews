package com.example.weatherupdatenews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final static  int REQUEST_CODE = 100;
    private EditText edSearch;
    private TextView tvThanhPho, tvKhuVuc, tvDoMax, tvDo, tvDoMin, tvTinhTrang, tvGio, tvNhietDoCamNhan, tvDoam, tvMay, tvTamnhinxa, tvApxuat, tvCongiomanh,
            tvHuongGio, tvLuongmua, tvMattroimoc;
    private Button btnSearch,btnVitri;
    private ImageView IconWeather;

    private String timeapp;

    FusedLocationProviderClient fusedLocationProviderClient;

    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "202cfcc7b6a65934be53dc23bd7e94af";
    private static String lat ;
    private static String lon ;

    DecimalFormat df = new DecimalFormat("#.##");
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        Mapping();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        timeapp = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private void Mapping() {
        mAdView = findViewById(R.id.adViewMain);
        edSearch = findViewById(R.id.edSearch);
        btnSearch = findViewById(R.id.btnSearch);
        tvThanhPho = findViewById(R.id.tvthanhpho);
        tvKhuVuc = findViewById(R.id.tvkhuvuc);
        tvDoMax = findViewById(R.id.tvcmax);
        tvDoMin = findViewById(R.id.tvcmin);
        tvDo = findViewById(R.id.tvtempc);
        tvTinhTrang = findViewById(R.id.tvtinhtrang);
        tvGio = findViewById(R.id.tvgio);
        tvNhietDoCamNhan = findViewById(R.id.tvnhietdocamnhan);
        tvDoam = findViewById(R.id.tvdoam);
        tvMay = findViewById(R.id.tvmay);
        tvTamnhinxa = findViewById(R.id.tvtamnhin);
        tvApxuat = findViewById(R.id.tvamsuat);
        tvHuongGio = findViewById(R.id.tvhuonggio);

        tvMattroimoc = findViewById(R.id.tvmattroimoc);
        IconWeather = findViewById(R.id.imgIcon);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void getWeatherData1(View view) {
        ///tempUrl = url + "?lat=" + lat + "?lon=" + lon + "&appid=" + appid + "&lang=vi";
        String tempUrl = "";
        String city = edSearch.getText().toString().trim();

        if (city.equals("123")) {

            Toast.makeText(this, "Không có thành phố", Toast.LENGTH_SHORT).show();
        } else {
            if (city.isEmpty()){
                tempUrl = url + "?lat=" + lat + "?lon=" + lon + "&appid=" + appid + "&lang=vi";
            }
            else {
                tempUrl = url + "?q=" + city + "&appid=" + appid + "&lang=vi";
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        String icon = jsonObjectWeather.getString("icon");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double tempmin = jsonObjectMain.getDouble("temp_min") - 274.15;
                        double tempmax = jsonObjectMain.getDouble("temp_max") - 272.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String tamnhinxa = jsonResponse.getString("visibility");
                        String wind = jsonObjectWind.getString("speed");
                        int huonggio = jsonObjectWind.getInt("deg");

                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String sunrise = jsonObjectSys.getString("sunrise");
                        String sunset = jsonObjectSys.getString("sunset");
                        String cityName = jsonResponse.getString("name");

                        tvDo.setText(df.format(temp) +"°");
                        tvDoMin.setText(df.format(tempmin) +"°");
                        tvDoMax.setText(df.format(tempmax) +"°");
                        tvNhietDoCamNhan.setText(df.format(feelsLike) +"°");
                        tvThanhPho.setText("Thành Phố : "+cityName +" "+ timeapp);
                        tvKhuVuc.setText("Khu vực : " +countryName);
                        tvGio.setText(wind+"m/s");
                        tvDoam.setText(humidity+"%");
                        tvMay.setText(clouds+"%");

                        tvApxuat.setText(pressure+"hPa");
                        tvTinhTrang.setText("Tình trạng : "+description);
                        tvTamnhinxa.setText(tamnhinxa+"/m");

                        String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
                        Picasso.get().load(iconUrl).into(IconWeather);
                        //// quy đổi unix trả về thời gian thực
                        long dv1 = Long.valueOf(sunrise)*1000;// its need to be in milisecond
                        long dv2 = Long.valueOf(sunset)*1000;
                        Date df1 = new Date(dv1);
                        Date df2 = new Date(dv2);
                        String binhminh = new SimpleDateFormat("hh:mm:ss").format(df1);
                        String hoanghon = new SimpleDateFormat("hh:mm:ss").format(df2);
                        tvMattroimoc.setText(binhminh);
                       if (timeapp.compareTo(binhminh) < 0){

                       }

                        ////quy đổi giá trị hướng gió
                        String huonggiover2 = "";
                        if (huonggio>0 && huonggio<22){
                            huonggiover2 = "Bắc";
                        }
                        else if (huonggio>22 && huonggio<70){
                            huonggiover2 = "Đông Bắc";
                        }
                        else if (huonggio>70 && huonggio<112){
                            huonggiover2 = "Đông";
                        }
                        else if (huonggio>112 && huonggio<157){
                            huonggiover2 = "Đông Nam";
                        }
                       else if (huonggio>157 && huonggio<202){
                            huonggiover2 = "Nam";
                        }
                       else if (huonggio>202 && huonggio<247){
                            huonggiover2 = "Tây Nam";
                        }
                        else if (huonggio>247 && huonggio<292){
                            huonggiover2 = "Tây";
                        }
                        else if (huonggio>292 && huonggio<337){
                            huonggiover2 = "Tây Bắc";
                        }
                        else {
                            huonggiover2 = "Bắc";
                        }
                        tvHuongGio.setText(huonggiover2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getLocation();
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null){

                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this,Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );
                        lat = String.valueOf(addresses.get(0).getLatitude());
                        lon = String.valueOf(addresses.get(0).getLongitude());
                        Log.d("lat",String.valueOf(addresses.get(0).getLatitude()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}