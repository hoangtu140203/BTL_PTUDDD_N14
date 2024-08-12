package com.example.btlptudddn14.common;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlptudddn14.R;
import com.example.btlptudddn14.models.Category;
import com.example.btlptudddn14.models.UserExpense;
import com.example.btlptudddn14.ui.ExpenseDetailActivity;

public class ExpenseViewHolder extends RecyclerView.ViewHolder{

    Context context;
    TextView name;
    TextView amount;
    TextView description;
    UserExpense expense;
    ImageView icon;

    public void setExpense(UserExpense expense) {
        this.expense = expense;
        icon.setImageResource(Category.values()[expense.getType()].drawable);
        name.setText(Category.values()[expense.getType()].name);
        amount.setText(String.valueOf(expense.getAmount()));
        description.setText(expense.getDescription());
    }

    public ExpenseViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        name = itemView.findViewById(R.id.name);
        amount = itemView.findViewById(R.id.amount);
        description = itemView.findViewById(R.id.description);
        icon = itemView.findViewById(R.id.icon);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ExpenseDetailActivity.class);
                intent.putExtra("expense", expense);
                context.startActivity(intent);
            }
        });
    }

}
