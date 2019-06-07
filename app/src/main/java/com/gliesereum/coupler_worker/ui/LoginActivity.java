package com.gliesereum.coupler_worker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chaos.view.PinView;
import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.code.CodeResponse;
import com.gliesereum.coupler_worker.network.json.code.SigninBody;
import com.gliesereum.coupler_worker.network.json.notificatoin.NotificatoinBody;
import com.gliesereum.coupler_worker.network.json.status.StatusRegistration;
import com.gliesereum.coupler_worker.network.json.user.UserResponse;
import com.gliesereum.coupler_worker.util.ErrorHandler;
import com.gliesereum.coupler_worker.util.FastSave;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.rilixtech.CountryCodePicker;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_EXPIRATION_DATE;
import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN_WITHOUT_BEARER;
import static com.gliesereum.coupler_worker.util.Constants.FIREBASE_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOGIN;
import static com.gliesereum.coupler_worker.util.Constants.REFRESH_EXPIRATION_DATE;
import static com.gliesereum.coupler_worker.util.Constants.REFRESH_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.USER_ID;
import static com.gliesereum.coupler_worker.util.Constants.USER_NAME;
import static com.gliesereum.coupler_worker.util.Constants.USER_SECOND_NAME;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private CountryCodePicker ccp;
    private Button getCodeBtn;
    private Button loginBtn;
    private TextInputEditText phoneTextView;
    private ConstraintLayout valueBlock;
    private PinView codeView;
    private TextView codeLabel1;
    private TextView codeLabel2;
    private TextView timerLabel;
    private String code;
    private CountDownTimer countDownTimer;
    private APIInterface API;
    private ErrorHandler errorHandler;
    boolean doubleBackToExitPressedOnce;
    private CustomCallback customCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FastSave.init(getApplicationContext());
        initData();
        initView();
    }

    private void initData() {
        FastSave.init(getApplicationContext());
        customCallback = new CustomCallback(this, this);
        API = APIClient.getClient().create(APIInterface.class);
        errorHandler = new ErrorHandler(this, this);
        doubleBackToExitPressedOnce = false;
    }

    private void initView() {
        getCodeBtn = findViewById(R.id.registerBtn);
        getCodeBtn.setOnClickListener(this);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        ccp = findViewById(R.id.ccp);
        phoneTextView = findViewById(R.id.phoneTextView);
        phoneTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 5) {
                    getCodeBtn.setEnabled(true);
                } else {
                    getCodeBtn.setEnabled(false);
                }
            }
        });
        valueBlock = findViewById(R.id.valueBlock);
        codeView = findViewById(R.id.codeView);
        codeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                code = String.valueOf(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 6) {
                    loginBtn.setEnabled(false);
                } else {
                    loginBtn.setEnabled(true);
                }

            }
        });
        codeLabel1 = findViewById(R.id.codeLabel1);
        codeLabel2 = findViewById(R.id.codeLabel2);
        timerLabel = findViewById(R.id.timerLabel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBtn:
                checkPhone(ccp.getFullNumber() + phoneTextView.getText().toString());
                break;
            case R.id.loginBtn:
                signIn(new SigninBody(ccp.getFullNumber() + phoneTextView.getText().toString(), code, "PHONE"));
                break;
        }

    }

    private void checkPhone(String phone) {
        API.checkExist(phone)
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<StatusRegistration>() {
                    @Override
                    public void onSuccessful(Call<StatusRegistration> call, Response<StatusRegistration> response) {
                        if (response.body().isExist()) {
                            getPhoneCode(ccp.getFullNumber() + phoneTextView.getText().toString());
                        } else {
                            new LottieAlertDialog.Builder(LoginActivity.this, DialogTypes.TYPE_WARNING)
                                    .setTitle("Предупреждение")
                                    .setDescription("Данного номера нет в базе. Обратитесь к владельцу безнеса.")
                                    .build()
                                    .show();
                        }
                    }

                    @Override
                    public void onEmpty(Call<StatusRegistration> call, Response<StatusRegistration> response) {

                    }
                }));

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(180000, 1000) {
            public void onTick(long millisUntilFinished) {
                setTimerTime(millisUntilFinished);
            }

            public void onFinish() {
                showValueBlock();
            }
        }.start();
    }

    private void saveUserInfo(UserResponse user) {
        FastSave.getInstance().saveBoolean(IS_LOGIN, true);
        FastSave.getInstance().saveString(USER_NAME, user.getUser().getFirstName());
        FastSave.getInstance().saveString(USER_SECOND_NAME, user.getUser().getLastName());
        FastSave.getInstance().saveString(USER_ID, user.getUser().getId());
        FastSave.getInstance().saveString(ACCESS_TOKEN, "Bearer " + user.getTokenInfo().getAccessToken());
        FastSave.getInstance().saveString(ACCESS_TOKEN_WITHOUT_BEARER, user.getTokenInfo().getAccessToken());
        FastSave.getInstance().saveString(REFRESH_TOKEN, user.getTokenInfo().getRefreshToken());
        FastSave.getInstance().saveLong(ACCESS_EXPIRATION_DATE, user.getTokenInfo().getAccessExpirationDate());
        FastSave.getInstance().saveLong(REFRESH_EXPIRATION_DATE, user.getTokenInfo().getRefreshExpirationDate());
        FastSave.getInstance().saveObject("userInfo", user);
    }

    private void getRegToken(UserResponse user) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("TAG_NOTIF", newToken);
            FastSave.getInstance().saveString(FIREBASE_TOKEN, newToken);
            NotificatoinBody notificatoinBody = new NotificatoinBody(newToken, true);
            API.sendRegistrationToken(FastSave.getInstance().getString(ACCESS_TOKEN, ""), notificatoinBody)
                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<NotificatoinBody>() {
                        @Override
                        public void onSuccessful(Call<NotificatoinBody> call, Response<NotificatoinBody> response) {
                            Toast.makeText(LoginActivity.this, "Уведомления включены", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onEmpty(Call<NotificatoinBody> call, Response<NotificatoinBody> response) {

                        }
                    }));
        });
    }

    public void signIn(SigninBody signinBody) {
        Call<UserResponse> call = API.signIn(signinBody);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 200) {
                    countDownTimer.cancel();
                    getRegToken(response.body());
                    saveUserInfo(response.body());
                    startActivity(new Intent(LoginActivity.this, ChooseCarWash.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    public void getPhoneCode(final String phone) {
        Call<CodeResponse> call = API.getPhoneCode(phone);
        call.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if (response.code() == 200) {
                    showCodeBlock();
                    setPhoneCodeLabel(phone);
                    startTimer();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    public void showValueBlock() {
        codeLabel1.setVisibility(View.GONE);
        codeLabel2.setVisibility(View.GONE);
        timerLabel.setVisibility(View.GONE);
        codeView.setVisibility(View.GONE);
        loginBtn.setVisibility(View.GONE);
        getCodeBtn.setVisibility(View.VISIBLE);
        valueBlock.setVisibility(View.VISIBLE);
        getCodeBtn.setEnabled(false);
    }

    public void showCodeBlock() {
        valueBlock.setVisibility(View.GONE);
        getCodeBtn.setVisibility(View.GONE);
        loginBtn.setVisibility(View.VISIBLE);
        codeLabel1.setVisibility(View.VISIBLE);
        codeLabel2.setVisibility(View.VISIBLE);
        timerLabel.setVisibility(View.VISIBLE);
        codeView.setVisibility(View.VISIBLE);
        loginBtn.setEnabled(false);
        codeView.requestFocus();
    }

    public void setTimerTime(long millisUntilFinished) {
        timerLabel.setText("" + String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
    }

    public void setPhoneCodeLabel(String phone) {
        codeLabel2.setText("+" + phone);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Пожалуйста, нажмите НАЗАД еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
