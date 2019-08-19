package com.gliesereum.coupler_worker.network;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.gliesereum.coupler_worker.util.ErrorHandler;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomCallback {

    private Context context;
    private ErrorHandler errorHandler;
    private LottieAlertDialog alertDialog;


    public CustomCallback(Context context, Activity activity) {
        this.context = context;
        this.errorHandler = new ErrorHandler(context, activity);
    }

    public <T> Callback<T> getResponse(ResponseCallback<T> responseCallback) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.code() == 200) {
                    responseCallback.onSuccessful(call, response);
                } else if (response.code() == 204) {
                    responseCallback.onEmpty(call, response);
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
            public void onFailure(Call<T> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        };
    }

    public <T> Callback<T> getResponseWithProgress(ResponseCallback<T> responseCallback) {
        showProgressDialog();
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.code() == 200) {
                    responseCallback.onSuccessful(call, response);
                } else if (response.code() == 204) {
                    responseCallback.onEmpty(call, response);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
                    }
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                Log.e("TAG", "onFailure: " + t.getMessage());
                closeProgressDialog();
            }
        };
    }

    public interface ResponseCallback<T> {
        void onSuccessful(Call<T> call, Response<T> response);

        void onEmpty(Call<T> call, Response<T> response);
    }

    private void showProgressDialog() {
        try {
            if (alertDialog == null || !alertDialog.isShowing()) {
                alertDialog = new LottieAlertDialog.Builder(context, DialogTypes.TYPE_LOADING)
                        .setTitle("Подождите")
                        .setDescription("Идет загрузка")
                        .build();
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        } catch (Exception e) {

        }

    }

    private void closeProgressDialog() {
        try {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }
}
