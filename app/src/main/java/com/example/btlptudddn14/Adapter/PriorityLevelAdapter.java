package com.example.btlptudddn14.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btlptudddn14.R;
import com.example.btlptudddn14.models.Target;

import java.util.ArrayList;

public class PriorityLevelAdapter extends RecyclerView.Adapter<PriorityLevelAdapter.viewholder>{
    ArrayList<Target> items;
    Context context;

    public PriorityLevelAdapter(ArrayList<Target> items) {
        this.items = items;
    }

    public void saveChangedTarget(ArrayList<Target> targets) {
        items = targets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PriorityLevelAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_level_target, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PriorityLevelAdapter.viewholder holder, int position) {
        holder.title_level.setText(items.get(position).getPlanName());
        int drawableResourceId = holder.itemView.getResources().getIdentifier(items.get(position).getImgSrc(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(context).load(drawableResourceId).into(holder.ic);
        int a = (int) (items.get(position).getSavedBudget() / items.get(position).getTotalBudget() * 100);
        holder.progressTxt.setText(a + "%");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView ic;
        TextView title_level, progressTxt;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            ic = itemView.findViewById(R.id.ic_level);
            title_level = itemView.findViewById(R.id.title_level);
            progressTxt = itemView.findViewById(R.id.progress_level);
        }
    }
}
