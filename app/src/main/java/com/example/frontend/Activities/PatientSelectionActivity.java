package com.example.frontend.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.Fragments.Dialogs.DrugDialog;
import com.example.frontend.Fragments.Dialogs.PatientDialog;
import com.example.frontend.Fragments.DrugsFragment;
import com.example.frontend.Models.Note;
import com.example.frontend.Service.DatabaseHelper;
import com.example.frontend.Service.JsonPlaceHolderApi;
import com.example.frontend.Models.Patient;
import com.example.frontend.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PatientSelectionActivity extends AppCompatActivity implements PatientDialog.PatientDialogListener {
    DatabaseHelper db;
    private TextView btnPatientTitle;
    private ImageView btnAddPatient;
    int lastPatientId;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private Button selectedIv;
    private String username = "";
    private List<Patient> allPatients;
    private int columnCounter = 1;
    Context context = this;
    private boolean editPatient = false;

    /* Only used for Heruoku Database

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://consapp.herokuapp.com/api/v1/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_selection);

        db = new DatabaseHelper(this);

        ll1 = (LinearLayout) findViewById(R.id.llFirstColumn);
        ll2 = (LinearLayout) findViewById(R.id.llSecondColumn);
        ll3 = (LinearLayout) findViewById(R.id.llThirdColumn);
        btnPatientTitle = (TextView) findViewById(R.id.btnPatient);
        btnPatientTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll1.removeAllViews();
                ll2.removeAllViews();
                ll3.removeAllViews();
                addAllPatients();
            }
        });

        btnAddPatient = (ImageView) findViewById(R.id.btnAddPatient);
        btnAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPatientDialog(null);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("usernameKey");
        }
        addAllPatients();
    }

    public void openPatientDialog(Patient patient) {
        PatientDialog patientDialog = new PatientDialog();
        Bundle args = new Bundle();
        args.putSerializable("patient", patient);
        patientDialog.setArguments(args);
        patientDialog.show(getSupportFragmentManager(), "Patient Dialog");
    }

    @Override
    public void applyTexts(String name, String gender) {
        Patient newPatient = new Patient();
        if (!name.isEmpty()) {
            newPatient.setShortname(name);
        }
        if (!gender.isEmpty()) {
            newPatient.setGender(gender);
        }
        if(editPatient)
        {
            updatePatient(lastPatientId, newPatient);
            ll1.removeAllViews();
            ll2.removeAllViews();
            ll3.removeAllViews();
            addAllPatients();
            editPatient = false;
        }else{
            addNewPatient(newPatient);
        }
    }

    public void addNewPatient(final Patient patient) {
        db.addPatient(patient);
        getInsertPatientId(patient);

        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createPatient(patient);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getInsertPatientId(patient);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PatientSelectionActivity.this, "createPatient NOT successful", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void navigateNextActivity(Patient patient) {
        //jump to Menu
        Intent intent = new Intent(PatientSelectionActivity.this, MenuActivity.class);
        intent.putExtra("patient", patient);
        startActivity(intent);
    }

    public void addAllPatients() {
        columnCounter = 1;
        allPatients = db.getAllPatients();
        for (Patient patient : allPatients) {
            addPatientBtn(patient.getId(), patient);
        }

        /*Only used for Heruoku Database

        Call<List<Patient>> call = jsonPlaceHolderApi.getAllPatients();
        call.enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (!response.isSuccessful()) {
                    return;
                } else {
                    allPatients = response.body();

                    for (Patient patient : allPatients) {
                        addPatientBtn(patient.getId(), patient);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
            }
        });*/
    }

    public void addPatientBtn(int id, final Patient patient) {
        Button btnPatient = new Button(context);
        btnPatient.setId(id);
        patient.setId(id);
        btnPatient.setText(patient.getShortname());
        btnPatient.setTextSize(18);
        btnPatient.setPadding(0, 45, 0, 45);
        btnPatient.setBackgroundResource(R.drawable.button_style);
        btnPatient.setTransformationMethod(null);
        btnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateNextActivity(patient);
            }
        });
        registerForContextMenu(btnPatient);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(15, 25, 15, 25);
        switch (columnCounter) {
            case 1:
                ll1.addView(btnPatient, lp);
                columnCounter++;
                break;
            case 2:
                ll2.addView(btnPatient, lp);
                columnCounter++;
                break;
            case 3:
                ll3.addView(btnPatient, lp);
                columnCounter = 1;
                break;
        }
    }

    public void getInsertPatientId(final Patient patient) {
        lastPatientId = db.selectLastPatientId();
        addPatientBtn(lastPatientId, patient);

        /*Only used for Heruoku Database

        Call<Integer> call = jsonPlaceHolderApi.getLastPatientId();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                lastPatientId = response.body();
                addPatientBtn(lastPatientId, patient);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(PatientSelectionActivity.this, "getId NOT successful", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.edit_delete_menu, menu);
        lastPatientId = v.getId();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        selectedIv = (Button) findViewById(lastPatientId);
        switch (item.getItemId()) {
            case R.id.editOption:
                editPatient = true;
                setUpPatientDialog(lastPatientId);
                return true;
            case R.id.deleteOption:
                deletePatient(lastPatientId);
                //((ViewManager)selectedIv.getParent()).removeView(selectedIv);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void setUpPatientDialog(int id) {
        openPatientDialog(db.getPatient(id));

        /* Only used for Heruoku Database

        Call<Patient> call = jsonPlaceHolderApi.getPatient(id);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                Patient patient = response.body();
                openPatientDialog(patient);
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Toast.makeText(PatientSelectionActivity.this, "get patient Id NOT successful", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void deletePatient(int patientId) {
        db.deletePatient(patientId);

        ll1.removeAllViews();
        ll2.removeAllViews();
        ll3.removeAllViews();
        addAllPatients();

        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.deletePatient(patientId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ll1.removeAllViews();
                ll2.removeAllViews();
                ll3.removeAllViews();
                addAllPatients();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });*/
    }

    public void updatePatient(int patientId, final Patient updatedPatient) {
        db.updatePatient(patientId,updatedPatient);

        /* Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.updatePatient(patientId, updatedPatient);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PatientSelectionActivity.this, "create patient NOT successful", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

}
