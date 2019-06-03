package com.gliesereum.coupler_worker.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.SplashActivity;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.json.user.TokenInfo;
import com.gliesereum.coupler_worker.network.json.user.UserResponse;
import com.gliesereum.coupler_worker.ui.ChooseCarWash;
import com.gliesereum.coupler_worker.ui.LoginActivity;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_EXPIRATION_DATE;
import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN_WITHOUT_BEARER;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOGIN;
import static com.gliesereum.coupler_worker.util.Constants.REFRESH_EXPIRATION_DATE;
import static com.gliesereum.coupler_worker.util.Constants.REFRESH_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.USER_ID;
import static com.gliesereum.coupler_worker.util.Constants.USER_NAME;
import static com.gliesereum.coupler_worker.util.Constants.USER_SECOND_NAME;

public class ErrorHandler {

    private Context context;
    private Activity activity;

    public ErrorHandler(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void showError(Integer errorCode) {
        switch (errorCode) {
            case 1104:
                refreshToken(FastSave.getInstance().getString(REFRESH_TOKEN, ""));
                break;
            case 1105:
                FastSave.getInstance().saveBoolean(IS_LOGIN, false);
                activity.startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                activity.finish();
                break;
            default:
                try {
                    new LottieAlertDialog.Builder(context, DialogTypes.TYPE_WARNING)
                            .setTitle("Предупреждение")
                            .setDescription(ErrorList.init(context).getErrorMessage(errorCode))
                            .build()
                            .show();
                } catch (Exception e) {
                    new LottieAlertDialog.Builder(context, DialogTypes.TYPE_ERROR)
                            .setTitle(context.getResources().getString(R.string.error))
                            .setDescription("[" + errorCode + "] " + context.getResources().getString(R.string.unknown_error))
                            .build()
                            .show();
                }
                break;
        }
    }

    public void showCustomError(String message) {
        try {
            activity.startActivity(new Intent(context, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            activity.finish();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void refreshToken(String refreshToken) {
        APIInterface API = APIClient.getClient().create(APIInterface.class);
        Call<UserResponse> call = API.refreshAccessToken(refreshToken);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 200) {
                    FastSave.getInstance().saveBoolean(IS_LOGIN, true);
                    Toast.makeText(context, "Refresh!", Toast.LENGTH_SHORT).show();
                    setTokenInfo(response.body().getTokenInfo());
                    if (FastSave.getInstance().getString(USER_NAME, "").equals("") || FastSave.getInstance().getString(USER_SECOND_NAME, "").equals("")) {
                        activity.startActivity(new Intent(context, ChooseCarWash.class));
                        activity.finish();
                    } else {
                        activity.startActivity(new Intent(context, LoginActivity.class));
                        activity.finish();
                    }
                } else {
                    try {
                        FastSave.getInstance().saveBoolean(IS_LOGIN, false);
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        showError(jObjError.getInt("code"));
                        activity.startActivity(new Intent(context, LoginActivity.class));
                        activity.finish();
                    } catch (Exception e) {
                        FastSave.getInstance().saveBoolean(IS_LOGIN, false);
                        showCustomError(e.getMessage());
                        activity.startActivity(new Intent(context, LoginActivity.class));
                        activity.finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
//                closeProgressDialog();
                showCustomError(t.getMessage());
                activity.startActivity(new Intent(context, LoginActivity.class));
                activity.finish();
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
}
