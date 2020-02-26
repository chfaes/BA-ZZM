package com.example.frontend.Fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.frontend.Fragments.Dialogs.ReasonDialog;
import com.example.frontend.Models.ImprovementReason;
import com.example.frontend.Models.PsychoSocialAfter;
import com.example.frontend.Models.PsychoSocialBefore;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;

public class PsychosocialFragment extends Fragment implements ReasonDialog.ReasonDialogListener {

    private int patientId;

    DatabaseHelper db;

    private Button btnPainBefore;
    private Button btnFamilyBefore;
    private Button btnWorkBefore;
    private Button btnFinancialBefore;
    private Button btnEventBefore;

    private Button btnPainAfter;
    private Button btnFamilyAfter;
    private Button btnWorkAfter;
    private Button btnFinancialAfter;
    private Button btnEventAfter;
    private boolean firstTouch = false;


    private ImageView btnReason;

    private int xDelta;
    private int yDelta;
    private RelativeLayout rlBefore;
    private RelativeLayout rlAfter;

    private ImprovementReason improvementReasonOfPatient = new ImprovementReason();
    private PsychoSocialBefore psychoSocialBeforeOfPatient = new PsychoSocialBefore();
    private PsychoSocialAfter psychoSocialAfterOfPatient = new PsychoSocialAfter();

    private RelativeLayout.LayoutParams lpPainBefore;
    private RelativeLayout.LayoutParams lpPainAfter;

    private RelativeLayout.LayoutParams lpFamilyBefore;
    private RelativeLayout.LayoutParams lpFamilyAfter;

    private RelativeLayout.LayoutParams lpWorkBefore;
    private RelativeLayout.LayoutParams lpWorkAfter;

    private RelativeLayout.LayoutParams lpFinancialBefore;
    private RelativeLayout.LayoutParams lpFinancialAfter;

    private RelativeLayout.LayoutParams lpEventBefore;
    private RelativeLayout.LayoutParams lpEventAfter;

    private boolean initialSetUpBeforeDone = false;
    private boolean initialSetUpAfterDone = false;

    /* Only used for Heruoku Database
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://consapp.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        patientId = getArguments().getInt("patientId");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_psychosocial, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());
        rlBefore = view.findViewById(R.id.rlBefore);
        rlAfter = view.findViewById(R.id.rlAfter);

        btnPainBefore = view.findViewById(R.id.btnPainBefore);
        btnPainBefore.setOnTouchListener(new ChoiceTouchListener());
        btnFamilyBefore = view.findViewById(R.id.btnFamilyBefore);
        btnFamilyBefore.setOnTouchListener(new ChoiceTouchListener());
        btnWorkBefore = view.findViewById(R.id.btnWorkBefore);
        btnWorkBefore.setOnTouchListener(new ChoiceTouchListener());
        btnFinancialBefore = view.findViewById(R.id.btnFinancialBefore);
        btnFinancialBefore.setOnTouchListener(new ChoiceTouchListener());
        btnEventBefore = view.findViewById(R.id.btnEventBefore);
        btnEventBefore.setOnTouchListener(new ChoiceTouchListener());

        btnPainAfter = view.findViewById(R.id.btnPainAfter);
        btnPainAfter.setOnTouchListener(new ChoiceTouchListener());
        btnFamilyAfter = view.findViewById(R.id.btnFamilyAfter);
        btnFamilyAfter.setOnTouchListener(new ChoiceTouchListener());
        btnWorkAfter = view.findViewById(R.id.btnWorkAfter);
        btnWorkAfter.setOnTouchListener(new ChoiceTouchListener());
        btnFinancialAfter = view.findViewById(R.id.btnFinancialAfter);
        btnFinancialAfter.setOnTouchListener(new ChoiceTouchListener());
        btnEventAfter = view.findViewById(R.id.btnEventAfter);
        btnEventAfter.setOnTouchListener(new ChoiceTouchListener());


        btnReason = view.findViewById(R.id.btnReason);
        btnReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReasonDialog();
            }
        });

        setPsychoSocialBefore();
        setPsychoSocialAfter();

    }

    int clickCount = 0;
    /*variable for storing the time of first click*/
    long startTime;
    /* variable for calculating the total time*/
    long duration;

    /* constant for defining the time duration between the click that can be considered as double-tap */
    private final class ChoiceTouchListener implements View.OnTouchListener {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int X = (int) motionEvent.getRawX();
            int Y = (int) motionEvent.getRawY();
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    xDelta = X - lParams.leftMargin;
                    yDelta = Y - lParams.topMargin;

                    clickCount++;

                    if (clickCount == 1) {
                        startTime = System.currentTimeMillis();
                    } else if (clickCount == 2) {
                        long duration = System.currentTimeMillis() - startTime;
                        if (duration <= 500) {
                            PopupWindow popupwindow_obj = popupDisplay(view, motionEvent);
                            popupwindow_obj.setBackgroundDrawable(new BitmapDrawable());
                            popupwindow_obj.showAsDropDown(view, -60, 15);
                            duration = 0;
                            clickCount = 0;
                        } else {
                            clickCount = 1;
                            startTime = System.currentTimeMillis();
                        }
                        break;
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    savePositions();
                    setPsychoSocialBefore();
                    setPsychoSocialAfter();
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    int rlWidth = rlBefore.getWidth() - 70;
                    int rlHeight = rlBefore.getHeight() - 70;

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    int newX = X - xDelta;
                    int newY = Y - yDelta;
                    if ((newX >= 0 && newX <= rlWidth && newY >= 0 && newY <= rlHeight)) {
                        layoutParams.leftMargin = newX;
                        layoutParams.topMargin = newY;
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        view.setLayoutParams(layoutParams);
                    }
                    break;
            }
            rlBefore.invalidate();

            return true;
        }
    }

    public void openReasonDialog() {
        ReasonDialog reasonDialog = new ReasonDialog();
        Bundle args = new Bundle();
        args.putInt("patient_id", patientId);
        reasonDialog.setArguments(args);
        reasonDialog.setTargetFragment(PsychosocialFragment.this, 1);
        reasonDialog.show(getActivity().getSupportFragmentManager(), "Reason Dialog");
    }

    @Override
    public void applyTexts(boolean drugsReason, boolean exercisesReason, boolean awarenessReason, boolean otherReasons, String otherReasonsText) {
        improvementReasonOfPatient.setPatient_id(patientId);
        improvementReasonOfPatient.setDrugs(drugsReason);
        improvementReasonOfPatient.setExercises(exercisesReason);
        improvementReasonOfPatient.setAwareness(awarenessReason);
        improvementReasonOfPatient.setOther_reason(otherReasons);
        improvementReasonOfPatient.setOther_reason_text(otherReasonsText);

        setImprovementReason();
    }

    public void addNewImprovementReason(ImprovementReason improvementReason) {
        db.addImprovementReason(improvementReason);

        /* Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createImprovementReason(improvementReason);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createNote NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void updateImprovementReason(final ImprovementReason updatedImprovementReason) {
        db.updateImprovementReason(patientId, updatedImprovementReason);

        /* Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.updateImprovementReason(patientId, updatedImprovementReason);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createImprovementReason NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void setImprovementReason() {
        boolean improvementReasonExists = db.existsImprovementReason(patientId);
        if (improvementReasonExists) {
            updateImprovementReason(improvementReasonOfPatient);
        } else {
            addNewImprovementReason(improvementReasonOfPatient);
        }

        /* Only used for Heruoku Database

        Call<Boolean> call = jsonPlaceHolderApi.existsImprovementReason(patientId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Get improvement reason not successful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    boolean improvementReasonExists = response.body();
                    if (improvementReasonExists) {
                        updateImprovementReason(improvementReasonOfPatient);
                    } else {
                        addNewImprovementReason(improvementReasonOfPatient);
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }


    public void addNewPsychoSocialBefore(PsychoSocialBefore psychoSocialBefore) {
        db.addPsychoSocialBefore(psychoSocialBefore);

        /* Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createPsychoSocialBefore(psychoSocialBefore);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createNote NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void updatePsychoSocialBefore(final PsychoSocialBefore updatedPsychoSocialBefore) {
        db.updatePsychoSocialBefore(patientId, updatedPsychoSocialBefore);

        /* Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.updatePsychoSocialBefore(patientId, updatedPsychoSocialBefore);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createPsychoSocialBefore NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void setPsychoSocialBefore() {
        boolean PsychoSocialBeforeExists = db.existsPsychoSocialBefore(patientId);
        if (PsychoSocialBeforeExists) {
            if (!initialSetUpBeforeDone) {
                setUpPositionsBefore();
                initialSetUpBeforeDone = true;
            } else {
                updatePsychoSocialBefore(psychoSocialBeforeOfPatient);
            }
        } else {
            if (initialSetUpBeforeDone) {
                setDefaultSizeColorBefore();
                savePositions();
                addNewPsychoSocialBefore(psychoSocialBeforeOfPatient);
            } else {
                initialSetUpBeforeDone = true;
            }
        }

        /* Only used for Heruoku Database

        Call<Boolean> call = jsonPlaceHolderApi.existsPsychoSocialBefore(patientId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    return;
                } else {
                    boolean PsychoSocialBeforeExists = response.body();

                    if (PsychoSocialBeforeExists) {
                        if(!initialSetUpBeforeDone){
                            setUpPositionsBefore();
                            initialSetUpBeforeDone = true;
                        }else{
                            updatePsychoSocialBefore(psychoSocialBeforeOfPatient);
                        }
                    } else {
                        if(initialSetUpBeforeDone){
                            addNewPsychoSocialBefore(psychoSocialBeforeOfPatient);
                        }else{
                            initialSetUpBeforeDone = true;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void addNewPsychoSocialAfter(PsychoSocialAfter psychoSocialAfter) {
        db.addPsychoSocialAfter(psychoSocialAfter);

        /* Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createPsychoSocialAfter(psychoSocialAfter);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createNote NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void updatePsychoSocialAfter(final PsychoSocialAfter updatedPsychoSocialAfter) {
        db.updatePsychoSocialAfter(patientId, updatedPsychoSocialAfter);

        /* Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.updatePsychoSocialAfter(patientId, updatedPsychoSocialAfter);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createPsychoSocialAfter NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void setPsychoSocialAfter() {
        boolean PsychoSocialAfterExists = db.existsPsychoSocialAfter(patientId);

        if (PsychoSocialAfterExists) {
            if (!initialSetUpAfterDone) {
                setUpPositionsAfter();
                initialSetUpAfterDone = true;
            } else {
                updatePsychoSocialAfter(psychoSocialAfterOfPatient);
            }
        } else {
            if (initialSetUpAfterDone) {
                setDefaultSizeColorAfter();
                savePositions();
                addNewPsychoSocialAfter(psychoSocialAfterOfPatient);
            } else {
                initialSetUpAfterDone = true;
            }
        }

        /* Only used for Heruoku Database

        Call<Boolean> call = jsonPlaceHolderApi.existsPsychoSocialAfter(patientId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    return;
                } else {
                    boolean PsychoSocialAfterExists = response.body();

                    if (PsychoSocialAfterExists) {
                        if(!initialSetUpAfterDone){
                            setUpPositionsAfter();
                            initialSetUpAfterDone = true;
                        }else{
                            updatePsychoSocialAfter(psychoSocialAfterOfPatient);
                        }
                    } else {
                        if(initialSetUpAfterDone) {
                            addNewPsychoSocialAfter(psychoSocialAfterOfPatient);
                        }else{
                            initialSetUpAfterDone = true;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    private void savePositions() {
        psychoSocialBeforeOfPatient.setPatient_id(patientId);
        psychoSocialAfterOfPatient.setPatient_id(patientId);

        lpPainBefore = (RelativeLayout.LayoutParams) btnPainBefore.getLayoutParams();
        psychoSocialBeforeOfPatient.setPain_xpos(lpPainBefore.leftMargin);
        psychoSocialBeforeOfPatient.setPain_ypos(lpPainBefore.topMargin);


        lpFamilyBefore = (RelativeLayout.LayoutParams) btnFamilyBefore.getLayoutParams();
        psychoSocialBeforeOfPatient.setFamily_xpos(lpFamilyBefore.leftMargin);
        psychoSocialBeforeOfPatient.setFamily_ypos(lpFamilyBefore.topMargin);

        lpWorkBefore = (RelativeLayout.LayoutParams) btnWorkBefore.getLayoutParams();
        psychoSocialBeforeOfPatient.setWork_xpos(lpWorkBefore.leftMargin);
        psychoSocialBeforeOfPatient.setWork_ypos(lpWorkBefore.topMargin);

        lpFinancialBefore = (RelativeLayout.LayoutParams) btnFinancialBefore.getLayoutParams();
        psychoSocialBeforeOfPatient.setFinance_xpos(lpFinancialBefore.leftMargin);
        psychoSocialBeforeOfPatient.setFinance_ypos(lpFinancialBefore.topMargin);

        lpEventBefore = (RelativeLayout.LayoutParams) btnEventBefore.getLayoutParams();
        psychoSocialBeforeOfPatient.setEvent_xpos(lpEventBefore.leftMargin);
        psychoSocialBeforeOfPatient.setEvent_ypos(lpEventBefore.topMargin);


        lpPainAfter = (RelativeLayout.LayoutParams) btnPainAfter.getLayoutParams();
        psychoSocialAfterOfPatient.setPain_xpos(lpPainAfter.leftMargin);
        psychoSocialAfterOfPatient.setPain_ypos(lpPainAfter.topMargin);

        lpFamilyAfter = (RelativeLayout.LayoutParams) btnFamilyAfter.getLayoutParams();
        psychoSocialAfterOfPatient.setFamily_xpos(lpFamilyAfter.leftMargin);
        psychoSocialAfterOfPatient.setFamily_ypos(lpFamilyAfter.topMargin);

        lpWorkAfter = (RelativeLayout.LayoutParams) btnWorkAfter.getLayoutParams();
        psychoSocialAfterOfPatient.setWork_xpos(lpWorkAfter.leftMargin);
        psychoSocialAfterOfPatient.setWork_ypos(lpWorkAfter.topMargin);

        lpFinancialAfter = (RelativeLayout.LayoutParams) btnFinancialAfter.getLayoutParams();
        psychoSocialAfterOfPatient.setFinance_xpos(lpFinancialAfter.leftMargin);
        psychoSocialAfterOfPatient.setFinance_ypos(lpFinancialAfter.topMargin);

        lpEventAfter = (RelativeLayout.LayoutParams) btnEventAfter.getLayoutParams();
        psychoSocialAfterOfPatient.setEvent_xpos(lpEventAfter.leftMargin);
        psychoSocialAfterOfPatient.setEvent_ypos(lpEventAfter.topMargin);
    }

    private void setUpPositionsBefore() {
        psychoSocialBeforeOfPatient = db.getPsychoSocialBefore(patientId);

        lpPainBefore = (RelativeLayout.LayoutParams) btnPainBefore.getLayoutParams();
        lpPainBefore.leftMargin = psychoSocialBeforeOfPatient.getPain_xpos();
        lpPainBefore.topMargin = psychoSocialBeforeOfPatient.getPain_ypos();
        btnPainBefore.setLayoutParams(lpPainBefore);
        setButtonSize(btnPainBefore, psychoSocialBeforeOfPatient.getPain_size());
        setButtonColor(btnPainBefore, psychoSocialBeforeOfPatient.getPain_color());


        lpFamilyBefore = (RelativeLayout.LayoutParams) btnFamilyBefore.getLayoutParams();
        lpFamilyBefore.leftMargin = psychoSocialBeforeOfPatient.getFamily_xpos();
        lpFamilyBefore.topMargin = psychoSocialBeforeOfPatient.getFamily_ypos();
        btnFamilyBefore.setLayoutParams(lpFamilyBefore);
        setButtonSize(btnFamilyBefore, psychoSocialBeforeOfPatient.getFamily_size());
        setButtonColor(btnFamilyBefore, psychoSocialBeforeOfPatient.getFamily_color());

        lpWorkBefore = (RelativeLayout.LayoutParams) btnWorkBefore.getLayoutParams();
        lpWorkBefore.leftMargin = psychoSocialBeforeOfPatient.getWork_xpos();
        lpWorkBefore.topMargin = psychoSocialBeforeOfPatient.getWork_ypos();
        btnWorkBefore.setLayoutParams(lpWorkBefore);
        setButtonSize(btnWorkBefore, psychoSocialBeforeOfPatient.getWork_size());
        setButtonColor(btnWorkBefore, psychoSocialBeforeOfPatient.getWork_color());

        lpFinancialBefore = (RelativeLayout.LayoutParams) btnFinancialBefore.getLayoutParams();
        lpFinancialBefore.leftMargin = psychoSocialBeforeOfPatient.getFinance_xpos();
        lpFinancialBefore.topMargin = psychoSocialBeforeOfPatient.getFinance_ypos();
        btnFinancialBefore.setLayoutParams(lpFinancialBefore);
        setButtonSize(btnFinancialBefore, psychoSocialBeforeOfPatient.getFinance_size());
        setButtonColor(btnFinancialBefore, psychoSocialBeforeOfPatient.getFinance_color());

        lpEventBefore = (RelativeLayout.LayoutParams) btnEventBefore.getLayoutParams();
        lpEventBefore.leftMargin = psychoSocialBeforeOfPatient.getEvent_xpos();
        lpEventBefore.topMargin = psychoSocialBeforeOfPatient.getEvent_ypos();
        btnEventBefore.setLayoutParams(lpEventBefore);
        setButtonSize(btnEventBefore, psychoSocialBeforeOfPatient.getEvent_size());
        setButtonColor(btnEventBefore, psychoSocialBeforeOfPatient.getEvent_color());

        /* Only used for Heruoku Database

        Call<PsychoSocialBefore> call = jsonPlaceHolderApi.getPsychoSocialBefore(patientId);
        call.enqueue(new Callback<PsychoSocialBefore>() {
            @Override
            public void onResponse(Call<PsychoSocialBefore> call, Response<PsychoSocialBefore> response) {
                if (!response.isSuccessful()) {
                    return;
                } else {
                    PsychoSocialBefore psychoSocialBefore = response.body();

                    lpPainBefore = (RelativeLayout.LayoutParams) btnPainBefore.getLayoutParams();
                    lpPainBefore.leftMargin = psychoSocialBefore.getPain_xpos();
                    lpPainBefore.topMargin = psychoSocialBefore.getPain_ypos();
                    btnPainBefore.setLayoutParams(lpPainBefore);

                    lpFamilyBefore = (RelativeLayout.LayoutParams) btnFamilyBefore.getLayoutParams();
                    lpFamilyBefore.leftMargin = psychoSocialBefore.getFamily_xpos();
                    lpFamilyBefore.topMargin = psychoSocialBefore.getFamily_ypos();
                    btnFamilyBefore.setLayoutParams(lpFamilyBefore);

                    lpWorkBefore = (RelativeLayout.LayoutParams) btnWorkBefore.getLayoutParams();
                    lpWorkBefore.leftMargin = psychoSocialBefore.getWork_xpos();
                    lpWorkBefore.topMargin = psychoSocialBefore.getWork_ypos();
                    btnWorkBefore.setLayoutParams(lpWorkBefore);

                    lpFinancialBefore = (RelativeLayout.LayoutParams) btnFinancialBefore.getLayoutParams();
                    lpFinancialBefore.leftMargin = psychoSocialBefore.getFinance_xpos();
                    lpFinancialBefore.topMargin = psychoSocialBefore.getFinance_ypos();
                    btnFinancialBefore.setLayoutParams(lpFinancialBefore);

                    lpEventBefore = (RelativeLayout.LayoutParams) btnEventBefore.getLayoutParams();
                    lpEventBefore.leftMargin = psychoSocialBefore.getEvent_xpos();
                    lpEventBefore.topMargin = psychoSocialBefore.getEvent_ypos();
                    btnEventBefore.setLayoutParams(lpEventBefore);
                }
            }

            @Override
            public void onFailure(Call<PsychoSocialBefore> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    private void setButtonColor(Button btn, int color) {
        if (color == 1) {
            btn.setBackgroundResource(R.drawable.roundedbutton_red);
        } else if (color == 0) {
            btn.setBackgroundResource(R.drawable.roundedbutton_green);
        }
    }

    private void setButtonSize(Button btn, int size) {
        int sizePixel = 0;
        if (size == 1) {
            sizePixel = 70;
        } else if (size == 2) {
            sizePixel = 90;
        } else if (size == 3) {
            sizePixel = 110;
        }

        ViewGroup.LayoutParams lp = btn.getLayoutParams();
        float factor = btn.getContext().getResources().getDisplayMetrics().density;
        lp.width = (int) (sizePixel * factor);
        lp.height = (int) (sizePixel * factor);
        btn.setLayoutParams(lp);
    }

    private void setUpPositionsAfter() {
        psychoSocialAfterOfPatient = db.getPsychoSocialAfter(patientId);

        lpPainAfter = (RelativeLayout.LayoutParams) btnPainAfter.getLayoutParams();
        lpPainAfter.leftMargin = psychoSocialAfterOfPatient.getPain_xpos();
        lpPainAfter.topMargin = psychoSocialAfterOfPatient.getPain_ypos();
        btnPainAfter.setLayoutParams(lpPainAfter);
        setButtonSize(btnPainAfter, psychoSocialAfterOfPatient.getPain_size());
        setButtonColor(btnPainAfter, psychoSocialAfterOfPatient.getPain_color());


        lpFamilyAfter = (RelativeLayout.LayoutParams) btnFamilyAfter.getLayoutParams();
        lpFamilyAfter.leftMargin = psychoSocialAfterOfPatient.getFamily_xpos();
        lpFamilyAfter.topMargin = psychoSocialAfterOfPatient.getFamily_ypos();
        btnFamilyAfter.setLayoutParams(lpFamilyAfter);
        setButtonSize(btnFamilyAfter, psychoSocialAfterOfPatient.getFamily_size());
        setButtonColor(btnFamilyAfter, psychoSocialAfterOfPatient.getFamily_color());

        lpWorkAfter = (RelativeLayout.LayoutParams) btnWorkAfter.getLayoutParams();
        lpWorkAfter.leftMargin = psychoSocialAfterOfPatient.getWork_xpos();
        lpWorkAfter.topMargin = psychoSocialAfterOfPatient.getWork_ypos();
        btnWorkAfter.setLayoutParams(lpWorkAfter);
        setButtonSize(btnWorkAfter, psychoSocialAfterOfPatient.getWork_size());
        setButtonColor(btnWorkAfter, psychoSocialAfterOfPatient.getWork_color());

        lpFinancialAfter = (RelativeLayout.LayoutParams) btnFinancialAfter.getLayoutParams();
        lpFinancialAfter.leftMargin = psychoSocialAfterOfPatient.getFinance_xpos();
        lpFinancialAfter.topMargin = psychoSocialAfterOfPatient.getFinance_ypos();
        btnFinancialAfter.setLayoutParams(lpFinancialAfter);
        setButtonSize(btnFinancialAfter, psychoSocialAfterOfPatient.getFinance_size());
        setButtonColor(btnFinancialAfter, psychoSocialAfterOfPatient.getFinance_color());

        lpEventAfter = (RelativeLayout.LayoutParams) btnEventAfter.getLayoutParams();
        lpEventAfter.leftMargin = psychoSocialAfterOfPatient.getEvent_xpos();
        lpEventAfter.topMargin = psychoSocialAfterOfPatient.getEvent_ypos();
        btnEventAfter.setLayoutParams(lpEventAfter);
        setButtonSize(btnEventAfter, psychoSocialAfterOfPatient.getEvent_size());
        setButtonColor(btnEventAfter, psychoSocialAfterOfPatient.getEvent_color());

        /* Only used for Heruoku Database

        Call<PsychoSocialAfter> call = jsonPlaceHolderApi.getPsychoSocialAfter(patientId);
        call.enqueue(new Callback<PsychoSocialAfter>() {
            @Override
            public void onResponse(Call<PsychoSocialAfter> call, Response<PsychoSocialAfter> response) {
                if (!response.isSuccessful()) {
                    return;
                } else {
                    PsychoSocialAfter psychoSocialAfter = response.body();

                    lpPainAfter = (RelativeLayout.LayoutParams) btnPainAfter.getLayoutParams();
                    lpPainAfter.leftMargin = psychoSocialAfter.getPain_xpos();
                    lpPainAfter.topMargin = psychoSocialAfter.getPain_ypos();
                    btnPainAfter.setLayoutParams(lpPainAfter);

                    lpFamilyAfter = (RelativeLayout.LayoutParams) btnFamilyAfter.getLayoutParams();
                    lpFamilyAfter.leftMargin = psychoSocialAfter.getFamily_xpos();
                    lpFamilyAfter.topMargin = psychoSocialAfter.getFamily_ypos();
                    btnFamilyAfter.setLayoutParams(lpFamilyAfter);

                    lpWorkAfter = (RelativeLayout.LayoutParams) btnWorkAfter.getLayoutParams();
                    lpWorkAfter.leftMargin = psychoSocialAfter.getWork_xpos();
                    lpWorkAfter.topMargin = psychoSocialAfter.getWork_ypos();
                    btnWorkAfter.setLayoutParams(lpWorkAfter);

                    lpFinancialAfter = (RelativeLayout.LayoutParams) btnFinancialAfter.getLayoutParams();
                    lpFinancialAfter.leftMargin = psychoSocialAfter.getFinance_xpos();
                    lpFinancialAfter.topMargin = psychoSocialAfter.getFinance_ypos();
                    btnFinancialAfter.setLayoutParams(lpFinancialAfter);

                    lpEventAfter = (RelativeLayout.LayoutParams) btnEventAfter.getLayoutParams();
                    lpEventAfter.leftMargin = psychoSocialAfter.getEvent_xpos();
                    lpEventAfter.topMargin = psychoSocialAfter.getEvent_ypos();
                    btnEventAfter.setLayoutParams(lpEventAfter);
                }
            }

            @Override
            public void onFailure(Call<PsychoSocialAfter> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public PopupWindow popupDisplay(final View clickedItem, final MotionEvent event) {

        final PopupWindow popupWindow = new PopupWindow(getContext());

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.psychosocial_menu, null);

        Button btnColor = (Button) view.findViewById(R.id.btnColor);
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePsychosocialColor(clickedItem);
                popupWindow.dismiss();
            }
        });
        Button btnSize1 = (Button) view.findViewById(R.id.size1);
        Button btnSize2 = (Button) view.findViewById(R.id.size2);
        Button btnSize3 = (Button) view.findViewById(R.id.size3);
        View.OnClickListener changeSize = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams lp = clickedItem.getLayoutParams();
                float factor = clickedItem.getContext().getResources().getDisplayMetrics().density;
                switch (view.getId()) {
                    case R.id.size1:
                        lp.width = (int) (70 * factor);
                        lp.height = (int) (70 * factor);
                        clickedItem.setLayoutParams(lp);
                        updatePsychosocialSize(clickedItem, 1);
                        break;
                    case R.id.size2:
                        lp.width = (int) (90 * factor);
                        lp.height = (int) (90 * factor);
                        clickedItem.setLayoutParams(lp);
                        updatePsychosocialSize(clickedItem, 2);
                        break;
                    case R.id.size3:
                        lp.width = (int) (110 * factor);
                        lp.height = (int) (110 * factor);
                        clickedItem.setLayoutParams(lp);
                        updatePsychosocialSize(clickedItem, 3);
                        break;
                }
                //event.setAction(MotionEvent.ACTION_MOVE);
                popupWindow.dismiss();
            }
        };

        btnSize1.setOnClickListener(changeSize);
        btnSize2.setOnClickListener(changeSize);
        btnSize3.setOnClickListener(changeSize);

        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);

        return popupWindow;
    }

    private void updatePsychosocialColor(View clickedButton) {
        if (clickedButton.getParent().equals(rlBefore)) {
            int newColor;
            switch (clickedButton.getId()) {
                case R.id.btnPainBefore:
                    if (psychoSocialBeforeOfPatient.getPain_color() == 1) {
                        newColor = 0;
                    } else {
                        newColor = 1;
                    }
                    setButtonColor((Button) clickedButton, newColor);
                    psychoSocialBeforeOfPatient.setPain_color(newColor);
                    break;
                case R.id.btnFamilyBefore:
                    if (psychoSocialBeforeOfPatient.getFamily_color() == 1) {
                        newColor = 0;
                    } else {
                        newColor = 1;
                    }
                    setButtonColor((Button) clickedButton, newColor);
                    psychoSocialBeforeOfPatient.setFamily_color(newColor);
                    break;
                case R.id.btnWorkBefore:
                    if (psychoSocialBeforeOfPatient.getWork_color() == 1) {
                        newColor = 0;
                    } else {
                        newColor = 1;
                    }
                    setButtonColor((Button) clickedButton, newColor);
                    psychoSocialBeforeOfPatient.setWork_color(newColor);
                    break;
                case R.id.btnFinancialBefore:
                    if (psychoSocialBeforeOfPatient.getFinance_color() == 1) {
                        newColor = 0;
                    } else {
                        newColor = 1;
                    }
                    setButtonColor((Button) clickedButton, newColor);
                    psychoSocialBeforeOfPatient.setFinance_color(newColor);
                    break;
                case R.id.btnEventBefore:
                    if (psychoSocialBeforeOfPatient.getEvent_color() == 1) {
                        newColor = 0;
                    } else {
                        newColor = 1;
                    }
                    setButtonColor((Button) clickedButton, newColor);
                    psychoSocialBeforeOfPatient.setEvent_color(newColor);
                    break;
            }
            updatePsychoSocialBefore(psychoSocialBeforeOfPatient);

        } else if (clickedButton.getParent().equals(rlAfter)) {
            int newColor;
            switch (clickedButton.getId()) {
                case R.id.btnPainAfter:
                    if (psychoSocialAfterOfPatient.getPain_color() == 1) {
                        newColor = 0;
                    } else {
                        newColor = 1;
                    }
                    setButtonColor((Button) clickedButton, newColor);
                    psychoSocialAfterOfPatient.setPain_color(newColor);
                    break;
                case R.id.btnFamilyAfter:
                    if (psychoSocialAfterOfPatient.getFamily_color() == 1) {
                        newColor = 0;
                    } else {
                        newColor = 1;
                    }
                    setButtonColor((Button) clickedButton, newColor);
                    psychoSocialAfterOfPatient.setFamily_color(newColor);
                    break;
                case R.id.btnWorkAfter:
                    if (psychoSocialAfterOfPatient.getWork_color() == 1) {
                        newColor = 0;
                    } else {
                        newColor = 1;
                    }
                    setButtonColor((Button) clickedButton, newColor);
                    psychoSocialAfterOfPatient.setWork_color(newColor);
                    break;
                case R.id.btnFinancialAfter:
                    if (psychoSocialAfterOfPatient.getFinance_color() == 1) {
                        newColor = 0;
                    } else {
                        newColor = 1;
                    }
                    setButtonColor((Button) clickedButton, newColor);
                    psychoSocialAfterOfPatient.setFinance_color(newColor);
                    break;
                case R.id.btnEventAfter:
                    if (psychoSocialAfterOfPatient.getEvent_color() == 1) {
                        newColor = 0;
                    } else {
                        newColor = 1;
                    }
                    setButtonColor((Button) clickedButton, newColor);
                    psychoSocialAfterOfPatient.setEvent_color(newColor);
                    break;
            }
            updatePsychoSocialAfter(psychoSocialAfterOfPatient);
        }

    }

    private void setDefaultSizeColorBefore() {
        psychoSocialBeforeOfPatient.setPain_size(1);
        psychoSocialBeforeOfPatient.setFamily_size(1);
        psychoSocialBeforeOfPatient.setWork_size(1);
        psychoSocialBeforeOfPatient.setFinance_size(1);
        psychoSocialBeforeOfPatient.setEvent_size(1);

        psychoSocialBeforeOfPatient.setPain_color(1);   // 1 red
        psychoSocialBeforeOfPatient.setFamily_color(0); // 0 green
        psychoSocialBeforeOfPatient.setWork_color(0);
        psychoSocialBeforeOfPatient.setFinance_color(0);
        psychoSocialBeforeOfPatient.setEvent_color(0);
    }

    private void setDefaultSizeColorAfter() {
        psychoSocialAfterOfPatient.setPain_size(1);
        psychoSocialAfterOfPatient.setFamily_size(1);
        psychoSocialAfterOfPatient.setWork_size(1);
        psychoSocialAfterOfPatient.setFinance_size(1);
        psychoSocialAfterOfPatient.setEvent_size(1);

        psychoSocialAfterOfPatient.setPain_color(1);   // 1 red
        psychoSocialAfterOfPatient.setFamily_color(0); // 0 green
        psychoSocialAfterOfPatient.setWork_color(0);
        psychoSocialAfterOfPatient.setFinance_color(0);
        psychoSocialAfterOfPatient.setEvent_color(0);
    }

    private void updatePsychosocialSize(View clickedItem, int size) {
        switch (clickedItem.getId()) {
            case R.id.btnPainBefore:
                psychoSocialBeforeOfPatient.setPain_size(size);
                break;
            case R.id.btnFamilyBefore:
                psychoSocialBeforeOfPatient.setFamily_size(size);
                break;
            case R.id.btnWorkBefore:
                psychoSocialBeforeOfPatient.setWork_size(size);
                break;
            case R.id.btnFinancialBefore:
                psychoSocialBeforeOfPatient.setFinance_size(size);
                break;
            case R.id.btnEventBefore:
                psychoSocialBeforeOfPatient.setEvent_size(size);
                break;
            case R.id.btnPainAfter:
                psychoSocialAfterOfPatient.setPain_size(size);
                break;
            case R.id.btnFamilyAfter:
                psychoSocialAfterOfPatient.setFamily_size(size);
                break;
            case R.id.btnWorkAfter:
                psychoSocialAfterOfPatient.setWork_size(size);
                break;
            case R.id.btnFinancialAfter:
                psychoSocialAfterOfPatient.setFinance_size(size);
                break;
            case R.id.btnEventAfter:
                psychoSocialAfterOfPatient.setEvent_size(size);
                break;
        }
        updatePsychoSocialAfter(psychoSocialAfterOfPatient);
        updatePsychoSocialBefore(psychoSocialBeforeOfPatient);
    }

}

