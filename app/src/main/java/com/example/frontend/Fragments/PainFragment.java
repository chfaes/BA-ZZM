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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.frontend.Models.PainCurrent;
import com.example.frontend.Models.PainSuperclass;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class PainFragment extends Fragment {

    private int patientId;
    DatabaseHelper db;
    private ImageView ivLocationTeeth;
    private ImageView ivLocationFaceLeft;
    private ImageView ivLocationFaceRight;
    private ImageView fragment_pain_png_base;
    private pl.droidsonroids.gif.GifImageView pain_gif_01;
    private pl.droidsonroids.gif.GifImageView pain_gif_02;
    private pl.droidsonroids.gif.GifImageView pain_gif_03;
    private pl.droidsonroids.gif.GifImageView pain_gif_04;
    private pl.droidsonroids.gif.GifImageView pain_gif_05;
    private pl.droidsonroids.gif.GifImageView pain_gif_06;
    private pl.droidsonroids.gif.GifImageView pain_gif_08;
    private int openedLocationImage;
    private String addPainItem;
    RadioGroup rgBeginningCurrent;
    RadioButton rbBeginning;
    RadioButton rbCurrent;

    Button btnDull;
    Button btnPulling;
    Button btnPulsating;
    Button btnStinging;
    Button btnBurning;
    Button btnTingling;
    Button btnPinsandneedles;
    Button btnNumb;

    private ImageView ivPermSlightFluc;
    private ImageView ivPermStrongFluc;
    private ImageView ivAttFreeInt;
    private ImageView ivAttNoFreeInt;

    private EditText etComment;

    boolean initialSetUpBeginningDone = false;
    boolean initialSetUpCurrentDone = false;
    boolean firstTimeOpen = true;

    private PainBeginning painOfPatientBeginning = new PainBeginning();
    private PainCurrent painOfPatientCurrent = new PainCurrent();

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
    private View.OnClickListener onClickLocationImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivLocationTeeth:
                    bmp = ((BitmapDrawable) ivLocationTeeth.getDrawable()).getBitmap();
                    openedLocationImage = R.id.ivLocationTeeth;
                    showImagePopup();
                    break;
                case R.id.ivLocationFaceLeft:
                    bmp = ((BitmapDrawable) ivLocationFaceLeft.getDrawable()).getBitmap();
                    openedLocationImage = R.id.ivLocationFaceLeft;
                    showImagePopup();
                    break;
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
            if (rgBeginningCurrent.getCheckedRadioButtonId() == R.id.rbBeginning) {
                switch (view.getId()) {
                    case R.id.btnDull:
                        painOfPatientBeginning.setDull(!painOfPatientBeginning.isDull());
                        break;
                    case R.id.btnPulling:
                        painOfPatientBeginning.setPulling(!painOfPatientBeginning.isPulling());
                        break;
                    case R.id.btnPulsating:
                        painOfPatientBeginning.setPulsating(!painOfPatientBeginning.isPulsating());
                        break;
                    case R.id.btnStinging:
                        painOfPatientBeginning.setStinging(!painOfPatientBeginning.isStinging());
                        break;
                    case R.id.btnBurning:
                        painOfPatientBeginning.setBurning(!painOfPatientBeginning.isBurning());
                        break;
                    case R.id.btnTingling:
                        painOfPatientBeginning.setTingling(!painOfPatientBeginning.isTingling());
                        break;
                    case R.id.btnPinsandneedles:
                        painOfPatientBeginning.setPinsneedles(!painOfPatientBeginning.isPinsneedles());
                        break;
                    case R.id.btnNumb:
                        painOfPatientBeginning.setNumb(!painOfPatientBeginning.isNumb());
                        break;
                }
                updatePainDisplay(painOfPatientBeginning);
            } else if (rgBeginningCurrent.getCheckedRadioButtonId() == R.id.rbCurrent){
                switch (view.getId()) {
                    case R.id.btnDull:
                        painOfPatientCurrent.setDull(!painOfPatientCurrent.isDull());
                        break;
                    case R.id.btnPulling:
                        painOfPatientCurrent.setPulling(!painOfPatientCurrent.isPulling());
                        break;
                    case R.id.btnPulsating:
                        painOfPatientCurrent.setPulsating(!painOfPatientCurrent.isPulsating());
                        break;
                    case R.id.btnStinging:
                        painOfPatientCurrent.setStinging(!painOfPatientCurrent.isStinging());
                        break;
                    case R.id.btnBurning:
                        painOfPatientCurrent.setBurning(!painOfPatientCurrent.isBurning());
                        break;
                    case R.id.btnTingling:
                        painOfPatientCurrent.setTingling(!painOfPatientCurrent.isTingling());
                        break;
                    case R.id.btnPinsandneedles:
                        painOfPatientCurrent.setPinsneedles(!painOfPatientCurrent.isPinsneedles());
                        break;
                    case R.id.btnNumb:
                        painOfPatientCurrent.setNumb(!painOfPatientCurrent.isNumb());
                        break;
                }
                updatePainDisplay(painOfPatientCurrent);
            }
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
        if (rgBeginningCurrent.getCheckedRadioButtonId() == R.id.rbBeginning) {
            savePainBeginning();
        } else if (rgBeginningCurrent.getCheckedRadioButtonId() == R.id.rbCurrent) {
            savePainCurrent();
        }
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
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (rgBeginningCurrent.getCheckedRadioButtonId() == R.id.rbBeginning) {
                    painOfPatientBeginning.setComment(etComment.getText().toString());
                } else {
                    painOfPatientCurrent.setComment(etComment.getText().toString());
                }
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
        pain_gif_01 = view.findViewById(R.id.fragment_pain_gif01);
        pain_gif_02 = view.findViewById(R.id.fragment_pain_gif02);
        pain_gif_03 = view.findViewById(R.id.fragment_pain_gif03);
        pain_gif_04 = view.findViewById(R.id.fragment_pain_gif04);
        pain_gif_05 = view.findViewById(R.id.fragment_pain_gif05);
        pain_gif_06 = view.findViewById(R.id.fragment_pain_gif06);
        pain_gif_08 = view.findViewById(R.id.fragment_pain_gif08);

        btnDull.setOnClickListener(onQualityClickListener);
        btnPulling.setOnClickListener(onQualityClickListener);
        btnPulsating.setOnClickListener(onQualityClickListener);
        btnStinging.setOnClickListener(onQualityClickListener);
        btnBurning.setOnClickListener(onQualityClickListener);
        btnTingling.setOnClickListener(onQualityClickListener);
        btnPinsandneedles.setOnClickListener(onQualityClickListener);
        btnNumb.setOnClickListener(onQualityClickListener);

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
        ivLocationFaceLeft = view.findViewById(R.id.ivLocationFaceLeft);
        ivLocationFaceLeft.setOnClickListener(onClickLocationImage);
        ivLocationFaceRight = view.findViewById(R.id.ivLocationFaceRight);
        ivLocationFaceRight.setOnClickListener(onClickLocationImage);
        fragment_pain_png_base = view.findViewById(R.id.fragment_pain_png_base);
        fragment_pain_png_base.setOnClickListener(onClickLocationImage);
        myDialog = new Dialog(getActivity());
        myDialog.setCanceledOnTouchOutside(false);
        addPainItem = "none";

        rbBeginning = view.findViewById(R.id.rbBeginning);
        rbCurrent = view.findViewById(R.id.rbCurrent);

        seekBar = (SeekBar) view.findViewById(R.id.sbIntensity);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TextView t = (TextView) view.findViewById(R.id.tvIntensity);
                t.setText(String.valueOf(i));
                if (rgBeginningCurrent.getCheckedRadioButtonId() == R.id.rbBeginning) {
                    painOfPatientBeginning.setIntensity(i);
                } else {
                    painOfPatientCurrent.setIntensity(i);
                }
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
                setUpSelectedViews();
                if (!firstTimeOpen) {
                    if (checkedId == R.id.rbCurrent) {
                        savePainBeginning();
                    }
                    if (checkedId == R.id.rbBeginning) {
                        savePainCurrent();
                    }
                }
            }
        });
        rbBeginning.setChecked(true);
        firstTimeOpen = false;

        initializePainsOfPatient();
        setUpSelectedViews();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        //Get Camera capture
        if(requestCode == 100){
            try {
                painOfPatientBeginning.setPhoto((Bitmap) data.getExtras().get("data"));
            } catch(Exception e) {
                //Photo cancelled.
                Log.d("Log", "Cancelled Photography. See PainFragment.java.");
            }
        }
    }

    private void updatePainDisplay(PainSuperclass painObject){
        //Updates the gifs to match the current selection of pain qualities. Note that
        //there is no distinction between "pins and needles" and "tingling" (both equal
        //to gif06).
        pain_gif_01.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_02.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_03.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_04.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_05.setImageResource(R.drawable.pain_gif_empty);
        pain_gif_06.setImageResource(R.drawable.pain_gif_empty);
        //pain gif 07 is missing - currently, there is no distinction between tingling and needles.
        pain_gif_08.setImageResource(R.drawable.pain_gif_empty);
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
        if (painObject.isPinsneedles() | painObject.isTingling()) {
            pain_gif_06.setImageResource(R.drawable.pain_gif_06);
        }
        if (painObject.isNumb()) {
            pain_gif_08.setImageResource(R.drawable.pain_gif_08);
        }

    }

    private void setUpSelectedViews() {
        if (rgBeginningCurrent.getCheckedRadioButtonId() == R.id.rbBeginning) {
            setBeginnViewsIfExist();

        } else if (rgBeginningCurrent.getCheckedRadioButtonId() == R.id.rbCurrent) {
            setCurrentViewsIfExist();
        }
    }

    private void setBeginnViewsIfExist() {
        boolean PainBeginningExists = db.existsPainBeginning(patientId);
        if (PainBeginningExists) {
            setUpAllViewsBeginning();
        } else {
            etComment.setText("");
            deselectAllQualityButtons();
            deselectPattern();
            seekBar.setProgress(0);
            ivLocationTeeth.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.teeth));
            ivLocationFaceLeft.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face_left));
            ivLocationFaceRight.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face_right));
        }
    }

    private void setCurrentViewsIfExist() {
        boolean PainBeginningExists = db.existsPainCurrent(patientId);
        if (PainBeginningExists) {
            setUpAllViewsCurrent();
        } else {
            etComment.setText("");
            deselectAllQualityButtons();
            deselectPattern();
            seekBar.setProgress(0);
            ivLocationTeeth.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.teeth));
            ivLocationFaceLeft.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face_left));
            ivLocationFaceRight.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face_right));
        }
    }

    private void initializePainsOfPatient() {
        painOfPatientBeginning.setPatient_id(patientId);

        painOfPatientBeginning.setIntensity(0);

        Bitmap bmpTeeth = BitmapFactory.decodeResource(getResources(), R.drawable.teeth);
        Bitmap bmpFaceLeft = BitmapFactory.decodeResource(getResources(), R.drawable.face_left);
        Bitmap bmpFaceRight = BitmapFactory.decodeResource(getResources(), R.drawable.face_right);
        painOfPatientBeginning.setLocation_teeth(bitmapToByte(bmpTeeth));
        painOfPatientBeginning.setLocation_face_left(bitmapToByte(bmpFaceLeft));
        painOfPatientBeginning.setLocation_face_right(bitmapToByte(bmpFaceRight));

        painOfPatientBeginning.setPain_pattern(null);

        painOfPatientBeginning.setDull(false);
        painOfPatientBeginning.setPulling(false);
        painOfPatientBeginning.setStinging(false);
        painOfPatientBeginning.setPulsating(false);
        painOfPatientBeginning.setBurning(false);
        painOfPatientBeginning.setPinsneedles(false);
        painOfPatientBeginning.setTingling(false);
        painOfPatientBeginning.setNumb(false);
        painOfPatientBeginning.setComment("");

        painOfPatientCurrent.setPatient_id(patientId);

        painOfPatientCurrent.setIntensity(0);

        Bitmap bmpTeethCurrent = BitmapFactory.decodeResource(getResources(), R.drawable.teeth);
        Bitmap bmpFaceLeftCurrent = BitmapFactory.decodeResource(getResources(), R.drawable.face_left);
        Bitmap bmpFaceRightCurrent = BitmapFactory.decodeResource(getResources(), R.drawable.face_right);
        painOfPatientCurrent.setLocation_teeth(bitmapToByte(bmpTeethCurrent));
        painOfPatientCurrent.setLocation_face_left(bitmapToByte(bmpFaceLeftCurrent));
        painOfPatientCurrent.setLocation_face_right(bitmapToByte(bmpFaceRightCurrent));

        painOfPatientCurrent.setPain_pattern(null);

        painOfPatientCurrent.setDull(false);
        painOfPatientCurrent.setPulling(false);
        painOfPatientCurrent.setStinging(false);
        painOfPatientCurrent.setPulsating(false);
        painOfPatientCurrent.setBurning(false);
        painOfPatientCurrent.setPinsneedles(false);
        painOfPatientCurrent.setTingling(false);
        painOfPatientCurrent.setNumb(false);
        painOfPatientCurrent.setComment("");
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


    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
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
                    case R.id.ivLocationFaceLeft:
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.face_left);
                        break;
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
                if (rgBeginningCurrent.getCheckedRadioButtonId() == R.id.rbBeginning) {
                    switch (openedLocationImage) {
                        case R.id.ivLocationTeeth:
                            ivLocationTeeth.setImageBitmap(alteredBitmap);
                            painOfPatientBeginning.setLocation_teeth(bitmapToByte(alteredBitmap));
                            break;
                        case R.id.ivLocationFaceLeft:
                            ivLocationFaceLeft.setImageBitmap(alteredBitmap);
                            painOfPatientBeginning.setLocation_face_left(bitmapToByte(alteredBitmap));
                            break;
                        case R.id.ivLocationFaceRight:
                            ivLocationFaceRight.setImageBitmap(alteredBitmap);
                            painOfPatientBeginning.setLocation_face_right(bitmapToByte(alteredBitmap));
                            break;
                    }
                } else {
                    switch (openedLocationImage) {
                        case R.id.ivLocationTeeth:
                            ivLocationTeeth.setImageBitmap(alteredBitmap);
                            painOfPatientCurrent.setLocation_teeth(bitmapToByte(alteredBitmap));
                            break;
                        case R.id.ivLocationFaceLeft:
                            ivLocationFaceLeft.setImageBitmap(alteredBitmap);
                            painOfPatientCurrent.setLocation_face_left(bitmapToByte(alteredBitmap));
                            break;
                        case R.id.ivLocationFaceRight:
                            ivLocationFaceRight.setImageBitmap(alteredBitmap);
                            painOfPatientCurrent.setLocation_face_right(bitmapToByte(alteredBitmap));
                            break;
                    }
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
        ArrayList<pl.droidsonroids.gif.GifImageView> popup_gif_list = new ArrayList<>(Arrays.asList(
                popup_gif_01, popup_gif_02, popup_gif_03, popup_gif_04, popup_gif_05, popup_gif_06,
                popup_gif_07, popup_gif_08));
        addPainItem = "none";
        ArrayList<PainSuperclass> both_pain_types = new ArrayList<>(Arrays.asList(painOfPatientBeginning, painOfPatientCurrent));

        //Initialize Buttons
        TextView btnClose;
        TextView btnAddPain;
        Button btnFoto;
        ImageView Photography;
        btnClose = myDialog.findViewById(R.id.btnCancel);
        btnAddPain = myDialog.findViewById(R.id.btnAddPain);
        btnFoto = myDialog.findViewById(R.id.btnTakePicture);
        Photography = myDialog.findViewById(R.id.painPhoto);

        btnClose.setOnClickListener(new View.OnClickListener() {
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
                            int index = 1;
                            if (rgBeginningCurrent.getCheckedRadioButtonId() == R.id.rbBeginning){index = 0;}

                            both_pain_types.get(index).setPainCoordinates(motionEvent.getX(), motionEvent.getY(), 0.0f, addPainItem);
                            updatePainPopup(popup_gif_list.get(both_pain_types.get(index).getPainList().indexOf(addPainItem)),
                                    motionEvent.getX(), motionEvent.getY(), addPainItem, both_pain_types.get(index));

                            addPainItem = "none";
                        }
                }
                return true;
            }
        });

        // Update all view coordinates before opening the popup; update Photography if exists.
        int index = 1;
        if (rgBeginningCurrent.getCheckedRadioButtonId() == R.id.rbBeginning){index = 0;}
        ArrayList temp = both_pain_types.get(index).getPainList();
        for (int i = 0; i < temp.size(); i++) {
            float x = Float.parseFloat(both_pain_types.get(index).getPainCoordinates(temp.get(i).toString()).get(0).toString());
            float y = Float.parseFloat(both_pain_types.get(index).getPainCoordinates(temp.get(i).toString()).get(1).toString());
            updatePainPopup(popup_gif_list.get(i), x, y, temp.get(i).toString(), both_pain_types.get(index));
        }
        if (painOfPatientBeginning.existsPhoto()){
            Photography.setImageBitmap(painOfPatientBeginning.getPhoto());
            Photography.setVisibility(View.VISIBLE);
        }

        myDialog.show();
    }

    private void updatePainPopup(pl.droidsonroids.gif.GifImageView view, float x, float y, String name, PainSuperclass pain_type){
        //Resets the Coordinates of a gif view. If a coordinate is = -1.0, it will by default not be updated.
        //If the pain does not exist yet, the gif is set to invisible.
        //40 and 120 is arbitrary, it adjusts the gif's position a bit to the upper left.
        if (pain_type.painIsSet(name)){
            view.setVisibility(View.VISIBLE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(Math.round(x)-120,Math.round(y)-40,0,0);
            view.setLayoutParams(params);
        } else {
            view.setVisibility(View.INVISIBLE);
        }

    }

    public void addNewPainBeginning(PainBeginning painBeginning) {
        db.addPainBeginning(painBeginning);
    }

    public void updatePainBeginning(final PainBeginning updatedPainBeginning) {
        db.updatePainBeginning(patientId, updatedPainBeginning);
    }

    public void savePainBeginning() {
        boolean painBeginningExists = db.existsPainBeginning(patientId);

        if (painBeginningExists) {
            updatePainBeginning(painOfPatientBeginning);
        } else {
            addNewPainBeginning(painOfPatientBeginning);
        }
    }

    public void addNewPainCurrent(PainCurrent painCurrent) {
        db.addPainCurrent(painCurrent);
    }

    public void updatePainCurrent(final PainCurrent updatedPainCurrent) {
        db.updatePainCurrent(patientId, updatedPainCurrent);
    }

    public void savePainCurrent() {
        boolean painCurrentExists = db.existsPainCurrent(patientId);

        if (painCurrentExists) {
            updatePainCurrent(painOfPatientCurrent);
        } else {
            addNewPainCurrent(painOfPatientCurrent);
        }
    }

    private void setUpAllViewsBeginning() {
        painOfPatientBeginning = db.getPainBeginningOfPatient(patientId);
        Bitmap bmTeeth = BitmapFactory.decodeByteArray(painOfPatientBeginning.getLocation_teeth(), 0, painOfPatientBeginning.getLocation_teeth().length);
        Bitmap bmFaceLeft = BitmapFactory.decodeByteArray(painOfPatientBeginning.getLocation_face_left(), 0, painOfPatientBeginning.getLocation_face_left().length);
        Bitmap bmFaceRight = BitmapFactory.decodeByteArray(painOfPatientBeginning.getLocation_face_right(), 0, painOfPatientBeginning.getLocation_face_right().length);
        ivLocationTeeth.setImageBitmap(bmTeeth);
        ivLocationFaceLeft.setImageBitmap(bmFaceLeft);
        ivLocationFaceRight.setImageBitmap(bmFaceRight);
        seekBar.setProgress(painOfPatientBeginning.getIntensity());
        etComment.setText(painOfPatientBeginning.getComment());

        deselectPattern();
        if (painOfPatientBeginning.getPain_pattern() != null && !painOfPatientBeginning.getPain_pattern().isEmpty()) {
            switch (painOfPatientBeginning.getPain_pattern()) {
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
        if (painOfPatientBeginning.isDull()) {
            selectDeselectView(btnDull);
        }
        if (painOfPatientBeginning.isPulling()) {
            selectDeselectView(btnPulling);
        }
        if (painOfPatientBeginning.isPulsating()) {
            selectDeselectView(btnPulsating);
        }
        if (painOfPatientBeginning.isPinsneedles()) {
            selectDeselectView(btnPinsandneedles);
        }
        if (painOfPatientBeginning.isTingling()) {
            selectDeselectView(btnTingling);
        }
        if (painOfPatientBeginning.isBurning()) {
            selectDeselectView(btnBurning);
        }
        if (painOfPatientBeginning.isStinging()) {
            selectDeselectView(btnStinging);
        }
        if (painOfPatientBeginning.isNumb()) {
            selectDeselectView(btnNumb);
        }
        updatePainDisplay(painOfPatientBeginning);
    }

    private void setUpAllViewsCurrent() {
        painOfPatientCurrent = db.getPainCurrentOfPatient(patientId);
        Bitmap bmTeeth = BitmapFactory.decodeByteArray(painOfPatientCurrent.getLocation_teeth(), 0, painOfPatientCurrent.getLocation_teeth().length);
        Bitmap bmFaceLeft = BitmapFactory.decodeByteArray(painOfPatientCurrent.getLocation_face_left(), 0, painOfPatientCurrent.getLocation_face_left().length);
        Bitmap bmFaceRight = BitmapFactory.decodeByteArray(painOfPatientCurrent.getLocation_face_right(), 0, painOfPatientCurrent.getLocation_face_right().length);
        ivLocationTeeth.setImageBitmap(bmTeeth);
        ivLocationFaceLeft.setImageBitmap(bmFaceLeft);
        ivLocationFaceRight.setImageBitmap(bmFaceRight);
        seekBar.setProgress(painOfPatientCurrent.getIntensity());
        etComment.setText(painOfPatientCurrent.getComment());

        deselectPattern();
        if (painOfPatientCurrent.getPain_pattern() != null && !painOfPatientCurrent.getPain_pattern().isEmpty()) {
            switch (painOfPatientCurrent.getPain_pattern()) {
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
        if (painOfPatientCurrent.isDull()) {
            selectDeselectView(btnDull);
        }
        if (painOfPatientCurrent.isPulling()) {
            selectDeselectView(btnPulling);
        }
        if (painOfPatientCurrent.isPulsating()) {
            selectDeselectView(btnPulsating);
        }
        if (painOfPatientCurrent.isPinsneedles()) {
            selectDeselectView(btnPinsandneedles);
        }
        if (painOfPatientCurrent.isTingling()) {
            selectDeselectView(btnTingling);
        }
        if (painOfPatientCurrent.isBurning()) {
            selectDeselectView(btnBurning);
        }
        if (painOfPatientCurrent.isStinging()) {
            selectDeselectView(btnStinging);
        }
        if (painOfPatientCurrent.isNumb()) {
            selectDeselectView(btnNumb);
        }
        updatePainDisplay(painOfPatientCurrent);
        initialSetUpBeginningDone = true;
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

        btnDull.setTextColor(Color.BLACK);
        btnBurning.setTextColor(Color.BLACK);
        btnNumb.setTextColor(Color.BLACK);
        btnPinsandneedles.setTextColor(Color.BLACK);
        btnPulling.setTextColor(Color.BLACK);
        btnStinging.setTextColor(Color.BLACK);
        btnTingling.setTextColor(Color.BLACK);
        btnPulsating.setTextColor(Color.BLACK);

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
        if (rgBeginningCurrent.getCheckedRadioButtonId() == R.id.rbBeginning) {
            switch (view.getId()) {
                case R.id.btnPermSlightFluc:
                    painOfPatientBeginning.setPain_pattern("PermSlightFluc");
                    break;
                case R.id.btnPermStrongFluc:
                    painOfPatientBeginning.setPain_pattern("PermStrongFluc");
                    break;
                case R.id.btnAttFreeInt:
                    painOfPatientBeginning.setPain_pattern("AttFreeInt");
                    break;
                case R.id.btnAttNoFreeInt:
                    painOfPatientBeginning.setPain_pattern("AttNoFreeInt");
                    break;
            }
        } else {
            switch (view.getId()) {
                case R.id.btnPermSlightFluc:
                    painOfPatientCurrent.setPain_pattern("PermSlightFluc");
                    break;
                case R.id.btnPermStrongFluc:
                    painOfPatientCurrent.setPain_pattern("PermStrongFluc");
                    break;
                case R.id.btnAttFreeInt:
                    painOfPatientCurrent.setPain_pattern("AttFreeInt");
                    break;
                case R.id.btnAttNoFreeInt:
                    painOfPatientCurrent.setPain_pattern("AttNoFreeInt");
                    break;
            }
        }
    }

}
