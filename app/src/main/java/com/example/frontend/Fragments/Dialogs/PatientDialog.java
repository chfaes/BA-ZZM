package com.example.frontend.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.frontend.Models.Patient;
import com.example.frontend.R;
import com.example.frontend.Service.JsonPlaceHolderApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PatientDialog extends AppCompatDialogFragment {
    private EditText etName;
    private boolean nameEmpty = true;
    private RadioGroup rgGender;
    Patient patient;

    public interface PatientDialogListener {
        void applyTexts(String name, String gender);
    }

    public PatientDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        patient = (Patient) getArguments().getSerializable("patient");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_patient_dialog, null);

        builder.setView(view)
                .setTitle(R.string.create_patient)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = "";
                        String gender = "";
                        name = etName.getText().toString();
                        switch (rgGender.getCheckedRadioButtonId()) {
                            case R.id.rbMale:
                                gender = "Male";
                                break;
                            case R.id.rbFemale:
                                gender = "Female";
                                break;

                        }
                        listener.applyTexts(name, gender);
                    }
                });

        etName = view.findViewById(R.id.etName);
        rgGender = view.findViewById(R.id.rgGender);


        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0){
                    nameEmpty = false;
                }else{
                    nameEmpty = true;
                }
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                validateInputs();
            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);


        if(patient != null){
            dialog.setTitle(R.string.edit_patient);
            etName.setText(patient.getShortname());
            if(patient.getGender().equals("Female")) {
                rgGender.check(R.id.rbFemale);
            }else{
                rgGender.check(R.id.rbMale);
            }
        }

    }

    private void validateInputs(){
        AlertDialog dialog = (AlertDialog) getDialog();
        if(!nameEmpty && !(rgGender.getCheckedRadioButtonId()==-1)){
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        }else{
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PatientDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PatientDialogListener ");
        }
    }

}
