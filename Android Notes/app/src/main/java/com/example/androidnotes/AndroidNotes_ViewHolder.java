package com.example.androidnotes;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class AndroidNotes_ViewHolder extends RecyclerView.ViewHolder {

    TextView title_notes;
    TextView title_dateTime;
    TextView title_description;

    AndroidNotes_ViewHolder(View view) {
        super(view);
        title_notes = view.findViewById(R.id.AndroidNotes_title);
        title_dateTime = view.findViewById(R.id.AndroidNotes_dateTime);
        title_description = view.findViewById(R.id.AndroidNotes_descrp);
    }

}


