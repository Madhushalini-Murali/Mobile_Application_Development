package com.example.civiladvocacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import java.util.ArrayList;

public class Official_Adapter extends RecyclerView.Adapter <Official_ViewHolder> {

        private final ArrayList<Official_class> listof_officials;
        private final MainActivity mainActivity;
        public Context cont;
        static int img_placeholder;
        static int img_broken;

        Official_Adapter(ArrayList<Official_class> listof_officials, MainActivity mainActivity, Context con) {
            this.listof_officials = listof_officials;
            this.mainActivity = mainActivity;
            this.cont =con;
            img_placeholder = cont.getResources().getIdentifier("missing", "drawable", cont.getPackageName());
            img_broken = cont.getResources().getIdentifier("brokenimage", "drawable", cont.getPackageName());
        }

        @NonNull
        @Override
        public Official_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.goverment_officials_recyclerview, parent, false);
            itemView.setOnClickListener((View.OnClickListener) mainActivity);
            return new Official_ViewHolder(itemView);
        }

    @Override
    public void onBindViewHolder(@NonNull Official_ViewHolder holder, int position) {
        Official_class off = listof_officials.get(position);
        holder.official_position.setText(off.getPosition());
        holder.official_name.setText(off.getName() + "(" + off.getParty() + ")");
        Glide.with(cont)
                .load(listof_officials.get(position).off_imageUrl)
                .placeholder(img_placeholder)
                .error(listof_officials.get(position).off_imageUrl.equals("")?img_placeholder:img_broken)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.official_image);
    }
    @Override
        public int getItemCount() {
            return listof_officials.size();
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        private int official_picture(String icon) {
            int iconID = mainActivity.getResources().getIdentifier(icon, "drawable", mainActivity.getPackageName());
            if (iconID == 0) {
                Log.d("OfficialAdapter", "parseCurrentRecord: CANNOT FIND ICON " + icon);
            }
            return iconID;
        }
    }


