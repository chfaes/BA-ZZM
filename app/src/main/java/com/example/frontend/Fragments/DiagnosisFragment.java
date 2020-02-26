package com.example.frontend.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.TooltipCompat;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.frontend.Fragments.Dialogs.DiagnosisDialog;
import com.example.frontend.Fragments.Dialogs.DiagnosisPriorityDialog;
import com.example.frontend.Fragments.Dialogs.DiagnosisTypeDialog;
import com.example.frontend.Models.DiagnosisType;
import com.example.frontend.Models.PatientDiagnosis;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;
import com.example.frontend.Service.JsonPlaceHolderApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DiagnosisFragment extends Fragment implements DiagnosisDialog.DiagnosisDialogListener, DiagnosisTypeDialog.DiagnosisTypeDialogListener , DiagnosisPriorityDialog.DiagnosisPriorityDialogListener {

    private int patientId;
    private View cView;
    DatabaseHelper db;
    private List<String> diagnosisClasses = new ArrayList<>();
    private List<DiagnosisType> allDiagnosisTypesOfClass = new ArrayList<>();
    private List<Integer> allPatientDiagnosisIdsOfClass = new ArrayList<>();
    private List<PatientDiagnosis> allPatientDiagnosisOfClass = new ArrayList<>();
    private PatientDiagnosis selectedPatientDiagnosis = new PatientDiagnosis();
    private int columnCounter = 1;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private RadioGroup radioGroup;
    private boolean editDiagnosisType = false;
    private int lastDiagnosisTypeId;
    private Button selectedDiagnosisBtn;
    private ImageView btnAddNewDiagnosis;
    private String lastInsertedClass = null;
    private String deletedDiagnosisTypeClass = null;

    private Button diagnosisPriorityBtn;

    /*Only used for Heruoku Database

    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://consapp.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class); */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        patientId = getArguments().getInt("patientId");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagnosis, container, false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        cView = view;
        db = new DatabaseHelper(getContext());
        ll1 = (LinearLayout) cView.findViewById(R.id.llFirstColumn);
        ll2 = (LinearLayout) cView.findViewById(R.id.llSecondColumn);
        ll3 = (LinearLayout) cView.findViewById(R.id.llThirdColumn);
        radioGroup = (RadioGroup) cView.findViewById(R.id.rgClasses);
        btnAddNewDiagnosis = (ImageView) cView.findViewById(R.id.btnAddDiagnosis);
        btnAddNewDiagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDiagnosisTypeDialog(null);
            }
        });
        selectedDiagnosisBtn = new Button(getContext());
        addClassButtons();

        diagnosisPriorityBtn = (Button) cView.findViewById(R.id.btnDiagnosisPriority);
        diagnosisPriorityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDiagnosisPriorityDialog();
            }
        });

    }

    public void addClassButtons() {
        diagnosisClasses = db.getAllDiagnosisTypeClasses();
        createClassRadioButtons(diagnosisClasses);

        /*Only used for Heruoku Database

        Call<List<String>> call = jsonPlaceHolderApi.getDiagnosisClasses();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "GetDiagnosisClasses not successful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    diagnosisClasses = response.body();
                    createClassRadioButtons(diagnosisClasses);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    private void createClassRadioButtons(List<String> allDiagnosisClasses) {
        String classToSelect = allDiagnosisClasses.get(0);
        if (lastInsertedClass != null) {
            classToSelect = lastInsertedClass;
            lastInsertedClass = null;
        }
        if (deletedDiagnosisTypeClass != null && allDiagnosisClasses.contains(deletedDiagnosisTypeClass)) {
            classToSelect = deletedDiagnosisTypeClass;
            deletedDiagnosisTypeClass = null;
        }
        for (String diagnosisClass : allDiagnosisClasses) {
            final RadioButton radioBtn = new RadioButton(getContext());
            radioBtn.setId(allDiagnosisClasses.indexOf(diagnosisClass));
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            radioBtn.setLayoutParams(lp);
            radioBtn.setBackgroundResource(R.drawable.radiobutton_selector);
            radioBtn.setGravity(Gravity.CENTER);
            radioBtn.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
            radioBtn.setPadding(0, 20, 0, 20);
            radioBtn.setTextSize(18);
            radioBtn.setSingleLine(true);
            radioBtn.setLayoutParams(lp);
            radioBtn.setText(diagnosisClass);
            radioBtn.setTextColor(getResources().getColorStateList(R.color.radiobutton_text_selected));
            radioBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allPatientDiagnosisIdsOfClass.clear();
                    addDiagnosisButtons((String) radioBtn.getText());
                }
            });
            radioGroup.addView(radioBtn);
            if (radioBtn.getText().toString().equals(classToSelect)) {
                allPatientDiagnosisIdsOfClass.clear();
                addDiagnosisButtons((String) radioBtn.getText());
                radioGroup.clearCheck();
                radioBtn.setChecked(true);
            }
        }
    }

    public void addDiagnosisButtons(final String selectedClass) {
        columnCounter = 1;

        removeAllDiagnosisBtns();

        //Get all PatientDiagnoses of Patient
        allPatientDiagnosisOfClass = db.getPatientDiagnosisOfClass(patientId, selectedClass);

        for (PatientDiagnosis patientDiagnosis : allPatientDiagnosisOfClass) {
            allPatientDiagnosisIdsOfClass.add(patientDiagnosis.getDiagnosisId());
        }

        //Add all Buttons
        addAllDiagnosisTypes(selectedClass);

        /*Only used for Heruoku Database

        Call<List<PatientDiagnosis>> call = jsonPlaceHolderApi.getPatientDiagnosesOfClass(patientId, selectedClass);
        call.enqueue(new Callback<List<PatientDiagnosis>>() {
            @Override
            public void onResponse(Call<List<PatientDiagnosis>> call, Response<List<PatientDiagnosis>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "GetPatientDiagnosesOfClass not successful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    removeAllDiagnosisBtns();
                    allPatientDiagnosisOfClass = response.body();

                    for (PatientDiagnosis patientDiagnosis : allPatientDiagnosisOfClass) {
                        allPatientDiagnosisIdsOfClass.add(patientDiagnosis.getDiagnosisId());
                    }
                    //Add all Buttons
                    addAllDiagnosisTypes(selectedClass);
                }
            }

            @Override
            public void onFailure(Call<List<PatientDiagnosis>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void addAllDiagnosisTypes(String selectedClass) {
        allDiagnosisTypesOfClass = db.getDiagnosisTypesByClass(selectedClass);
        for (DiagnosisType diagnosisType : allDiagnosisTypesOfClass) {
            addDiagnosisTypeBtn(diagnosisType.getId(), diagnosisType);
        }

        /*Only used for Heruoku Database

        Call<List<DiagnosisType>> call = jsonPlaceHolderApi.getAllDiagnosisTypesOfClass(selectedClass);
        call.enqueue(new Callback<List<DiagnosisType>>() {
            @Override
            public void onResponse(Call<List<DiagnosisType>> call, Response<List<DiagnosisType>> response) {
                if (!response.isSuccessful()) {
                    return;
                } else {
                    allDiagnosisTypesOfClass = response.body();
                    for (DiagnosisType diagnosisType : allDiagnosisTypesOfClass) {
                        addDiagnosisTypeBtn(diagnosisType.getId(), diagnosisType);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DiagnosisType>> call, Throwable t) {
            }
        });*/
    }

    public void addDiagnosisTypeBtn(int id, final DiagnosisType diagnosisType) {
        final Button btnDiagnosisType = new Button(getContext());
        btnDiagnosisType.setText(diagnosisType.getName());
        // TooltipCompat.setTooltipText(btnDiagnosisType.getRootView(), diagnosisType.getDescription());
        btnDiagnosisType.setId(id);
        btnDiagnosisType.setTextSize(16);
//        btnDiagnosisType.setSingleLine(true);
        btnDiagnosisType.setHeight(210);
        btnDiagnosisType.setPadding(45, 45, 45, 45);
        btnDiagnosisType.setBackgroundResource(R.drawable.button_selector_effect);
        btnDiagnosisType.setTransformationMethod(null);
        registerForContextMenu(btnDiagnosisType);
        if (allPatientDiagnosisIdsOfClass.contains(diagnosisType.getId())) {
            btnDiagnosisType.setSelected(true);
            btnDiagnosisType.setTextColor(Color.WHITE);
        }
        btnDiagnosisType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnDiagnosisType.isSelected()) {
                    deletePatientDiagnosis(patientId, diagnosisType.getId());
                    btnDiagnosisType.setSelected(false);
                    btnDiagnosisType.setTextColor(Color.BLACK);
                } else {
                    selectedPatientDiagnosis = new PatientDiagnosis();
                    selectedPatientDiagnosis.setPatientId(patientId);
                    selectedPatientDiagnosis.setDiagnosisId(diagnosisType.getId());
                    selectedDiagnosisBtn = btnDiagnosisType;
                    openDiagnosisDialog();
                }
            }
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 30, 20, 30);
        switch (columnCounter) {
            case 1:
                ll1.addView(btnDiagnosisType, lp);
                columnCounter++;
                break;
            case 2:
                ll2.addView(btnDiagnosisType, lp);
                columnCounter++;
                break;
            case 3:
                ll3.addView(btnDiagnosisType, lp);
                columnCounter = 1;
                break;
        }
    }

    public void addNewPatientDiagnosis(final PatientDiagnosis patientDiagnosis) {
        db.addPatientDiagnosis(patientDiagnosis);

        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createPatientDiagnosis(patientDiagnosis);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createPatientDiagnosis NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void deletePatientDiagnosis(int patientId, int diagnosistypeId) {
        db.deletePatientDiagnosis(patientId,diagnosistypeId);

        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.deletePatientDiagnosis(patientId, diagnosistypeId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        }); */
    }

    public void removeAllDiagnosisBtns() {
        if (ll1.getChildCount() > 0)
            ll1.removeAllViews();
        if (ll2.getChildCount() > 0)
            ll2.removeAllViews();
        if (ll3.getChildCount() > 0)
            ll3.removeAllViews();
        columnCounter = 1;
    }

    public void openDiagnosisPriorityDialog() {
        List<PatientDiagnosis> allPatientDiagnosis = db.getAllDiagnosesOfPatient(patientId);
        List<DiagnosisType> allDiagnosisTypes = db.getAllDiagnosisTypes();

        ArrayList<PatientDiagnosis> arrayListAllPatientDiagnosis = new ArrayList<>(allPatientDiagnosis.size());
        arrayListAllPatientDiagnosis.addAll(allPatientDiagnosis);
        ArrayList<DiagnosisType> arrayListAllDiagnosisTypes = new ArrayList<>(allDiagnosisTypes.size());
        arrayListAllDiagnosisTypes.addAll(allDiagnosisTypes);

        DiagnosisPriorityDialog diagnosisPriorityDialog = new DiagnosisPriorityDialog();
        Bundle args = new Bundle();
        args.putSerializable("patientDiagnosesList", arrayListAllPatientDiagnosis);
        args.putSerializable("diagnosisTypesList", arrayListAllDiagnosisTypes);

        diagnosisPriorityDialog.setArguments(args);
        diagnosisPriorityDialog.setTargetFragment(DiagnosisFragment.this, 1);
        diagnosisPriorityDialog.show(getActivity().getSupportFragmentManager(), "Diagnosis Priority Dialog");
    }

    public void openDiagnosisDialog() {
        DiagnosisDialog diagnosisDialog = new DiagnosisDialog();
        diagnosisDialog.setTargetFragment(DiagnosisFragment.this, 1);
        diagnosisDialog.show(getActivity().getSupportFragmentManager(), "Diagnosis Dialog");
    }

    @Override
    public void applyTexts(String comment) {
        if (!comment.isEmpty()) {
            selectedPatientDiagnosis.setComment(comment);
        }
        addNewPatientDiagnosis(selectedPatientDiagnosis);
        selectedDiagnosisBtn.setSelected(true);
        selectedDiagnosisBtn.setTextColor(Color.WHITE);
    }


    public void openDiagnosisTypeDialog(DiagnosisType diagnosisType) {
        DiagnosisTypeDialog diagnosisTypeDialog = new DiagnosisTypeDialog();
        Bundle args = new Bundle();
        args.putSerializable("diagnosistype", diagnosisType);
        diagnosisTypeDialog.setArguments(args);
        diagnosisTypeDialog.setTargetFragment(DiagnosisFragment.this, 1);
        diagnosisTypeDialog.show(getActivity().getSupportFragmentManager(), "DiagnosisType Dialog");
    }


    @Override
    public void applyDiagnosisType(String name, String type, String description) {
        DiagnosisType newDiagnosisType = new DiagnosisType();
        if (!name.isEmpty()) {
            newDiagnosisType.setName(name);
        }
        if (!description.isEmpty()) {
            newDiagnosisType.setDescription(description);
        }
        if (!type.isEmpty()) {
            newDiagnosisType.setType(type);
            lastInsertedClass = type;
        }
        if (editDiagnosisType) {
            updateDiagnosisType(lastDiagnosisTypeId, newDiagnosisType);
            RadioButton selectedRadioBtn = cView.findViewById(radioGroup.getCheckedRadioButtonId());
            deletedDiagnosisTypeClass = selectedRadioBtn.getText().toString();
            radioGroup.removeAllViews();
            removeAllDiagnosisBtns();
            addClassButtons();
            editDiagnosisType = false;
        } else {
            addNewDiagnosisType(newDiagnosisType);
        }
    }

    public void addNewDiagnosisType(final DiagnosisType diagnosisType) {
        db.addDiagnosisType(diagnosisType);
        getInsertDiagnosisTypeId(diagnosisType);

        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createDiagnosisType(diagnosisType);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getInsertDiagnosisTypeId(diagnosisType);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createDiagnosisType NOT successful", Toast.LENGTH_SHORT).show();
            }
        });  */
    }

    public void getInsertDiagnosisTypeId(final DiagnosisType diagnosisType) {
        lastDiagnosisTypeId = db.selectLastDiagnosisTypeId();
        radioGroup.removeAllViews();
        removeAllDiagnosisBtns();
        addClassButtons();

        /*Only used for Heruoku Database

        Call<Integer> call = jsonPlaceHolderApi.getLastDiagnosisTypeId();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                lastDiagnosisTypeId = response.body();
                //addDiagnosisTypeBtn(lastDiagnosisTypeId, diagnosisType);
                radioGroup.removeAllViews();
                removeAllDiagnosisBtns();
                addClassButtons();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getActivity(), "getId NOT successful", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void updateDiagnosisType(int diagnosisTypeId, final DiagnosisType updatedDiagnosisType) {
        db.updateDiagnosisType(diagnosisTypeId, updatedDiagnosisType);

        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.updateDiagnosisType(diagnosisTypeId, updatedDiagnosisType);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "create diagnosisType NOT successful", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_delete_menu, menu);
        lastDiagnosisTypeId = v.getId();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Button selectedBtn = cView.findViewById(lastDiagnosisTypeId);
        switch (item.getItemId()) {
            case R.id.editOption:
                editDiagnosisType = true;
                setUpDiagnosisTypeDialog(lastDiagnosisTypeId);
                return true;
            case R.id.deleteOption:
                RadioButton selectedRadioBtn = cView.findViewById(radioGroup.getCheckedRadioButtonId());
                deletedDiagnosisTypeClass = selectedRadioBtn.getText().toString();
                deleteDiagnosisType(lastDiagnosisTypeId);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void setUpDiagnosisTypeDialog(int id) {
        DiagnosisType diagnosisType = db.getDiagnosisType(id);
        openDiagnosisTypeDialog(diagnosisType);

        /*Only used for Heruoku Database

        Call<DiagnosisType> call = jsonPlaceHolderApi.getDiagnosisType(id);
        call.enqueue(new Callback<DiagnosisType>() {
            @Override
            public void onResponse(Call<DiagnosisType> call, Response<DiagnosisType> response) {
                DiagnosisType diagnosisType = response.body();
                openDiagnosisTypeDialog(diagnosisType);
            }

            @Override
            public void onFailure(Call<DiagnosisType> call, Throwable t) {
                Toast.makeText(getActivity(), "get diagnosisType Id NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void deleteDiagnosisType(int diagnosisTypeId) {
        db.deleteDiagnosisType(diagnosisTypeId);
        radioGroup.removeAllViews();
        removeAllDiagnosisBtns();
        addClassButtons();

        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.deleteDiagnosisType(diagnosisTypeId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                radioGroup.removeAllViews();
                removeAllDiagnosisBtns();
                addClassButtons();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        }); */
    }

}
