package com.gliesereum.karmaworker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gliesereum.karmaworker.network.APIClient;
import com.gliesereum.karmaworker.network.APIInterface;
import com.gliesereum.karmaworker.network.json.status.StatusResponse;
import com.gliesereum.karmaworker.network.json.user.TokenInfo;
import com.gliesereum.karmaworker.network.json.user.UserResponse;
import com.gliesereum.karmaworker.ui.ChooseCarWash;
import com.gliesereum.karmaworker.ui.LoginActivity;
import com.gliesereum.karmaworker.util.ErrorHandler;
import com.gliesereum.karmaworker.util.FastSave;
import com.gliesereum.karmaworker.util.Util;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karmaworker.util.Constants.ACCESS_EXPIRATION_DATE;
import static com.gliesereum.karmaworker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karmaworker.util.Constants.ACCESS_TOKEN_WITHOUT_BEARER;
import static com.gliesereum.karmaworker.util.Constants.IS_LOGIN;
import static com.gliesereum.karmaworker.util.Constants.REFRESH_EXPIRATION_DATE;
import static com.gliesereum.karmaworker.util.Constants.REFRESH_TOKEN;
import static com.gliesereum.karmaworker.util.Constants.STATUS_UP;
import static com.gliesereum.karmaworker.util.Constants.USER_ID;
import static com.gliesereum.karmaworker.util.Constants.USER_NAME;
import static com.gliesereum.karmaworker.util.Constants.USER_SECOND_NAME;

public class SplashActivity extends AppCompatActivity {

    private APIInterface API;
    private ErrorHandler errorHandler;
    private ConstraintLayout errorBlock;
    private Button refreshBtn;
    private String TAG = "test_log";
    private LottieAlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
        initView();
        checkStatus();

    }

    private void initData() {
        FastSave.init(getApplicationContext());
        errorHandler = new ErrorHandler(this, this);
        API = APIClient.getClient().create(APIInterface.class);
    }

    private void initView() {
        errorBlock = findViewById(R.id.errorBlock);
        refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(v -> checkStatus());
    }

    public void checkStatus() {
        errorBlock.setVisibility(View.GONE);
        showProgressDialog();
        Call<StatusResponse> call = API.checkStatus();
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getAccountService() != null && response.body().getAccountService().equals(STATUS_UP)
                            && response.body().getMailService() != null && response.body().getMailService().equals(STATUS_UP)
                            && response.body().getKarmaService() != null && response.body().getKarmaService().equals(STATUS_UP)) {
                        checkAccessToken();
                    } else {
                        errorBlock.setVisibility(View.VISIBLE);
                    }
                } else {
                    errorBlock.setVisibility(View.VISIBLE);
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                errorBlock.setVisibility(View.VISIBLE);
                closeProgressDialog();
            }
        });
    }

    public void checkAccessToken() {
        Log.d(TAG, "checkAccessToken: ");
        Call<UserResponse> call = API.checkAccessToken(FastSave.getInstance().getString(ACCESS_TOKEN_WITHOUT_BEARER, ""));
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 200) {
                    if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
                        checkToken();
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    checkToken();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                errorBlock.setVisibility(View.VISIBLE);
                closeProgressDialog();
            }
        });
    }

    public void checkToken() {
        Log.d(TAG, "checkToken: ");
        Long accessExpirationDate = FastSave.getInstance().getLong(ACCESS_EXPIRATION_DATE, 0);
        Long refreshExpirationDate = FastSave.getInstance().getLong(REFRESH_EXPIRATION_DATE, 0);

        if (accessExpirationDate != 0) {
            if (Util.checkExpirationToken(accessExpirationDate)) {
                FastSave.getInstance().saveBoolean(IS_LOGIN, true);
                if (FastSave.getInstance().getString(USER_NAME, "").equals("") || FastSave.getInstance().getString(USER_SECOND_NAME, "").equals("")) {
                    startActivity(new Intent(SplashActivity.this, ChooseCarWash.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, ChooseCarWash.class));
                    finish();
                }
            } else {
                if (Util.checkExpirationToken(refreshExpirationDate)) {
                    refreshToken(FastSave.getInstance().getString(REFRESH_TOKEN, ""));
                } else {
                    FastSave.getInstance().saveBoolean(IS_LOGIN, false);
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        } else {
            FastSave.getInstance().saveBoolean(IS_LOGIN, false);
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

    public void refreshToken(String refreshToken) {
        Log.d(TAG, "refreshToken: ");
        API = APIClient.getClient().create(APIInterface.class);
        Call<UserResponse> call = API.refreshAccessToken(refreshToken);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 200) {
                    FastSave.getInstance().saveBoolean(IS_LOGIN, true);
                    Toast.makeText(SplashActivity.this, "Refresh!", Toast.LENGTH_SHORT).show();
                    setTokenInfo(response.body().getTokenInfo());
                    if (FastSave.getInstance().getString(USER_NAME, "").equals("") || FastSave.getInstance().getString(USER_SECOND_NAME, "").equals("")) {
                        startActivity(new Intent(SplashActivity.this, ChooseCarWash.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    try {
                        FastSave.getInstance().saveBoolean(IS_LOGIN, false);
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    } catch (Exception e) {
                        FastSave.getInstance().saveBoolean(IS_LOGIN, false);
                        errorHandler.showCustomError(e.getMessage());
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void setTokenInfo(TokenInfo response) {
        FastSave.getInstance().saveBoolean(IS_LOGIN, true);
        FastSave.getInstance().saveString(ACCESS_TOKEN, "Bearer " + response.getAccessToken());
        FastSave.getInstance().saveString(ACCESS_TOKEN_WITHOUT_BEARER, response.getAccessToken());
        FastSave.getInstance().saveString(REFRESH_TOKEN, response.getRefreshToken());
        FastSave.getInstance().saveString(USER_ID, response.getUserId());
        FastSave.getInstance().saveLong(ACCESS_EXPIRATION_DATE, response.getAccessExpirationDate());
        FastSave.getInstance().saveLong(REFRESH_EXPIRATION_DATE, response.getRefreshExpirationDate());


    }

    public void showProgressDialog() {
        alertDialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
                .setTitle("Загрузка")
                .setDescription("Загружается контент, подождите")
                .build();
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    public void closeProgressDialog() {
        alertDialog.dismiss();
    }
}
