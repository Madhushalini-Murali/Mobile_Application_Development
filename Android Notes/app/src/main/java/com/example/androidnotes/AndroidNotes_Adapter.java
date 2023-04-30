package com.example.androidnotes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class AndroidNotes_Adapter extends RecyclerView.Adapter<AndroidNotes_ViewHolder>
{
    private static final String TAG = "Android_Notes_Adapter";
    private final List<AndroidNotes> notesArray;
    private final MainActivity mainAct;

    AndroidNotes_Adapter(List<AndroidNotes> notesArray, MainActivity ma)
    {
        this.notesArray = notesArray;
        mainAct = ma;
    }

    @NonNull
    @Override
    public AndroidNotes_ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW AndroidNotesViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noterecycler, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new AndroidNotes_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AndroidNotes_ViewHolder holder, int position)
    {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Employee " + position);

        AndroidNotes androidNotes = notesArray.get(position);

        holder.title_notes.setText(androidNotes.getNote_Title());
        holder.title_dateTime.setText(androidNotes.getNote_DateTime());
        holder.title_description.setText(androidNotes.getNote_Text());
        if (androidNotes.getNote_Text().length() >= 80)
        {
            String display_text = androidNotes.getNote_Text().substring(0,80);
            holder.title_description.setText( display_text+ "...");
        }
        else
        {
            holder.title_description.setText(androidNotes.getNote_Text());
        }
    }

    @Override
    public int getItemCount()
    {
        return notesArray.size();
    }

}

