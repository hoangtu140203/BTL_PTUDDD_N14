package com.example.btlptudddn14.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TypeTargetAdapter extends RecyclerView.Adapter<TypeTargetAdapter.Viewholder>{
    ArrayList<ListTypeTarget> items;
    Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ListTypeTarget item);
    }

    public TypeTargetAdapter(Context context, ArrayList<ListTypeTarget> items, OnItemClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public TypeTargetAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_type, parent, false);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeTargetAdapter.Viewholder holder, int position) {

        final ListTypeTarget item = items.get(position);
        holder.titleTxt.setText(items.get(position).getTitle());

        int drawableResourceId = holder.itemView.getResources()
                .getIdentifier(items.get(position).getImgpath(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourceId)
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(items.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        private boolean isItemSelected;
        ConstraintLayout layoutItem;
        TextView titleTxt;
        ImageView img;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.type_item);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            img = itemView.findViewById(R.id.img_type);
        }
    }


}
