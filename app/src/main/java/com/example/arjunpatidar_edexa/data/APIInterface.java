package com.example.arjunpatidar_edexa.data;

import com.example.arjunpatidar_edexa.data.response.EmployeesResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIInterface {
//
//    @GET
//    Call<List<EmployeesResponseData>> getEmployeesDetails();

    @GET("{key}/")
    Call<List<EmployeesResponseData>> getEmployeesDetails(@Path("key") String username);
}
