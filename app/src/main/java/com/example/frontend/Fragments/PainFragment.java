package com.example.frontend.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.Models.PainBeginning;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PainFragment extends Fragment {

    private int patientId;
    DatabaseHelper db;
    private ImageView ivLocationTeeth;
    //private ImageView ivLocationFaceLeft;
    private ImageView ivLocationFaceRight;
    private ImageView fragment_pain_png_base;
    private pl.droidsonroids.gif.GifImageView pain_gif_01;
    private pl.droidsonroids.gif.GifImageView pain_gif_02;
    private pl.droidsonroids.gif.GifImageView pain_gif_03;
    private pl.droidsonroids.gif.GifImageView pain_gif_04;
    private pl.droidsonroids.gif.GifImageView pain_gif_05;
    private pl.droidsonroids.gif.GifImageView pain_gif_06;
    private pl.droidsonroids.gif.GifImageView pain_gif_07;
    private pl.droidsonroids.gif.GifImageView pain_gif_08;
    private pl.droidsonroids.gif.GifImageView pain_gif_09;
    private pl.droidsonroids.gif.GifImageView pain_gif_10;
    private int openedLocationImage;
    private String addPainItem;
    RadioGroup rgBeginningCurrent;
    //RadioButton rbBeginning;
    //RadioButton rbCurrent;

    Map<String, String> RadioGroupMap = new HashMap<String, String>();
    String activeRadioButton = "";

    Button btnDull;
    Button btnPulling;
    Button btnPulsating;
    Button btnStinging;
    Button btnBurning;
    Button btnTingling;
    Button btnPinsandneedles;
    Button btnNumb;
    Button btnElectric;
    Button btnPressing;
    Button btnAddNewRB;
    Button btn3d;

    private ImageView ivPermSlightFluc;
    private ImageView ivPermStrongFluc;
    private ImageView ivAttFreeInt;
    private ImageView ivAttNoFreeInt;

    private EditText etComment;
    private EditText etPain_trigger;

    boolean firstTimeOpen = true;

    private PainBeginning painOfPatient = new PainBeginning();

    SeekBar seekBar;

    Bitmap alteredBitmap = Bitmap.createBitmap(450, 300, Bitmap.Config.ARGB_8888);
    Bitmap bmp = Bitmap.createBitmap(450, 300, Bitmap.Config.ARGB_8888);
    Canvas canvas;
    Paint paint;
    float downx = 0;
    float downy = 0;
    float upx = 0;
    float upy = 0;
    boolean init = true;
    Dialog myDialog;

    private View.OnClickListener onClickAddNewRB = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            addNewRB("-1", true);
            btnAddNewRB.setEnabled(false);
            btnAddNewRB.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnAddNewRB.setEnabled(true);
                }
            }, 5000);
        }
    };

    private View.OnClickListener onClickOpen3d = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            //This URL format inputs Username and Password automatically.
            //zzm.ifi.uzh.ch can be accessed manually any time, but username "webuser" and password
            //"Suach8qu" would then have to be entered manually as well.
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://webuser:Suach8qu@zzm.ifi.uzh.ch/"));

            startActivity(browserIntent);
        }
    };

    private View.OnClickListener onClickLocationImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivLocationTeeth:
                    bmp = ((BitmapDrawable) ivLocationTeeth.getDrawable()).getBitmap();
                    openedLocationImage = R.id.ivLocationTeeth;
                    showImagePopup();
                    break;
                /*case R.id.ivLocationFaceLeft:
                    bmp = ((BitmapDrawable) ivLocationFaceLeft.getDrawable()).getBitmap();
                    openedLocationImage = R.id.ivLocationFaceLeft;
                    showImagePopup();
                    break;*/
                case R.id.ivLocationFaceRight:
                    bmp = ((BitmapDrawable) ivLocationFaceRight.getDrawable()).getBitmap();
                    openedLocationImage = R.id.ivLocationFaceRight;
                    showImagePopup();
                    break;
                case R.id.fragment_pain_png_base:
                    bmp = ((BitmapDrawable) fragment_pain_png_base.getDrawable()).getBitmap();
                    openedLocationImage = R.id.fragment_pain_png_base;
                    showPainPopup();
                    break;

            }
            //bmp = resize(bmp, 400, 300);

        }
    };
    private Button.OnClickListener onQualityClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectDeselectView(view);
            switch (view.getId()) {
                case R.id.btnDull:
                    painOfPatient.setDull(!painOfPatient.isDull());
                    break;
                case R.id.btnPulling:
                    painOfPatient.setPulling(!painOfPatient.isPulling());
                    break;
                case R.id.btnPulsating:
                    painOfPatient.setPulsating(!painOfPatient.isPulsating());
                    break;
                case R.id.btnStinging:
                    painOfPatient.setStinging(!painOfPatient.isStinging());
                    break;
                case R.id.btnBurning:
                    painOfPatient.setBurning(!painOfPatient.isBurning());
                    break;
                case R.id.btnTingling:
                    painOfPatient.setTingling(!painOfPatient.isTingling());
                    break;
                case R.id.btnPinsandneedles:
                    painOfPatient.setPinsneedles(!painOfPatient.isPinsneedles());
                    break;
                case R.id.btnNumb:
                    painOfPatient.setNumb(!painOfPatient.isNumb());
                    break;
                case R.id.btnElectric:
                    painOfPatient.setElectric(!painOfPatient.isElectric());
                    break;
                case R.id.btnPressing:
                    painOfPatient.setPressing(!painOfPatient.isPressing());
                    break;
            }
            updatePainDisplay(painOfPatient);
        }
    };

    private ImageView.OnClickListener onPatternClickListener = new ImageView.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectPattern(view);
        }
    };

    @Override
    public void onDestroyView() {
        savePainBeginning();
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        patientId = getArguments().getInt("patientId");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pain, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());
        etComment = view.findViewById(R.id.etComment);
        etPain_trigger = view.findViewById(R.id.etCauses);
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                painOfPatient.setComment(etComment.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        etPain_trigger.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                painOfPatient.setPain_trigger(etPain_trigger.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        btnDull = view.findViewById(R.id.btnDull);
        btnPulling = view.findViewById(R.id.btnPulling);
        btnPulsating = view.findViewById(R.id.btnPulsating);
        btnStinging = view.findViewById(R.id.btnStinging);
        btnBurning = view.findViewById(R.id.btnBurning);
        btnTingling = view.findViewById(R.id.btnTingling);
        btnPinsandneedles = view.findViewById(R.id.btnPinsandneedles);
        btnNumb = view.findViewById(R.id.btnNumb);
        btnElectric = view.findViewById(R.id.btnElectric);
        btnPressing = view.findViewById(R.id.btnPressing);
        btnAddNewRB = view.findViewById(R.id.BtnAddNew);
        btn3d = view.findViewById(R.id.Btn3d);
        pain_gif_01 = view.findViewById(R.id.fragment_pain_gif01);
        pain_gif_02 = view.findViewById(R.id.fragment_pain_gif02);
        pain_gif_03 = view.findViewById(R.id.fragment_pain_gif03);
        pain_gif_04 = view.findViewById(R.id.fragment_pain_gif04);
        pain_gif_05 = view.findViewById(R.id.fragment_pain_gif05);
        pain_gif_06 = view.findViewById(R.id.fragment_pain_gif06);
        pain_gif_07 = view.findViewById(R.id.fragment_pain_gif07);
        pain_gif_08 = view.findViewById(R.id.fragment_pain_gif08);
        pain_gif_09 = view.findViewById(R.id.fragment_pain_gif09);
        pain_gif_10 = view.findViewById(R.id.fragment_pain_gif10);

        btnDull.setOnClickListener(onQualityClickListener);
        btnPulling.setOnClickListener(onQualityClickListener);
        btnPulsating.setOnClickListener(onQualityClickListener);
        btnStinging.setOnClickListener(onQualityClickListener);
        btnBurning.setOnClickListener(onQualityClickListener);
        btnTingling.setOnClickListener(onQualityClickListener);
        btnPinsandneedles.setOnClickListener(onQualityClickListener);
        btnNumb.setOnClickListener(onQualityClickListener);
        btnElectric.setOnClickListener(onQualityClickListener);
        btnPressing.setOnClickListener(onQualityClickListener);

        btnAddNewRB.setOnClickListener(onClickAddNewRB);
        btn3d.setOnClickListener(onClickOpen3d);

        ivPermSlightFluc = view.findViewById(R.id.btnPermSlightFluc);
        ivPermStrongFluc = view.findViewById(R.id.btnPermStrongFluc);
        ivAttFreeInt = view.findViewById(R.id.btnAttFreeInt);
        ivAttNoFreeInt = view.findViewById(R.id.btnAttNoFreeInt);

        ivPermSlightFluc.setOnClickListener(onPatternClickListener);
        ivPermStrongFluc.setOnClickListener(onPatternClickListener);
        ivAttFreeInt.setOnClickListener(onPatternClickListener);
        ivAttNoFreeInt.setOnClickListener(onPatternClickListener);

        ivLocationTeeth = view.findViewById(R.id.ivLocationTeeth);
        ivLocationTeeth.setOnClickListener(onClickLocationImage);
        /*ivLocationFaceLeft = view.findViewById(R.id.ivLocationFaceLeft);
        ivLocationFaceLeft.setOnClickListener(onClickLocationImage);*/
        ivLocationFaceRight = view.findViewById(R.id.ivLocationFaceRight);
        ivLocationFaceRight.setOnClickListener(onClickLocationImage);
        fragment_pain_png_base = view.findViewById(R.id.fragment_pain_png_base);
        fragment_pain_png_base.setOnClickListener(onClickLocationImage);
        myDialog = new Dialog(getActivity());
        myDialog.setCanceledOnTouchOutside(false);
        addPainItem = "none";

        seekBar = (SeekBar) view.findViewById(R.id.sbIntensity);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TextView t = (TextView) view.findViewById(R.id.tvIntensity);
                t.setText(String.valueOf(i));
                painOfPatient.setIntensity(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rgBeginningCurrent = view.findViewById(R.id.rgBeginningCurrent);
        rgBeginningCurrent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(!firstTimeOpen){
                    savePainBeginning();
                }
                setUpSelectedViews();
            }
        });

        initializeFragment();
        firstTimeOpen = false;

        initializePainsOfPatient();
        setUpSelectedViews();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        //Get Camera capture
        if(requestCode == 100){
            try {
                painOfPatient.setPhoto((Bitmap) data.getExtras().get("data"));
                ImageView Photography = myDialog.findViewById(R.id.painPhoto);
                Photography.setImageBitmap(painOfPatient.getPhoto());
                Photography.setVisibility(View.VISIBLE);
                savePainBeginning();
            } catch(Exception e) {
                //Photo cancelled.
                Log.d("Log", "Cancelled Photography. See PainFragment.java.");
            }
        }
    }

    private void updatePainDisplay(PainBeginning painObject){
        //Updates the gifs to match the current selection of pain qualities. Note that
        //there is no distinction between "pins and needles" and "tingling" (both equal
        //to gif06).
        pain_gif_01.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_02.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_03.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_04.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_05.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_06.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_07.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_08.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_09.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_10.setImageResource(R.drawable.pain_gif_empty);
        if (painObject.isPulsating()) {
            pain_gif_01.setImageResource(R.drawable.pain_gif_01);
        }
        if (painObject.isPulling()) {
            pain_gif_02.setImageResource(R.drawable.pain_gif_02);
        }
        if (painObject.isDull()) {
            pain_gif_03.setImageResource(R.drawable.pain_gif_03);
        }
        if (painObject.isStinging()) {
            pain_gif_04.setImageResource(R.drawable.pain_gif_04);
        }
        if (painObject.isBurning()) {
            pain_gif_05.setImageResource(R.drawable.pain_gif_05);
        }
        if (painObject.isTingling()) {
            pain_gif_06.setImageResource(R.drawable.pain_gif_06);
        }
        if (painObject.isPinsneedles()) {
            pain_gif_07.setImageResource(R.drawable.pain_gif_07_small);
        }
        if (painObject.isNumb()) {
            pain_gif_08.setImageResource(R.drawable.pain_gif_08);
        }
        if (painObject.isElectric()) {
            pain_gif_09.setImageResource(R.drawable.pain_gif_09);
        }
        if (painObject.isPressing()) {
            pain_gif_10.setImageResource(R.drawable.pain_gif_10);
        }

    }

    private void initializeFragment(){
        if (!db.existsPain(patientId)){
            // If the patient has no database entries: add a new pain object (encoded) to the DB.
            db.addPain(patientId, getDateAndTime(), encodeInstance(initializePainInstance(new PainBeginning())));
        }

        ArrayList<String> temp_list = db.getPainDates(patientId);

        for (int i = 0; i < temp_list.size(); i++) {
            //create Radiobuttons from all existing dates of this patient.
            addNewRB(temp_list.get(i), false);
        }

        //Check the first Radiobutton in the group by default. Set the string "activeRadioButton"
        //accordingly.
        rgBeginningCurrent.check(rgBeginningCurrent.getChildAt(0).getId());
        activeRadioButton = RadioGroupMap.get(String.valueOf(rgBeginningCurrent.getCheckedRadioButtonId()));

    }

    private void setUpSelectedViews() {
        // Gets the currently checked Radiobutton and fills in the view with the contents
        // from the respective database entry.
        activeRadioButton = RadioGroupMap.get(String.valueOf(rgBeginningCurrent.getCheckedRadioButtonId()));
        String str = db.getPainEncoded(patientId, activeRadioButton);
        PainBeginning pain = decodePainFromString(str);
        painOfPatient = pain;
        setUpAllViewsBeginning();
    }

    private void initializePainsOfPatient() {
        painOfPatient.setPatient_id(patientId);

        painOfPatient.setIntensity(0);

        Bitmap bmpTeeth = BitmapFactory.decodeResource(getResources(), R.drawable.teeth);
        Bitmap bmpFaceLeft = BitmapFactory.decodeResource(getResources(), R.drawable.face_left);
        Bitmap bmpFaceRight = BitmapFactory.decodeResource(getResources(), R.drawable.face_right);
        painOfPatient.setLocation_teeth(bitmapToByte(bmpTeeth));
        painOfPatient.setLocation_face_left(bitmapToByte(bmpFaceLeft));
        painOfPatient.setLocation_face_right(bitmapToByte(bmpFaceRight));

        painOfPatient.setPain_pattern(null);

        painOfPatient.setDull(false);
        painOfPatient.setPulling(false);
        painOfPatient.setStinging(false);
        painOfPatient.setPulsating(false);
        painOfPatient.setBurning(false);
        painOfPatient.setPinsneedles(false);
        painOfPatient.setTingling(false);
        painOfPatient.setNumb(false);
        painOfPatient.setElectric(false);
        painOfPatient.setPressing(false);
        painOfPatient.setComment("");
        painOfPatient.setPain_trigger("");
    }

    public byte[] bitmapToByte(Bitmap drawing) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        drawing.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] drawingByteArray = bos.toByteArray();
        return drawingByteArray;
    }

    public void setUpCanvas(final ImageView imageView) {
        if (init) {
            paint = new Paint();
            paint.setColor(getResources().getColor(R.color.colorDarkBlue));
            paint.setStrokeWidth(5);
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setXfermode(null);
            paint.setAlpha(0xff);
            init = false;
        }
        alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        canvas = new Canvas(alteredBitmap);
        Matrix matrix = new Matrix();
        canvas.drawBitmap(bmp, matrix, paint);
        imageView.setImageBitmap(alteredBitmap);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        downx = event.getX();
                        downy = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        upx = event.getX();
                        upy = event.getY();
                        canvas.drawLine(downx, downy, upx, upy, paint);
                        imageView.invalidate();
                        downx = upx;
                        downy = upy;
                        break;
                    case MotionEvent.ACTION_UP:
                        upx = event.getX();
                        upy = event.getY();
                        canvas.drawLine(downx, downy, upx, upy, paint);
                        imageView.invalidate();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void showImagePopup() {
        myDialog.setContentView(R.layout.popup_image_drawable);

        Button btnCancel;
        Button btnSave;
        ImageView btnDelete;
        final ImageView imageView = (ImageView) myDialog.findViewById(R.id.ivDisplay);
        btnDelete = (ImageView) myDialog.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (openedLocationImage) {
                    case R.id.ivLocationTeeth:
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.teeth);
                        break;
                    /*case R.id.ivLocationFaceLeft:
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.face_left);
                        break;*/
                    case R.id.ivLocationFaceRight:
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.face_right);
                        break;
                    case R.id.fragment_pain_png_base:
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pain_base_skull);
                        break;
                }
                //bmp = resize(bmp, 400, 275);
                setUpCanvas(imageView);
            }
        });
        btnCancel = (Button) myDialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        btnSave = (Button) myDialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (openedLocationImage) {
                    case R.id.ivLocationTeeth:
                        ivLocationTeeth.setImageBitmap(alteredBitmap);
                        painOfPatient.setLocation_teeth(bitmapToByte(alteredBitmap));
                        break;
                    /*case R.id.ivLocationFaceLeft:
                        ivLocationFaceLeft.setImageBitmap(alteredBitmap);
                        painOfPatient.setLocation_face_left(bitmapToByte(alteredBitmap));
                        break;*/
                    case R.id.ivLocationFaceRight:
                        ivLocationFaceRight.setImageBitmap(alteredBitmap);
                        painOfPatient.setLocation_face_right(bitmapToByte(alteredBitmap));
                        break;
                }
                myDialog.dismiss();
            }
        });
        //bmp = resize(bmp, 400, 300);
        setUpCanvas(imageView);
        myDialog.show();
    }

    private void showPainPopup() {
        //This function expands the picture of the human skull with all the pain animations;
        //it also sets up Cancel, Save and the Popup Menu for pain selection.
        //Once an Item from Popup Menu is selected, "addPainItem" is set to the item title
        //and any click on the surface will set the coordinates for the selected pain item.
        //Note that "both_pain_types" is used to have a simple way of accessing both beginning and current pain.

        //Ask Camera permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 100);
        }

        //Set all gifs, create gif array
        myDialog.setContentView(R.layout.popup_image_pain_positions);
        pl.droidsonroids.gif.GifImageView popup_gif_01 = myDialog.findViewById(R.id.popup_pain_gif01);
        pl.droidsonroids.gif.GifImageView popup_gif_02 = myDialog.findViewById(R.id.popup_pain_gif02);
        pl.droidsonroids.gif.GifImageView popup_gif_03 = myDialog.findViewById(R.id.popup_pain_gif03);
        pl.droidsonroids.gif.GifImageView popup_gif_04 = myDialog.findViewById(R.id.popup_pain_gif04);
        pl.droidsonroids.gif.GifImageView popup_gif_05 = myDialog.findViewById(R.id.popup_pain_gif05);
        pl.droidsonroids.gif.GifImageView popup_gif_06 = myDialog.findViewById(R.id.popup_pain_gif06);
        pl.droidsonroids.gif.GifImageView popup_gif_07 = myDialog.findViewById(R.id.popup_pain_gif07);
        pl.droidsonroids.gif.GifImageView popup_gif_08 = myDialog.findViewById(R.id.popup_pain_gif08);
        pl.droidsonroids.gif.GifImageView popup_gif_09 = myDialog.findViewById(R.id.popup_pain_gif09);
        pl.droidsonroids.gif.GifImageView popup_gif_10 = myDialog.findViewById(R.id.popup_pain_gif10);
        ArrayList<pl.droidsonroids.gif.GifImageView> popup_gif_list = new ArrayList<>(Arrays.asList(
                popup_gif_01, popup_gif_02, popup_gif_03, popup_gif_04, popup_gif_05, popup_gif_06,
                popup_gif_07, popup_gif_08, popup_gif_09, popup_gif_10));
        addPainItem = "none";
        //Initialize Buttons
        SeekBar popupSeekBar;
        SeekBar popupSeekBar2;
        TextView btnSave;
        TextView btnAddPain;
        TextView btnRemovePain;
        Button btnFoto;
        ImageView Photography;
        popupSeekBar = myDialog.findViewById(R.id.popupSeekBar);
        popupSeekBar2 = myDialog.findViewById(R.id.popupSeekBar2);
        btnSave = myDialog.findViewById(R.id.btnSave);
        btnAddPain = myDialog.findViewById(R.id.btnAddPain);
        btnRemovePain = myDialog.findViewById(R.id.btnRemovePain);
        btnFoto = myDialog.findViewById(R.id.btnTakePicture);
        Photography = myDialog.findViewById(R.id.painPhoto);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        btnAddPain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.pain_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(getActivity(), menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        addPainItem = menuItem.getTitle().toString();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        btnRemovePain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.pain_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        painOfPatient.deletePainCoordinates(menuItem.getTitle().toString());
                        Toast.makeText(getActivity(), menuItem.getTitle().toString() + " was deleted.", Toast.LENGTH_SHORT).show();
                        myDialog.dismiss();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        //Camera Button
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        //Surface Touch logic
        myDialog.findViewById(R.id.ivDisplay).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch(action){
                    case(MotionEvent.ACTION_DOWN):
                        // Touching the screen sets new coordinates for the selected pain.
                        if (!addPainItem.equals("none")){
                            painOfPatient.setPainCoordinates(motionEvent.getX(),
                                                            motionEvent.getY(),
                                                            (float) popupSeekBar.getProgress(),
                                                            (float) popupSeekBar2.getProgress(),
                                                            addPainItem);
                            updatePainPopup(
                                    popup_gif_list.get(painOfPatient.getPainList().indexOf(addPainItem)),
                                    motionEvent.getX(),
                                    motionEvent.getY(),
                                    (float) popupSeekBar.getProgress(),
                                    (float) popupSeekBar2.getProgress(),
                                    addPainItem,
                                    painOfPatient);

                            addPainItem = "none";
                        }
                }
                return true;
            }
        });

        // Update all view coordinates before opening the popup; update Photography if exists.
        ArrayList temp = painOfPatient.getPainList();
        for (int i = 0; i < temp.size(); i++) {
            float x = Float.parseFloat(painOfPatient.getPainCoordinates(temp.get(i).toString()).get(0).toString());
            float y = Float.parseFloat(painOfPatient.getPainCoordinates(temp.get(i).toString()).get(1).toString());
            float z = Float.parseFloat(painOfPatient.getPainCoordinates(temp.get(i).toString()).get(2).toString());
            float t = Float.parseFloat(painOfPatient.getPainCoordinates(temp.get(i).toString()).get(3).toString());
            updatePainPopup(popup_gif_list.get(i), x, y, z, t, temp.get(i).toString(), painOfPatient);
        }
        if (painOfPatient.existsPhoto()){
            Photography.setImageBitmap(painOfPatient.getPhoto());
            Photography.setVisibility(View.VISIBLE);
        }

        myDialog.show();
    }

    private void updatePainPopup(pl.droidsonroids.gif.GifImageView view, float x, float y, float z, float t, String name, PainBeginning pain_type){
        //Resets the Coordinates of a gif view. If a coordinate is = -1.0, it will by default not be updated.
        //If the pain does not exist yet, the gif is set to invisible.
        //80 and 160 is arbitrary, it adjusts the gif's position a bit to the upper left.
        //The z-Value is the scaling of the gif. The t-value corresponds to the transparency (alpha) value.
        //Both z and t are expected to be in the range of 1-100.
        view.setScaleX((z/33) + 0.5f);
        view.setScaleY((z/33) + 0.5f);
        view.setAlpha((t * 0.008f) + 0.2f);
        if (pain_type.painIsSet(name)){
            view.setVisibility(View.VISIBLE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(Math.round(x)-160,Math.round(y)-80,0,0);
            view.setLayoutParams(params);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public void savePainBeginning() {
        db.updatePain(patientId, activeRadioButton, encodeInstance(painOfPatient));
    }

    private void setUpAllViewsBeginning() {
        Bitmap bmTeeth = BitmapFactory.decodeByteArray(painOfPatient.getLocation_teeth(), 0, painOfPatient.getLocation_teeth().length);
        //Bitmap bmFaceLeft = BitmapFactory.decodeByteArray(painOfPatient.getLocation_face_left(), 0, painOfPatient.getLocation_face_left().length);
        Bitmap bmFaceRight = BitmapFactory.decodeByteArray(painOfPatient.getLocation_face_right(), 0, painOfPatient.getLocation_face_right().length);
        ivLocationTeeth.setImageBitmap(bmTeeth);
        //ivLocationFaceLeft.setImageBitmap(bmFaceLeft);
        ivLocationFaceRight.setImageBitmap(bmFaceRight);
        seekBar.setProgress(painOfPatient.getIntensity());
        etComment.setText(painOfPatient.getComment());
        etPain_trigger.setText(painOfPatient.getPain_trigger());

        deselectPattern();
        if (painOfPatient.getPain_pattern() != null && !painOfPatient.getPain_pattern().isEmpty()) {
            switch (painOfPatient.getPain_pattern()) {
                case "PermSlightFluc":
                    selectPattern(ivPermSlightFluc);
                    break;
                case "PermStrongFluc":
                    selectPattern(ivPermStrongFluc);
                    break;
                case "AttFreeInt":
                    selectPattern(ivAttFreeInt);
                    break;
                case "AttNoFreeInt":
                    selectPattern(ivAttNoFreeInt);
                    break;
            }
        }

        deselectAllQualityButtons();
        if (painOfPatient.isDull()) {
            selectDeselectView(btnDull);
        }
        if (painOfPatient.isPulling()) {
            selectDeselectView(btnPulling);
        }
        if (painOfPatient.isPulsating()) {
            selectDeselectView(btnPulsating);
        }
        if (painOfPatient.isPinsneedles()) {
            selectDeselectView(btnPinsandneedles);
        }
        if (painOfPatient.isTingling()) {
            selectDeselectView(btnTingling);
        }
        if (painOfPatient.isBurning()) {
            selectDeselectView(btnBurning);
        }
        if (painOfPatient.isStinging()) {
            selectDeselectView(btnStinging);
        }
        if (painOfPatient.isNumb()) {
            selectDeselectView(btnNumb);
        }
        if (painOfPatient.isElectric()) {
            selectDeselectView(btnElectric);
        }
        if (painOfPatient.isPressing()) {
            selectDeselectView(btnPressing);
        }
        updatePainDisplay(painOfPatient);
    }

    private void deselectAllQualityButtons() {
        btnDull.setSelected(false);
        btnBurning.setSelected(false);
        btnNumb.setSelected(false);
        btnPinsandneedles.setSelected(false);
        btnPulling.setSelected(false);
        btnPulsating.setSelected(false);
        btnStinging.setSelected(false);
        btnTingling.setSelected(false);
        btnElectric.setSelected(false);
        btnPressing.setSelected(false);

        btnDull.setTextColor(Color.BLACK);
        btnBurning.setTextColor(Color.BLACK);
        btnNumb.setTextColor(Color.BLACK);
        btnPinsandneedles.setTextColor(Color.BLACK);
        btnPulling.setTextColor(Color.BLACK);
        btnStinging.setTextColor(Color.BLACK);
        btnTingling.setTextColor(Color.BLACK);
        btnPulsating.setTextColor(Color.BLACK);
        btnElectric.setTextColor(Color.BLACK);
        btnPressing.setTextColor(Color.BLACK);

    }

    private void selectDeselectView(View view) {
        Button selectedBtn = (Button) view;
        if (view.isSelected()) {
            view.setSelected(false);
            selectedBtn.setTextColor(Color.BLACK);
        } else {
            view.setSelected(true);
            selectedBtn.setTextColor(Color.WHITE);
        }
    }

    private void deselectPattern() {
        ivPermSlightFluc.setBackgroundColor(getResources().getColor(R.color.white));
        ivPermStrongFluc.setBackgroundColor(getResources().getColor(R.color.white));
        ivAttFreeInt.setBackgroundColor(getResources().getColor(R.color.white));
        ivAttNoFreeInt.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void selectPattern(View view) {
        deselectPattern();
        view.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        switch (view.getId()) {
            case R.id.btnPermSlightFluc:
                painOfPatient.setPain_pattern("PermSlightFluc");
                break;
            case R.id.btnPermStrongFluc:
                painOfPatient.setPain_pattern("PermStrongFluc");
                break;
            case R.id.btnAttFreeInt:
                painOfPatient.setPain_pattern("AttFreeInt");
                break;
            case R.id.btnAttNoFreeInt:
                painOfPatient.setPain_pattern("AttNoFreeInt");
                break;
        }
    }

    public String getDateAndTime(){
        //Date and time including minutes and seconds, i.e. 2020-05-01-23-59-59, as int: 20200501235959
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return formatter.format(date);
    }

    private void addNewRB(String date, boolean addToDB){
        //adds a new RadioButton. Its tag and text are the date and time.
        //date is "optional": if "-1" is passed, it retrieves the current date and time.
        //addToDB: if set to true, a new PainObject with the respective date and time will be added
        //to the DB.
        if (date.equals("-1")) {
            date = getDateAndTime();
        }

        RadioButton rbn = new RadioButton(getContext());
        String temp_string = "  " + date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8) + " " +
                date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12,14) + "  ";
        rbn.setText(temp_string);
        rbn.setTag(date);
        rbn.setBackgroundResource(R.drawable.radiobutton_selector);
        rbn.setButtonDrawable(null);

        //Attach button to RadioGroup, adds key value pair to Map.
        rgBeginningCurrent.addView(rbn);
        RadioGroupMap.put(String.valueOf(rbn.getId()), date);

        if (addToDB){
            // Creates new Pain instance, calls method to initialize its values and
            // saves the encoded instance in DB.
            db.addPain(patientId, date, encodeInstance(initializePainInstance(new PainBeginning())));
        }
    }

    private PainBeginning initializePainInstance(PainBeginning pain){
        pain.setPatient_id(patientId);

        pain.setIntensity(0);

        Bitmap bmpTeeth = BitmapFactory.decodeResource(getResources(), R.drawable.teeth);
        Bitmap bmpFaceLeft = BitmapFactory.decodeResource(getResources(), R.drawable.face_left);
        Bitmap bmpFaceRight = BitmapFactory.decodeResource(getResources(), R.drawable.face_right);
        pain.setLocation_teeth(bitmapToByte(bmpTeeth));
        pain.setLocation_face_left(bitmapToByte(bmpFaceLeft));
        pain.setLocation_face_right(bitmapToByte(bmpFaceRight));

        pain.setPain_pattern(null);

        pain.setDull(false);
        pain.setPulling(false);
        pain.setStinging(false);
        pain.setPulsating(false);
        pain.setBurning(false);
        pain.setPinsneedles(false);
        pain.setTingling(false);
        pain.setNumb(false);
        pain.setElectric(false);
        pain.setPressing(false);
        pain.setComment("");
        pain.setPain_trigger("");

        return pain;
    }

    private PainBeginning decodePainFromString(String object){
        //updates "values" from "values_encoded"
        PainBeginning obj = null;
        try {
            byte[] b = Base64.decode(object, Base64.NO_WRAP);
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            obj = (PainBeginning) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return obj;
    }

    public String encodeInstance(PainBeginning object){
        String return_str = "";
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(object);
            so.flush();
            return_str = Base64.encodeToString(bo.toByteArray(), Base64.NO_WRAP);

        } catch (Exception e) {
            System.out.println(e);
        }
        return return_str;
    }
}
