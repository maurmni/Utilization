package com.example.utilization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.List;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeContentFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_content, container, false);
        TextView tvTip = view.findViewById(R.id.tvEcoTip);

        EcoApi api = RetrofitEco.getApi();

        api.getTips().enqueue(new Callback<List<EcoTip>>() {
            @Override
            public void onResponse(Call<List<EcoTip>> call, Response<List<EcoTip>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    int index = new Random().nextInt(response.body().size());
                    tvTip.setText(response.body().get(index).text);
                } else {
                    tvTip.setText("Нет советов");
                }
            }

            @Override
            public void onFailure(Call<List<EcoTip>> call, Throwable t) {
                tvTip.setText("Берегите природу");
                t.printStackTrace();
            }
        });

        return view;
    }
}
