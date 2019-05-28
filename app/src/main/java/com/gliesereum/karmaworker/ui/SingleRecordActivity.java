package com.gliesereum.karmaworker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.karmaworker.R;
import com.gliesereum.karmaworker.adapter.ServiceListAdapter;
import com.gliesereum.karmaworker.network.APIClient;
import com.gliesereum.karmaworker.network.APIInterface;
import com.gliesereum.karmaworker.network.CustomCallback;
import com.gliesereum.karmaworker.network.json.car.AllCarResponse;
import com.gliesereum.karmaworker.network.json.record.AllRecordResponse;
import com.gliesereum.karmaworker.util.FastSave;
import com.gliesereum.karmaworker.util.Util;
import com.google.android.material.button.MaterialButton;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.karmaworker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karmaworker.util.Constants.SINGLE_RECORD_ACTIVITY;

public class SingleRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private APIInterface API;
    private CustomCallback customCallback;
    private AllRecordResponse record;
    private MaterialButton progressBtn;
    private Button doneBtn;
    private Button cancelBtn;
    private TextView carName;
    private TextView timeLabel;
    private TextView durationLabel;
    private TextView moneyLabel;
    private List<String> serviceList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ServiceListAdapter serviceListAdapter;
    private LottieAlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_record);
        initData();
        initView();
        fillActivity();
        if (record.getTargetId() != null) {
            getCar(record.getTargetId());
        } else {
            carName.setText("________");
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private void initData() {
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        record = FastSave.getInstance().getObject("RECORD", AllRecordResponse.class);
    }

    private void initView() {
        progressBtn = findViewById(R.id.progressBtn);
        doneBtn = findViewById(R.id.doneBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        progressBtn.setOnClickListener(this);
        doneBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        carName = findViewById(R.id.carName);
        timeLabel = findViewById(R.id.timeLabel);
        durationLabel = findViewById(R.id.durationLabel);
        moneyLabel = findViewById(R.id.moneyLabel);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceListAdapter = new ServiceListAdapter(SingleRecordActivity.this);
        recyclerView.setAdapter(serviceListAdapter);
    }

    private void fillActivity() {
        if (record.getStatusProcess().equals("COMPLETED") || record.getStatusProcess().equals("CANCELED")) {
            progressBtn.setEnabled(false);
            doneBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
        }
        if (record.getStatusProcess().equals("IN_PROCESS")) {
            progressBtn.setEnabled(false);
        }
        timeLabel.setText(Util.getStringTime(record.getBegin()));
//        durationLabel.setText(String.valueOf((record.getFinish() - record.getBegin()) / 60000) + " мин");
        durationLabel.setText(Util.getStringTime(record.getFinish()));
        moneyLabel.setText(String.valueOf(record.getPrice()) + " грн");
        if (record.getPackageDto() != null) {
            for (int i = 0; i < record.getPackageDto().getServices().size(); i++) {
                serviceList.add(record.getPackageDto().getServices().get(i).getName());
            }
        }
        if (record.getServices() != null) {
            for (int i = 0; i < record.getServices().size(); i++) {
                serviceList.add(record.getServices().get(i).getName());
            }
        }
        serviceListAdapter.setItems(serviceList);
    }

    private void getCar(String targetId) {
        API.getCarById(FastSave.getInstance().getString(ACCESS_TOKEN, ""), targetId)
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<AllCarResponse>() {
                    @Override
                    public void onSuccessful(Call<AllCarResponse> call, Response<AllCarResponse> response) {
                        carName.setText(response.body().getBrand().getName() + " " + response.body().getModel().getName());
                    }

                    @Override
                    public void onEmpty(Call<AllCarResponse> call, Response<AllCarResponse> response) {

                    }
                }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.progressBtn:
                recordInProgress();
                break;
            case R.id.doneBtn:
                recordDone();
                break;
            case R.id.cancelBtn:
                recordCancel();
                break;
        }
    }

    private void recordCancel() {
        API.canceleRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), record.getId())
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<AllRecordResponse>() {
                    @Override
                    public void onSuccessful(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                        startActivity(new Intent(SingleRecordActivity.this, RecordListActivity.class));
                        finish();
                    }

                    @Override
                    public void onEmpty(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {

                    }
                }));
    }

    private void recordDone() {
        API.changeRecordStatus(FastSave.getInstance().getString(ACCESS_TOKEN, ""), record.getId(), "COMPLETED")
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<AllRecordResponse>() {
                    @Override
                    public void onSuccessful(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                        startActivity(new Intent(SingleRecordActivity.this, RecordListActivity.class));
                        finish();
                    }

                    @Override
                    public void onEmpty(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {

                    }
                }));
    }

    private void recordInProgress() {
        API.changeRecordStatus(FastSave.getInstance().getString(ACCESS_TOKEN, ""), record.getId(), "IN_PROCESS")
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<AllRecordResponse>() {
                    @Override
                    public void onSuccessful(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                        startActivity(new Intent(SingleRecordActivity.this, RecordListActivity.class));
                        finish();
                    }

                    @Override
                    public void onEmpty(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {

                    }
                }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SingleRecordActivity.this, RecordListActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FastSave.getInstance().saveBoolean(SINGLE_RECORD_ACTIVITY, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FastSave.getInstance().saveBoolean(SINGLE_RECORD_ACTIVITY, false);
    }


}
