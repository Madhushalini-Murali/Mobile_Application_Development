package com.example.news_aggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import java.util.Random;
import android.text.SpannableString;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.news_aggregator.databinding.ActivityMainBinding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import com.android.volley.AuthFailureError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
{
    //API KEY- 9d1f70cfe4764b2a89b7438a3086d0c0
    public static final String TAG = "MainActivity";
    private static RequestQueue request_queue;
    private Menu menu_Option;
    private DrawerLayout drawer_Layout;
    private ListView news_array;
    private ActionBarDrawerToggle drawer_Toggle;

    private final HashMap<String, HashSet<String>> map_topic_Channel = new HashMap<>();
    private final HashMap<String, String> map_channel_Id = new HashMap<>();
    private final HashMap<String, Integer> map_Column_topic = new HashMap<>();
    private final HashMap<String, Integer> map_Column_channel = new HashMap<>();
    private String[] drawer_items;
    private final ArrayList<String> channel_array = new ArrayList<>();
    private ArrayAdapter<String> drawer_Adapter;
    private final ArrayList<News_Class> news_list = new ArrayList<>();
    private ViewPager2 news_ViewPager;
    private News_Adapter news_Adapter;
    String selected_Article = "";
    String selected_Article_Id = "";
    String selected_TopicItem = "";
    HashMap<String, HashSet<String>> map_topic_Channel1 = new HashMap<>();
    private ActivityMainBinding news_binding;
    private static final String news_url =
            "https://newsapi.org/v2/sources?apiKey=9d1f70cfe4764b2a89b7438a3086d0c0";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        news_binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(news_binding.getRoot());
        request_queue = Volley.newRequestQueue(this);

        drawer_Layout = news_binding.drawerLayout;
        drawer_Toggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawer_Layout,         /* DrawerLayout object */
                R.string.show_drawer,  /* "open drawer" description for accessibility */
                R.string.close_drawer  /* "close drawer" description for accessibility */
        );

        news_array = news_binding.drawerListView;

         drawer_Adapter= new ArrayAdapter<String>(this,R.layout.drawer_layout, channel_array)
         {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
            {
                View view =super.getView(position, convertView, parent);
                TextView textView=(TextView) view.findViewById(R.id.drawer_list_display);
                int val;
                Log.d(TAG, "getView: "+channel_array.get(position));
                textView.setText(channel_array.get(position));
                val =map_Column_channel.get(textView.getText().toString());
                textView.setTextColor(val);
                return textView;
            }
        };
        news_array.setAdapter(drawer_Adapter);
        news_array.setOnItemClickListener(
                (parent, view, position, id) -> {
                    selectItem(position);
                }
        );
        news_Adapter = new News_Adapter(this, news_list,this);
        news_ViewPager = news_binding.viewPager;
        news_ViewPager.setAdapter(news_Adapter);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        if (hasNetworkConnection())
        {
            volley_download();
            Log.d(TAG, "Has Network Connection ");
        }
        else
        {
            setTitle("No Network Connection");
            Log.d(TAG, "No Network Connection");

        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawer_Toggle.syncState(); // <== IMPORTANT
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
       drawer_Toggle .onConfigurationChanged(newConfig); // <== IMPORTANT
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        selected_Article = "";
        selected_Article_Id = "";
        selected_TopicItem = " ";
        if (drawer_Toggle.onOptionsItemSelected(item))
        {
            Log.d("mainActivity", "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }
        selected_TopicItem = item.getTitle().toString();
        news_list.clear();
        news_Adapter.notifyDataSetChanged();
        channel_array.clear();
        map_topic_Channel1=map_topic_Channel;
        HashSet<String> lst = map_topic_Channel.get(item.getTitle().toString());
        if (lst != null)
        {
            channel_array.addAll(lst);
        }
        drawer_Adapter.notifyDataSetChanged();
        Collections.sort(channel_array);
        setTitle("News Gateway"+ "("+Integer.toString(channel_array.size())+")");
        return super.onOptionsItemSelected(item);
   }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("Topic", selected_TopicItem);
        outState.putString("Article", selected_Article);
        outState.putString("Article_ID", selected_Article_Id);
        super.onSaveInstanceState(outState);
    }
    private boolean hasNetworkConnection()
    {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu_Option = menu;
        return true;
    }
    public void volley_download()
    {
        Random random = new Random();
        Response.Listener<JSONObject> listener =
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray sourcesArray = response.getJSONArray("sources");
                            for (int i = 0; i < sourcesArray.length(); i++)
                            {
                                String news_channel_Id;
                                String news_channel_Name;
                                String news_channel_Category;
                                JSONObject sourceObj = sourcesArray.getJSONObject(i);
                                news_channel_Id = sourceObj.getString("id");
                                news_channel_Name = sourceObj.getString("name");
                                news_channel_Category = sourceObj.getString("category");
                                //add channel name
                                channel_array.add(news_channel_Name);

                                if (map_topic_Channel.containsKey(news_channel_Category))
                                {
                                    map_topic_Channel.get(news_channel_Category).add(news_channel_Name);
                                }
                                else
                                {
                                    HashSet<String> channelSet = new HashSet<>();
                                    channelSet.add(news_channel_Name);
                                    map_topic_Channel.put(news_channel_Category, channelSet);
                                }
                                if (map_topic_Channel.containsKey("All"))
                                {
                                    map_topic_Channel.get("All").add(news_channel_Name);
                                }
                                else
                                {
                                    HashSet<String> channelSet = new HashSet<>();
                                    channelSet.add(news_channel_Name);
                                    map_topic_Channel.put("All", channelSet);
                                }
                                // Add channel to id map
                                map_channel_Id.put(news_channel_Name, news_channel_Id);
                            }
                            // Sort categories alphabetically and assign random colors
                            ArrayList<String> categoryList = new ArrayList<>(map_topic_Channel.keySet());
                            Collections.sort(categoryList);
                            for (String category : categoryList)
                            {
                                // Assign random color to category name
                                int color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                                SpannableString spannableCategory = new SpannableString(category);
                                spannableCategory.setSpan(new ForegroundColorSpan(color), 0, category.length(), 0);
                                map_Column_topic.put(category, color);

                                // Assign category color to each channel in category
                                HashSet<String> channelSet = map_topic_Channel.get(category);
                                for (String channel : channelSet)
                                {
                                    map_Column_channel.put(channel, color);
                                }
                                // Add category to menu
                                menu_Option.add(spannableCategory);
                            }

                            drawer_Adapter.notifyDataSetChanged();
                            setTitle("News Gateway"+ "("+Integer.toString(channel_array.size())+")");
                        }
                        catch (Exception e)
                        {
                            // Handle exception
                        }
                    }
                };

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try
                {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, news_url, null, listener, error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "News-App");
                        return headers;
                    }
                };
        request_queue.add(jsonObjectRequest);
    }
    private void selectItem(int position)
    {
        drawer_Layout.closeDrawer(news_array);
        String news_channel = channel_array.get(position);
        String news_channel_id = (map_channel_Id.get(news_channel).toString());
        selected_Article=news_channel;
        selected_Article_Id=news_channel_id;
        setTitle(selected_Article);
        news_list.clear();
        news_volley_download(selected_Article_Id);
    }
    public void news_volley_download(String news_channel_id) {
        setTitle(selected_Article);
        news_binding.constlayout.setBackgroundColor(Color.WHITE);
        String urllink="https://newsapi.org/v2/top-headlines?sources="+news_channel_id+"&apiKey=9d1f70cfe4764b2a89b7438a3086d0c0";
        Response.Listener<JSONObject> listener =
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray sourcesArray = response.getJSONArray("articles");
                            for (int i = 0; i < sourcesArray.length(); i++)
                            {
                                String news_author;
                                String news_headline;
                                String news_link;
                                String news_urltoImg;
                                String news_publishAt;
                                String news_description;

                                JSONObject sourceObj = sourcesArray.getJSONObject(i);
                                news_author=sourceObj.getString("author");
                                news_headline=sourceObj.getString("title");
                                news_link=sourceObj.getString("url");
                                news_urltoImg=sourceObj.getString("urlToImage");
                                news_publishAt=sourceObj.getString("publishedAt");
                                news_description=sourceObj.getString("description");
                                news_list.add(new News_Class(news_author,news_headline,news_description,news_link,news_urltoImg,news_publishAt));

                            }
                            news_Adapter.notifyDataSetChanged();
                            news_ViewPager.setCurrentItem(0);

                        } catch (Exception e) {
                        }
                    }
                };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urllink,
                        null, listener, error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "News-App");
                        return headers;
                    }
                };
        // Add the request to the RequestQueue.
        request_queue.add(jsonObjectRequest);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        selected_TopicItem = savedInstanceState.getString("Topic");
        selected_Article = savedInstanceState.getString("Article");
        selected_Article_Id = savedInstanceState.getString("Article_ID");
        if(!TextUtils.isEmpty(selected_Article))
        {
            news_volley_download(selected_Article_Id);
        }
    }
    public void clickNewsPage(View view)
    {

            int val = (int) view.getTag();
            String url=news_list.get(val).news_url;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
            Log.d(TAG,"Entering news source web site");
    }


}