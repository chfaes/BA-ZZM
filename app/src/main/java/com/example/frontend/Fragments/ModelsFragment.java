package com.example.frontend.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.example.frontend.R;

public class ModelsFragment extends Fragment {
    private int patientId;
    private WebView modelView;
    private String currentUrl;
    private ImageView ivKauen;
    private ImageView ivBruxismus;
    private ImageView ivNervensystem;
    private ImageView ivNeuropathie;
    private ImageView ivMyelinscheide;
    private ImageView ivMigraene;
    private ImageView ivQuerschnitt;
    private ImageView ivSensitiv;
    private ImageView ivErosion;
    private ImageView ivHoehlen;
    private Button btnAnomalous;
    private int currentModel;
    private boolean firstModelSelected = false;

    private View.OnClickListener onClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        patientId = getArguments().getInt("patientId");
        View v = inflater.inflate(R.layout.fragment_models, container, false);
        modelView = (WebView) v.findViewById(R.id.modelView);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.iv_kauen:
                        deselectImageView(currentModel);
                        currentModel = R.id.iv_kauen;
                        selectImageView(currentModel);
                        currentUrl = "https://human.biodigital.com/widget/?be=2vwf&ui-annotations=true&ui-info=true&ui-zoom=true&ui-share=true&uaid=4Aty9&lang=de";
                        break;
                    case R.id.iv_bruxismus:
                        deselectImageView(currentModel);
                        currentModel = R.id.iv_bruxismus;
                        selectImageView(currentModel);
                        currentUrl = "https://human.biodigital.com/widget/?be=31sB&ui-tools=true&ui-dissect=true&ui-isolate=true&ui-xray=true&ui-cross-section=false&ui-annotations=true&ui-info=true&ui-zoom=true&ui-share=true&uaid=4B0RT&lang=de";
                        break;
                    case R.id.iv_nervensystem:
                        deselectImageView(currentModel);
                        currentModel = R.id.iv_nervensystem;
                        selectImageView(currentModel);
                        currentUrl = "https://human.biodigital.com/widget/?be=31sG&ui-tools=true&ui-dissect=true&ui-isolate=true&ui-xray=true&ui-cross-section=false&ui-annotations=true&ui-info=true&ui-zoom=true&ui-share=true&uaid=4B15y&lang=de";
                        break;
                    case R.id.iv_neuropathie:
                        deselectImageView(currentModel);
                        currentModel = R.id.iv_neuropathie;
                        selectImageView(currentModel);
                        currentUrl = "https://human.biodigital.com/widget/?be=31sN&ui-tools=true&ui-dissect=true&ui-isolate=true&ui-xray=true&ui-cross-section=false&ui-annotations=true&ui-info=true&ui-zoom=true&ui-share=true&uaid=4B18A&lang=de";
                        break;
                    case R.id.iv_myelinscheide:
                        deselectImageView(currentModel);
                        currentModel = R.id.iv_myelinscheide;
                        selectImageView(currentModel);
                        currentUrl = "https://human.biodigital.com/widget/?be=31sD&ui-tools=true&ui-dissect=true&ui-isolate=true&ui-xray=true&ui-cross-section=false&ui-annotations=true&ui-info=true&ui-zoom=true&ui-share=true&uaid=4B1iH&lang=de";
                        break;
                    case R.id.iv_migraene:
                        deselectImageView(currentModel);
                        currentModel = R.id.iv_migraene;
                        selectImageView(currentModel);
                        currentUrl = "https://human.biodigital.com/widget/?be=31sL&ui-tools=true&ui-dissect=true&ui-isolate=true&ui-xray=true&ui-cross-section=false&ui-annotations=true&ui-info=true&ui-zoom=true&ui-share=true&uaid=4B13t&lang=de";
                        break;
                    case R.id.iv_querschnitt:
                        deselectImageView(currentModel);
                        currentModel = R.id.iv_querschnitt;
                        selectImageView(currentModel);
                        currentUrl = "https://human.biodigital.com/widget/?be=31qc&ui-tools=true&ui-dissect=true&ui-isolate=true&ui-xray=true&ui-cross-section=false&ui-annotations=true&ui-info=true&ui-zoom=true&ui-share=true&uaid=4B1cA&lang=de";
                        break;
                    case R.id.iv_sensitiv:
                        deselectImageView(currentModel);
                        currentModel = R.id.iv_sensitiv;
                        selectImageView(currentModel);
                        currentUrl = "https://human.biodigital.com/widget/?be=31sV&ui-tools=true&ui-dissect=true&ui-isolate=true&ui-xray=true&ui-cross-section=false&ui-annotations=true&ui-info=true&ui-zoom=true&ui-share=true&uaid=4B1Yj&lang=de";
                        break;
                    case R.id.iv_erosion:
                        deselectImageView(currentModel);
                        currentModel = R.id.iv_erosion;
                        selectImageView(currentModel);
                        currentUrl = "https://human.biodigital.com/widget/?be=31sR&ui-tools=true&ui-dissect=true&ui-isolate=true&ui-xray=true&ui-cross-section=false&ui-annotations=true&ui-info=true&ui-zoom=true&ui-share=true&uaid=4B1U2&lang=de";
                        break;
                    case R.id.iv_hoehlen:
                        deselectImageView(currentModel);
                        currentModel = R.id.iv_hoehlen;
                        selectImageView(currentModel);
                        currentUrl = "https://human.biodigital.com/widget/?be=31sW&ui-tools=true&ui-dissect=true&ui-isolate=true&ui-xray=true&ui-cross-section=false&ui-annotations=true&ui-info=true&ui-zoom=true&ui-share=true&uaid=4B1dV&lang=de";
                        break;
                    }
                modelView.loadUrl(currentUrl);
                // Enable Javascript
                WebSettings webSettings = modelView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setDomStorageEnabled(true);

                // Force links and redirects to open in the WebView instead of in a browser
                modelView.setWebViewClient(new WebViewClient());
            }
        };
        return v;
    }

    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        btnAnomalous = (Button) view.findViewById(R.id.btnAnomalous);
        btnAnomalous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getActivity()
                        .getPackageManager()
                        .getLaunchIntentForPackage("com.anomalousmedical.android");
                startActivity(i);
            }
        });

        ivKauen = view.findViewById(R.id.iv_kauen);
        ivKauen.setOnClickListener(onClickListener);
        ivBruxismus = view.findViewById(R.id.iv_bruxismus);
        ivBruxismus.setOnClickListener(onClickListener);

        ivNervensystem = view.findViewById(R.id.iv_nervensystem);
        ivNervensystem.setOnClickListener(onClickListener);
        ivNeuropathie = view.findViewById(R.id.iv_neuropathie);
        ivNeuropathie.setOnClickListener(onClickListener);
        ivMyelinscheide = view.findViewById(R.id.iv_myelinscheide);
        ivMyelinscheide.setOnClickListener(onClickListener);
        ivMigraene = view.findViewById(R.id.iv_migraene);
        ivMigraene.setOnClickListener(onClickListener);

        ivQuerschnitt = view.findViewById(R.id.iv_querschnitt);
        ivQuerschnitt.setOnClickListener(onClickListener);
        ivSensitiv = view.findViewById(R.id.iv_sensitiv);
        ivSensitiv.setOnClickListener(onClickListener);
        ivErosion = view.findViewById(R.id.iv_erosion);
        ivErosion.setOnClickListener(onClickListener);
        ivHoehlen = view.findViewById(R.id.iv_hoehlen);
        ivHoehlen.setOnClickListener(onClickListener);
    }

    public void deselectImageView(int imageViewId){
        if(firstModelSelected){
            ImageView imageView = (ImageView)getView().findViewById(imageViewId);
            imageView.setSelected(false);
            imageView.setBackgroundColor(0);
        }
    }

    public void selectImageView(int imageViewId){
        firstModelSelected = true;
        ImageView imageView = (ImageView)getView().findViewById(imageViewId);
        imageView.setSelected(true);
        imageView.setBackgroundColor(getResources().getColor(R.color.colorBlue));
    }
}
