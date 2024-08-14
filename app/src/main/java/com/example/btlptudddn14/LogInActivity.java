package com.example.btlptudddn14;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlptudddn14.models.DBHandler;
import com.example.btlptudddn14.models.User;
import com.example.btlptudddn14.ui.settings.FogetPassActivity;

import java.util.Map;

public class LogInActivity extends AppCompatActivity {
    EditText etLoginUser, etLoginPass;
    TextView tvNavigateSignIn, tvForgetPass;
    Button btnLogin;
    CheckBox cbRememberMe;
    LinearLayout contain2;

    String tenThongTinDangNhap = "login";

    @Override
    protected void onPause() {
        super.onPause();
        saveLogInState();
    }
    public void saveLogInState()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(tenThongTinDangNhap, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", etLoginUser.getText().toString());
        editor.putString("password", etLoginPass.getText().toString());
        editor.putBoolean("save", cbRememberMe.isChecked());
        editor.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(tenThongTinDangNhap, MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        boolean save = sharedPreferences.getBoolean("save", false);
        if(save)
        {
            etLoginUser.setText(email);
            etLoginPass.setText(password);
            cbRememberMe.setChecked(save);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        // Ánh xạ
        etLoginUser = findViewById(R.id.etUserLogin);
        etLoginPass = findViewById(R.id.etPassWordLogin);
        btnLogin = findViewById(R.id.btnLogIn);
        tvNavigateSignIn = findViewById(R.id.tvNavigateSignIn);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        tvForgetPass = findViewById(R.id.tvForgetPass);
        contain2 = findViewById(R.id.LLogIn);

        contain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });
        // Handle event navigate home screen
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etLoginUser.getText().toString();
                String password = etLoginPass.getText().toString();
                DBHandler dbHandler = DBHandler.getInstance(LogInActivity.this);
                Map<String, String> userData = dbHandler.checkLogin(email, User.getInstance().hashPassword(password));

                if (userData != null) {
                    int id = Integer.parseInt(userData.get("id"));
                    String firstname = userData.get("firstname");
                    String lastname = userData.get("lastname");
                    String userEmail = userData.get("email");
                    String userPassword = userData.get("password");

                    User.getInstance().initialize(id, firstname, lastname, userEmail, userPassword);

                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LogInActivity.this, "Wrong email or password!", Toast.LENGTH_SHORT).show();
                }
            }

        });
        // Handle event navigate sign up
        tvNavigateSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, signUpActivity.class);
                startActivity(intent);
            }
        });

        // Handle forget password
        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, FogetPassActivity.class);
                startActivity(intent);
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