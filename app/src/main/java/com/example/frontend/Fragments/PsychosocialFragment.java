package com.example.frontend.Fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class PsychosocialFragment extends Fragment implements ReasonDialog.ReasonDialogListener {

    private int patientId;

    DatabaseHelper db;

    private Button btnPainBefore;
    private Button btnFamilyBefore;
    private Button btnWorkBefore;
    private Button btnFinancialBefore;
    private Button btnEventBefore;
    private Button btnCreateBefore;

    private Button btnPainAfter;
    private Button btnFamilyAfter;
    private Button btnWorkAfter;
    private Button btnFinancialAfter;
    private Button btnEventAfter;
    private boolean firstTouch = false;


    private ImageView btnReason;

    private RelativeLayout relativeLayout = null;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        patientId = getArguments().getInt("patientId");
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
        btnCreateBefore = view.findViewById(R.id.psy_social_btn_01);

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

        //Initializes the button to create new Context Factors.
        //
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rlBefore);
        btnCreateBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Button to create new Context Factors.
                createBtn(view, 42, 142, 2, 1, "NEW", true, "-1");
            }
        });

        setPsychoSocialBefore();
        setPsychoSocialAfter();

        setPreexistingButtons(view);

    }

    int clickCount = 0;
    /*variable for storing the time of first click*/
    long startTime;
    /* variable for calculating the total time*/
    long duration;

    public void createBtn(View view, int x, int y, int size, int colour, String text, boolean isNew, String tag){
        //Creates a new Button, draws it and if it's a new Button (that is, it has been generated by
        //the user by clicking "New Button" in the Fragment) adds its values to "values" in the
        //Psychosocial Class. Note that "tag" is generated if the Button is new; if the button exists already,
        //the tag has to be provided in the function call.
        //Log.d("Log", "Zitrone 2:" + psychoSocialBeforeOfPatient.getValues().toString());
        Button btn = new Button(getActivity());
        //Text and Tag
        btn.setText(text);
        if (isNew){
            btn.setTag(psychoSocialBeforeOfPatient.getNextTag());
        } else {
            btn.setTag(tag);
        }
        //Dimensions
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        float factor = btn.getContext().getResources().getDisplayMetrics().density;
        params.width = params.height = (int) ((70 + ((size-1)*20))*factor);
        params.setMargins(x, y, 0, 0);
        btn.setLayoutParams(params);
        //Colour
        if(colour==1){
            btn.setBackgroundResource(R.drawable.roundedbutton_red);
        } else {
            btn.setBackgroundResource(R.drawable.roundedbutton_green);
        }
        //Everything else
        btn.setOnTouchListener(new ChoiceTouchListener());
        relativeLayout.addView(btn);
        if (isNew){
            psychoSocialBeforeOfPatient.setValues(btn.getTag().toString(), (int) btn.getX(), (int) btn.getY(), size, colour);
            psychoSocialBeforeOfPatient.setText(btn.getTag().toString(), btn.getText().toString());
        }
    }

    public void setPreexistingButtons(View view){
        //Special routine for the dynamically generated buttons: Gets all pre-existing buttons
        //from values and creates them.
        Log.d("Log", "hier1");
        Map PsySocBefore = psychoSocialBeforeOfPatient.getValues();
        Log.d("Log", "hier2:: " + PsySocBefore.toString());
        Iterator it = PsySocBefore.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ArrayList<Integer> templist = (ArrayList<Integer>) pair.getValue();
            Log.d("Log", ":::hier:"+pair.getValue().toString());
            createBtn(view,templist.get(0),templist.get(1),templist.get(2),templist.get(3),"NEW",false, pair.getKey().toString());
        }
    }

    /* constant for defining the time duration between the click that can be considered as double-tap */
    private final class ChoiceTouchListener implements View.OnTouchListener {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            int X = (int) motionEvent.getRawX();
            int Y = (int) motionEvent.getRawY();
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    xDelta = X - layoutParams.leftMargin;
                    yDelta = Y - layoutParams.topMargin;

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
                    //Log.d("Log", "Zitrone: " + String.valueOf(view.getTag()) + ":" + String.valueOf(layoutParams.leftMargin));
                    if (view.getTag()!=null){psychoSocialBeforeOfPatient.setCoordinates(view.getTag().toString(), layoutParams.leftMargin, layoutParams.topMargin);}
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
    }

    public void updateImprovementReason(final ImprovementReason updatedImprovementReason) {
        db.updateImprovementReason(patientId, updatedImprovementReason);
    }

    public void setImprovementReason() {
        boolean improvementReasonExists = db.existsImprovementReason(patientId);
        if (improvementReasonExists) {
            updateImprovementReason(improvementReasonOfPatient);
        } else {
            addNewImprovementReason(improvementReasonOfPatient);
        }
    }

    public void addNewPsychoSocialBefore(PsychoSocialBefore psychoSocialBefore) {
        db.addPsychoSocialBefore(psychoSocialBefore);
    }

    public void updatePsychoSocialBefore(final PsychoSocialBefore updatedPsychoSocialBefore) {
        db.updatePsychoSocialBefore(patientId, updatedPsychoSocialBefore);
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
    }

    public void addNewPsychoSocialAfter(PsychoSocialAfter psychoSocialAfter) {
        db.addPsychoSocialAfter(psychoSocialAfter);
    }

    public void updatePsychoSocialAfter(final PsychoSocialAfter updatedPsychoSocialAfter) {
        db.updatePsychoSocialAfter(patientId, updatedPsychoSocialAfter);
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
        // Separate method for dynamically generated buttons: Flips the colour in the class itself,
        // then sets the button colour of the current button that we clicked on.
        if (clickedButton.getTag() != null){
            psychoSocialBeforeOfPatient.flipColour(clickedButton.getTag().toString());
            setButtonColor((Button) clickedButton, psychoSocialBeforeOfPatient.getByTagAndIndex(clickedButton.getTag().toString(), 3));
        }
        //.......................
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
        // Separate method for dynamically added buttons
        if (clickedItem.getTag()!=null){
            psychoSocialBeforeOfPatient.setSize(clickedItem.getTag().toString(), size);
        }
        //..........................
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

