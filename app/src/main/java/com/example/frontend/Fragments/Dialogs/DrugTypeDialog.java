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

import com.example.frontend.Models.DrugType;
import com.example.frontend.Models.Patient;
import com.example.frontend.R;
import com.example.frontend.Service.JsonPlaceHolderApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DrugTypeDialog extends AppCompatDialogFragment {
    private EditText etName;
    private EditText etDescription;
    DrugType drugType;

    public interface DrugTypeDialogListener {
        void applyDrugType(String name, String description);
    }

    public DrugTypeDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        drugType = (DrugType) getArguments().getSerializable("drugtype");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_drugtype_dialog, null);

        builder.setView(view)
                .setTitle(R.string.add_new_drug)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = "";
                        String description = "";
                        name = etName.getText().toString();
                        description = etDescription.getText().toString();
                        listener.applyDrugType(name, description);
                    }
                });

        etName = view.findViewById(R.id.etName);
        etDescription = view.findViewById(R.id.etDescription);


        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                AlertDialog dialog = (AlertDialog) getDialog();
                if(charSequence.length() != 0){
                    dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
                }else{
                    dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);

        if(drugType != null){
            dialog.setTitle(R.string.edit_drugtype);
            etName.setText(drugType.getName());
            etDescription.setText(drugType.getDescription());
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DrugTypeDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DrugTypeDialogListener ");
        }
    }

}
