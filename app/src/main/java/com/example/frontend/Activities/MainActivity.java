package com.example.frontend.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.frontend.Globals;
import com.example.frontend.Models.User;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;
import com.facebook.stetho.Stetho;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvAttemptsInfo;
    private TextView tvTest;
    private Button btnLogin;
    private int counter = 3;
    private List<User> userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // this.deleteDatabase("Pacons_DB");
        //To view database on Chrome: chrome://inspect/#devices -> inspect
        Stetho.initializeWithDefaults(this);

        db = new DatabaseHelper(this);
        db.createDefaultUsersIfNeed();
        userList =  db.getAllUsers();

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvAttemptsInfo = (TextView) findViewById(R.id.tvAttempts);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(etUsername.getText().toString(), etPassword.getText().toString());
            }
        });

        //jump to Menu for faster testing puroposes
        //Intent intent = new Intent(MainActivity.this, PatientSelectionActivity.class);
        //intent.putExtra("usernameKey", "Admin");
        //startActivity(intent);
    }


    private void validate(String username, String password) {
        boolean userFound = false;
        for(User user: userList){
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                Globals g = Globals.getInstance();
                g.setUser(user);
                Intent intent = new Intent(MainActivity.this, PatientSelectionActivity.class);
                intent.putExtra("usernameKey", etUsername.getText().toString());
                startActivity(intent);
            }else{
                userFound = false;
            }
        }
        if (!userFound){
            counter--;
            tvAttemptsInfo.setText("Anzahl übriger Versuche: " + counter);
            if (counter == 0) {
                btnLogin.setEnabled(false);
            }
        }
    }
}
