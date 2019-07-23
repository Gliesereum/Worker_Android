package com.gliesereum.coupler_worker.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.pin.RemindPinCodeResponse;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOCK;
import static com.gliesereum.coupler_worker.util.Constants.PIN_CODE;

public class LockActivity extends AppCompatActivity {

    private PinView codeView;
    private PinView newPinCode;
    private PinView confirmPinCode;
    private String pin = "";
    private String newPin = "";
    private String confirmPin = "";
    private Button createPinBtn;
    private APIInterface API;
    private CustomCallback customCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);

        NDialog pinDialog = new NDialog(LockActivity.this, ButtonType.NO_BUTTON);
        pinDialog.isCancelable(false);
        pinDialog.setCustomView(R.layout.lock_dialod);
        List<View> childViews = pinDialog.getCustomViewChildren();
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
                            if (s.length() == 4) {
                                if (FastSave.getInstance().getString(PIN_CODE, "").equals(pin)) {
                                    pin = "";
                                    FastSave.getInstance().saveBoolean(IS_LOCK, false);
//                                    lockBtn.setImageResource(R.drawable.ic_lock_open_black_24dp);
//                                    pinDialog.dismiss();
                                    finish();
                                } else {
                                    Toast.makeText(LockActivity.this, "Неверный PIN код", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    break;
                case R.id.remindMe:
                    TextView remindMe = childView.findViewById(R.id.remindMe);
                    remindMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            API.remindPinCode(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<RemindPinCodeResponse>() {
                                        @Override
                                        public void onSuccessful(Call<RemindPinCodeResponse> call, Response<RemindPinCodeResponse> response) {
                                            Toast.makeText(LockActivity.this, "PIN код был отправлен Вам на телефон", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onEmpty(Call<RemindPinCodeResponse> call, Response<RemindPinCodeResponse> response) {

                                        }
                                    }));

                        }
                    });
                    break;
            }
        }
        pinDialog.show();

    }
}
