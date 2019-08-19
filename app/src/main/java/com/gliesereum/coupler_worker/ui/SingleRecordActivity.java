package com.gliesereum.coupler_worker.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.ServiceListAdapter;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.car.AllCarResponse;
import com.gliesereum.coupler_worker.network.json.record.AllRecordResponse;
import com.gliesereum.coupler_worker.util.CircleTransform;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.BUSINESS_TYPE;
import static com.gliesereum.coupler_worker.util.Constants.CLIENT_AVATAR_URL;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOCK;
import static com.gliesereum.coupler_worker.util.Constants.RECORD;
import static com.gliesereum.coupler_worker.util.Constants.SINGLE_RECORD_ACTIVITY;

public class SingleRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private APIInterface API;
    private CustomCallback customCallback;
    private AllRecordResponse record;
    private Button progressBtn;
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
    private TextView dataLabel;
    private ImageView backImg;
    private TextView clientNameLabel;
    private TextView commentTextView;
    private ImageView avatarImg;
    private ImageView imageView8;
    private TextView textView21;
    private ImageButton lockBtn;
    private ImageButton updateTimeBtn;
    private Calendar date;
    private Long begin = 0L;
    private ImageView cancelImg;
    private TextView cancelDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_record_new);
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
            alertDialog = new LottieAlertDialog.Builder(SingleRecordActivity.this, DialogTypes.TYPE_ERROR)
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


//            NDialog cancelInfoDialog = new NDialog(SingleRecordActivity.this, ButtonType.NO_BUTTON);
//            cancelInfoDialog.setCustomView(R.layout.cancele_info_dialog);
//            List<View> childViews = cancelInfoDialog.getCustomViewChildren();
//            for (View childView : childViews) {
//                switch (childView.getId()) {
//                    case R.id.canceledDescription:
//                        TextView canceledDescription = childView.findViewById(R.id.canceledDescription);
//                        canceledDescription.setText(record.getCanceledDescription());
//                        break;
//                    case R.id.dismissBtn:
//                        Button dismissBtn = childView.findViewById(R.id.dismissBtn);
//                        dismissBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                cancelInfoDialog.dismiss();
//                            }
//                        });
//                        break;
//                }
//            }
//            cancelInfoDialog.isCancelable(true);
//            cancelInfoDialog.show();
        }
    }

    private void initData() {
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        record = FastSave.getInstance().getObject(RECORD, AllRecordResponse.class);
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
                new Util().lockScreen(SingleRecordActivity.this, SingleRecordActivity.this, lockBtn);
            }
        });
        updateTimeBtn = findViewById(R.id.updateTimeBtn);
        updateTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });
        cancelImg = findViewById(R.id.cancelImg);
        cancelDescription = findViewById(R.id.cancelDescription);
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        new DatePickerDialog(SingleRecordActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(SingleRecordActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        begin = date.getTimeInMillis();
                        API.updateTimeRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), begin, record.getId())
                                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<AllRecordResponse>() {
                                    @Override
                                    public void onSuccessful(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                                        startActivity(new Intent(SingleRecordActivity.this, RecordListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                                        finish();
                                    }

                                    @Override
                                    public void onEmpty(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {

                                    }
                                }));
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
            }
        },
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }


    private void fillActivity() {
        if (record.getClient() != null) {
            if (record.getClient().getAvatarUrl() != null) {
                Picasso.get().load(record.getClient().getAvatarUrl()).transform(new CircleTransform()).into(avatarImg);
                avatarImg.setVisibility(View.VISIBLE);
                FastSave.getInstance().deleteValue(CLIENT_AVATAR_URL);
            } else {
                avatarImg.setVisibility(View.GONE);
            }
        }

        if (record.getStatusProcess().equals("COMPLETED") || record.getStatusProcess().equals("CANCELED")) {
            progressBtn.setEnabled(false);
            progressBtn.setVisibility(View.GONE);
            doneBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
        }
        if (record.getStatusProcess().equals("IN_PROCESS")) {
            doneBtn.setVisibility(View.VISIBLE);
            progressBtn.setVisibility(View.GONE);
            doneBtn.setEnabled(true);
            progressBtn.setEnabled(false);
        }
        if (record.getStatusProcess().equals("WAITING")) {
            progressBtn.setVisibility(View.VISIBLE);
            doneBtn.setVisibility(View.GONE);
            progressBtn.setEnabled(true);
            doneBtn.setEnabled(false);
        }
        if (record.getClient() != null) {
            if (record.getClient().getFirstName() != null && record.getClient().getMiddleName() != null) {
                clientNameLabel.setText(record.getClient().getFirstName() + " " + record.getClient().getMiddleName());
            } else {
                clientNameLabel.setText("");
            }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.progressBtn:
                recordInProgress();
                break;
            case R.id.doneBtn:
                recordDone();
                break;
            case R.id.cancelBtn:
                NDialog cancelDialog = new NDialog(SingleRecordActivity.this, ButtonType.NO_BUTTON);
                cancelDialog.setCustomView(R.layout.cancele_dialog);
                List<View> childViews = cancelDialog.getCustomViewChildren();
                for (View childView : childViews) {
                    switch (childView.getId()) {
                        case R.id.commentTextView:
                            commentTextView = childView.findViewById(R.id.commentTextView);
                            break;
                        case R.id.deleteRecord:
                            Button deleteRecord = childView.findViewById(R.id.deleteRecord);
                            deleteRecord.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!commentTextView.getText().toString().equals("")) {
                                        cancelRecord();
                                        cancelDialog.dismiss();
                                    } else {
                                        Toast.makeText(SingleRecordActivity.this, "Введите причину отмены заказа", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            break;
                        case R.id.cancelBtn:
                            Button cancelBtn = childView.findViewById(R.id.cancelBtn);
                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cancelDialog.dismiss();
                                }
                            });
                            break;

                    }
                }
                cancelDialog.show();
                break;
        }
    }

    private void cancelRecord() {
        API.canceleRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), record.getId(), commentTextView.getText().toString())
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<AllRecordResponse>() {
                    @Override
                    public void onSuccessful(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                        startActivity(new Intent(SingleRecordActivity.this, RecordListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                        Toast.makeText(SingleRecordActivity.this, "Заказ отменен", Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        FastSave.getInstance().saveBoolean(SINGLE_RECORD_ACTIVITY, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FastSave.getInstance().saveBoolean(SINGLE_RECORD_ACTIVITY, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FastSave.getInstance().saveBoolean(SINGLE_RECORD_ACTIVITY, false);
    }
}
