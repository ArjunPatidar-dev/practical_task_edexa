package com.example.arjunpatidar_edexa.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arjunpatidar_edexa.R;
import com.example.arjunpatidar_edexa.data.response.EmployeesResponseData;
import com.google.gson.Gson;

import java.util.List;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder>{
    private final String TAG = EmployeesAdapter.this.getClass().getSimpleName();
    private List<EmployeesResponseData> employeesResponseDataList;
    private Context context;

    public EmployeesAdapter(List<EmployeesResponseData> employeesResponseDataList, Context context){
        this.employeesResponseDataList = employeesResponseDataList;
        this.context = context;
    }

    @Override
    public EmployeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.employee_item, parent,false);
        return new EmployeesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeesViewHolder holder, int position) {
        final EmployeesResponseData employeesResponseData = employeesResponseDataList.get(position);

        Log.i(TAG, "onBindViewHolder: employeesResponseData.getFirstName()"+ new Gson().toJson(employeesResponseData));

        holder.tvNameTitle.setText(""+employeesResponseData.getFirstName().charAt(0));
        holder.tvFirstName.setText(""+employeesResponseData.getFirstName());
        holder.tvLastName.setText(""+employeesResponseData.getLastName());
        holder.tvCity.setText(""+employeesResponseData.getCity());
        holder.tvId.setText(""+employeesResponseData.getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, employeesResponseData.getFirstName() +" "+ employeesResponseData.getLastName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeesResponseDataList.size();
    }

    public class EmployeesViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNameTitle;
        public TextView tvFirstName;
        public TextView tvLastName;
        public TextView tvCity;
        public TextView tvId;
        public EmployeesViewHolder(View itemView){
            super(itemView);

            tvNameTitle = itemView.findViewById(R.id.tv_name_title);
            tvFirstName = itemView.findViewById(R.id.tv_first_name);
            tvLastName = itemView.findViewById(R.id.tv_last_name);
            tvCity = itemView.findViewById(R.id.tv_city);
            tvId = itemView.findViewById(R.id.tv_id);
        }
    }
}
