package com.example.androidnotes;

import static android.content.ContentValues.TAG;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.result.contract.ActivityResultContracts;
import android.widget.Toast;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener
{
    private List<AndroidNotes> notesArray=new ArrayList<>();
    private RecyclerView recycleView;
    private AndroidNotes_Adapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycleView = findViewById(R.id.recycle_view);
        mAdapter = new AndroidNotes_Adapter(notesArray, this);
        recycleView.setAdapter(mAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(linearLayoutManager);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);
        loadFile();
    }

   public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.information_menu)
        {
            Toast.makeText(this, "Displaying Information menu", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, about_activity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId()==R.id.add_notes_menu)
        {
            Intent intent = new Intent(this, edit_activity.class);
            activityResultLauncher.launch(intent);
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    public void loadFile()
    {
        setTitle("AndroidNotes ("+ notesArray.size()+")");
        Log.d(TAG, "loadFile: Loading JSON File");
        try
        {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String sentence;
            while ((sentence = reader.readLine()) != null)
            {
                sb.append(sentence);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject json_Object = jsonArray.getJSONObject(i);
                Log.d(TAG, "loadFile: vv  "+json_Object);
                String note_title = json_Object.getString("Note_Title");
                String note_dateTime = json_Object.getString("Note_dateTime");
                String note_description = json_Object.getString("Note_Desc");
                AndroidNotes androidNotes= new AndroidNotes(note_title, note_dateTime, note_description);
                Log.d(TAG, "loadFile: " + androidNotes);
                notesArray.add(androidNotes);

                //mAdapter.notifyItemInserted(notesArray.size());
            }
            setTitle("AndroidNotes ("+ notesArray.size()+")");
            latest_NotesSort();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
            notesArray.clear();
            mAdapter.notifyItemInserted(notesArray.size());
        } catch (Exception e) {
            e.printStackTrace();
            notesArray.clear();
            mAdapter.notifyItemInserted(notesArray.size());
            }

    }
    public void handleResult(ActivityResult result)
    {
        if (result.getResultCode() == RESULT_OK)
        {
            Intent data = result.getData();
            if (data == null)
                return;
            if (data.hasExtra("INSERTION_NOTE"))
            {
                Log.d(TAG, "handleResult: ");
                AndroidNotes note = (AndroidNotes) data.getSerializableExtra("INSERTION_NOTE");
                notesArray.add(note);
                save_AndroidNote();
                Toast.makeText(this, getString(R.string.note_save), Toast.LENGTH_SHORT).show();
                mAdapter.notifyItemInserted(notesArray.size());
            }
            else if (data.hasExtra("NOTE IS UPDATED"))
            {
                AndroidNotes edit_note=(AndroidNotes)data.getSerializableExtra("NOTE IS UPDATED");
                int pos=data.getIntExtra("POSITION IS UPDATED",0);
                AndroidNotes updated_note = notesArray.get(pos);
                updated_note.setNote_Title(edit_note.getNote_Title());
                updated_note.setNote_DateTime(edit_note.getNote_DateTime());
                updated_note.setNote_Text(edit_note.getNote_Text());
                save_AndroidNote();
                mAdapter.notifyItemChanged(pos);
            }
        }
        else
        {

        }
    }
    @Override
    public void onClick(View view)
    {
        int pos;
        pos = recycleView.getChildLayoutPosition(view);
        AndroidNotes notes = notesArray.get(pos);
        Intent i = new Intent(this, edit_activity.class);
        i.putExtra("NOTE_EDIT",notes);
        i.putExtra("POSITION OF THE NOTE", pos);
        activityResultLauncher.launch(i);
    }


    @Override
    public boolean onLongClick(View view)
    {
        int pos;
        pos = recycleView.getChildLayoutPosition(view);
        AndroidNotes notes = notesArray.get(pos);
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Note_Delete");
        build.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                notesArray.remove(pos);
                save_AndroidNote();
                mAdapter.notifyItemRemoved(pos);
            }
        });
        build.setNegativeButton("NO", (dialogInterface, i) -> {
        });
        build.setMessage("Delete Note '" + notes.getNote_Title() + "'?");
        AlertDialog dialog = build.create();
        dialog.show();
        return true;
    }
    private void save_AndroidNote()
    {
        setTitle("AndroidNotes ("+ notesArray.size()+")");
        latest_NotesSort();
        try
        {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(notesArray);
            printWriter.close();
            fos.close();
            Toast.makeText(this, "Notes Updated Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            e.getStackTrace();
        }
        Log.d(TAG, "saveAndroidNotes: Saving JSON File");
    }

    public void latest_NotesSort()
    {
        Collections.sort(notesArray, new Comparator<AndroidNotes>()
        {
            public int compare(AndroidNotes note1, AndroidNotes note2)
            {
                if (note2.getNote_DateTime() == null || note1.getNote_DateTime() == null)
                {
                    return 0;
                }
                return note2.getNote_DateTime().compareTo(note1.getNote_DateTime());
            }
        });
        mAdapter.notifyDataSetChanged();
    }
}