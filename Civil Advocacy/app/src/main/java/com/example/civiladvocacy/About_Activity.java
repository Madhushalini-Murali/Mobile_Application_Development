package com.example.civiladvocacy;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class About_Activity extends AppCompatActivity
{
    private static final String TAG = "About_Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
    }
    public void clickAboutActivityLink(View v)
    {
        String aboutActivityURL="https://developers.google.com/civic-information/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(aboutActivityURL));
        startActivity(intent);
        Log.d(TAG,"Clicking on Google API Link");
    }
}