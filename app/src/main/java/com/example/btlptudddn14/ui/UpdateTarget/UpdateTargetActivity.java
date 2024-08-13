package com.example.btlptudddn14.ui.UpdateTarget;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btlptudddn14.R;
import com.example.btlptudddn14.models.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.example.btlptudddn14.models.DBHandler;

public class UpdateTargetActivity extends AppCompatActivity {
    Target target;
    EditText editTextTotalBudget, editTextSavedBudget, editTextDeadline;
    TextView textViewName;
    Spinner spinner;
    ProgressBar progressBar;
    ImageView imageViewDatePicker;
    Button btnUpdate, btnBack;
    SeekBar seekBar;
    List<String> type = Arrays.asList("Small", "Middle", "Big", "Short-term", "Long-term");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_target);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setWidget();
        target = (Target) getIntent().getParcelableExtra("Target");
        setupDataForFragment();
        editTextSavedBudgetEvent();
        setupSpinner();
        editTextDeadlineEvent();
        btnUpdateEvent();
        btnBackEvent();
        imageViewDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });
    }

    private void btnBackEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void btnUpdateEvent() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (editTextTotalBudget.getText().toString().equals("")) {
                        Toast.makeText(UpdateTargetActivity.this, "Total Budget can not empty", Toast.LENGTH_SHORT).show();
                    } else if (editTextSavedBudget.getText().toString().equals("")) {
                        Toast.makeText(UpdateTargetActivity.this, "Saved Budget can not empty", Toast.LENGTH_SHORT).show();
                    } else if (editTextDeadline.getText().toString().equals("")) {
                        Toast.makeText(UpdateTargetActivity.this, "Deadline can not empty", Toast.LENGTH_SHORT).show();
                    }

                    Target newtarget = new Target(target.getID(), textViewName.getText().toString(), Double.parseDouble(editTextTotalBudget.getText().toString()), Double.parseDouble(editTextSavedBudget.getText().toString()), editTextDeadline.getText().toString(), spinner.getSelectedItem().toString(), seekBar.getProgress(), "link_img");
                    DBHandler.getInstance(UpdateTargetActivity.this).updateTarget(newtarget);
                    Toast.makeText(UpdateTargetActivity.this, "updated Target successfully", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(UpdateTargetActivity.this, "update target Error" , Toast.LENGTH_SHORT).show();
                }
                finish();

            }
        });
    }

    private void editTextDeadlineEvent() {
        editTextDeadline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    pickDate();
                    view.clearFocus();
                }
            }
        });
    }

    private void setupSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(UpdateTargetActivity.this, android.R.layout.simple_spinner_item, type);
        spinner.setAdapter(spinnerAdapter);
    }

    private void editTextSavedBudgetEvent() {
        editTextSavedBudget.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (editTextSavedBudget.getText().toString().equals("")) {
                    editTextSavedBudget.setText("0");
                }

                int progressprecent = (int) (Double.parseDouble(editTextSavedBudget.getText().toString()) / Double.parseDouble(editTextTotalBudget.getText().toString()) * 100);
                progressBar.setProgress(progressprecent);
            }
        });
    }

    private void setupDataForFragment() {

        textViewName.setText(target.getPlanName());
        editTextSavedBudget.setText(String.valueOf(target.getSavedBudget()));
        editTextTotalBudget.setText(String.valueOf(target.getTotalBudget()));
        editTextDeadline.setText(target.getDeadline());
    }

    private void setWidget() {
        textViewName =findViewById(R.id.planname);
        editTextTotalBudget = findViewById(R.id.editTextTotalBudget);
        editTextSavedBudget = findViewById(R.id.editTextSavedBudget);
        editTextDeadline = findViewById(R.id.editTextDeadline);
        imageViewDatePicker = findViewById(R.id.imageView);
        spinner = findViewById(R.id.spinner);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);
        seekBar = findViewById(R.id.seekBar);
    }

    public void pickDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy");
        Calendar calendar = Calendar.getInstance();
        int year, month, day;
        try {
            Date date = df.parse(editTextDeadline.getText().toString());
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                UpdateTargetActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        editTextDeadline.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                },
                year, month, day);
        datePickerDialog.show();
    }


}