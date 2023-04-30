package com.example.civiladvocacy;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.Toast;

import com.android.volley.RequestQueue;

public class Official_Activity extends AppCompatActivity {

    private TextView location_current;
    private TextView nameof_official;
    private TextView officeof_official;
    private TextView partyof_official;
    private TextView address_textview, full_address;
    private TextView phone_textview, full_phnumber;
    private TextView email_textview, full_emailaddress;
    private TextView website_textview, full_websitelink;
    private ImageView imageof_official, logoOf_party;
    private ImageView facebook_button, twitter_button,youtube_button;
    private Official_class officialClass;
    private static RequestQueue request_queue;
    private ActivityResultLauncher<Intent> result_launch;
    private long beginTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.official_activity);
        location_current=findViewById(R.id.location_textbox);
        nameof_official=findViewById(R.id.govt_official_name);
        officeof_official=findViewById(R.id.govt_official_office);
        partyof_official=findViewById(R.id.govt_official_party);
        address_textview=findViewById(R.id.official_address);
        full_address=findViewById(R.id.full_address_desc);
        phone_textview=findViewById(R.id.phone);
        full_phnumber=findViewById(R.id.phone_number);
        email_textview=findViewById(R.id.email);
        full_emailaddress=findViewById(R.id.email_address);
        website_textview=findViewById(R.id.website);
        full_websitelink=findViewById(R.id.website_link);
        imageof_official=findViewById(R.id.official_imagepic);
        logoOf_party=findViewById(R.id.party_logo);
        facebook_button =findViewById(R.id.facebook_icon);
        twitter_button=findViewById(R.id.twitter_icon);
        youtube_button= findViewById(R.id.youtube_icon);
        result_launch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::result_handler);
        Intent intent=getIntent();
        if (intent.hasExtra("ACTIVITY_OFFICIAL"))
        {
            officialClass=(Official_class) intent.getSerializableExtra("ACTIVITY_OFFICIAL");
        }
        View officialsView = getWindow().getDecorView();
        location_current.setText(officialClass.getPresentLocation());
        nameof_official.setText(officialClass.getName());
        partyof_official.setText("(" + officialClass.getParty() + ")");
        officeof_official.setText(officialClass.getPosition());
        SpannableString addressContent = new SpannableString(officialClass.getAddress());
        addressContent.setSpan(new UnderlineSpan(), 0, addressContent.length(), 0);
        full_address.setText(addressContent);
        SpannableString phoneContent = new SpannableString(officialClass.getPhoneNumber());
        phoneContent.setSpan(new UnderlineSpan(), 0, phoneContent.length(), 0);
        full_phnumber.setText(phoneContent);
        SpannableString emailContent = new SpannableString(officialClass.getEmail());
        emailContent.setSpan(new UnderlineSpan(), 0, emailContent.length(), 0);
        full_emailaddress.setText(emailContent);
        SpannableString websiteContent = new SpannableString(officialClass.getWebUrl());
        websiteContent.setSpan(new UnderlineSpan(), 0, websiteContent.length(), 0);
        full_websitelink.setText(websiteContent);
        if (officialClass.getAddress().equals(""))
        {
            address_textview.setVisibility(View.GONE);
            full_address.setVisibility(View.GONE);
        }
        if (officialClass.getPhoneNumber().equals(""))
        {
            phone_textview.setVisibility(View.GONE);
            full_phnumber.setVisibility(View.GONE);
        }

        if (officialClass.getWebUrl().equals(""))
        {
            website_textview.setVisibility(View.GONE);
            full_websitelink.setVisibility(View.GONE);
        }

        if (officialClass.getEmail().equals(""))
        {
            email_textview.setVisibility(View.GONE);
            full_emailaddress.setVisibility(View.GONE);
        }

        facebook_button.setVisibility(officialClass.getLinkFacebook().equals("") ? View.INVISIBLE : View.VISIBLE);
        youtube_button.setVisibility(officialClass.getLinkYouTube().equals("") ? View.INVISIBLE : View.VISIBLE);
        twitter_button.setVisibility(officialClass.getLinkTwitter().equals("") ? View.INVISIBLE : View.VISIBLE);

        if (officialClass.getParty().equals("Republican Party"))
        {
            logoOf_party.setImageResource(get_official_image("rep_logo"));
            officialsView.setBackgroundColor(0xffff0000);
        }
        else if (officialClass.getParty().equals("Democratic Party"))
        {
            logoOf_party.setImageResource(get_official_image("dem_logo"));
            officialsView.setBackgroundColor(0xff0000ff);
        }
        else
        {
            logoOf_party.setVisibility(View.INVISIBLE);
            officialsView.setBackgroundColor(Color.BLACK);
        }
        Glide.with(getApplicationContext())
                .load(officialClass.off_imageUrl)
                .placeholder(Official_Adapter.img_placeholder)
                .error(officialClass.off_imageUrl.equals("")?Official_Adapter.img_placeholder:Official_Adapter.img_broken)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if(officialClass.off_imageUrl.equals("")){
                            imageof_official.setClickable(false);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageof_official);
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

    public void clicking_facebookicon(View view)
    {
        Intent intent;
        try
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.facebook.katana");
            intent.setData(Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/"+officialClass.getLinkFacebook()));
            startActivity(intent);
        }
        catch(Exception exception)
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.facebook.com/"+officialClass.getLinkFacebook()));
            startActivity(intent);
        }
    }

    public void clicking_youtubeicon(View view)
    {
        //String name = <the official’s youtube id from download>;
        Intent intent = null;
        try
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + nameof_official));
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + nameof_official)));
        }
    }

    public void clicking_twittericon(View view)
    {
        Intent intent;
        try
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.twitter.android");
            intent.setData(Uri.parse("twitter://user?screen_name="+officialClass.getLinkTwitter()));
            startActivity(intent);
        }
        catch(Exception exception)
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.twitter.com/"+officialClass.getLinkTwitter()));
            startActivity(intent);
        }
    }
    public void party_website(View view)
    {
        String partylink="";
        if (officialClass.getParty().equals("Republican Party"))
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
        startActivity(intent);
    }
    public void  official_addressClick (View view)
    {
        String official_address=officialClass.getAddress();
        Uri addressURL = Uri.parse("geo:0,0?q=" + Uri.encode(official_address));

        Intent intent = new Intent(Intent.ACTION_VIEW, addressURL);
        startActivity(intent);
        Log.d(TAG,"Calling Address");
    }
    public void official_phnumClick(View view)
    {
        String phone_number=officialClass.getPhoneNumber();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone_number));
        startActivity(intent);
        Log.d(TAG,"Calling Phone Number");
    }

    public void official_emailClick(View view)
    {
        String[] email = new String[]{officialClass.getEmail()};

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "This comes from EXTRA_SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT, "Email text body from EXTRA_TEXT...");
        startActivity(intent);
        Log.d(TAG,"Calling Email Address");

    }
    public void official_websitelinkClick (View view)
    {
        String official_websiteURL=officialClass.getWebUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(official_websiteURL));
        startActivity(intent);
        Log.d(TAG,"Calling Website URL");
    }
    public void official_photoClick(View view)
    {
        if (!officialClass.getImageUrl().equals("")) {
            Intent intent = new Intent(this, Photo_Activity.class);
            intent.putExtra("PHOTO_OFFICIAL", officialClass);
            result_launch.launch(intent);
        }
    }
}