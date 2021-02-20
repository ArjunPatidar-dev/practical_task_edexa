package com.example.arjunpatidar_edexa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arjunpatidar_edexa.R;
import com.example.arjunpatidar_edexa.adapter.EmployeesAdapter;
import com.example.arjunpatidar_edexa.data.APIInterface;
import com.example.arjunpatidar_edexa.data.RetrofitAPI;
import com.example.arjunpatidar_edexa.data.response.EmployeesResponseData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.this.getClass().getSimpleName();
    private APIInterface apiInterface;
    private RecyclerView recyclerView;
    private EmployeesAdapter employeesAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<EmployeesResponseData> allEmployeesResponseData = new ArrayList<>();
    private List<EmployeesResponseData> tempEmployeesResponseData = new ArrayList<>();
    private List<EmployeesResponseData> cityEmployeesResponseData = new ArrayList<>();
    private List<EmployeesResponseData> searchEmployeesResponseData = new ArrayList<>();
    private ProgressDialog progressDialog;

    private TextView tvBtnAll;
    private TextView tvBtnChicago;
    private TextView tvBtnLos;
    private TextView tvBtnNewYork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        initViews();
        initListener();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Log.i(TAG, "onOptionsItemSelected: ");
        }
        return super.onOptionsItemSelected(item);
    }

    void filter(String text){
        searchEmployeesResponseData.clear();
        searchEmployeesResponseData.addAll(allEmployeesResponseData);
        List<EmployeesResponseData> temp = new ArrayList();
        for(EmployeesResponseData d: allEmployeesResponseData){
            if(d.getFirstName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        updateList(temp);
    }
    public void updateList(List<EmployeesResponseData> list){
        tempEmployeesResponseData.clear();
        tempEmployeesResponseData.addAll(list);
        employeesAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "onQueryTextSubmit: query::"+ query);
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange: query::" + newText);
                filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_employees_list);
        tvBtnAll = findViewById(R.id.btn_all);
        tvBtnChicago = findViewById(R.id.btn_chicago);
        tvBtnLos = findViewById(R.id.btn_los);
        tvBtnNewYork = findViewById(R.id.btn_newyork);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        getData();
        employeesAdapter = new EmployeesAdapter(tempEmployeesResponseData, this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(employeesAdapter);
        employeesAdapter.notifyDataSetChanged();

    }

    private void initListener(){
        tvBtnAll.setOnClickListener(this);
        tvBtnChicago.setOnClickListener(this);
        tvBtnNewYork.setOnClickListener(this);
        tvBtnLos.setOnClickListener(this);
    }

    private void getData() {
        Retrofit retrofit = RetrofitAPI.getRetrofit();
        apiInterface = retrofit.create(APIInterface.class);

        Call<List<EmployeesResponseData>> listCall = apiInterface.getEmployeesDetails("81ada0361bbd877efb9e");
        listCall.enqueue(new Callback<List<EmployeesResponseData>>() {
            @Override
            public void onResponse(Call<List<EmployeesResponseData>> call, Response<List<EmployeesResponseData>> response) {

                Log.i(TAG, "onResponse: response data::"+ response.body().toString());

                allEmployeesResponseData.addAll(response.body());
                Log.i(TAG, "onResponse: apiResponseList size::"+ allEmployeesResponseData.size());
//                employeesAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
                updateList(allEmployeesResponseData);

            }

            @Override
            public void onFailure(Call<List<EmployeesResponseData>> call, Throwable t) {
                Log.i(TAG, "onResponse: response data::"+ t.getMessage());

                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void selectedCityList(String city){
        cityEmployeesResponseData.clear();
        for (int i = 0; i < allEmployeesResponseData.size(); i++) {
            EmployeesResponseData employeesResponseData = allEmployeesResponseData.get(i);
            if (employeesResponseData.getCity().equalsIgnoreCase(city)){
                Log.i(TAG, "selectedCityList:employeesResponseData:: "+ new Gson().toJson(employeesResponseData));
                cityEmployeesResponseData.add(employeesResponseData);
            }
        }
        updateList(cityEmployeesResponseData);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_all:
                updateList(allEmployeesResponseData);
                tvBtnAll.setTextColor(getResources().getColor(R.color.colorAccent));
                tvBtnLos.setTextColor(getResources().getColor(R.color.black));
                tvBtnChicago.setTextColor(getResources().getColor(R.color.black));
                tvBtnNewYork.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.btn_chicago:
                selectedCityList("Chicago");
                tvBtnChicago.setTextColor(getResources().getColor(R.color.colorAccent));
                tvBtnLos.setTextColor(getResources().getColor(R.color.black));
                tvBtnAll.setTextColor(getResources().getColor(R.color.black));
                tvBtnNewYork.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.btn_los:
                selectedCityList("Los Angeles");
                tvBtnLos.setTextColor(getResources().getColor(R.color.colorAccent));
                tvBtnAll.setTextColor(getResources().getColor(R.color.black));
                tvBtnChicago.setTextColor(getResources().getColor(R.color.black));
                tvBtnNewYork.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.btn_newyork:
                selectedCityList("NewYork");
                tvBtnNewYork.setTextColor(getResources().getColor(R.color.colorAccent));
                tvBtnLos.setTextColor(getResources().getColor(R.color.black));
                tvBtnChicago.setTextColor(getResources().getColor(R.color.black));
                tvBtnAll.setTextColor(getResources().getColor(R.color.black));
                break;
            default:
                break;
        }
    }
}