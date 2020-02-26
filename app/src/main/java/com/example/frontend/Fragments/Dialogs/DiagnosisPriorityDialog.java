package com.example.frontend.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.frontend.Models.DiagnosisType;
import com.example.frontend.Models.PatientDiagnosis;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DiagnosisPriorityDialog extends AppCompatDialogFragment {
    private RecyclerView rvDiagnoses;
    private RecyclerAdapter recyclerAdapter;
    private List<PatientDiagnosis> allPatientDiagnoses;
    private List<DiagnosisType> allDiagnosisTypes;

    public interface DiagnosisPriorityDialogListener {
    }

    public DiagnosisPriorityDialogListener listener ;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        allPatientDiagnoses = (List<PatientDiagnosis>) getArguments().getSerializable("patientDiagnosesList");
        allDiagnosisTypes = (List<DiagnosisType>) getArguments().getSerializable("diagnosisTypesList");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_diagnosis_priority_dialog, null);
        rvDiagnoses = (RecyclerView) view.findViewById(R.id.rvDiagnoses);
        rvDiagnoses.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerAdapter = new RecyclerAdapter(allPatientDiagnoses,allDiagnosisTypes, getContext());
        rvDiagnoses.setAdapter(recyclerAdapter);

        //Adds Divider (Lines) between rows of the Recycler of Diagnoses
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvDiagnoses.addItemDecoration(dividerItemDecoration);

        //Adds Drag and Drop function to Recycler View of Diagnoses
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvDiagnoses);

        builder.setView(view)
                .setTitle(getString(R.string.diagnosis_priorization))
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });


        return builder.create();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(allPatientDiagnoses, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);
            recyclerView.getAdapter().notifyItemChanged(fromPosition);
            recyclerView.getAdapter().notifyItemChanged(toPosition);


            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DiagnosisPriorityDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DiagnosisPriorityDialogListener ");
        }
    }

}
