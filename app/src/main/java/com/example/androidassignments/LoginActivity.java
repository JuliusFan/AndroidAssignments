package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText textLogin = findViewById(R.id.editText);
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String defaultEmail = sharedPref.getString(getString(R.string.LoginName),"email@domain.com");
        textLogin.setText(defaultEmail);

        final Button buttonLogin = findViewById(R.id.button);
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.LoginName),textLogin.getText().toString());
                editor.apply();
                Intent intent = new Intent(LoginActivity.this,StartActivity.class);
                startActivity(intent);
            }
        });
    }

    //@Override
    public void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume()");
    }

    public void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME,"In onStart()");

    }

    public void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause()");
    }

    public void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStop()");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }
}
