package com.example.frontend.Fragments.Dialogs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.frontend.Models.DiagnosisType;
import com.example.frontend.Models.PatientDiagnosis;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>  {
        private List<PatientDiagnosis> diagnosesList = new ArrayList<>();
        private List<DiagnosisType> diagnosisTypeList = new ArrayList<>();
        private DatabaseHelper db ;


    // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public  class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
             TextView tvDiagnosisRow;
             TextView tvPriorityNumber;

            public MyViewHolder(View v) {
                super(v);
                tvDiagnosisRow = v.findViewById(R.id.tvDiagnosisRow);
                tvPriorityNumber = v.findViewById(R.id.tvPriorityNumber);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public RecyclerAdapter(List<PatientDiagnosis> myDataset, List<DiagnosisType> diagnosisTypes, Context context) {
            db = new DatabaseHelper(context);
            diagnosesList = myDataset;
            diagnosisTypeList = diagnosisTypes;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
            // create a new view
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.recycler_row_item, parent, false);
            MyViewHolder vh = new MyViewHolder(view);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            int patientId = diagnosesList.get(position).getPatientId();
            int diagnosisId = diagnosesList.get(position).getDiagnosisId();
            String comment = diagnosesList.get(position).getComment();

            holder.tvPriorityNumber.setText(String.valueOf(position+1));
            for(DiagnosisType dt : diagnosisTypeList){
                if( diagnosisId == dt.getId()){
                    holder.tvDiagnosisRow.setText(dt.getName());
                    PatientDiagnosis newPd = new PatientDiagnosis(patientId, diagnosisId, comment, position+1);
                    db.updatePatientDiagnosis(patientId, diagnosisId, newPd);
                }
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return diagnosesList.size();
        }

}
