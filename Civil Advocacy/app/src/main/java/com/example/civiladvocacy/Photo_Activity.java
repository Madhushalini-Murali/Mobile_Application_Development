package com.example.civiladvocacy;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class Photo_Activity extends AppCompatActivity {

    private TextView location_textbox;
    private TextView off_name,off_office;
    private ImageView off_photo,party_logo;
    private static RequestQueue request_queue;
    private long beginTime;
    private  Official_class off_class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail_activity);
        Intent intent=getIntent();
        location_textbox=findViewById(R.id.location_textbox);
        off_name=findViewById(R.id.official_name);
        off_office=findViewById(R.id.official_office);
        off_photo=findViewById(R.id.official_photo);
        party_logo=findViewById(R.id.partylogo_official);
        if (intent.hasExtra("PHOTO_OFFICIAL"))
        {
            off_class=(Official_class) intent.getSerializableExtra("PHOTO_OFFICIAL");
        }
        View view = this.getWindow().getDecorView();
        off_name.setText(off_class.getName());
        off_office.setText(off_class.getPosition());
        location_textbox.setText(off_class.getPresentLocation());
        request_queue = Volley.newRequestQueue(this);
        get_Image(off_class.getImageUrl());

        if(off_class.getParty().equals("Republican Party"))
        {
            party_logo.setImageResource(get_official_image("rep_logo"));
            view.setBackgroundColor(0xffff0000);
        }
        else if(off_class.getParty().equals("Democratic Party"))
        {
            party_logo.setImageResource(get_official_image("dem_logo"));
            view.setBackgroundColor(0xff0000ff);
        }
        else
        {
            party_logo.setVisibility(View.INVISIBLE);
            view.setBackgroundColor(000000);
        }

    }
    private int get_official_image(String img)
    {
        int imageResourceID ;
        imageResourceID = this.getResources().getIdentifier(img, "drawable", this.getPackageName());
        if (imageResourceID != 0)
        {
            return imageResourceID;
        }
        else
        {
            Toast.makeText(this, "The image could not be found" +img, Toast.LENGTH_SHORT).show();
            return 0;
        }
    }
    public void get_Image(String reqURL)
    {
        Response.Listener<Bitmap> imageListener = new Response.Listener<Bitmap>()
        {
            @Override
            public void onResponse(Bitmap response)
            {
                off_photo.setImageBitmap(response);
            }
        };

        reqURL = reqURL.replace("http://", "https://");
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                off_photo.setImageResource(get_official_image("brokenimage"));
            }
        };

        ImageRequest imageRequest = new ImageRequest(reqURL, imageListener, 0, 0,
                ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565, errorListener);
        beginTime = System.currentTimeMillis();
        request_queue.add(imageRequest);
    }
    public void party_website(View view)
    {
        String partylink="";
        if (off_class.getParty().equals("Republican Party"))
        {
            partylink="https://www.gop.com";
        }
        else
        {
            partylink="https://democrats.org/";
        }
        clickAboutActivityLink(partylink);
    }
    public void clickAboutActivityLink(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
        else
        {
            Log.d(TAG,"No such URL Found for the Official's party");
        }
    }
}