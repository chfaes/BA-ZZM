package com.example.frontend.Activities;

import android.content.Intent;
import android.os.Bundle;
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

import org.rajawali3d.loader.IParser;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;



public class TestActivity extends AppCompatActivity {

    //Renderer renderer;
    private TextView txt_coord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        };

    public boolean onTouchEvent(MotionEvent event){
        txt_coord = (TextView) this.findViewById(R.id.activity_test_coord_text);
        int action = event.getAction();
        switch(action){
            case(MotionEvent.ACTION_DOWN):
                float x = event.getX();
                txt_coord.setText(Float.toString(x));
                Log.d("Log", "x-Achse:" + Float.toString(x));
        }
        return true;


    };


    /*
        final RajawaliSurfaceView surface = new RajawaliSurfaceView(this);
        surface.setFrameRate(60.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);

        // Add mSurface to your root view
        addContentView(surface, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));

        renderer = new Renderer(this);
        surface.setSurfaceRenderer(renderer);*/
    }





