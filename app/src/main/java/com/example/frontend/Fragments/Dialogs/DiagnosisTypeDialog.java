package com.example.frontend.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.frontend.Models.DiagnosisType;
import com.example.frontend.R;


public class DiagnosisTypeDialog extends AppCompatDialogFragment {
    private EditText etName;
    private EditText etType;
    private EditText etDescription;
    boolean nameEmpty = true;
    boolean typeEmpty = true;
    DiagnosisType diagnosisType;

    public interface DiagnosisTypeDialogListener {
        void applyDiagnosisType(String name,String type, String description);
    }

    public DiagnosisTypeDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        diagnosisType = (DiagnosisType) getArguments().getSerializable("diagnosistype");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_diagnosistype_dialog, null);

        builder.setView(view)
                .setTitle(R.string.add_new_diagnosis)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = "";
                        String type = "";
                        String description = "";
                        name = etName.getText().toString();
                        type = etType.getText().toString();
                        description = etDescription.getText().toString();
                        listener.applyDiagnosisType(name,type, description);
                    }
                });

        etName = view.findViewById(R.id.etName);
        etType = view.findViewById(R.id.etType);
        etDescription = view.findViewById(R.id.etDescription);

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

        etType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                AlertDialog dialog = (AlertDialog) getDialog();
                if(charSequence.length() != 0){
                    typeEmpty = false;
                }else{
                    typeEmpty = true;
                }
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return builder.create();
    }
    private void validateInputs(){
        AlertDialog dialog = (AlertDialog) getDialog();
        if(!nameEmpty && !typeEmpty){
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        }else{
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getWindow().setLayout(2000, 800);
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);

        if(diagnosisType != null){
            dialog.setTitle(R.string.edit_diagnosistype);
            etName.setText(diagnosisType.getName());
            etType.setText(diagnosisType.getType());
            etDescription.setText(diagnosisType.getDescription());
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DiagnosisTypeDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DiagnosisTypeDialogListener ");
        }
    }

}
