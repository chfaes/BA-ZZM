package com.example.frontend.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;

import org.rajawali3d.loader.IParser;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

import static org.flywaydb.core.api.android.ContextHolder.getContext;


public class TestActivity extends AppCompatActivity {

    //Renderer renderer;
    private TextView txt_coord;
    private Button testBtn1;
    private Button testBtn2;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
            testBtn1 = (Button) findViewById(R.id.button1);
            testBtn2 = (Button) findViewById(R.id.button2);
            txt_coord = (TextView) this.findViewById(R.id.activity_test_coord_text);
            db = new DatabaseHelper(this);

            testBtn2.setOnClickListener(new View.OnClickListener() {
                //Set data
                @Override
                public void onClick(View view) {
                    db.addPain(5,20200428, "asdfqwerasdfqwerpoiu");
                    db.addPain(5,20200628, "w88w88w88w88");
                    txt_coord.setText("Zitrone: Habe Daten gesetzt.");
                }
            });

            testBtn1.setOnClickListener(new View.OnClickListener() {
                //Load data
                @Override
                public void onClick(View view) {
                    txt_coord.setText(db.getPainListEncoded(5).get(1));
                }
            });
    };
}





