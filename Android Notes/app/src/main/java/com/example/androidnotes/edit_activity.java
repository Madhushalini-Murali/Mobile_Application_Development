package com.example.androidnotes;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;

public class edit_activity extends AppCompatActivity {

    private EditText note_title;
    private EditText note_text;
    private AndroidNotes androidNotes;

    private static final String TAG = "edit_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        note_title=findViewById(R.id.noteTitle);
        note_text=findViewById(R.id.noteDescription);
        Intent intent=getIntent();
        if(intent.hasExtra("NOTE_EDIT")){
            androidNotes = (AndroidNotes)intent.getSerializableExtra("NOTE_EDIT");
            note_title.setText((androidNotes.getNote_Title()));
            note_text.setText(androidNotes.getNote_Text());

        }
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.edit_activity_menu,menu);
        return true;
    }

    public void no_TitleCheck()
    {


        if(TextUtils.isEmpty(note_title.getText().toString()))
        {
            AlertDialog.Builder build = new AlertDialog.Builder(this);
            build.setPositiveButton("Ok", (dialog, which) -> {
                dialog.dismiss();
                edit_activity.super.onBackPressed();
            });
            build.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            build.setTitle("Note cannot be saved without a Title! \n Are you sure you want to exit?");
            AlertDialog dialog = build.create();
            dialog.show();
            return;
        }
        note_Save();
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.save_menu)
        {
            no_TitleCheck();
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
    protected void onResume()
    {
        if (androidNotes != null) {
            note_title.setText(androidNotes.getNote_Title());
            note_text.setText(androidNotes.getNote_Text());
        }
        super.onResume();
    }
    public void note_Save() {
        String title_Note = note_title.getText().toString();
        String desc_note = note_text.getText().toString();
        String dateTime_note=new Date().toString();
        if (androidNotes != null)
        {
            if (androidNotes.getNote_Title().equals(title_Note))
            {
                if( androidNotes.getNote_Text().equals(desc_note))
                {
                    dateTime_note = androidNotes.getNote_DateTime();
                }
            }
        }
        AndroidNotes note=new AndroidNotes(title_Note,dateTime_note,desc_note);
        Log.d(TAG, "note_Save: "+note);
        String message ="INSERTION_NOTE";

        Intent intent = getIntent();
        if (intent.hasExtra("NOTE_EDIT"))
        {
            message = "NOTE IS UPDATED";
        }

        Intent data =new Intent();
        data.putExtra(message,note);
        if(intent.hasExtra("POSITION OF THE NOTE"))
        {
            int pos=intent.getIntExtra("POSITION OF THE NOTE",0);
            data.putExtra("POSITION IS UPDATED",pos);
        }
        setResult(RESULT_OK, data);
        Toast.makeText(this, getString(R.string.saved_note), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        String title = note_title.getText().toString().trim();
        String description= note_text.getText().toString().trim();
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        if (androidNotes != null)
        {
            if (androidNotes.getNote_Title().equals(title))
            {
                if(androidNotes.getNote_Text().equals(description))
                {
                    super.onBackPressed();
                }
            }
        }
        else if (title.isEmpty() || description.isEmpty())
        {
            Toast.makeText(edit_activity.this, "The notes cannot be empty! Please type something",
                    Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        else
        {
            build.setTitle("Save Note");
            build.setMessage("Please press OK to save and CANCEL to exit ");
            build.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    note_Save();
                }
            });
            build.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    finish();
                }
            });
            AlertDialog dialog = build.create();
            dialog.show();
        }
    }


}