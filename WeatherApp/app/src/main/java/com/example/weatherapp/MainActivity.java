package com.example.weatherapp;

import android.Manifest;
import com.android.volley.RequestQueue;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity
{
    TextView  currentDateTime;
    TextView  temperature;
    TextView  feels_like;
    TextView  weather_description;
    TextView  wind;
    TextView  humidity;
    TextView  uv_index;
    TextView  visibility;
    TextView  morning_temp;
    TextView  afternoon_temp;
    TextView  evening_temp;
    TextView  night_temp;
    TextView  morningtext;
    TextView  afternoontext;
    TextView  eveningtext;
    TextView  nighttext;
    TextView  sunrise;
    TextView  sunset;
    ImageView icon;
    public SharedPreferences sharedPreferences;
    public Context context;
    private static final String TAG = "MainActivity";
    private RequestQueue queue;
    private ArrayList<Weather_Application> array_weather = new ArrayList<>();
    private ArrayList<Hourly_Weather> dailyweather_array = new ArrayList<>();
    private final HashMap<String, ArrayList<Weather_Application>> Weather_Info = new HashMap<>();
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private SwipeRefreshLayout swipe;
    private String weather_location = "Chicago,Illinois";
    private String degree_metric="metric";
    private boolean has_net_connectivity;
    MenuItem degree_menu, calendar_menu, location_menu;
    private List<Hourly_Weather> array1_hourlyWeather = new ArrayList<>();
    private List<Hourly_Weather> array2_hourlyWeather = new ArrayList<>();
    private RecyclerView recyclerView;
    private Hourly_Weather_Adapter mAdapter;
    ConstraintLayout layout;
    private String w_lattitude = "";
    private String w_longitude = "";
    double w_lat = 0.0;
    double w_lng = 0.0;
    private String location_acti;
    private boolean date_update=false;
    private String check_location=" ";
    private String unitGroup = "us";
    private SharedPreferences preferences;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private String locationString = "Unspecified Location";
    static String deg_val = "°F";
    private String unit_val = "units_f";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentDateTime  = findViewById(R.id.currentDateTime);
        temperature  = findViewById(R.id.temperature);
        weather_description  = findViewById(R.id.weather_description);
        wind  = findViewById(R.id.wind);
        feels_like = findViewById(R.id.feels_like);
        humidity  = findViewById(R.id.humidity);
        uv_index  = findViewById(R.id.uv_index);
        visibility  = findViewById(R.id.visibility);
        morning_temp  = findViewById(R.id.morning_temp);
        afternoon_temp  = findViewById(R.id.afternoon_temp);
        evening_temp  = findViewById(R.id.evening_temp);
        night_temp  = findViewById(R.id.night_temp);
        morningtext  = findViewById(R.id.morningtext);
        afternoontext  = findViewById(R.id.afternoontext);
        eveningtext  = findViewById(R.id.eveningtext);
        nighttext  = findViewById(R.id.nighttext);
        sunrise  = findViewById(R.id.sunrise);
        sunset  = findViewById(R.id.sunset);
        icon  = findViewById(R.id.icon);
        recyclerView  = findViewById(R.id.recyclerView);
        mAdapter = new Hourly_Weather_Adapter(array2_hourlyWeather, this);
        recyclerView.setAdapter(mAdapter);
        layout = (ConstraintLayout) this.findViewById(R.id.MainLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        has_net_connectivity = hasNetworkConnection();
        swipe = findViewById(R.id.swipe);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        drawActionBar();
        loadPreferences();
        queue = Volley.newRequestQueue(this);
        doDownload();
        // setData_Location();
        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        find_Location();

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);

        /////Implementing Extra Credit Options
        /////Swipe Refresh
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() 
            {
                array_weather.removeAll(array_weather);
                dailyweather_array.removeAll(dailyweather_array);
                doDownload();
                swipe.setRefreshing(false);
                if(hasNetworkConnection())
                {
                    Log.d("Refresh", "Refreshing Page: ");
                    Toast.makeText(getApplicationContext(), "Reload- Success!!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext()," No Network! \n Please turn ON the Network!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    ////creating menu buttons degree, calendar and location
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.weather_menu, menu);


        degree_menu=menu.findItem(R.id.degree_menu);
        location_menu=menu.findItem(R.id.location_menu);
        calendar_menu=menu.findItem(R.id.calendar_menu);
        String degree_metric= preferences.getString("degreeselected","metric");
        if(unit_val.equals("units_f"))
        {
            degree_menu.setIcon(R.drawable.units_f);
        }
        else
        {
            degree_menu.setIcon(R.drawable.units_c);
        }

        return true;
    }

    ////Clicking on the menu buttons
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (hasNetworkConnection()) {
            item.setEnabled(true);
            if (item.getItemId() == R.id.degree_menu)
            {
                networkONcheck();
                degree_menu.setEnabled(true);
                if(unit_val.equals("units_c"))
                {
                    unit_val = "units_f";
                    deg_val = "°F";
                    unitGroup = "us";
                    if(hasNetworkConnection())
                        Toast.makeText(getApplicationContext(), "Converted to Fahrenheit", Toast.LENGTH_SHORT).show();
                }
                else if (unit_val.equals("units_f"))
                {
                    unit_val = "units_c";
                    deg_val = "°C";
                    unitGroup = "uk";
                    if(hasNetworkConnection())
                        Toast.makeText(getApplicationContext(), "Converted to Celsius", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this," No Network! \n Please turn ON the Network!", Toast.LENGTH_SHORT).show();

                }
                int icon_id = getApplicationContext().getResources().getIdentifier(unit_val, "drawable", getApplicationContext().getPackageName());
                item.setIcon(icon_id);
                array_weather.clear();
                array2_hourlyWeather.clear();
                doDownload();
            }

            //// selecting the calendar menu
            else if(item.getItemId() == R.id.calendar_menu)
            {
                networkONcheck();
                degree_menu.setEnabled(true);
                Intent intent = new Intent(this, Sevenday_Weather_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("weather_location", weather_location);
                bundle.putSerializable("List_of_Weather", array_weather);
                intent.putExtras(bundle);
                activityResultLauncher.launch(intent);
                if(hasNetworkConnection())
                    Toast.makeText(getApplicationContext(), "Opening 15 days weather results", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this," No Network! \n Please turn ON the Network!", Toast.LENGTH_SHORT).show();
            }

            //// selecting the location menu
            else if (item.getItemId() == R.id. location_menu)
            {
                networkONcheck();
                degree_menu.setEnabled(true);
                changeLocationDialogBox();
                if(hasNetworkConnection())
                    Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this," No Network! \n Please turn ON the Network!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            item.setEnabled(false);
            return  true;
        }

        //selecting the degree C/F menu

        return super.onOptionsItemSelected(item);
    }

    ///checking if the network is Turned ON
    public void networkONcheck()
    {

    }

    ///changing to the specific location after selecting the location menu
    public void changeLocationDialogBox()
    {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        final EditText location_name = new EditText(this);
        location_name.setInputType(InputType.TYPE_CLASS_TEXT);
        location_name.setGravity(Gravity.CENTER_HORIZONTAL);
        build.setTitle("Enter a location");
        build.setMessage("For US locations enter as 'City', or 'City, State'.\n\n" +
                "For International locations enter as 'City, Country'.");
        build.setView(location_name);
        build.setCancelable(true);
        build.setNegativeButton(
                "CANCEL",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                        Log.d(TAG,"Cancel button clicked in Location");
                        Toast.makeText(getApplicationContext(), "CANCEL Button clicked", Toast.LENGTH_SHORT).show();
                    }
                });
        build.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        weather_location = location_name.getText().toString();
                        array_weather.clear();
                        array2_hourlyWeather.clear();
                        doDownload();
                        Log.d(TAG,"OK button clicked in Location");
                        Toast.makeText(getApplicationContext(), "OK Button clicked", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = build.create();
        alert.show();
    }

    public void drawActionBar()
    {
        ActionBar action_bar;
        action_bar = getSupportActionBar();
        ColorDrawable color = new ColorDrawable(Color.parseColor("#FF786A50"));
        action_bar.setBackgroundDrawable(color);
    }

    ////check for network connection
    private boolean hasNetworkConnection()
    {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
    private void  find_Location()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some situations this can be null.
                    if (location != null) {
                        locationString = location_get(location);
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_LONG).show());
    }
    public void handleResult(ActivityResult activity_result)
    {
        if (activity_result != null || activity_result.getData() != null)
        {
            Toast.makeText(this, " ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.d(TAG, "handleResult: Received NULL ActivityResult");
            return;

        }
    }
    private String location_get(Location location) {

        StringBuilder sb = new StringBuilder();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            sb.append(String.format(
                    Locale.getDefault(),
                    "%s, %s%n%nProvider: %s%n%n%.5f, %.5f",
                    city, state, location.getProvider(), location.getLatitude(), location.getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    ////using volley function to get the data from the api
    public void doDownload() {
        if (!hasNetworkConnection())
        {
            currentDateTime.setText("No internet connection!");
            return;
        }
        String url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+weather_location+"/?unitGroup="+unitGroup+"&lang=en&key=ZU9VCAB83KW7KCCAU7KTNNZCU";
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                morningtext.setVisibility(View.VISIBLE);
                eveningtext.setVisibility(View.VISIBLE);
                afternoontext.setVisibility(View.VISIBLE);
                nighttext.setVisibility(View.VISIBLE);

                try {
                    JSONObject currentConditions = response.getJSONObject("currentConditions");
                    JSONArray days = response.getJSONArray("days");
                    Log.d(TAG, "onResponse: "+currentConditions);
                    JSONArray hours_array = days.getJSONObject(0).getJSONArray("hours");
                    check_location = response.get("resolvedAddress").toString();
                    getSupportActionBar().setTitle(check_location);
                    String tmp = currentConditions.get("temp").toString();
                    String datetimeEpoch = currentConditions.get("datetimeEpoch").toString();
                    String feelsLike = currentConditions.get("feelslike").toString();
                    String img_icon = currentConditions.get("icon").toString().replace("-", "_");
                    String conditions = currentConditions.get("conditions").toString();
                    String cloudcover = currentConditions.get("cloudcover").toString();
                    String winddirection = getDirection(Double.parseDouble(currentConditions.get("winddir").toString()));
                    String windspeed = currentConditions.get("windspeed").toString();
                    String windGust = currentConditions.get("windgust").toString();
                    String wea_humid = currentConditions.get("humidity").toString();
                    String uvindex1 = currentConditions.get("uvindex").toString();
                    String visibility1 = currentConditions.get("visibility").toString();
                    String morningtmp = ((JSONObject) hours_array.get(8)).get("temp").toString();
                    String noontmp = ((JSONObject) hours_array.get(13)).get("temp").toString();
                    String eveningtmp = ((JSONObject) hours_array.get(17)).get("temp").toString();
                    String nighttmp = ((JSONObject) hours_array.get(23)).get("temp").toString();
                    String sun_rise = currentConditions.get("sunriseEpoch").toString();
                    String sun_set = currentConditions.get("sunsetEpoch").toString();
                    temperature.setText(tmp+deg_val);
                    currentDateTime.setText(fullDateFormat(datetimeEpoch));
                    feels_like.setText("Feels like "+feelsLike+deg_val);
                    int icon_id = getApplicationContext().getResources().getIdentifier(img_icon, "drawable", getApplicationContext().getPackageName());
                    icon.setImageResource(icon_id);
                    icon.setVisibility(View.VISIBLE);
                    weather_description.setText(conditions + " (" + cloudcover + "% clouds)");
                    if (windGust.equals("null"))
                    {
                        wind.setText("Winds: " + winddirection + " at " + windspeed + " mph" + "\n(not gusting)");

                    } else
                    {
                        wind.setText("Winds: " + winddirection + " at " + windspeed + " mph" + "\ngusting to " + windGust + " mph");

                    }
                    humidity.setText("Humidity: " + wea_humid + "%");
                    uv_index.setText("UV Index: " + uvindex1);
                    visibility.setText("Visibility: " + visibility1 + "mi");
                    morning_temp.setText(morningtmp + deg_val);
                    morningtext.setVisibility(View.VISIBLE);
                    afternoon_temp.setText(noontmp + deg_val);
                    afternoontext.setVisibility(View.VISIBLE);
                    evening_temp.setText(eveningtmp + deg_val);
                    eveningtext.setVisibility(View.VISIBLE);
                    night_temp.setText(nighttmp + deg_val);
                    nighttext.setVisibility(View.VISIBLE);
                    sunrise.setText("Sunrise: " + time_Set(sun_rise));
                    sunset.setText("Sunset: " + time_Set(sun_set));
                    setHourlyWeathers(days, getHourOfDay(datetimeEpoch));
                    setDailyWeathers(days);
                    recyclerView.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                } catch (Exception exception) {
                    Log.d("KEY", "ExceptionListener" + exception.toString());
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                }
                catch(Exception exception){
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                    loadPreferences();
                    Log.d("KEY", "ExceptionErrorListener");

                }
            }
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        queue.add(jsonObjectRequest);
    }
    public String fullDateFormat(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        SimpleDateFormat fullDate = new SimpleDateFormat("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        String fullDateStr = fullDate.format(date); // Thu Sep 29 12:00 AM, 2022 String timeOnlyStr = timeOnly.format(dateTime); // 12:00 AM
        return fullDateStr;
    }

    public String time_Set(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String timeOnlyStr = timeOnly.format(date); // 12:00 AM
        return timeOnlyStr;
    }

    public String dayOnlySet(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        SimpleDateFormat dayOnly = new SimpleDateFormat("EEE", Locale.getDefault());
        String dayOnlyStr = dayOnly.format(date); // 12:00 AM
        return dayOnlyStr;
    }

    public String dayDateSet(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        SimpleDateFormat dayDate = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
        String dayDateStr = dayDate.format(date);
        return dayDateStr;
    }
    public int getHourOfDay(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        return date.getHours();
    }

    private String getDirection(double degrees)
    {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
                return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
                return "E";
        if (degrees >= 112.5 && degrees < 157.5)
                return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
                return "S";
        if (degrees >= 202.5 && degrees < 247.5)
                return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
                return "W";
        if (degrees >= 292.5 && degrees < 337.5)
                return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }
    public void setHourlyWeathers(JSONArray days, int currentTimeIndex) throws Exception
    {
        String day;
        String time;
        int icon_id;
        String temperature;
        String description;
        JSONObject hour;
        JSONArray hours_array;
        for(int i=0; i<4; i++){
            hours_array = days.getJSONObject(i).getJSONArray("hours");
            if(i == 0)
            {
                day = "Today";
                for(int j=currentTimeIndex+1; j<24; j++){
                    hour = hours_array.getJSONObject(j);
                    time = time_Set(hour.get("datetimeEpoch").toString());
                    icon_id = getApplicationContext().getResources().getIdentifier(hour.get("icon").toString().replace("-", "_"), "drawable", getApplicationContext().getPackageName());
                    temperature = hour.get("temp").toString();
                    description = hour.get("conditions").toString();
                    array2_hourlyWeather.add(new Hourly_Weather(day, time, icon_id,description, temperature));
                }
            }
            else{
                day = dayOnlySet(((JSONObject)days.get(i)).get("datetimeEpoch").toString());
                for(int j=0; j<24; j++){
                    hour = hours_array.getJSONObject(j);
                    time = time_Set(hour.get("datetimeEpoch").toString());
                    icon_id = getApplicationContext().getResources().getIdentifier(hour.get("icon").toString().replace("-", "_"), "drawable", getApplicationContext().getPackageName());
                    temperature = hour.get("temp").toString();
                    description = hour.get("conditions").toString();
                    array2_hourlyWeather.add(new Hourly_Weather(day, time, icon_id, description, temperature));

                }
                mAdapter.notifyDataSetChanged();

            }
        }
    }

    public void setDailyWeathers(JSONArray days) throws Exception{
        String date;
        String temp_max;
        String temp_min;
        String description;
        String precip;
        String uv;
        String morning;
        String afternoon;
        String evening;
        String night;
        int icon_id;
        for(int i=0; i<15; i++){
            date = dayDateSet(((JSONObject)days.get(i)).get("datetimeEpoch").toString());
            temp_max = ((JSONObject)days.get(i)).get("tempmax").toString();
            temp_min = ((JSONObject)days.get(i)).get("tempmin").toString();
            description = ((JSONObject)days.get(i)).get("description").toString();
            precip = ((JSONObject)days.get(i)).get("precipprob").toString();
            uv = ((JSONObject)days.get(i)).get("uvindex").toString();
            morning = ((JSONObject)((JSONArray)((JSONObject)days.get(i)).get("hours")).get(8)).get("temp").toString();
            afternoon = ((JSONObject)((JSONArray)((JSONObject)days.get(i)).get("hours")).get(13)).get("temp").toString();
            evening = ((JSONObject)((JSONArray)((JSONObject)days.get(i)).get("hours")).get(17)).get("temp").toString();
            night = ((JSONObject)((JSONArray)((JSONObject)days.get(i)).get("hours")).get(22)).get("temp").toString();
            icon_id = getApplicationContext().getResources().getIdentifier(((JSONObject)days.get(i)).get("icon").toString().replace("-", "_"), "drawable", getApplicationContext().getPackageName());
            array_weather.add(new Weather_Application(date, temp_max, temp_min, description, precip, uv, morning, afternoon, evening, night, icon_id));
        }
        Log.d(TAG, "setDailyWeathers: "+array_weather.toString());
    }
    public void loadPreferences()
    {
        sharedPreferences = getSharedPreferences("loc", Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("location", "").equals("")){
            weather_location = sharedPreferences.getString("location", "");
        }

        if(!sharedPreferences.getString("unit", "").equals("")){
            unitGroup = sharedPreferences.getString("unit", "");
        }

        if(!sharedPreferences.getString("unit_value", "").equals("")){
            deg_val = sharedPreferences.getString("unit_value", "");
        }

        if(!sharedPreferences.getString("unit_icon", "").equals("")){
            unit_val = sharedPreferences.getString("unit_icon", "");
        }

    }

    ///Implementing Extra Credits
    ///Saving user settings
    public void onPause()
    {
        Log.d("Pause Message ", "onPause: ");
        Log.d("Pause Message", "User Settings is Saved: ");
        saveUserSettings();
        super.onPause();
    }
    public void saveUserSettings()
    {
        SharedPreferences user_settings = getSharedPreferences("loc", Context.MODE_PRIVATE);
        SharedPreferences.Editor settings_editor = user_settings.edit();
        settings_editor.putString("location", weather_location);
        settings_editor.putString("unit", unitGroup);
        settings_editor.putString("unit_value", deg_val);
        settings_editor.putString("unit_icon",unit_val);
        settings_editor.apply();
    }

}