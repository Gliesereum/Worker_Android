package com.gliesereum.coupler_worker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.NewClientListAdapter;
import com.gliesereum.coupler_worker.adapter.customadapterrecycleview.listener.OnClickItemListener;
import com.gliesereum.coupler_worker.adapter.customadapterrecycleview.listener.OnLoadMoreListener;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.client_new.ClientItem;
import com.gliesereum.coupler_worker.network.json.client_new.NewClientResponse;
import com.gliesereum.coupler_worker.util.ErrorHandler;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_DONE;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_FIRST_NAME;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_ID;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_SECOND_NAME;
import static com.gliesereum.coupler_worker.util.Constants.CLIENT_AVATAR_URL;
import static com.gliesereum.coupler_worker.util.Constants.CORPORATION_ID;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOCK;
import static com.gliesereum.coupler_worker.util.Constants.ONLY_CLIENT;
import static com.gliesereum.coupler_worker.util.Constants.REG_NEW_CLIENT;

public class ClientsListActivity extends AppCompatActivity implements View.OnClickListener {

    private APIInterface API;
    private ErrorHandler errorHandler;
    private ConstraintLayout constraintLayout13;
    private ImageView backBtn;
    private TextView bussinesName;
    private ImageView imageView3;
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout19;
    private TextView textView15;
    private TextView textView27;
    private Button addRecord;
    private CustomCallback customCallback;
    private RecyclerView recyclerView;
    //    private ClientListAdapter clientListAdapter;
    private NewClientListAdapter newClientListAdapter;
    private List<NewClientResponse> clientsList;
    private Button addNewClient;
    private EditText searchTextView;
    private Button lockBtn;
    private boolean loadingFlag = true;
    private Integer page = 0;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_list);
        initView();
        getAllClients();

        if (FastSave.getInstance().getBoolean(IS_LOCK, false)) {
            new Util().lockScreen(this, this, lockBtn);
        }
    }

    private void getAllClients() {
        if (page == 0) {
            API.getAllClientsByCorporation(FastSave.getInstance().getString(ACCESS_TOKEN, ""), FastSave.getInstance().getString(CORPORATION_ID, ""), page, 20)
                    .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<NewClientResponse>() {
                        @Override
                        public void onSuccessful(Call<NewClientResponse> call, Response<NewClientResponse> response) {
//                        clientListAdapter.setItems(response.body().getContent());
                            newClientListAdapter.addItems(response.body().getContent());
                            loadingFlag = true;
                        }

                        @Override
                        public void onEmpty(Call<NewClientResponse> call, Response<NewClientResponse> response) {

                        }
                    }));
        } else {
            API.getAllClientsByCorporation(FastSave.getInstance().getString(ACCESS_TOKEN, ""), FastSave.getInstance().getString(CORPORATION_ID, ""), page, 20)
                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<NewClientResponse>() {
                        @Override
                        public void onSuccessful(Call<NewClientResponse> call, Response<NewClientResponse> response) {
//                        clientListAdapter.setItems(response.body().getContent());
                            newClientListAdapter.addItems(response.body().getContent());
                            loadingFlag = true;
                        }

                        @Override
                        public void onEmpty(Call<NewClientResponse> call, Response<NewClientResponse> response) {
                            newClientListAdapter.hiddenLoading();
                        }
                    }));
        }


    }

    private void searchClients(String text) {
        API.searchClients(FastSave.getInstance().getString(ACCESS_TOKEN, ""), FastSave.getInstance().getString(CORPORATION_ID, ""), text)
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<NewClientResponse>() {
                    @Override
                    public void onSuccessful(Call<NewClientResponse> call, Response<NewClientResponse> response) {
                        newClientListAdapter.removeAll();
                        newClientListAdapter.addItems(response.body().getContent());
                    }

                    @Override
                    public void onEmpty(Call<NewClientResponse> call, Response<NewClientResponse> response) {

                    }
                }));

    }

    private void initView() {
        FastSave.init(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        new Util(this, toolbar, 2).addNavigation();
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);

        constraintLayout13 = findViewById(R.id.constraintLayout13);
        backBtn = findViewById(R.id.backBtn);
        bussinesName = findViewById(R.id.bussinesName);
        imageView3 = findViewById(R.id.imageView3);
        toolbar = findViewById(R.id.toolbar);
        constraintLayout19 = findViewById(R.id.constraintLayout19);
        textView15 = findViewById(R.id.textView15);
        textView27 = findViewById(R.id.textView27);
        addRecord = findViewById(R.id.addRecord);

        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        clientListAdapter = new ClientListAdapter(ClientsListActivity.this);
//        clientListAdapter.setClickListener(ClientsListActivity.this);
//        recyclerView.setAdapter(clientListAdapter);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
//                startActivity(new Intent(MainActivity.this, MainActivity.class));
                newClientListAdapter.removeAll();
                page = 0;
                getAllClients();
//                finish();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        newClientListAdapter = new NewClientListAdapter(this);
        newClientListAdapter.endLessScrolled(recyclerView);
        recyclerView.setAdapter(newClientListAdapter);

        newClientListAdapter.setOnClickItemListener(recyclerView, new OnClickItemListener<ClientItem>() {
            @Override
            public void onClickItem(int position, ClientItem element) {
                if (element != null) {
                    if (FastSave.getInstance().getBoolean(ONLY_CLIENT, false)) {
                        FastSave.getInstance().saveString(CHOOSE_CLIENT_ID, element.getId());
                        FastSave.getInstance().saveString(CHOOSE_CLIENT_FIRST_NAME, element.getFirstName());
                        FastSave.getInstance().saveString(CHOOSE_CLIENT_SECOND_NAME, element.getMiddleName());
                        FastSave.getInstance().saveString(CLIENT_AVATAR_URL, element.getAvatarUrl());
                        startActivity(new Intent(ClientsListActivity.this, ClientRecordListActivity.class));
                    } else {
                        FastSave.getInstance().saveString(CHOOSE_CLIENT_ID, element.getId());
                        FastSave.getInstance().saveString(CHOOSE_CLIENT_FIRST_NAME, element.getFirstName());
                        FastSave.getInstance().saveString(CHOOSE_CLIENT_SECOND_NAME, element.getMiddleName());
                        FastSave.getInstance().saveString(CLIENT_AVATAR_URL, element.getAvatarUrl());
                        FastSave.getInstance().saveBoolean(CHOOSE_CLIENT_DONE, true);
                        finish();
                    }
                }
            }

            @Override
            public void onLongClickItem(int position, ClientItem element) {

            }
        });

        newClientListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadingFlag = false;
                newClientListAdapter.showLoading();
                ++page;
                getAllClients();
            }
        });

        addNewClient = findViewById(R.id.addNewClient);
        addNewClient.setOnClickListener(this);
        searchTextView = findViewById(R.id.searchTextView);
        searchTextView.addTextChangedListener(searchWatcher);

        if (FastSave.getInstance().getBoolean(ONLY_CLIENT, false)) {
            addNewClient.setVisibility(View.GONE);
        }
        lockBtn = findViewById(R.id.lockBtn);
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Util().lockScreen(ClientsListActivity.this, ClientsListActivity.this, lockBtn);
            }
        });
    }

    TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() >= 3) {
                searchClients(String.valueOf(s));

//                secondNameTextInputLayout.setError("Обязательное поле");
//                firstNameFlag = false;
//                checkButton();
            } else {
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addNewClient:
                FastSave.getInstance().saveBoolean(REG_NEW_CLIENT, true);
                startActivity(new Intent(ClientsListActivity.this, LoginActivity.class));
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FastSave.getInstance().deleteValue(ONLY_CLIENT);

    }
}
