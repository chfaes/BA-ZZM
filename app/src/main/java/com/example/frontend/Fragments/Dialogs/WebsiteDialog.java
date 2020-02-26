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

import com.example.frontend.Models.WebsiteType;
import com.example.frontend.R;
import com.example.frontend.Service.JsonPlaceHolderApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebsiteDialog extends AppCompatDialogFragment {
    private EditText etTitle;
    private EditText etUrl;
    private EditText etDescription;

    public interface WebsiteDialogListener {
        void applyTexts(WebsiteType websiteType);
    }

    public WebsiteDialogListener listener ;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TextWatcher onTextChangeListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_website_dialog, null);

        builder.setView(view)
                .setTitle(R.string.add_website)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WebsiteType websiteType = new WebsiteType();
                        websiteType.setName(etTitle.getText().toString());
                        websiteType.setUrl(etUrl.getText().toString());
                        websiteType.setDescription(etDescription.getText().toString());

                        listener.applyTexts(websiteType);
                    }
                });
        etTitle = view.findViewById(R.id.etTitle);
        etTitle.addTextChangedListener(onTextChangeListener);
        etUrl = view.findViewById(R.id.etUrl);
        etUrl.addTextChangedListener(onTextChangeListener);
        etDescription = view.findViewById(R.id.etDescription);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);

    }

    private void validateInputs(){
        AlertDialog dialog = (AlertDialog) getDialog();
        if(etTitle.getText().length() > 0 && etUrl.getText().length()> 0){
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        }else{
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (WebsiteDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement WebsiteDialogListener ");
        }
    }

}
