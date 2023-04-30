package com.example.weatherapp;

import android.util.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

public class Weather_Application implements Serializable
{
    public String required_date;
    public String maximum_temperature;
    public String minimum_temperature;
    public String weather_description;
    public String precipitation_probability;
    public String UV_Index;
    public int weatherimage_icon;
    public String morning_temperature;
    public String afternoon_temperature;
    public String evening_temperature;
    public String night_temperature;
    public String humidity;
    public String wind_direction;
    public String wind_speed;
    public String wind_gust;
    public String sunrise;
    public String sunset;
    public String temperature;
    public String feels_like;
    public String visibility;
    public List<Hourly_Weather> array_hourWeather;
    public String cloudcover;
    public String format_day;
    public String geo_location;
    public String geo_lattitude;
    public String geo_longitude;
    public String conditions;

//    Weather_Application(String required_date,String format_day,String maximum_temperature,String minimum_temperature,String weather_description,String precipitation_probability,String humidity,String UV_Index,
//            int weatherimage_icon, String wind_direction,String wind_gust,String wind_speed,String morning_temperature,String afternoon_temperature,String evening_temperature, String night_temperature,
//            String sunrise,String sunset,String temperature,String feels_like,String visibility,String cloudcover,List<Hourly_Weather> array_hourWeather,String geo_location,String geo_lattitude,String geo_longitude,String conditions)
//    {
//        this.required_date=required_date;
//        this.maximum_temperature=maximum_temperature;
//        this.minimum_temperature=minimum_temperature;
//        this.weather_description= weather_description;
//        this.precipitation_probability = precipitation_probability;
//        this.UV_Index=UV_Index;
//        this.weatherimage_icon=weatherimage_icon;
//        this.morning_temperature= morning_temperature;
//        this.afternoon_temperature = afternoon_temperature;
//        this.evening_temperature = evening_temperature;
//        this.night_temperature = night_temperature;
//        this.humidity=humidity;
//        this.wind_direction = wind_direction;
//        this.wind_gust =wind_gust;
//        this.wind_speed = wind_speed;
//        this.sunrise=sunrise;
//        this.sunset=sunset;
//        this.temperature =temperature;
//        this.feels_like=feels_like;
//        this.visibility=visibility;
//        this.array_hourWeather=array_hourWeather;
//        this.cloudcover=cloudcover;
//        this.format_day= format_day;
//        this.geo_location =geo_location;
//        this.geo_lattitude =geo_lattitude;
//        this.geo_longitude = geo_longitude;
//        this.conditions=conditions;
//    }

    public Weather_Application(String date, String max_temp, String min_temp, String description, String precipitation, String uv, String morning, String afternoon, String evening, String night, int icon_id)
    {
        this.required_date = date;
        this.maximum_temperature = max_temp;
        this.minimum_temperature = min_temp;
        this.weather_description = description;
        this.precipitation_probability = precipitation;
        this.UV_Index = uv;
        this.morning_temperature = morning;
        this.afternoon_temperature = afternoon;
        this.evening_temperature = evening;
        this.night_temperature = night;
        this.weatherimage_icon = icon_id;
    }
    public String get_reqDate()
    {
        return this.required_date;
    }
    public String get_Maximum_temperature()
    {
        return this.maximum_temperature;
    }
    public String get_Minimum_temperature()
    {
        return this.minimum_temperature;
    }
    public String get_Precipitation_probability()
    {
        return this.precipitation_probability;
    }
    public String get_weather_description()
    {
        return this.weather_description;
    }
    public String get_UVIndex()
    {
        return this.UV_Index;
    }
    public int get_Weatherimage_icon()
    {
        return this.weatherimage_icon;
    }
    public String get_Morning_temperature()
    {
        return this.morning_temperature;
    }
    public String get_Afternoon_temperature()
    {
        return this.afternoon_temperature;
    }
    public String get_Evening_temperature()
    {
        return this.evening_temperature;
    }
    public String get_Night_temperature()
    {
        return this.night_temperature;
    }
    public String get_Wind_direction()
    {
        return this.wind_direction;
    }
    public String get_Wind_speed()
    {
        return this.wind_speed;
    }
    public String get_Wind_gust()
    {
        return this.wind_gust;
    }
    public String get_Sunrise()
    {
        return this.sunrise;
    }
    public String get_Sunset()
    {
        return this.sunset;
    }
    public String get_Weather_Temprature()
    {
        return this.temperature;}

    public String get_Feelslike()
    {
        return this.feels_like;
    }
    public String get_Humidity()
    {
        return this.humidity;
    }
    public String get_Visibility()
    {
        return this.visibility;
    }
    public String get_Cloudcover()
    {
        return this.cloudcover;
    }
    public String get_Format_day()
    {
        return this.format_day;
    }
    public String get_geoLocation()
    {
        return this.geo_location;
    }
    public String get_geoLattitude()
    {
        return this.geo_lattitude;
    }
    public String get_geoLongitude()
    {
        return this.geo_longitude;
    }
    public String get_Conditions()
    {
        return  this.conditions;
    }

    public String toString(){
        try{
            StringWriter sw=new StringWriter();
            JsonWriter jsonWriter=new JsonWriter(sw);
            jsonWriter.setIndent(" ");
            jsonWriter.beginObject();

            jsonWriter.name("day_date").value(get_reqDate());

            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

}
