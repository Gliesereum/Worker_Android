package com.gliesereum.coupler_worker.ui;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.ServiceListAdapter;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.car.AllCarResponse;
import com.gliesereum.coupler_worker.network.json.client_record_new.RecordItem;
import com.gliesereum.coupler_worker.util.CircleTransform;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.BUSINESS_TYPE;
import static com.gliesereum.coupler_worker.util.Constants.CLIENT_AVATAR_URL;
import static com.gliesereum.coupler_worker.util.Constants.CLIENT_RECORD;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOCK;

public class SingleClientRecordActivity extends AppCompatActivity {

    private APIInterface API;
    private CustomCallback customCallback;
    private RecordItem record;
    private TextView carName;
    private TextView timeLabel;
    private TextView durationLabel;
    private TextView moneyLabel;
    private List<String> serviceList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ServiceListAdapter serviceListAdapter;
    private LottieAlertDialog alertDialog;
    private TextView dataLabel;
    private ImageView backImg;
    private TextView clientNameLabel;
    private TextView commentTextView;
    private ImageView avatarImg;
    private ImageView imageView8;
    private TextView textView21;
    private ImageButton lockBtn;
    private ImageView cancelImg;
    private TextView cancelDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_client_record);
        initData();
        initView();
        fillActivity();
        if (FastSave.getInstance().getString(BUSINESS_TYPE, "").equals("HUMAN")) {
            imageView8.setVisibility(View.GONE);
            textView21.setVisibility(View.GONE);
            carName.setVisibility(View.GONE);
        } else {
            if (record.getTargetId() != null) {
                getCar(record.getTargetId());
            } else {
                carName.setText("");
            }
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (FastSave.getInstance().getBoolean(IS_LOCK, false)) {
            new Util().lockScreen(this, this, lockBtn);
        }

        checkCancelRecord();

    }

    private void checkCancelRecord() {
        if (record.getStatusProcess().equals("CANCELED") && record.getCanceledDescription() != null) {
            cancelImg.setVisibility(View.VISIBLE);
            cancelDescription.setVisibility(View.VISIBLE);
            cancelDescription.setText(record.getCanceledDescription());
            alertDialog = new LottieAlertDialog.Builder(SingleClientRecordActivity.this, DialogTypes.TYPE_ERROR)
                    .setTitle("Причина отмены заказа:")
                    .setDescription(record.getCanceledDescription())
                    .setPositiveText("Ок")
                    .setPositiveButtonColor(getResources().getColor(R.color.colorPrimaryGreen))
                    .setPositiveListener(new ClickListener() {
                        @Override
                        public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                            alertDialog.dismiss();
                        }
                    })
                    .build();
            alertDialog.setCancelable(true);
            alertDialog.show();
        }
    }


    private void initData() {
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        record = FastSave.getInstance().getObject(CLIENT_RECORD, RecordItem.class);
    }

    private void initView() {
        carName = findViewById(R.id.carName);
        timeLabel = findViewById(R.id.timeLabel);
        durationLabel = findViewById(R.id.durationLabel);
        moneyLabel = findViewById(R.id.moneyLabel);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceListAdapter = new ServiceListAdapter(SingleClientRecordActivity.this);
        recyclerView.setAdapter(serviceListAdapter);
        dataLabel = findViewById(R.id.dataLabel);
        backImg = findViewById(R.id.backBtn);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        clientNameLabel = findViewById(R.id.clientNameLabel);
        avatarImg = findViewById(R.id.avatarImg);
        imageView8 = findViewById(R.id.imageView8);
        textView21 = findViewById(R.id.textView21);
        lockBtn = findViewById(R.id.lockBtn);
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Util().lockScreen(SingleClientRecordActivity.this, SingleClientRecordActivity.this, lockBtn);
            }
        });
        cancelImg = findViewById(R.id.cancelImg);
        cancelDescription = findViewById(R.id.cancelDescription);
    }

    private void fillActivity() {
        if (!FastSave.getInstance().getString(CLIENT_AVATAR_URL, "").equals("")) {
            Picasso.get().load(FastSave.getInstance().getString(CLIENT_AVATAR_URL, "")).transform(new CircleTransform()).into(avatarImg);
            avatarImg.setVisibility(View.VISIBLE);
            FastSave.getInstance().deleteValue(CLIENT_AVATAR_URL);
        } else {
            avatarImg.setVisibility(View.GONE);
        }
        if (record.getFirstName() != null && record.getMiddleName() != null) {
            clientNameLabel.setText(record.getFirstName() + " " + record.getMiddleName());
        } else {
            clientNameLabel.setText("");
        }
        timeLabel.setText(Util.getStringTime(record.getBegin()));
        durationLabel.setText(Util.getStringTime(record.getFinish()));
        dataLabel.setText(Util.getStringFullDateTrue(record.getBegin()));
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
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(SingleRecordActivity.this, RecordListActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        FastSave.getInstance().saveBoolean(SINGLE_RECORD_ACTIVITY, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        FastSave.getInstance().saveBoolean(SINGLE_RECORD_ACTIVITY, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        FastSave.getInstance().saveBoolean(SINGLE_RECORD_ACTIVITY, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        FastSave.getInstance().saveBoolean(SINGLE_RECORD_ACTIVITY, false);
    }
}
