package com.example.civiladvocacy;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Official_ViewHolder extends RecyclerView.ViewHolder
{
    TextView official_name;
    TextView official_position;
    ImageView official_image,separator_line;
    public Official_ViewHolder(@NonNull View itemView)
    {
        super(itemView);
        official_name=itemView.findViewById(R.id.name_govt_official);
        official_position= itemView.findViewById(R.id.position_govt_official);
        official_image=itemView.findViewById(R.id.imageView);
        separator_line=itemView.findViewById(R.id.separator_line);
    }
}
