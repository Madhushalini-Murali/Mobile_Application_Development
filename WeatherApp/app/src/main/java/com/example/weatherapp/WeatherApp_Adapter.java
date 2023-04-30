package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
public class WeatherApp_Adapter extends RecyclerView.Adapter<WeatherApp_ViewHolder>
{
    private final List<Weather_Application> array_weather;
    private final Sevenday_Weather_Activity sevenday_weather_activity;

    WeatherApp_Adapter(List<Weather_Application> array_weather, Sevenday_Weather_Activity ma)
    {
        this.array_weather =array_weather ;
        this.sevenday_weather_activity=ma;
    }
    public WeatherApp_ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seven_day_weather, parent, false);
        return new WeatherApp_ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull WeatherApp_ViewHolder holder, int place)
    {

        Weather_Application weather = array_weather.get(place);

        holder.required_date.setText(weather.get_reqDate());
        holder.highLow_temp.setText(weather.get_Maximum_temperature()+MainActivity.deg_val+"/"+weather.get_Minimum_temperature()+MainActivity.deg_val);
        holder.weather_description.setText(weather.get_weather_description());
        holder.weaImage_icon.setImageResource(weather.get_Weatherimage_icon());
        holder.uv_index.setText("UV Index:"+weather.get_UVIndex());
        holder.probability_precipitation.setText("("+weather.get_Precipitation_probability()+"%precip)");
        holder.mrng_temperature.setText(weather.get_Morning_temperature()+MainActivity.deg_val);
        holder.afternoon_temperature.setText(weather.get_Afternoon_temperature()+MainActivity.deg_val);
        holder.evng_temperature.setText(weather.get_Evening_temperature()+MainActivity.deg_val);
        holder.night_temperature.setText(weather.get_Night_temperature()+MainActivity.deg_val);

    }

    public int getItemCount()
    {
        return array_weather.size();
    }
}
