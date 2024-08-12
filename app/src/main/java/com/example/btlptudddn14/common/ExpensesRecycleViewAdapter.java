package com.example.btlptudddn14.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlptudddn14.R;
import com.example.btlptudddn14.models.UserExpense;

import java.util.List;

public class ExpensesRecycleViewAdapter extends RecyclerView.Adapter<ExpenseViewHolder>{
    Context context;
    List<UserExpense> expenses;

    public ExpensesRecycleViewAdapter(Context context, List<UserExpense> expenses) {
        this.context = context;
        this.expenses = expenses;
    }
    public void setExpenses(List<UserExpense> expenses){
        this.expenses = expenses;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExpenseViewHolder(LayoutInflater.from(context).inflate(R.layout.expense_recycle_view_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        holder.setExpense(expenses.get(position));
    }

    @Override
    public int getItemCount() {
        if(expenses != null)
            return expenses.size();
        return 0;
    }
}
