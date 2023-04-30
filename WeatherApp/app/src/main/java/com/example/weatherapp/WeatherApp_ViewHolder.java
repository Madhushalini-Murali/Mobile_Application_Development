package com.example.weatherapp;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherApp_ViewHolder extends RecyclerView.ViewHolder
{
    TextView required_date;
    TextView highLow_temp;
    TextView weather_description;
    TextView probability_precipitation;
    TextView uv_index;
    ImageView weaImage_icon;
    TextView mrng_temperature, afternoon_temperature, evng_temperature, night_temperature;

    WeatherApp_ViewHolder(View view) {
        super(view);
        required_date = view.findViewById(R.id.date_day);
        highLow_temp = view.findViewById(R.id.high_low_temp);
        weather_description = view.findViewById(R.id.weather_desc);
        probability_precipitation = view.findViewById(R.id.precipitation);
        uv_index = view.findViewById(R.id.uvIndex);
        weaImage_icon = view.findViewById(R.id.icon);
        mrng_temperature = view.findViewById(R.id.morning_temp);
        afternoon_temperature = view.findViewById(R.id.afternoon_temp);
        evng_temperature = view.findViewById(R.id.evening_temp);
        night_temperature = view.findViewById(R.id.night_temp);
    }
}
