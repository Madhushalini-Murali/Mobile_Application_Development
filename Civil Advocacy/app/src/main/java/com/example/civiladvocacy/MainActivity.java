package com.example.civiladvocacy;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "MainActivity";
    private TextView user_specified_location;
    private TextView no_network;
    private RecyclerView govt_officials_recyclerView;
    private Official_Adapter official_adapter;
    MenuItem about_activity_menu,search_location_menu;
    private ActivityResultLauncher<Intent> result_launch;
    private LinearLayoutManager linearLayoutManager;
    private RequestQueue request_queue;
    private FusedLocationProviderClient fusedLocation_client;

    private static final int LOCATION_REQUEST = 111;
    private final ArrayList<Official_class> array_official = new ArrayList<>();
    private String get_location;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_specified_location = (TextView) findViewById(R.id.user_speciified_current_location);
        no_network = (TextView) findViewById(R.id.no_internet_textbox);
        govt_officials_recyclerView= (RecyclerView) findViewById (R.id.government_officials_recycleView);
        official_adapter = new Official_Adapter(array_official, this,this);
        govt_officials_recyclerView.setAdapter(official_adapter);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        govt_officials_recyclerView.setLayoutManager(linearLayoutManager);
        result_launch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::result_handler);
        request_queue = Volley.newRequestQueue(this);
        fusedLocation_client = LocationServices.getFusedLocationProviderClient(this);
        if(savedInstanceState != null && !savedInstanceState.isEmpty())
        {
            get_location = savedInstanceState.getString("KEY_locationstring");
            user_specified_location.setText(get_location);
            volley_download();
        }
        else
        {
            if (hasNetworkConnection())
            {
                locationFind();
            }
            else
            {
                user_specified_location.setText("No Data for Location");
                no_network.setText("No Network Connection"+"\n"+"Data cannot be accessed/loaded without an internet connection");
                ActionBar actionBar;
                actionBar = getSupportActionBar();
                ColorDrawable colorDrawable
                        = new ColorDrawable(Color.parseColor("#FFBAA2BD"));

                actionBar.setBackgroundDrawable(colorDrawable);
            }
        }

    }
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putString("KEY_locationstring", user_specified_location.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        about_activity_menu=menu.findItem(R.id.about_activity_menu);
        search_location_menu=menu.findItem(R.id.search_location_menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()==R.id.about_activity_menu)
        {
            Intent intent = new Intent(this, About_Activity.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.search_location_menu)
        {
            if (!hasNetworkConnection())
            {
                Toast.makeText(this, "There is no network connection! \n Please turn ON the Internet", Toast.LENGTH_SHORT).show();
            }
            else
            {
                AlertDialog.Builder build = new AlertDialog.Builder(this);
                final EditText text = new EditText(MainActivity.this);
                build.setPositiveButton("OK", (dialogInterface, i) -> {
                    get_location = text.getText().toString();
                    volley_download();
                });
                build.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
                build.setTitle("Enter a Location");
                build.setView(text);
                AlertDialog dialog = build.create();
                dialog.show();

            }
        }

        else
        {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private boolean hasNetworkConnection()
    {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
    public void result_handler(ActivityResult result)
    {
        if (result.getResultCode() == RESULT_OK)
        {
            Intent data = result.getData();
            if (data == null)
                return;
        }
        else
        {
            Log.d(TAG,"Result_Handler: Result NOT OK");
        }
    }
    public void volley_download()
    {
        if(!hasNetworkConnection())
        {
            no_network.setVisibility(View.VISIBLE);
            user_specified_location.setText("NO DATA FOR LOCATION");
            return;
        }
        Log.d(TAG, "volley_download: "+get_location);
        String url = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyBzvPfW4ZmJZHgLjvDtkQDCssJK4xDjz_s";
        Uri.Builder url_build = Uri.parse(url).buildUpon();
        url_build.appendQueryParameter("address", get_location);
        String download_url = url_build.build().toString();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    array_official.clear();
                    String new_address="";
                    String var1 ="";
                    JSONObject location_jsonobj=response.getJSONObject("normalizedInput");
                    if (!location_jsonobj.getString("line1").isEmpty())
                    {
                        var1= location_jsonobj.getString("line1")+" ";
                        new_address=new_address+ var1;
                    }
                    if (!location_jsonobj.getString("city").isEmpty())
                    {
                        var1= location_jsonobj.getString("city")+" ,";
                        new_address=new_address+ var1;
                    }
                    if (!location_jsonobj.getString("state").isEmpty())
                    {
                        var1= location_jsonobj.getString("state")+" ";
                        new_address=new_address+ var1;
                    }
                    if (!location_jsonobj.getString("zip").isEmpty())
                    {
                        var1= location_jsonobj.getString("zip");;
                        new_address=new_address+ var1;
                    }
                    //Toast.makeText(MainActivity.this,R.string.address ,Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Location fetch success");
                    JSONArray officeList = response.getJSONArray("offices");
                    JSONArray officialList = response.getJSONArray("officials");
                    int i = 0;
                    while (i < officeList.length()) {
                        JSONObject office = officeList.getJSONObject(i);
                        String position = office.getString("name");

                        JSONArray officialIndices = office.getJSONArray("officialIndices");
                        int j = 0;
                        while (j < officialIndices.length()) {
                            int index = officialIndices.getInt(j);
                            JSONObject official = officialList.getJSONObject(index);

                            // Name
                            String name = official.getString("name");
                            String address = "";
                            if (official.has("address")) {
                                JSONArray addressArr = official.getJSONArray("address");
                                JSONObject addressObj = addressArr.getJSONObject(0);
                                address = addressObj.getString("line1") +","+ addressObj.getString("city") + ","+addressObj.getString("state") +","+ addressObj.getString("zip");
                            }
                            String party = "";
                            if (official.has("party")) {
                                party = official.getString("party");
                            }
                            String phone = "";
                            if (official.has("phones")) {
                                JSONArray phoneArr = official.getJSONArray("phones");
                                int k = 0;
                                while (k < phoneArr.length()) {
                                    phone = phone + phoneArr.getString(k) + "\n";
                                    k++;
                                }
                            }
                            String imageUrl = "";
                            if (official.has("photoUrl")) {
                              //  imageUrl = official.getString("photoUrl");
                                imageUrl = official.get("photoUrl").toString().replace("http:", "https:");
                            }
                            String email = "";
                            if (official.has("emails")) {
                                JSONArray emailArr = official.getJSONArray("emails");
                                email = emailArr.getString(0);
                            }
                            String websiteUrl = "";
                            if (official.has("urls")) {
                                JSONArray websiteUrlArr = official.getJSONArray("urls");
                                websiteUrl = websiteUrlArr.getString(0);
                            }

                            String youtubeUrl="";
                            String facebookUrl="";
                            String twitterUrl="";
                            if (official.has("channels")){
                                JSONArray channelArr = official.getJSONArray("channels");
                                int k=0;
                                while (k<channelArr.length()){
                                    JSONObject channelObj = channelArr.getJSONObject(k);
                                    if (channelObj.getString("type").equals("Facebook")){
                                        facebookUrl = channelObj.getString("id");
                                    }
                                    if (channelObj.getString("type").equals("Twitter")){
                                        twitterUrl = channelObj.getString("id");
                                    }
                                    if (channelObj.getString("type").equals("Youtube")){
                                        youtubeUrl = channelObj.getString("id");
                                    }
                                    k++;
                                }
                            }

                            array_official.add(new Official_class(imageUrl, party, position, phone, name, address, email, new_address, facebookUrl, youtubeUrl, twitterUrl, websiteUrl));
                            j++;
                        }
                        i++;
                    }

                    official_adapter.notifyDataSetChanged();
                    user_specified_location.setText(array_official.get(0).getPresentLocation());

                } catch (Exception e)
                {
                    Log.d(TAG, "onResponse: ");
                }
            }
        };
        Response.ErrorListener request_error = request_error1 -> {
            try {
                Toast.makeText(MainActivity.this, R.string.check_location, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, download_url,
                        null, listener, request_error);
        request_queue.add(jsonObjectRequest);

    }

    private void  locationFind() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        fusedLocation_client.getLastLocation()
                .addOnSuccessListener(this, location1 -> {
                    List<Address> addresses;
                    if (location1 != null)
                    {
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        try
                        {
                            addresses = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                        }
                        catch (IOException e)
                        {
                            throw new RuntimeException(e);
                        }
                        get_location = addresses.get(0).getAddressLine(0).toString();
                        Log.d(TAG, "determineLocation: "+get_location);
                        volley_download();
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_LONG).show());
    }


    @Override
    public void onClick(View view)
    {
        int click = govt_officials_recyclerView.getChildLayoutPosition(view);
        Official_class official = array_official.get(click);
        Intent intent = new Intent(this, Official_Activity.class);
        intent.putExtra("ACTIVITY_OFFICIAL", official);
        result_launch.launch(intent);
    }
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST)
        {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION))
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    locationFind();
                }
                else
                {
                    Toast.makeText(this, R.string.access_denied, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}