package com.example.btlptudddn14.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.btlptudddn14.LogInActivity;
import com.example.btlptudddn14.R;
import com.example.btlptudddn14.models.DBHandler;
import com.example.btlptudddn14.models.User;
import com.example.btlptudddn14.signUpActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FogetPassActivity extends AppCompatActivity {
    EditText etFPFirstName, etFPLastName, etFPEmail, etFPNewPass, etFPConNewPass;
    Button btnUpdateNewPass, btnCancleNewpass;
    LinearLayout contain3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foget_pass);
        etFPFirstName = findViewById(R.id.etForPassFName);
        etFPLastName = findViewById(R.id.etForPassLName);
        etFPEmail = findViewById(R.id.etForPassEmail);
        etFPNewPass = findViewById(R.id.etForPassNewPass);
        etFPConNewPass = findViewById(R.id.etForPassConNewPass);
        contain3 = findViewById(R.id.LLForgetPass);
        btnUpdateNewPass = findViewById(R.id.btnUpdateForPass);
        btnCancleNewpass = findViewById(R.id.btnCancelForPass);

        contain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });
        btnCancleNewpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnUpdateNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = etFPNewPass.getText().toString();
                String confNewPass = etFPConNewPass.getText().toString();
                String firstname = etFPFirstName.getText().toString();
                String lastname = etFPLastName.getText().toString();
                String email = etFPEmail.getText().toString();

                if (TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lastname) ||
                        TextUtils.isEmpty(email) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confNewPass)) {
                    Toast.makeText(FogetPassActivity.this, "Please enter complete information!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPass.equals(confNewPass)) {
                    Toast.makeText(FogetPassActivity.this, "Password and confirm password do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check syntax password
                if(!(User.getInstance().isPasswordValid(newPass)))
                {
                    Toast.makeText(FogetPassActivity.this, "Password must be at least 5 characters, contain uppercase letters and numbers!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(DBHandler.getInstance(FogetPassActivity.this).checkInforForgetPass(firstname, lastname, email))
                {
                    if(DBHandler.getInstance(FogetPassActivity.this).updateForgetPassword(email, User.getInstance().hashPassword(newPass)))
                    {
                        Toast.makeText(FogetPassActivity.this, "Update password successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(FogetPassActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(FogetPassActivity.this, "Account information is incorrect!", Toast.LENGTH_SHORT).show();
                }
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