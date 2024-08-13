package com.example.btlptudddn14.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btlptudddn14.R;
import com.example.btlptudddn14.models.Category;

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {
    public CategorySpinnerAdapter(Context context,
                          Category categories[]) {
        super(context, 0, categories);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
    View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
    View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_spinner, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView textViewName = convertView.findViewById(R.id.text_view);
        Category currentItem = getItem(position);

        if (currentItem != null) {
            imageView.setImageResource(currentItem.drawable);
            textViewName.setText(currentItem.name);
        }
        return convertView;
    }
}
