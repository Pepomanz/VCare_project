package com.example.vcare.vcare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vcare.vcare.R;
import com.example.vcare.vcare.dao.UserDetail;
import com.example.vcare.vcare.http.NetworkConnectManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PepoManZ on 2/8/2018.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSignUpOK,btnSignUpBack;
    private EditText etSignUpUsername,etSignUpPassword,etSignUpEmail;
    private String username,password,email,reply;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        instantiate();
    }

    private void instantiate() {
        etSignUpUsername = (EditText) findViewById(R.id.etSignUpUserName);
        etSignUpEmail = (EditText) findViewById(R.id.etSignUpEmail);
        etSignUpPassword = (EditText) findViewById(R.id.etSignUpPass);
        btnSignUpBack = (Button) findViewById(R.id.btnSignUpBack);
        btnSignUpOK = (Button) findViewById(R.id.btnSignUpOk);
        btnSignUpOK.setOnClickListener(this);
        btnSignUpBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v == btnSignUpBack)
        {
            finish();
        }
        else if (v == btnSignUpOK)
        {
            username = etSignUpUsername.getText().toString();
            password = etSignUpPassword.getText().toString();
            email = etSignUpEmail.getText().toString();
            UserDetail user = new UserDetail(null,null,null,null,
                    null,username,password,email,null,null,null,
                    null,null,null,null,null,null,null);
            Call<String> call = NetworkConnectManager.getInstance().getService().register(user);
            call.enqueue(new Callback<String>() {
                @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            reply = response.body().toString();
                            if(reply.equals("Uploaded")){
                                Toast.makeText(RegisterActivity.this,"Registeration Completed",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                        else {
                            Log.d("Check Regis","Error");
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("Check Regis","Error");
                    }
                });
            }
            else
            {
                Toast.makeText(RegisterActivity.this,"Password is not the same",Toast.LENGTH_SHORT).show();
            }
        }
}
