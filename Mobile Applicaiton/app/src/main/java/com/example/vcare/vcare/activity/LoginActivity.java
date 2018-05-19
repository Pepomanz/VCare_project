package com.example.vcare.vcare.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vcare.vcare.R;
import com.example.vcare.vcare.dao.LoginDetail;
import com.example.vcare.vcare.dao.UserDetail;
import com.example.vcare.vcare.http.NetworkConnectManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogIn,btnSignUp;
    private EditText etUsername,etPassword;
    private String username,password,sessionId;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    int check = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instantitate();
        //checkSession();
    }

    private void checkSession() {
        username = shared.getString("username","null");
        password = shared.getString("password","null");
        if(!(username.equals(null) && password.equals(null)))
        {
            GotoMainPage();
        }
    }

    private void GotoMainPage() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    private void instantitate() {
        btnLogIn = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        etUsername = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());
        shared = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor = shared.edit();
        btnLogIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnLogIn)
        {
            username = etUsername.getText().toString();
            password = etPassword.getText().toString();
            LoginDetail temp = new LoginDetail(username,password);
            Call<String> call = NetworkConnectManager.getInstance().getService().logIn(temp);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()) {
                        sessionId = response.body().toString();
                       /* UserDetail temp = response.body();
                        editor.putString("name",temp.getfName()+" "+temp.getlName());
                        editor.putString("year",temp.getYear());
                        editor.putString("tel",temp.getTel());
                        editor.putString("email",temp.getEmail());
                        editor.putString("brand",temp.getBrand());
                        editor.putString("model",temp.getModel());
                        editor.putString("picture",temp.getPicture());
                        editor.putString("mileage",temp.getSensorId49());
                        editor.putString("engineload",temp.getSensorId4());
                        editor.putString("battery",temp.getSensorId66());
                        editor.commit();*/
                        //res = temp.getResult();
                        if(!sessionId.equals("null"))
                        {
                            editor.putString("username",username);
                            editor.putString("password",password);
                            editor.putString("sessionId",sessionId);
                            GotoMainPage();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Username or Password Incorrect",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Log.d("CheckLogin","Error");
                        Toast.makeText(getApplicationContext(),response.errorBody().toString(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Connection Fail",Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(v == btnSignUp)
        {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
        }
    }
}
