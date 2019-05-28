package com.gliesereum.karmaworker.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.karmaworker.R;
import com.gliesereum.karmaworker.adapter.RecordListAdapter;
import com.gliesereum.karmaworker.network.APIClient;
import com.gliesereum.karmaworker.network.APIInterface;
import com.gliesereum.karmaworker.network.CustomCallback;
import com.gliesereum.karmaworker.network.json.notificatoin.NotificatoinBody;
import com.gliesereum.karmaworker.network.json.notificatoin.UserSubscribe;
import com.gliesereum.karmaworker.network.json.record.AllRecordResponse;
import com.gliesereum.karmaworker.network.json.record.RecordsSearchBody;
import com.gliesereum.karmaworker.util.ErrorHandler;
import com.gliesereum.karmaworker.util.FastSave;
import com.gliesereum.karmaworker.util.Util;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karmaworker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karmaworker.util.Constants.BUSINESS_CATEGORY_ID;
import static com.gliesereum.karmaworker.util.Constants.CARWASH_ID;
import static com.gliesereum.karmaworker.util.Constants.FIREBASE_TOKEN;
import static com.gliesereum.karmaworker.util.Constants.FROM_DATE;
import static com.gliesereum.karmaworker.util.Constants.KARMA_BUSINESS_RECORD;
import static com.gliesereum.karmaworker.util.Constants.RECORD_LIST_ACTIVITY;
import static com.gliesereum.karmaworker.util.Constants.STATUS_FILTER;
import static com.gliesereum.karmaworker.util.Constants.TO_DATE;

public class RecordListActivity extends AppCompatActivity implements RecordListAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private RecordListAdapter recordListAdapter;
    private List<AllRecordResponse> recordsList = new ArrayList<>();
    private Map<String, String> carWashNameMap = new HashMap<>();
    private APIInterface API;
    private ErrorHandler errorHandler;
    private TextView splashTextView;
    private ProgressDialog progressDialog;
    //    private StompClient mStompClient;
    private String TAG = "TAG";
    private NDialog nDialog;
    private CustomCallback customCallback;
    private Button addRecord;
    private Button filterBtn;
    private Calendar date;
    private Long begin = 0L;
    private RecordsSearchBody recordsSearchBody;
    private TextView fromDateLabel;
    private TextView toDateLabel;
    private ChipGroup chipGroup;
    private TextView bussinesName;
    private Button backBtn;
    private TextView moneyCount;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        FastSave.init(getApplicationContext());
        initView();
//        subscribeToChanel();
        getAllRecord();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void subscribeToChanel() {
        UserSubscribe userSubscribe = new UserSubscribe(true, KARMA_BUSINESS_RECORD);
        userSubscribe.setObjectId(FastSave.getInstance().getString(CARWASH_ID, ""));
        NotificatoinBody notificatoinBody = new NotificatoinBody(FastSave.getInstance().getString(FIREBASE_TOKEN, ""), true, Arrays.asList(userSubscribe));
        API.subscribeToChanel(FastSave.getInstance().getString(ACCESS_TOKEN, ""), notificatoinBody, true)
                .enqueue(new Callback<List<UserSubscribe>>() {
                    @Override
                    public void onResponse(Call<List<UserSubscribe>> call, Response<List<UserSubscribe>> response) {
                        Toast.makeText(RecordListActivity.this, "OK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<List<UserSubscribe>> call, Throwable t) {
                        Toast.makeText(RecordListActivity.this, "NOT OK", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAllRecord() {
        recordsSearchBody = new RecordsSearchBody();
        recordsSearchBody.setBusinessCategoryId(FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, ""));
        recordsSearchBody.setFrom(FastSave.getInstance().getLong(FROM_DATE, 0));
        recordsSearchBody.setTo(FastSave.getInstance().getLong(TO_DATE, 0));
        recordsSearchBody.setBusinessIds(Arrays.asList(FastSave.getInstance().getString(CARWASH_ID, "")));
        recordsSearchBody.setProcesses(FastSave.getInstance().getObjectsList(STATUS_FILTER, String.class));

        API.getAllRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), recordsSearchBody)
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<List<AllRecordResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<AllRecordResponse>> call, Response<List<AllRecordResponse>> response) {
                        recordsList = response.body();
                        if (recordsList != null && recordsList.size() > 0) {
                            bussinesName.setText(recordsList.get(0).getBusiness().getName());
                            recordListAdapter.setItems(recordsList);
                            int count = 0;
                            for (int i = 0; i < recordsList.size(); i++) {
                                if (recordsList.get(i).getStatusProcess().equals("COMPLETED")) {
                                    count += recordsList.get(i).getPrice();
                                }
                            }
                            moneyCount.setText(count + " грн");
                        }
                    }

                    @Override
                    public void onEmpty(Call<List<AllRecordResponse>> call, Response<List<AllRecordResponse>> response) {
                        Toast.makeText(RecordListActivity.this, "204", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void initView() {
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        errorHandler = new ErrorHandler(this, this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordListAdapter = new RecordListAdapter(RecordListActivity.this);
        recordListAdapter.setClickListener(RecordListActivity.this);
        recyclerView.setAdapter(recordListAdapter);
        addRecord = findViewById(R.id.addRecord);
        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecordListActivity.this, OrderActivity.class));
            }
        });
        filterBtn = findViewById(R.id.filterBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
//                startActivity(new Intent(RecordListActivity.this, OrderNewActivity.class));
//                openFilterDialog();
            }
        });
        bussinesName = findViewById(R.id.bussinesName);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecordListActivity.this, ChooseCarWash.class));
                finish();
            }
        });
        moneyCount = findViewById(R.id.moneyCount);
    }

    private void openFilterDialog() {
        NDialog filterDialog = new NDialog(RecordListActivity.this, ButtonType.NO_BUTTON);
//        filterDialog.isCancelable(true);
        filterDialog.setCustomView(R.layout.filter_dialod);
        List<View> childViews = filterDialog.getCustomViewChildren();
        for (View childView : childViews) {
            switch (childView.getId()) {
                case R.id.chipGroup:
                    chipGroup = childView.findViewById(R.id.chipGroup);
                    for (int i = 0; i < chipGroup.getChildCount(); i++) {
                        if (FastSave.getInstance().getObjectsList(STATUS_FILTER, String.class).contains(chipGroup.getChildAt(i).getTag())) {
                            ((Chip) chipGroup.getChildAt(i)).setChecked(true);
                        }
                    }
                    break;
                case R.id.fromDateLabel:
                    fromDateLabel = childView.findViewById(R.id.fromDateLabel);
                    fromDateLabel.setText(Util.getStringDateTrue(Util.startOfDay(FastSave.getInstance().getLong(FROM_DATE, 0))));
                    break;
                case R.id.toDateLabel:
                    toDateLabel = childView.findViewById(R.id.toDateLabel);
                    toDateLabel.setText(Util.getStringDateTrue(Util.endOfDay(FastSave.getInstance().getLong(TO_DATE, 0))));
                    break;
                case R.id.fromBtn:
                    MaterialButton fromBtn = childView.findViewById(R.id.fromBtn);
                    fromBtn.setOnClickListener(v -> {
                        fromDatePicker();
                    });
                    break;
                case R.id.toBtn:
                    MaterialButton toBtn = childView.findViewById(R.id.toBtn);
                    toBtn.setOnClickListener(v -> {
                        toDatePicker();
                    });
                    break;
                case R.id.acceptFilter:
                    MaterialButton nowOrderBtn = childView.findViewById(R.id.acceptFilter);
                    nowOrderBtn.setOnClickListener(v -> {
                        List<String> statusList = new ArrayList<>();
                        for (int i = 0; i < chipGroup.getChildCount(); i++) {
                            if (((Chip) chipGroup.getChildAt(i)).isChecked()) {
                                statusList.add((String) chipGroup.getChildAt(i).getTag());
                            }
                        }
                        FastSave.getInstance().saveObjectsList(STATUS_FILTER, statusList);
                        filterDialog.dismiss();
                        startActivity(new Intent(RecordListActivity.this, RecordListActivity.class));
                        finish();
                    });
                    break;
                case R.id.cancelFilter:
                    MaterialButton cancelFilter = childView.findViewById(R.id.cancelFilter);
                    cancelFilter.setOnClickListener(v -> {
                        FastSave.getInstance().saveLong(FROM_DATE, Util.startOfDay(System.currentTimeMillis()));
                        FastSave.getInstance().saveLong(TO_DATE, Util.endOfDay(System.currentTimeMillis()));
                        FastSave.getInstance().saveObjectsList(STATUS_FILTER, Arrays.asList("CANCELED", "WAITING", "IN_PROCESS", "COMPLETED"));
                        filterDialog.dismiss();
                        startActivity(new Intent(RecordListActivity.this, RecordListActivity.class));
                        finish();
                    });
                    break;
            }
        }
        filterDialog.show();
    }

    public void fromDatePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        new DatePickerDialog(RecordListActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                FastSave.getInstance().saveLong(FROM_DATE, Util.startOfDay(date.getTimeInMillis()));
                fromDateLabel.setText(Util.getStringDateTrue(Util.startOfDay(FastSave.getInstance().getLong(FROM_DATE, 0))));
            }
        },
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void toDatePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        new DatePickerDialog(RecordListActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                FastSave.getInstance().saveLong(TO_DATE, Util.endOfDay(date.getTimeInMillis()));
                toDateLabel.setText(Util.getStringDateTrue(Util.endOfDay(FastSave.getInstance().getLong(TO_DATE, 0))));
            }
        },
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        new DatePickerDialog(RecordListActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(RecordListActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        begin = date.getTimeInMillis();
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
            }
        },
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "Ща сек...", "Ща все сделаю...");
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mStompClient.disconnect();
    }

    @Override
    public void onItemClick(View view, int position) {
        API.getSingleRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), recordListAdapter.getItem(position).getId())
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<AllRecordResponse>() {
                    @Override
                    public void onSuccessful(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                        FastSave.getInstance().saveObject("RECORD", response.body());
                        startActivity(new Intent(RecordListActivity.this, SingleRecordActivity.class));
                        finish();
                    }

                    @Override
                    public void onEmpty(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {

                    }
                }));

//        nDialog = new NDialog(RecordListActivity.this, ButtonType.NO_BUTTON);
//        nDialog.isCancelable(true);
//        nDialog.setCustomView(R.layout.edit_record_view);
//        List<View> childViews = nDialog.getCustomViewChildren();
//        for (View childView : childViews) {
//            switch (childView.getId()) {
//                case R.id.inProgresBtn:
//                    Button inProgresBtn = childView.findViewById(R.id.inProgresBtn);
//                    inProgresBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            recordInProgress(recordListAdapter.getItem(position).getId());
//                        }
//                    });
//
//                    break;
//                case R.id.doneBtn:
//                    Button doneBtn = childView.findViewById(R.id.doneBtn);
//                    doneBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            recordDone(recordListAdapter.getItem(position).getId());
//                        }
//                    });
//                    break;
//                case R.id.cancelBtn:
//                    Button cancelBtn = childView.findViewById(R.id.cancelBtn);
//                    cancelBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                           recordCancele(recordListAdapter.getItem(position).getId());
//                        }
//                    });
//                    break;
//            }
//        }
//        nDialog.show();

    }

    private void recordCancele(String recordId) {
        API = APIClient.getClient().create(APIInterface.class);
        Call<AllRecordResponse> call = API.canceleRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), recordId);
        call.enqueue(new Callback<AllRecordResponse>() {
            @Override
            public void onResponse(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                if (response.code() == 200) {
                    Toast.makeText(RecordListActivity.this, "recordCancele", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.code() == 204) {
                        Toast.makeText(RecordListActivity.this, "204", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorHandler.showError(jObjError.getInt("code"));
                        } catch (Exception e) {
                            errorHandler.showCustomError(e.getMessage());
                            closeProgressDialog();
                        }
                    }
                }
                if (nDialog != null) {
                    nDialog.dismiss();
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(Call<AllRecordResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                if (nDialog != null) {
                    nDialog.dismiss();
                }
                closeProgressDialog();
            }
        });
    }

    private void recordDone(String recordId) {
        API = APIClient.getClient().create(APIInterface.class);
        Call<AllRecordResponse> call = API.changeRecordStatus(FastSave.getInstance().getString(ACCESS_TOKEN, ""), recordId, "COMPLETED");
        call.enqueue(new Callback<AllRecordResponse>() {
            @Override
            public void onResponse(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                if (response.code() == 200) {
                    Toast.makeText(RecordListActivity.this, "recordDone", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.code() == 204) {
                        Toast.makeText(RecordListActivity.this, "204", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorHandler.showError(jObjError.getInt("code"));
                        } catch (Exception e) {
                            errorHandler.showCustomError(e.getMessage());
                            closeProgressDialog();
                        }
                    }
                }
                if (nDialog != null) {
                    nDialog.dismiss();
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(Call<AllRecordResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                if (nDialog != null) {
                    nDialog.dismiss();
                }
                closeProgressDialog();
            }
        });
    }

    private void recordInProgress(String recordId) {
        API = APIClient.getClient().create(APIInterface.class);
        Call<AllRecordResponse> call = API.changeRecordStatus(FastSave.getInstance().getString(ACCESS_TOKEN, ""), recordId, "IN_PROCESS");
        call.enqueue(new Callback<AllRecordResponse>() {
            @Override
            public void onResponse(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                if (response.code() == 200) {
                    Toast.makeText(RecordListActivity.this, "recordInProgress", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.code() == 204) {
                        Toast.makeText(RecordListActivity.this, "204", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorHandler.showError(jObjError.getInt("code"));
                        } catch (Exception e) {
                            errorHandler.showCustomError(e.getMessage());
                            closeProgressDialog();
                        }
                    }
                }
                if (nDialog != null) {
                    nDialog.dismiss();
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(Call<AllRecordResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                if (nDialog != null) {
                    nDialog.dismiss();
                }
                closeProgressDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RecordListActivity.this, ChooseCarWash.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, false);
    }
}
