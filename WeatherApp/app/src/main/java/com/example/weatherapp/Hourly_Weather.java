package com.example.weatherapp;

import java.io.Serializable;

public class Hourly_Weather implements Serializable
{
    private String weather_day;
    private String weather_time;
    private int image_icon;
    private String weather_temperature;
    private String weather_description;

    public  Hourly_Weather(String day,String hour,int icon,String descrp,String tmp)
    {
        this.weather_day=day;
        this.weather_time=hour;
        this.image_icon=icon;
        this.weather_description=descrp;
        this.weather_temperature=tmp;
    }

    public String get_Weather_Day()
    {
        return this.weather_day;
    }
    public String get_Weather_Time()
    {
        return this.weather_time;
    }
    public int get_Weatherimage_icon()
    {
        return this.image_icon;
    }
    public String get_Weather_Temperature()
    {
            return this.weather_temperature;
    }
    public String get_weather_description()
    {
        return this.weather_description;
    }

    public String toString() {
        return "HourlyWeather{" +
                "day='" + weather_day + '\'' +
                ", time='" + weather_time + '\'' +
                ", icon_id='" + image_icon + '\'' +
                ", temperature='" + weather_temperature + '\'' +
                ", description='" + weather_description + '\'' +
                '}';
    }

}
