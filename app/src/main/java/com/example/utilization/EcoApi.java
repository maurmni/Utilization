package com.example.utilization;
import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface EcoApi {

    @GET("Лист1/")
    Call<List<EcoTip>> getTips();
}
