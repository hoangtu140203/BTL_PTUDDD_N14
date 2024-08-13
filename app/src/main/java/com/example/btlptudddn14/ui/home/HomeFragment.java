package com.example.btlptudddn14.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlptudddn14.R;
import com.example.btlptudddn14.common.ExpensesRecycleViewAdapter;
import com.example.btlptudddn14.databinding.FragmentHomeBinding;
import com.example.btlptudddn14.models.DBHandler;
import com.example.btlptudddn14.models.UserExpense;
import com.example.btlptudddn14.ui.AddExpenseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private CalendarView calendarView;
    private FloatingActionButton fab;
    private List<UserExpense> expenses;
    private ExpensesRecycleViewAdapter adapter;
    private String currentSelectedDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycleView);
        calendarView = view.findViewById(R.id.calendarView);
        fab = view.findViewById(R.id.fab);
        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy");
        currentSelectedDate = df.format(calendarView.getDate());
        expenses = DBHandler.getInstance(getContext()).fetchExpensesByDate(currentSelectedDate);
        adapter = new ExpensesRecycleViewAdapter(getContext(), expenses);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddExpenseActivity.class);
                intent.putExtra("date", currentSelectedDate);
                startActivity(intent);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                currentSelectedDate = i2 + "-" + (i1+1) + "-" + i;
                expenses = DBHandler.getInstance(getContext()).fetchExpensesByDate(currentSelectedDate);
                adapter.setExpenses(expenses);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        expenses = DBHandler.getInstance(getContext()).fetchExpensesByDate(currentSelectedDate);
        adapter.setExpenses(expenses);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}