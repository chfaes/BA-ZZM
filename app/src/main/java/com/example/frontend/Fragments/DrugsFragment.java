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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.frontend.Fragments.Dialogs.DrugDialog;
import com.example.frontend.Fragments.Dialogs.DrugTypeDialog;
import com.example.frontend.Models.DrugType;
import com.example.frontend.Models.Patient;
import com.example.frontend.Models.PatientDrug;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;
import com.example.frontend.Service.JsonPlaceHolderApi;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DrugsFragment extends Fragment implements DrugDialog.DrugDialogListener, DrugTypeDialog.DrugTypeDialogListener {
    private View cView;
    DatabaseHelper db;
    private List<DrugType> allDrugTypes = new ArrayList<>();
    private List<PatientDrug> allDrugsOfPatient = new ArrayList<>();
    private List<Integer> allDrugIdsOfPatient = new ArrayList<>();
    private PatientDrug selectedPatientDrug = new PatientDrug();
    private Button selectedDrugButton;
    private int patientId;
    private int columnCounter = 1;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private boolean editDrugType = false;
    private int lastDrugTypeId;
    private ImageView btnAddNewDrug;

    /*Only used for Heruoku Database

    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://consapp.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        patientId = getArguments().getInt("patientId");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drugs, container, false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        cView = view;
        db = new DatabaseHelper(getContext());
        ll1 = (LinearLayout) cView.findViewById(R.id.llFirstColumn);
        ll2 = (LinearLayout) cView.findViewById(R.id.llSecondColumn);
        ll3 = (LinearLayout) cView.findViewById(R.id.llThirdColumn);
        btnAddNewDrug = (ImageView) cView.findViewById(R.id.btnAddDrug);
        btnAddNewDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrugTypeDialog(null);
            }
        });
        selectedDrugButton = new Button(getContext());
        addDrugButtons(patientId);
    }

    public void addAllDrugsTypes() {
        columnCounter = 1;

        allDrugTypes = db.getAllDrugTypes();
        for (DrugType drugType : allDrugTypes) {
            addDrugTypeBtn(drugType.getId(), drugType);
        }

         /*Only used for Heruoku Database

        Call<List<DrugType>> call = jsonPlaceHolderApi.getAllDrugTypes();
        call.enqueue(new Callback<List<DrugType>>() {
            @Override
            public void onResponse(Call<List<DrugType>> call, Response<List<DrugType>> response) {
                if (!response.isSuccessful()) {
                    return;
                } else {
                    allDrugTypes = response.body();

                    for (DrugType drugType : allDrugTypes) {
                        addDrugTypeBtn(drugType.getId(), drugType);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DrugType>> call, Throwable t) {
            }
        });*/
    }

    public void addDrugTypeBtn(int id, final DrugType drugType) {
        final Button btnDrugType = new Button(getContext());
        btnDrugType.setText(drugType.getName());
       // TooltipCompat.setTooltipText(btnDrugType.getRootView(), drugType.getDescription());
        btnDrugType.setId(id);
        btnDrugType.setTextSize(18);
        btnDrugType.setPadding(0, 45, 0, 45);
        btnDrugType.setBackgroundResource(R.drawable.button_selector_effect);
        btnDrugType.setTransformationMethod(null);
        registerForContextMenu(btnDrugType);
        if (allDrugIdsOfPatient.contains(drugType.getId())) {
            btnDrugType.setSelected(true);
            btnDrugType.setTextColor(Color.WHITE);
        }

        btnDrugType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnDrugType.isSelected()) {
                    deletePatientDrug(patientId, drugType.getId());
                    btnDrugType.setSelected(false);
                    btnDrugType.setTextColor(Color.BLACK);
                } else {
                    selectedPatientDrug.setPatientId(patientId);
                    selectedPatientDrug.setDrugId(drugType.getId());
                    selectedDrugButton = btnDrugType;
                    openDrugDialog();
                }
            }
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 30, 20, 30);
        switch (columnCounter) {
            case 1:
                ll1.addView(btnDrugType, lp);
                columnCounter++;
                break;
            case 2:
                ll2.addView(btnDrugType, lp);
                columnCounter++;
                break;
            case 3:
                ll3.addView(btnDrugType, lp);
                columnCounter = 1;
                break;
        }
    }

    public void addDrugButtons(final int patientId) {
        //Get all PatientDrugs of Patient
        allDrugsOfPatient= db.getAllDrugsOfPatient(patientId);

        for (PatientDrug patientDrug : allDrugsOfPatient) {
            allDrugIdsOfPatient.add(patientDrug.getDrugTypeId());
        }

        //Add all Buttons
        addAllDrugsTypes();

        /*Only used for Heruoku Database

        Call<List<PatientDrug>> call = jsonPlaceHolderApi.getAllDrugsOfPatient(patientId);
        call.enqueue(new Callback<List<PatientDrug>>() {
            @Override
            public void onResponse(Call<List<PatientDrug>> call, Response<List<PatientDrug>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "not successful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allDrugsOfPatient = response.body();

                    for (PatientDrug patientDrug : allDrugsOfPatient) {
                        allDrugIdsOfPatient.add(patientDrug.getDrugTypeId());
                    }
                    //Add all Buttons
                    addAllDrugsTypes();
                }
            }

            @Override
            public void onFailure(Call<List<PatientDrug>> call, Throwable t) {
                // tvPatientlist.setText(t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void addNewPatientDrug(PatientDrug patientDrug) {
        //add selected drug for patient in database
        db.addPatientDrug(patientDrug);

        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createPatientDrug(patientDrug);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        }); */
    }

    public void deletePatientDrug(int patientId, int drugtypeId) {
        db.deletePatientDrug(patientId, drugtypeId);
        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.deletePatientDrug(patientId, drugtypeId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });*/
    }

    public void openDrugDialog() {
        DrugDialog drugDialog = new DrugDialog();
        drugDialog.setTargetFragment(DrugsFragment.this, 1);
        drugDialog.show(getActivity().getSupportFragmentManager(), "Drug Dialog");
    }

    @Override
    public void applyTexts(String amount, String dosis, String comment) {
        if (!amount.isEmpty()) {
            selectedPatientDrug.setAmount(amount);
        }
        if (!dosis.isEmpty()) {
            selectedPatientDrug.setDosis(dosis);
        }
        if (!dosis.isEmpty()) {
            selectedPatientDrug.setComment(comment);
        }
        addNewPatientDrug(selectedPatientDrug);
        selectedDrugButton.setSelected(true);
        selectedDrugButton.setTextColor(Color.WHITE);
    }

    public void openDrugTypeDialog(DrugType drugType) {
        DrugTypeDialog drugTypeDialog = new DrugTypeDialog();
        Bundle args = new Bundle();
        args.putSerializable("drugtype", drugType);
        drugTypeDialog.setArguments(args);
        drugTypeDialog.setTargetFragment(DrugsFragment.this, 1);
        drugTypeDialog.show(getActivity().getSupportFragmentManager(), "DrugType Dialog");
    }

    @Override
    public void applyDrugType(String name, String description) {
        DrugType newDrugType = new DrugType();
        if (!name.isEmpty()) {
            newDrugType.setName(name);
        }
        if (!description.isEmpty()) {
            newDrugType.setDescription(description);
        }
        if(editDrugType)
        {
            updateDrugType(lastDrugTypeId, newDrugType);
            ll1.removeAllViews();
            ll2.removeAllViews();
            ll3.removeAllViews();
            addDrugButtons(patientId);
            editDrugType = false;
        }else{
            addNewDrugType(newDrugType);
        }
    }

    public void addNewDrugType(final DrugType drugType) {
        db.addDrugType(drugType);
        getInsertDrugTypeId(drugType);

        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createDrugType(drugType);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getInsertDrugTypeId(drugType);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createDrugType NOT successful", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void getInsertDrugTypeId(final DrugType drugType) {
        lastDrugTypeId = db.selectLastDrugTypeId();
        addDrugTypeBtn(lastDrugTypeId,drugType);

        /*Only used for Heruoku Database

        Call<Integer> call = jsonPlaceHolderApi.getLastDrugTypeId();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                lastDrugTypeId = response.body();
                addDrugTypeBtn(lastDrugTypeId, drugType);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getActivity(), "getId NOT successful", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void updateDrugType(int drugTypeId, final DrugType updatedDrugType) {
        db.updateDrugType(drugTypeId,updatedDrugType);

        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.updateDrugType(drugTypeId, updatedDrugType);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "create drugType NOT successful", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_delete_menu, menu);
        lastDrugTypeId = v.getId();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Button selectedBtn = cView.findViewById(lastDrugTypeId);
        switch (item.getItemId()) {
            case R.id.editOption:
                editDrugType = true;
                setUpDrugTypeDialog(lastDrugTypeId);
                return true;
            case R.id.deleteOption:
                deleteDrugType(lastDrugTypeId);
                //((ViewManager)selectedBtn.getParent()).removeView(selectedBtn);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void setUpDrugTypeDialog(int id) {
        openDrugTypeDialog(db.getDrugType(id));

        /*Only used for Heruoku Database

        Call<DrugType> call = jsonPlaceHolderApi.getDrugType(id);
        call.enqueue(new Callback<DrugType>() {
            @Override
            public void onResponse(Call<DrugType> call, Response<DrugType> response) {
                DrugType drugType = response.body();
                openDrugTypeDialog(drugType);
            }

            @Override
            public void onFailure(Call<DrugType> call, Throwable t) {
                Toast.makeText(getActivity(), "get drugType Id NOT successful", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void deleteDrugType(int drugTypeId) {
        db.deleteDrugType(drugTypeId);
        ll1.removeAllViews();
        ll2.removeAllViews();
        ll3.removeAllViews();
        addDrugButtons(patientId);

        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.deleteDrugType(drugTypeId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ll1.removeAllViews();
                ll2.removeAllViews();
                ll3.removeAllViews();
                addDrugButtons(patientId);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });*/
    }

}
