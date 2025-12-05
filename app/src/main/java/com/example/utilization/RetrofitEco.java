package com.example.utilization;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitEco {
    private static Retrofit retrofit;

    public static EcoApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://opensheet.elk.sh/1v8q94__FB-BW4yRJ70mNvlmPXGjDx9hSg2cilO-C5hI/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(EcoApi.class);
    }
}
