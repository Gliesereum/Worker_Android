package com.gliesereum.coupler_worker.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.user.User;
import com.gliesereum.coupler_worker.util.FastSave;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ANDROID_APP;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_DONE;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_FIRST_NAME;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_ID;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_SECOND_NAME;
import static com.gliesereum.coupler_worker.util.Constants.CLIENT_ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.NEW_CLIENT_OBJECT;

public class RegisterClientActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout constraintLayout13;
    private TextView bussinesName;
    private ImageView imageView3;
    private ConstraintLayout constraintLayout3;
    private TextInputLayout secondNameTextInputLayout;
    private TextInputEditText secondNameTextView;
    private TextInputLayout firstNameTextInputLayout;
    private TextInputEditText firstNameTextView;
    private TextInputLayout middleNameTextInputLayout;
    private TextInputEditText middleNameTextView;
    private Button saveClientInfoBtn;
    private User user;
    private APIInterface API;
    private CustomCallback customCallback;
    private boolean firstNameFlag;
    private boolean secondNameFlag;
    private boolean thirdNameFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);
        initData();
        initView();
    }

    private void initData() {
        FastSave.init(getApplicationContext());
        user = FastSave.getInstance().getObject(NEW_CLIENT_OBJECT, User.class);
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
    }

    private void initView() {
        constraintLayout13 = findViewById(R.id.constraintLayout13);
        bussinesName = findViewById(R.id.bussinesName);
        imageView3 = findViewById(R.id.imageView3);
        constraintLayout3 = findViewById(R.id.constraintLayout3);
        secondNameTextInputLayout = findViewById(R.id.secondNameTextInputLayout);
        secondNameTextView = findViewById(R.id.secondNameTextView);
        firstNameTextInputLayout = findViewById(R.id.firstNameTextInputLayout);
        firstNameTextView = findViewById(R.id.firstNameTextView);
        middleNameTextInputLayout = findViewById(R.id.middleNameTextInputLayout);
        middleNameTextView = findViewById(R.id.middleNameTextView);
        saveClientInfoBtn = findViewById(R.id.saveClientInfoBtn);
        firstNameTextView.addTextChangedListener(firstNameListener);
        secondNameTextView.addTextChangedListener(secondNameListener);
        middleNameTextView.addTextChangedListener(thirdNameListener);
        saveClientInfoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveClientInfoBtn:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        if (!firstNameTextView.getText().toString().equals("") && !secondNameTextView.getText().toString().equals("") && !middleNameTextView.getText().toString().equals("")) {
            user.setFirstName(firstNameTextView.getText().toString());
            user.setLastName(secondNameTextView.getText().toString());
            user.setMiddleName(middleNameTextView.getText().toString());
            user.setCountry(ANDROID_APP);
            user.setAddress(ANDROID_APP);
            user.setCity(ANDROID_APP);
            user.setAddAddress(ANDROID_APP);
            user.setPosition(ANDROID_APP);

            API.updateUser(FastSave.getInstance().getString(CLIENT_ACCESS_TOKEN, ""), user)
                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<User>() {
                        @Override
                        public void onSuccessful(Call<User> call, Response<User> response) {
                            FastSave.getInstance().saveString(CHOOSE_CLIENT_ID, response.body().getId());
                            FastSave.getInstance().saveString(CHOOSE_CLIENT_FIRST_NAME, response.body().getFirstName());
                            FastSave.getInstance().saveString(CHOOSE_CLIENT_SECOND_NAME, response.body().getMiddleName());
                            FastSave.getInstance().saveBoolean(CHOOSE_CLIENT_DONE, true);
                            finish();
                        }

                        @Override
                        public void onEmpty(Call<User> call, Response<User> response) {

                        }
                    }));
        } else {
            Toast.makeText(RegisterClientActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }

    TextWatcher firstNameListener = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() < 2) {
                secondNameTextInputLayout.setError("Обязательное поле");
                firstNameFlag = false;
                checkButton();
            } else {
                secondNameTextInputLayout.setError(null);
                firstNameFlag = true;
                checkButton();

            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    TextWatcher secondNameListener = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() < 2) {
                secondNameTextInputLayout.setError("Обязательное поле");
                secondNameFlag = false;
                checkButton();
            } else {
                secondNameTextInputLayout.setError(null);
                secondNameFlag = true;
                checkButton();

            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    TextWatcher thirdNameListener = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() < 3) {
                middleNameTextInputLayout.setError("Обязательное поле");
                thirdNameFlag = false;
                checkButton();
            } else {
                middleNameTextInputLayout.setError(null);
                thirdNameFlag = true;
                checkButton();
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    private void checkButton() {
        if (firstNameFlag && secondNameFlag && thirdNameFlag) {
            saveClientInfoBtn.setEnabled(true);
        } else {
            saveClientInfoBtn.setEnabled(false);
        }
    }
}
