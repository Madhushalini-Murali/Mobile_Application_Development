package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class Hourly_Weather_Adapter extends RecyclerView.Adapter <HourlyWeather_ViewHolder>
{
    private final List<Hourly_Weather> array_hourlyWeather;
    private final MainActivity mainAct;

    Hourly_Weather_Adapter(List<Hourly_Weather> array_hourlyWeather, MainActivity ma) {
        this.array_hourlyWeather = array_hourlyWeather;
        mainAct = ma;
    }

    public HourlyWeather_ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_weather, parent, false);

        return new HourlyWeather_ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull HourlyWeather_ViewHolder holder, int place)
    {

        Hourly_Weather hourlyWeather = array_hourlyWeather.get(place);

        holder.weather_day.setText(hourlyWeather.get_Weather_Day());
        holder.weather_time.setText(hourlyWeather.get_Weather_Time());
        holder.image_icon.setImageResource(hourlyWeather.get_Weatherimage_icon());
        holder.weather_tmp.setText(hourlyWeather.get_Weather_Temperature()+mainAct.deg_val);
        holder.weather_descrp.setText(hourlyWeather.get_weather_description());
    }

    public int getItemCount()
    {
        return array_hourlyWeather.size();
    }
}
