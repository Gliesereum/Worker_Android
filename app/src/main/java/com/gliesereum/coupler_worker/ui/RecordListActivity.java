package com.gliesereum.coupler_worker.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.NewRecordListAdapter;
import com.gliesereum.coupler_worker.adapter.customadapterrecycleview.listener.OnClickItemListener;
import com.gliesereum.coupler_worker.adapter.customadapterrecycleview.listener.OnLoadMoreListener;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.carwash.WorkersItem;
import com.gliesereum.coupler_worker.network.json.client_record_new.ClientRecordNewResponse;
import com.gliesereum.coupler_worker.network.json.client_record_new.RecordItem;
import com.gliesereum.coupler_worker.network.json.notificatoin.NotificatoinBody;
import com.gliesereum.coupler_worker.network.json.notificatoin.UserSubscribe;
import com.gliesereum.coupler_worker.network.json.pin.RemindPinCodeResponse;
import com.gliesereum.coupler_worker.network.json.record.PaymentResponse;
import com.gliesereum.coupler_worker.network.json.record.RecordsSearchBody;
import com.gliesereum.coupler_worker.util.ErrorHandler;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;

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

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.BUSSINES_ID;
import static com.gliesereum.coupler_worker.util.Constants.BUSSINES_NAME;
import static com.gliesereum.coupler_worker.util.Constants.FIREBASE_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.FROM_DATE;
import static com.gliesereum.coupler_worker.util.Constants.IS_ADMIN;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOCK;
import static com.gliesereum.coupler_worker.util.Constants.KARMA_BUSINESS_RECORD;
import static com.gliesereum.coupler_worker.util.Constants.RECORD_LIST_ACTIVITY;
import static com.gliesereum.coupler_worker.util.Constants.STATUS_FILTER;
import static com.gliesereum.coupler_worker.util.Constants.TO_DATE;
import static com.gliesereum.coupler_worker.util.Constants.WORKERS_LIST;
import static com.gliesereum.coupler_worker.util.Constants.WORKERS_MAP;

public class RecordListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    //    private RecordListAdapter recordListAdapter;
    private NewRecordListAdapter newRecordListAdapter;
    private List<RecordItem> recordsList = new ArrayList<>();
    private Map<String, String> carWashNameMap = new HashMap<>();
    private APIInterface API;
    private ErrorHandler errorHandler;
    private TextView splashTextView;
    private ProgressDialog progressDialog;
    //    private StompClient mStompClient;
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
    private ImageView backBtn;
    private TextView moneyCount;
    private String TAG = "activityTest";
    private CheckBox checkWaiting;
    private CheckBox checkInProcess;
    private CheckBox checkCompleted;
    private CheckBox checkCanceled;
    private ImageView fromBtn;
    private ImageView toBtn;
    private TextView fromLabel1;
    private TextView fromLabel2;
    private TextView toLabel1;
    private TextView toLabel2;
    private Toolbar toolbar;
    private Button lockBtn;
    private Integer page = 0;
    private boolean loadingFlag = true;
    private SwipeRefreshLayout swipeRefreshLayout;




    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list_new);

        initView();
        getAllRecord();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (FastSave.getInstance().getBoolean(IS_LOCK, false)) {
            new Util().lockScreen(this, this, lockBtn);
        }
        checkIsAdmin();

    }

    private void checkIsAdmin() {
        API.isAdministrator(FastSave.getInstance().getString(ACCESS_TOKEN, ""), FastSave.getInstance().getString(BUSSINES_ID, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<RemindPinCodeResponse>() {
                    @Override
                    public void onSuccessful(Call<RemindPinCodeResponse> call, Response<RemindPinCodeResponse> response) {
                        FastSave.getInstance().saveBoolean(IS_ADMIN, response.body().getResult());
                    }

                    @Override
                    public void onEmpty(Call<RemindPinCodeResponse> call, Response<RemindPinCodeResponse> response) {

                    }
                }));
    }

    private void subscribeToChanel() {
        UserSubscribe userSubscribe = new UserSubscribe(true, KARMA_BUSINESS_RECORD);
        userSubscribe.setObjectId(FastSave.getInstance().getString(BUSSINES_ID, ""));
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
        if (page == 0) {
            recordsSearchBody = new RecordsSearchBody();
            recordsSearchBody.setFrom(FastSave.getInstance().getLong(FROM_DATE, 0));
            recordsSearchBody.setTo(FastSave.getInstance().getLong(TO_DATE, 0));
            recordsSearchBody.setBusinessIds(Arrays.asList(FastSave.getInstance().getString(BUSSINES_ID, "")));
            recordsSearchBody.setProcesses(FastSave.getInstance().getObjectsList(STATUS_FILTER, String.class));
            recordsSearchBody.setSize(20);
            recordsSearchBody.setPage(page);

            List<WorkersItem> workersItemList = FastSave.getInstance().getObjectsList(WORKERS_LIST, WorkersItem.class);
            Map<String, WorkersItem> workersMap = new HashMap<>();
            for (int i = 0; i < workersItemList.size(); i++) {
                workersMap.put(workersItemList.get(i).getId(), workersItemList.get(i));
            }
            FastSave.getInstance().saveObject(WORKERS_MAP, workersMap);

            API.getAllRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), recordsSearchBody)
                    .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<ClientRecordNewResponse>() {
                        @Override
                        public void onSuccessful(Call<ClientRecordNewResponse> call, Response<ClientRecordNewResponse> response) {
                            recordsList = response.body().getContent();
                            if (recordsList != null && recordsList.size() > 0) {
                                bussinesName.setText("Список заказов: " + FastSave.getInstance().getString(BUSSINES_NAME, ""));
//                            recordListAdapter.setItems(recordsList);
                                newRecordListAdapter.addItems(recordsList);
                                loadingFlag = true;
////                                int count = 0;
////                                for (int i = 0; i < recordsList.size(); i++) {
////                                    if (recordsList.get(i).getStatusProcess().equals("COMPLETED")) {
////                                        count += recordsList.get(i).getPrice();
////                                    }
////                                }
//                                moneyCount.setText("Касса: " + count + " грн");
                            }
                            Log.d(TAG, "onSuccessful: ");
                            FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, true);
                        }

                        @Override
                        public void onEmpty(Call<ClientRecordNewResponse> call, Response<ClientRecordNewResponse> response) {
                            Toast.makeText(RecordListActivity.this, "Список заказов на сегодня пуст", Toast.LENGTH_SHORT).show();
                            newRecordListAdapter.hiddenLoading();
                            FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, true);
                        }
                    }));
            recordsSearchBody.setProcesses(Arrays.asList("COMPLETED"));
            API.getAllRecordPayment(FastSave.getInstance().getString(ACCESS_TOKEN, ""), recordsSearchBody)
                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<PaymentResponse>() {
                        @Override
                        public void onSuccessful(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                            moneyCount.setText("Касса: " + response.body().getSum() + " грн");
                        }

                        @Override
                        public void onEmpty(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                            moneyCount.setText("Касса: " + 0 + " грн");

                        }
                    }));
        } else {
            recordsSearchBody = new RecordsSearchBody();
            recordsSearchBody.setFrom(FastSave.getInstance().getLong(FROM_DATE, 0));
            recordsSearchBody.setTo(FastSave.getInstance().getLong(TO_DATE, 0));
            recordsSearchBody.setBusinessIds(Arrays.asList(FastSave.getInstance().getString(BUSSINES_ID, "")));
            recordsSearchBody.setProcesses(FastSave.getInstance().getObjectsList(STATUS_FILTER, String.class));
            recordsSearchBody.setSize(20);
            recordsSearchBody.setPage(page);

            List<WorkersItem> workersItemList = FastSave.getInstance().getObjectsList(WORKERS_LIST, WorkersItem.class);
            Map<String, WorkersItem> workersMap = new HashMap<>();
            for (int i = 0; i < workersItemList.size(); i++) {
                workersMap.put(workersItemList.get(i).getId(), workersItemList.get(i));
            }
            FastSave.getInstance().saveObject(WORKERS_MAP, workersMap);

            API.getAllRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), recordsSearchBody)
                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<ClientRecordNewResponse>() {
                        @Override
                        public void onSuccessful(Call<ClientRecordNewResponse> call, Response<ClientRecordNewResponse> response) {
                            recordsList = response.body().getContent();
                            if (recordsList != null && recordsList.size() > 0) {
                                bussinesName.setText("Список заказов: " + FastSave.getInstance().getString(BUSSINES_NAME, ""));
//                            recordListAdapter.setItems(recordsList);
                                newRecordListAdapter.addItems(recordsList);
                                loadingFlag = true;
                            }
                            Log.d(TAG, "onSuccessful: ");
                            FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, true);
                        }

                        @Override
                        public void onEmpty(Call<ClientRecordNewResponse> call, Response<ClientRecordNewResponse> response) {
                            newRecordListAdapter.hiddenLoading();
                            FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, true);
                        }
                    }));
        }

    }


    private void initView() {
        FastSave.init(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        new Util(this, toolbar, 1).addNavigation();
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        errorHandler = new ErrorHandler(this, this);
        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recordListAdapter = new RecordListAdapter(RecordListActivity.this);
//        recordListAdapter.setClickListener(RecordListActivity.this);
//        recyclerView.setAdapter(recordListAdapter);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
//                startActivity(new Intent(MainActivity.this, MainActivity.class));
                FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, true);
                newRecordListAdapter.removeAll();
                page = 0;
                getAllRecord();
//                finish();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        newRecordListAdapter = new NewRecordListAdapter(this);
        newRecordListAdapter.endLessScrolled(recyclerView);
        recyclerView.setAdapter(newRecordListAdapter);

        newRecordListAdapter.setOnClickItemListener(recyclerView, new OnClickItemListener<RecordItem>() {
            @Override
            public void onClickItem(int position, RecordItem element) {
                if (element != null) {
                    FastSave.getInstance().saveObject("RECORD", element);
                    startActivity(new Intent(RecordListActivity.this, SingleRecordActivity.class));
                    finish();
                }
            }

            @Override
            public void onLongClickItem(int position, RecordItem element) {

            }
        });

        newRecordListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadingFlag = false;
                newRecordListAdapter.showLoading();
                ++page;
                getAllRecord();
            }
        });


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
        lockBtn = findViewById(R.id.lockBtn);
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Util().lockScreen(RecordListActivity.this, RecordListActivity.this, lockBtn);
            }
        });
    }

    View.OnClickListener fromDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fromDatePicker();
        }
    };

    View.OnClickListener toDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toDatePicker();
        }
    };

    private void openFilterDialog() {
        NDialog filterDialog = new NDialog(RecordListActivity.this, ButtonType.NO_BUTTON);
//        filterDialog.isCancelable(true);
        filterDialog.setCustomView(R.layout.filter_dialod_new);
        List<View> childViews = filterDialog.getCustomViewChildren();
        for (View childView : childViews) {
            switch (childView.getId()) {
                case R.id.checkWaiting:
                    checkWaiting = childView.findViewById(R.id.checkWaiting);
                    if (FastSave.getInstance().getObjectsList(STATUS_FILTER, String.class).contains(checkWaiting.getTag())) {
                        checkWaiting.setChecked(true);
                    }
                    break;
                case R.id.checkInProcess:
                    checkInProcess = childView.findViewById(R.id.checkInProcess);
                    if (FastSave.getInstance().getObjectsList(STATUS_FILTER, String.class).contains(checkInProcess.getTag())) {
                        checkInProcess.setChecked(true);
                    }
                    break;
                case R.id.checkCompleted:
                    checkCompleted = childView.findViewById(R.id.checkCompleted);
                    if (FastSave.getInstance().getObjectsList(STATUS_FILTER, String.class).contains(checkCompleted.getTag())) {
                        checkCompleted.setChecked(true);
                    }
                    break;
                case R.id.checkCanceled:
                    checkCanceled = childView.findViewById(R.id.checkCanceled);
                    if (FastSave.getInstance().getObjectsList(STATUS_FILTER, String.class).contains(checkCanceled.getTag())) {
                        checkCanceled.setChecked(true);
                    }
                    break;
                case R.id.fromLabel1:
                    fromLabel1 = childView.findViewById(R.id.fromLabel1);
                    fromLabel1.setOnClickListener(fromDateListener);
                    break;
                case R.id.fromLabel2:
                    fromLabel2 = childView.findViewById(R.id.fromLabel2);
                    fromLabel2.setText(Util.getStringDate(Util.startOfDay(FastSave.getInstance().getLong(FROM_DATE, 0))));
                    fromLabel2.setOnClickListener(fromDateListener);
                    break;
                case R.id.toLabel1:
                    toLabel1 = childView.findViewById(R.id.toLabel1);
                    toLabel1.setOnClickListener(toDateListener);
                    break;
                case R.id.toLabel2:
                    toLabel2 = childView.findViewById(R.id.toLabel2);
                    toLabel2.setText(Util.getStringDate(Util.endOfDay(FastSave.getInstance().getLong(TO_DATE, 0))));
                    toLabel2.setOnClickListener(toDateListener);
                    break;
                case R.id.fromBtn:
                    fromBtn = childView.findViewById(R.id.fromBtn);
                    fromBtn.setOnClickListener(fromDateListener);
                    break;
                case R.id.toBtn:
                    toBtn = childView.findViewById(R.id.toBtn);
                    toBtn.setOnClickListener(toDateListener);
                    break;
                case R.id.acceptFilter:
                    MaterialButton nowOrderBtn = childView.findViewById(R.id.acceptFilter);
                    nowOrderBtn.setOnClickListener(v -> {
                        List<String> statusList = new ArrayList<>();
//                        for (int i = 0; i < chipGroup.getChildCount(); i++) {
//                            if (((Chip) chipGroup.getChildAt(i)).isChecked()) {
//                                statusList.add((String) chipGroup.getChildAt(i).getTag());
//                            }
//                        }
                        if (checkWaiting.isChecked()) {
                            statusList.add((String) checkWaiting.getTag());
                        }
                        if (checkInProcess.isChecked()) {
                            statusList.add((String) checkInProcess.getTag());
                        }
                        if (checkCompleted.isChecked()) {
                            statusList.add((String) checkCompleted.getTag());
                        }
                        if (checkCanceled.isChecked()) {
                            statusList.add((String) checkCanceled.getTag());
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
        date = Calendar.getInstance();
        new DatePickerDialog(RecordListActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                FastSave.getInstance().saveLong(FROM_DATE, Util.startOfDay(date.getTimeInMillis()));
                fromLabel2.setText(Util.getStringDate(Util.startOfDay(FastSave.getInstance().getLong(FROM_DATE, 0))));
            }
        },
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void toDatePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(RecordListActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                FastSave.getInstance().saveLong(TO_DATE, Util.endOfDay(date.getTimeInMillis()));
                toLabel2.setText(Util.getStringDate(Util.endOfDay(FastSave.getInstance().getLong(TO_DATE, 0))));
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

//    @Override
//    public void onItemClick(View view, int position) {
//        FastSave.getInstance().saveObject("RECORD", recordListAdapter.getItem(position));
//        startActivity(new Intent(RecordListActivity.this, SingleRecordActivity.class));
//        finish();
////        API.getSingleRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), recordListAdapter.getItem(position).getId())
////                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<AllRecordResponse>() {
////                    @Override
////                    public void onSuccessful(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
////                        FastSave.getInstance().saveObject("RECORD", response.body());
////                        startActivity(new Intent(RecordListActivity.this, SingleRecordActivity.class));
////                        finish();
////                    }
////
////                    @Override
////                    public void onEmpty(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
////
////                    }
////                }));
//    }

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
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, true);
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, false);
        Log.d(TAG, "onStop: ");
    }
}
