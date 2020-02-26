package com.example.frontend.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frontend.Models.ImprovementReason;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;
import com.example.frontend.Service.JsonPlaceHolderApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ReasonDialog extends AppCompatDialogFragment {
    private int patientId;
    private CheckBox cbDrugs;
    private CheckBox cbExercises;
    private CheckBox cbAwareness;
    private CheckBox cbOthers;
    private EditText etOthers;
    DatabaseHelper db;

    /* Only used for Heruoku Database
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://consapp.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    */

    public interface ReasonDialogListener {
        void applyTexts(boolean drugsReason, boolean exercisesReason, boolean awarenessReason, boolean otherReasons, String otherReasonsText);
    }

    public ReasonDialogListener listener ;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        patientId = getArguments().getInt("patient_id");
        db = new DatabaseHelper(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_reason_dialog, null);

        builder.setView(view)
                .setTitle(R.string.improvement_reason)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String otherReasonsText = "";
                        boolean drugsReason = cbDrugs.isChecked();
                        boolean exercisesReason = cbExercises.isChecked();
                        boolean awarenessReason = cbAwareness.isChecked();
                        boolean otherReason = cbOthers.isChecked();
                        if(etOthers.getText()!=null){
                            otherReasonsText = etOthers.getText().toString();
                        }
                        listener.applyTexts(drugsReason,exercisesReason,awarenessReason,otherReason,otherReasonsText);
                    }
                });
        cbDrugs = view.findViewById(R.id.cbDrugs);
        cbExercises = view.findViewById(R.id.cbExercises);
        cbAwareness = view.findViewById(R.id.cbAwareness);
        cbOthers = view.findViewById(R.id.cbOthers);
        etOthers = view.findViewById(R.id.etOtherReason);

        cbOthers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cbOthers.isChecked()){
                    etOthers.setVisibility(View.VISIBLE);
                }else{
                    etOthers.setVisibility(View.INVISIBLE);
                }
            }
        });
        setImprovementReason();

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ReasonDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ReasonDialogListener ");
        }
    }

    public void getImprovementReason() {
        ImprovementReason improvementReasonOfPatient = db.getImprovementReason(patientId);
        cbDrugs.setChecked(improvementReasonOfPatient.isDrugs());
        cbExercises.setChecked(improvementReasonOfPatient.isExercises());
        cbAwareness.setChecked(improvementReasonOfPatient.isAwareness());
        cbOthers.setChecked(improvementReasonOfPatient.isOther_reason());
        etOthers.setText(improvementReasonOfPatient.getOther_reason_text());

        /* Only used for Heruoku Database

        Call<ImprovementReason> call = jsonPlaceHolderApi.getImprovementReason(patientId);
        call.enqueue(new Callback<ImprovementReason>() {
            @Override
            public void onResponse(Call<ImprovementReason> call, Response<ImprovementReason> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Get improvement reason not successful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ImprovementReason improvementReasonOfPatient = response.body();
                    cbDrugs.setChecked(improvementReasonOfPatient.isDrugs());
                    cbExercises.setChecked(improvementReasonOfPatient.isExercises());
                    cbAwareness.setChecked(improvementReasonOfPatient.isAwareness());
                    cbOthers.setChecked(improvementReasonOfPatient.isOther_reason());
                    etOthers.setText(improvementReasonOfPatient.getOther_reason_text());
                }
            }

            @Override
            public void onFailure(Call<ImprovementReason> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }


    public void setImprovementReason() {
        boolean improvementReasonExists = db.existsImprovementReason(patientId);
        if (improvementReasonExists) {
            getImprovementReason();

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
                    if(improvementReasonExists){
                        getImprovementReason();                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
        }
    }
}
