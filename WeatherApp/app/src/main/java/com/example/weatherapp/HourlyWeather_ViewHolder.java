package com.example.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HourlyWeather_ViewHolder extends RecyclerView.ViewHolder
{
    TextView weather_day;
    TextView weather_time;
    TextView weather_tmp;
    TextView weather_descrp;
    ImageView image_icon;
    HourlyWeather_ViewHolder(View view){
        super(view);
        weather_day=view.findViewById(R.id.day_name);
        weather_time=view.findViewById(R.id.time);
        image_icon= view.findViewById(R.id.weather_icon);
        weather_tmp=view.findViewById(R.id.hour_temp);
        weather_descrp= view.findViewById(R.id.description);
    }
}
