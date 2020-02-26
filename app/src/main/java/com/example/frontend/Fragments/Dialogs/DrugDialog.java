package com.example.frontend.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.Service.JsonPlaceHolderApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DrugDialog extends AppCompatDialogFragment {
    private EditText etAmount;
    private EditText etComment;
    private RadioButton rbMorning;
    private RadioButton rbLunch;
    private RadioButton rbEvening;
    private RadioButton rbNight;
    private TextView tvMorning;
    private TextView tvLunch;
    private TextView tvEvening;
    private TextView tvNight;


    public interface DrugDialogListener {
        void applyTexts(String amount, String dosis, String comment);
    }

    public DrugDialogListener listener ;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_drug_dialog, null);

        builder.setView(view)
                .setTitle(R.string.dosisForDrug)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String amount = etAmount.getText().toString();
                        String comment = etComment.getText().toString();
                        int morning = rbMorning.isChecked() ? 1 : 0;
                        int lunch = rbLunch.isChecked() ? 1 : 0;
                        int evening = rbEvening.isChecked() ? 1 : 0;
                        int night = rbNight.isChecked() ? 1 : 0;
                        String dosis = "" + morning + lunch + evening + night;

                        listener.applyTexts(amount, dosis, comment);
                    }
                });
        etAmount = view.findViewById(R.id.edit_amount);

        rbMorning = view.findViewById(R.id.morning);
        rbLunch = view.findViewById(R.id.lunch);
        rbEvening = view.findViewById(R.id.evening);
        rbNight = view.findViewById(R.id.night);

        View.OnClickListener selectDosis = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.morningOverlay:
                        rbMorning.setChecked(!rbMorning.isChecked());
                        break;
                    case R.id.lunchOverlay:
                        rbLunch.setChecked(!rbLunch.isChecked());
                        break;
                    case R.id.eveningOverlay:
                        rbEvening.setChecked(!rbEvening.isChecked());
                        break;
                    case R.id.nightOverlay:
                        rbNight.setChecked(!rbNight.isChecked());
                        break;
                }
            }
        };
        tvMorning = view.findViewById(R.id.morningOverlay);
        tvMorning.setOnClickListener(selectDosis);
        tvLunch = view.findViewById(R.id.lunchOverlay);
        tvLunch.setOnClickListener(selectDosis);
        tvEvening = view.findViewById(R.id.eveningOverlay);
        tvEvening.setOnClickListener(selectDosis);
        tvNight = view.findViewById(R.id.nightOverlay);
        tvNight.setOnClickListener(selectDosis);

        etComment = view.findViewById(R.id.edit_comment);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DrugDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DrugDialogListener ");
        }
    }

}
