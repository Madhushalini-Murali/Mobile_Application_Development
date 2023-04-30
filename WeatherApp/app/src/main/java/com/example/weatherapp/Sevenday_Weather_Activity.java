package com.example.weatherapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Sevenday_Weather_Activity extends AppCompatActivity
{
    private RecyclerView recyclerView;

    private static final String TAG = "Sevenday_Weather_Activity";
    private List<Weather_Application> array_weather=new ArrayList<>();
    private WeatherApp_Adapter mAdapter;

    String location_unit;
    private LinearLayoutManager linearLayoutManager;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_recyclerview);
        recyclerView = findViewById(R.id.day_recycler);
        mAdapter = new WeatherApp_Adapter(array_weather, this);
        recyclerView.setAdapter(mAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);
        Bundle bnd = getIntent().getExtras();
        if (bnd != null) {
            location_unit = bnd.getString("weather_location");
            ArrayList<Weather_Application> w_list = (ArrayList<Weather_Application>) bnd.getSerializable("List_of_Weather");
            for (Weather_Application weather : w_list) {
                array_weather.add(weather);
                Log.d(TAG, "onCreate: "+weather.afternoon_temperature.toString());
            }
            setTitle(location_unit+" "+array_weather.size() + " Day");
        }
        mAdapter.notifyDataSetChanged();
        ActionBar action_bar;
        action_bar = getSupportActionBar();
        ColorDrawable color = new ColorDrawable(Color.parseColor("#FF786A50"));
        action_bar.setBackgroundDrawable(color);
    }
    public void handleResult(ActivityResult activity_result) {
        Toast.makeText(this,"Weather Application ",Toast.LENGTH_SHORT).show();
        Intent data = activity_result.getData();
        if (data.hasExtra("WEATHER_INFORMATION"))
        {
            Toast.makeText(this," Has Weather Information",Toast.LENGTH_SHORT).show();
            Weather_Application weather = (Weather_Application) data.getSerializableExtra("WEATHER_INFORMATION");
            Toast.makeText(this,"Date ="+ weather.get_reqDate(),Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this," No Weather Information",Toast.LENGTH_SHORT).show();
        }

    }
}