package com.example.btlptudddn14.ui.AddNewTarget;

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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btlptudddn14.R;
import com.example.btlptudddn14.models.DBHandler;
import com.example.btlptudddn14.models.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNewTargetActivity extends AppCompatActivity {
    Button btnCreate, btnDelete;
    ProgressBar progressBar;
    EditText editTextName, editTextTotalBudget, editTextSavedBudget, editTextDate;
    Spinner spinner;
    SeekBar seekBar;
    ImageView imageViewDatePicker;
    List<String> type = Arrays.asList("Small", "Middle", "Big", "Short-term", "Long-term");  // data ta for spinner item


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_target);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setWidget();
        progressBar.setProgress(0);
        editTextDateOnFocusEvent();
        editTextSavedBudgetOnFocusEvent();
        setUpSpinner();
        btnCreateEvent();
        btnDeleteEvent();
        imageViewDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });
    }

    private void btnCreateEvent() {

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (editTextName.getText().toString().equals("")) {
                        Toast.makeText(AddNewTargetActivity.this, "Name can not empty", Toast.LENGTH_SHORT).show();
                    } else if (editTextTotalBudget.getText().toString().equals("")) {
                        Toast.makeText(AddNewTargetActivity.this, "Total Budget can not empty", Toast.LENGTH_SHORT).show();
                    } else if (editTextSavedBudget.getText().toString().equals("")) {
                        Toast.makeText(AddNewTargetActivity.this, "Saved Budget can not empty", Toast.LENGTH_SHORT).show();
                    } else if (editTextDate.getText().toString().equals("")) {
                        Toast.makeText(AddNewTargetActivity.this, "Deadline can not empty", Toast.LENGTH_SHORT).show();
                    } else {
                        Target target = new Target(0, editTextName.getText().toString(), Double.parseDouble(editTextTotalBudget.getText().toString()), Double.parseDouble(editTextSavedBudget.getText().toString()), editTextDate.getText().toString(), spinner.getSelectedItem().toString(), seekBar.getProgress(), "link_img");
                        DBHandler.getInstance(AddNewTargetActivity.this).addTarget(target);
                        Toast.makeText(AddNewTargetActivity.this, "New Target added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(AddNewTargetActivity.this, "Add target Error" + e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void btnDeleteEvent() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextName.setText("");
                editTextDate.setText("");
                editTextSavedBudget.setText("0");
                editTextTotalBudget.setText("0");
            }
        });
    }

    private void setUpSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(AddNewTargetActivity.this, android.R.layout.simple_spinner_item, type);
        spinner.setAdapter(spinnerAdapter);
    }

    private void editTextSavedBudgetOnFocusEvent() {
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

    private void editTextDateOnFocusEvent() {
        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    pickDate();
                    view.clearFocus();
                }
            }
        });
    }

    private void setWidget() {
        btnCreate = findViewById(R.id.btn_create);
        btnDelete = findViewById(R.id.btn_delete);
        editTextName = findViewById(R.id.editTextName);
        editTextTotalBudget = findViewById(R.id.editTextTotalBudget);
        editTextSavedBudget = findViewById(R.id.editTextSavedBudget);
        editTextDate = findViewById(R.id.editTextDate);
        spinner = findViewById(R.id.spinner);
        progressBar = findViewById(R.id.progressBar);
        seekBar = findViewById(R.id.seekBar);
        imageViewDatePicker = findViewById(R.id.imageViewDatePicker);
    }

    public void pickDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy");
        Calendar calendar = Calendar.getInstance();
        int year, month, day;
        try {
            Date date = df.parse(editTextDate.getText().toString());
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddNewTargetActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        editTextDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

}