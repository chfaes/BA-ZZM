package com.example.frontend.Fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frontend.Activities.MenuActivity;
import com.example.frontend.Globals;
import com.example.frontend.R;

import java.io.OutputStream;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class TranslatorFragment extends Fragment {

    private int patientId;
    private Button btnGoogleTranslate;
    private Button btnEnglish;
    private Button btnGerman;
    private Button btnFrench;
    private Button btnRussian;
    private String defaultLang = Locale.getDefault().getLanguage();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        patientId = getArguments().getInt("patientId");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_translator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnGoogleTranslate = (Button) view.findViewById(R.id.btnGoogleTranslate);
        btnGoogleTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.apps.translate");
                startActivity(i);
            }
        });
        btnGerman = (Button) view.findViewById(R.id.btnLangGer);
        btnGerman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.getInstance().setLanguage("de");
                setApplicationLanguage("de");
            }
        });
        btnEnglish = (Button) view.findViewById(R.id.btnLangEn);
        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.getInstance().setLanguage("en");
                setApplicationLanguage("en");
            }
        });
        btnFrench = (Button) view.findViewById(R.id.btnLangFr);
        btnFrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.getInstance().setLanguage("fr");
                setApplicationLanguage("fr");
            }
        });
        btnRussian = (Button) view.findViewById(R.id.btnLangRu);
        btnRussian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.getInstance().setLanguage("ru");
                setApplicationLanguage("ru");
            }
        });
        markSelectedLanguage();


    }

    private void markSelectedLanguage() {
        Globals g = Globals.getInstance();
        String lang = g.getLanguage();
        if (lang.equals("de")){
            btnGerman.setSelected(true);
            btnGerman.setTextColor(Color.WHITE);
        }else if (lang.equals("en")){
            btnEnglish.setSelected(true);
            btnEnglish.setTextColor(Color.WHITE);
        } else if (lang.equals("fr")) {
            btnFrench.setSelected(true);
            btnFrench.setTextColor(Color.WHITE);
        } else if (lang.equals("ru")) {
            btnRussian.setSelected(true);
            btnRussian.setTextColor(Color.WHITE);
        }
    }

    public void setApplicationLanguage(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics display = res.getDisplayMetrics();
        Configuration configuration = res.getConfiguration();
        configuration.locale = myLocale;
        res.updateConfiguration(configuration, display);
        Intent refresh = new Intent(getActivity(), MenuActivity.class);
        Globals g = Globals.getInstance();
        refresh.putExtra("patient", g.getPatient());
        this.startActivity(refresh);

    }
}

