package com.example.btlptudddn14.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btlptudddn14.R;
import com.example.btlptudddn14.common.CategorySpinnerAdapter;
import com.example.btlptudddn14.models.Category;
import com.example.btlptudddn14.models.DBHandler;
import com.example.btlptudddn14.models.User;
import com.example.btlptudddn14.models.UserExpense;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity {
    Spinner spinner;
    EditText dateEdt;
    EditText amountEdt;
    EditText descriptionEdt;
    ImageButton datePickerBtn;
    Category categories[];
    Date date;
    Button saveBtn;
    MaterialToolbar toolbar;
    ImageButton backButton;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);
        setupUI();
        setEventListeners();
    }
    public void pickDate(){
        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy");
        Calendar calendar = Calendar.getInstance();
        int year, month, day;
        try {
            Date date = df.parse(dateEdt.getText().toString());
            calendar.setTime(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddExpenseActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateEdt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void setupUI(){
        parentLayout = findViewById(R.id.parentLayout);
        categories = Category.values();
        spinner = findViewById(R.id.spinner);
        dateEdt = findViewById(R.id.dateEdt);
        amountEdt = findViewById(R.id.amountEdt);
        descriptionEdt = findViewById(R.id.descriptionEdt);
        datePickerBtn = findViewById(R.id.datePickerBtn);
        saveBtn = findViewById(R.id.saveBtn);
        toolbar = (MaterialToolbar) findViewById(R.id.detailToolbar);
        backButton = (ImageButton) toolbar.findViewById(R.id.backButton);
        CategorySpinnerAdapter spinnerAdapter = new CategorySpinnerAdapter(this, categories);
        spinner.setAdapter(spinnerAdapter);
        dateEdt.setText(getIntent().getStringExtra("date"));
    }

    private void setEventListeners(){
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
            }
        });
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });
        dateEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    pickDate();
                    view.clearFocus();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amountEdt.getText().toString().isEmpty()){
                    Toast.makeText(AddExpenseActivity.this, "Please input money", Toast.LENGTH_SHORT).show();
                    return;
                }
                 UserExpense expense = new UserExpense(
                        0,
                        User.getInstance().getId(),
                         spinner.getSelectedItemPosition(),
                        descriptionEdt.getText().toString(),
                        dateEdt.getText().toString(),
                        Double.valueOf(amountEdt.getText().toString())
                );

                DBHandler.getInstance(AddExpenseActivity.this).addExpense(expense);
                finish();
                Toast.makeText(AddExpenseActivity.this, "Expense added successfully", Toast.LENGTH_SHORT).show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(amountEdt.getText().toString().isEmpty()){
                   Toast.makeText(AddExpenseActivity.this, "Please input amount", Toast.LENGTH_SHORT).show();
                   return;
               }
                UserExpense expense = new UserExpense(User.getInstance().getId(), User.getInstance().getId(),spinner.getSelectedItemPosition(), descriptionEdt.getText().toString(),dateEdt.getText().toString(),Double.valueOf(amountEdt.getText().toString()));
                DBHandler.getInstance(AddExpenseActivity.this).addExpense(expense);
                finish();
                Toast.makeText(AddExpenseActivity.this, "Expense added successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}

