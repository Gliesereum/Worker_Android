package com.gliesereum.coupler_worker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaos.view.PinView;
import com.gliesereum.coupler_worker.MyFirebaseMessagingService;
import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.MyRecyclerViewAdapter;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.carwash.AllCarWashResponse;
import com.gliesereum.coupler_worker.network.json.notificatoin.NotificatoinBody;
import com.gliesereum.coupler_worker.network.json.notificatoin.RegistrationTokenDeleteResponse;
import com.gliesereum.coupler_worker.network.json.notificatoin.UserSubscribe;
import com.gliesereum.coupler_worker.network.json.pin.PinResponse;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_EXPIRATION_DATE;
import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN_WITHOUT_BEARER;
import static com.gliesereum.coupler_worker.util.Constants.BUSINESS_CATEGORY_ID;
import static com.gliesereum.coupler_worker.util.Constants.CARWASH_ID;
import static com.gliesereum.coupler_worker.util.Constants.CARWASH_NAME;
import static com.gliesereum.coupler_worker.util.Constants.CARWASH_TIME_ZONE;
import static com.gliesereum.coupler_worker.util.Constants.CORPORATION_ID;
import static com.gliesereum.coupler_worker.util.Constants.FIREBASE_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.FROM_DATE;
import static com.gliesereum.coupler_worker.util.Constants.IS_EXIST_PIN;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOCK;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOGIN;
import static com.gliesereum.coupler_worker.util.Constants.KARMA_BUSINESS_RECORD;
import static com.gliesereum.coupler_worker.util.Constants.PIN_CODE;
import static com.gliesereum.coupler_worker.util.Constants.REFRESH_EXPIRATION_DATE;
import static com.gliesereum.coupler_worker.util.Constants.REFRESH_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.STATUS_FILTER;
import static com.gliesereum.coupler_worker.util.Constants.TO_DATE;
import static com.gliesereum.coupler_worker.util.Constants.USER_ID;

public class ChooseCarWash extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private APIInterface API;
    private CustomCallback customCallback;
    private List<AllCarWashResponse> carWashList;
    private MyRecyclerViewAdapter adapter;
    private Button logoutBtn;
    private ImageView imageView3;
    private ImageButton lockBtn;
    private PinView codeView;
    private String pin = "";
    private Button enterPinBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_car_wash_new);
        FastSave.init(getApplicationContext());
        initView();
        cleanRecordFilter();
        getAllCarWash();
        startService(new Intent(this, MyFirebaseMessagingService.class));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        checkPin();
        if (FastSave.getInstance().getBoolean(IS_LOCK, false)) {
            lockScreen();
        }


    }

    private void checkPin() {
        API.getPinCode(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<PinResponse>() {
                    @Override
                    public void onSuccessful(Call<PinResponse> call, Response<PinResponse> response) {
                        FastSave.getInstance().saveBoolean(IS_EXIST_PIN, true);
                        FastSave.getInstance().saveString(PIN_CODE, response.body().getPinCode());
                    }

                    @Override
                    public void onEmpty(Call<PinResponse> call, Response<PinResponse> response) {
                        FastSave.getInstance().deleteValue(IS_EXIST_PIN);
                        FastSave.getInstance().deleteValue(PIN_CODE);

                    }
                }));

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
        logoutBtn = findViewById(R.id.backBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        lockBtn = findViewById(R.id.lockBtn);
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockScreen();
            }
        });
    }

    private void lockScreen() {
        if (FastSave.getInstance().getBoolean(IS_EXIST_PIN, false)) {
            FastSave.getInstance().saveBoolean(IS_LOCK, true);
            NDialog nDialog = new NDialog(ChooseCarWash.this, ButtonType.NO_BUTTON);
            nDialog.isCancelable(false);
            nDialog.setCustomView(R.layout.lock_dialod);
            List<View> childViews = nDialog.getCustomViewChildren();
            for (View childView : childViews) {
                switch (childView.getId()) {
                    case R.id.codeView:
                        codeView = childView.findViewById(R.id.codeView);
                        codeView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                pin = String.valueOf(s);
                                Log.d("PIN", "onTextChanged: " + pin);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (s.length() < 4) {
                                    enterPinBtn.setEnabled(false);
                                } else {
                                    enterPinBtn.setEnabled(true);
                                }

                            }
                        });
                        break;
                    case R.id.enterPinBtn:
                        enterPinBtn = childView.findViewById(R.id.enterPinBtn);
                        enterPinBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (FastSave.getInstance().getString(PIN_CODE, "").equals(pin)) {
                                    pin = "";
                                    FastSave.getInstance().saveBoolean(IS_LOCK, false);
                                    nDialog.dismiss();
                                } else {
                                    Toast.makeText(ChooseCarWash.this, "Неверный PIN код", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        break;
                }
            }
            nDialog.show();
        } else {

        }

    }

    private void logout() {
        deleteRegistrationToken();
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

    private void deleteRegistrationToken() {
        API.deleteRegistrationToken(FastSave.getInstance().getString(ACCESS_TOKEN, ""), FastSave.getInstance().getString(FIREBASE_TOKEN, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<RegistrationTokenDeleteResponse>() {
                    @Override
                    public void onSuccessful(Call<RegistrationTokenDeleteResponse> call, Response<RegistrationTokenDeleteResponse> response) {

                    }

                    @Override
                    public void onEmpty(Call<RegistrationTokenDeleteResponse> call, Response<RegistrationTokenDeleteResponse> response) {

                    }
                }));

    }

    private void getAllCarWash() {

        API.getAllBusiness(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
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
        FastSave.getInstance().saveString(CORPORATION_ID, adapter.getItem(position).getCorporationId());
        FastSave.getInstance().saveString(CARWASH_NAME, adapter.getItem(position).getName());
        FastSave.getInstance().saveString(BUSINESS_CATEGORY_ID, adapter.getItem(position).getBusinessCategoryId());
        FastSave.getInstance().saveInt(CARWASH_TIME_ZONE, adapter.getItem(position).getTimeZone());
        subscribeToChanel();
        startActivity(new Intent(ChooseCarWash.this, RecordListActivity.class));
    }

    private void subscribeToChanel() {
        UserSubscribe userSubscribe = new UserSubscribe(true, KARMA_BUSINESS_RECORD);
        userSubscribe.setObjectId(FastSave.getInstance().getString(CARWASH_ID, ""));
        NotificatoinBody notificatoinBody = new NotificatoinBody(FastSave.getInstance().getString(FIREBASE_TOKEN, ""), true, Arrays.asList(userSubscribe));
        API.subscribeToChanel(FastSave.getInstance().getString(ACCESS_TOKEN, ""), notificatoinBody, true)
                .enqueue(new Callback<List<UserSubscribe>>() {
                    @Override
                    public void onResponse(Call<List<UserSubscribe>> call, Response<List<UserSubscribe>> response) {

                    }

                    @Override
                    public void onFailure(Call<List<UserSubscribe>> call, Throwable t) {

                    }
                });
    }
}
