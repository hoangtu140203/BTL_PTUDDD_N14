package com.example.btlptudddn14.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.btlptudddn14.LogInActivity;
import com.example.btlptudddn14.MainActivity;
import com.example.btlptudddn14.R;
import com.example.btlptudddn14.models.DBHandler;
import com.example.btlptudddn14.models.User;

public class changeInfor extends AppCompatActivity {
    EditText etChgeFirstName, etchaneLastName;
    Button btnUpdatePro, btnCancelUpdatePro, btnDeleteAccount;
    LinearLayout contain5;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeprofile);
        etChgeFirstName = findViewById(R.id.etChangeFirstName);
        etchaneLastName = findViewById(R.id.etChangeLastName);
        btnUpdatePro = findViewById(R.id.btnUpdateProfile);
        btnCancelUpdatePro = findViewById(R.id.btnCancelUpdateProfile);
        btnDeleteAccount = findViewById(R.id.btnDeleteUser);
        contain5 = findViewById(R.id.LLChangeInfor);
        contain5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });
        btnCancelUpdatePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnUpdatePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = etChgeFirstName.getText().toString();
                String lastname = etchaneLastName.getText().toString();
                if(TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lastname))
                {
                    Toast.makeText(changeInfor.this, "Please enter complete information!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(DBHandler.getInstance(changeInfor.this).updateProfile(firstname, lastname))
                {
                    Toast.makeText(changeInfor.this, "Update your profile successfully!", Toast.LENGTH_SHORT).show();
                    User.getInstance().setFirstname(firstname);
                    User.getInstance().setLastname(lastname);
                    Intent intent = new Intent(changeInfor.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(changeInfor.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aler = new AlertDialog.Builder(changeInfor.this);
                aler.setTitle("Notification!");
                aler.setMessage("Are you sure to delete this account?");
                aler.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHandler.getInstance(changeInfor.this).deleteUser();
                        User.getInstance().destroy();
                        Toast.makeText(changeInfor.this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(changeInfor.this, LogInActivity.class);
                        startActivity(intent);
                    }
                });
                aler.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                aler.show();
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
