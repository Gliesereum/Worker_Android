package com.gliesereum.karmaworker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.karmaworker.R;
import com.gliesereum.karmaworker.adapter.MyRecyclerViewAdapter;
import com.gliesereum.karmaworker.network.APIClient;
import com.gliesereum.karmaworker.network.APIInterface;
import com.gliesereum.karmaworker.network.CustomCallback;
import com.gliesereum.karmaworker.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karmaworker.util.ErrorHandler;
import com.gliesereum.karmaworker.util.FastSave;
import com.gliesereum.karmaworker.util.Util;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.karmaworker.util.Constants.ACCESS_EXPIRATION_DATE;
import static com.gliesereum.karmaworker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karmaworker.util.Constants.ACCESS_TOKEN_WITHOUT_BEARER;
import static com.gliesereum.karmaworker.util.Constants.BUSINESS_CATEGORY_ID;
import static com.gliesereum.karmaworker.util.Constants.CARWASH_ID;
import static com.gliesereum.karmaworker.util.Constants.CARWASH_NAME;
import static com.gliesereum.karmaworker.util.Constants.FROM_DATE;
import static com.gliesereum.karmaworker.util.Constants.FROM_TIME;
import static com.gliesereum.karmaworker.util.Constants.IS_LOGIN;
import static com.gliesereum.karmaworker.util.Constants.REFRESH_EXPIRATION_DATE;
import static com.gliesereum.karmaworker.util.Constants.REFRESH_TOKEN;
import static com.gliesereum.karmaworker.util.Constants.STATUS_FILTER;
import static com.gliesereum.karmaworker.util.Constants.TO_DATE;
import static com.gliesereum.karmaworker.util.Constants.TO_TIME;
import static com.gliesereum.karmaworker.util.Constants.USER_ID;

public class ChooseCarWash extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private APIInterface API;
    private CustomCallback customCallback;
    private ErrorHandler errorHandler = new ErrorHandler(this, this);
    private List<AllCarWashResponse> carWashList;
    private MyRecyclerViewAdapter adapter;
    private MaterialButton logoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_car_wash);
        FastSave.init(getApplicationContext());
        initView();
        cleanRecordFilter();
        getAllCarWash();
    }

    private void cleanRecordFilter() {
        FastSave.getInstance().saveLong(FROM_DATE, Util.startOfDay(System.currentTimeMillis()));
        FastSave.getInstance().saveLong(TO_DATE, Util.endOfDay(System.currentTimeMillis()));
        FastSave.getInstance().saveObjectsList(STATUS_FILTER, Arrays.asList("CANCELED", "WAITING", "IN_PROCESS", "COMPLETED"));
    }

    private void initView() {
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        FastSave.getInstance().deleteValue(IS_LOGIN);
        FastSave.getInstance().deleteValue(USER_ID);
        FastSave.getInstance().deleteValue(ACCESS_TOKEN);
        FastSave.getInstance().deleteValue(ACCESS_TOKEN_WITHOUT_BEARER);
        FastSave.getInstance().deleteValue(REFRESH_TOKEN);
        FastSave.getInstance().deleteValue(ACCESS_EXPIRATION_DATE);
        FastSave.getInstance().deleteValue(REFRESH_EXPIRATION_DATE);
        startActivity(new Intent(ChooseCarWash.this, LoginActivity.class));
        finish();
    }

    private void getAllCarWash() {

        API.getAllCarWash(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<List<AllCarWashResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<AllCarWashResponse>> call, Response<List<AllCarWashResponse>> response) {
                        carWashList = response.body();
                        if (carWashList != null && carWashList.size() > 0) {
                            adapter = new MyRecyclerViewAdapter(ChooseCarWash.this, carWashList);
                            adapter.setClickListener(ChooseCarWash.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onEmpty(Call<List<AllCarWashResponse>> call, Response<List<AllCarWashResponse>> response) {
                        Toast.makeText(ChooseCarWash.this, "204", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    public void onItemClick(View view, int position) {
        FastSave.getInstance().saveObject("carWash", adapter.getItem(position));
        FastSave.getInstance().saveString(CARWASH_ID, adapter.getItem(position).getBusinessId());
        FastSave.getInstance().saveString(CARWASH_NAME, adapter.getItem(position).getName());
        FastSave.getInstance().saveString(BUSINESS_CATEGORY_ID, adapter.getItem(position).getBusinessCategoryId());
        startActivity(new Intent(ChooseCarWash.this, RecordListActivity.class));
    }
}
