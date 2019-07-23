package com.gliesereum.coupler_worker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.ClientRecordListAdapter;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.client_record_new.ClientRecordNewResponse;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_ID;
import static com.gliesereum.coupler_worker.util.Constants.CLIENT_RECORD;
import static com.gliesereum.coupler_worker.util.Constants.CORPORATION_ID;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOCK;

public class ClientRecordListActivity extends AppCompatActivity implements ClientRecordListAdapter.ItemClickListener {

    private APIInterface API;
    private CustomCallback customCallback;
    private ImageView backBtn;
    private TextView bussinesName;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ClientRecordListAdapter clientRecordListAdapter;
    private ImageButton lockBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_record_list);

        initData();
        initView();
        getAllRecord();
        if (FastSave.getInstance().getBoolean(IS_LOCK, false)) {
            new Util().lockScreen(this, this, lockBtn);
        }
    }

    private void initData() {
        FastSave.init(getApplicationContext());
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
    }

    private void initView() {
        backBtn = findViewById(R.id.backBtn);
        bussinesName = findViewById(R.id.bussinesName);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        new Util(this, toolbar, 1).addNavigation();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        clientRecordListAdapter = new ClientRecordListAdapter(ClientRecordListActivity.this);
        clientRecordListAdapter.setClickListener(ClientRecordListActivity.this);
        recyclerView.setAdapter(clientRecordListAdapter);
        lockBtn = findViewById(R.id.lockBtn);
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Util().lockScreen(ClientRecordListActivity.this, ClientRecordListActivity.this, lockBtn);
            }
        });
    }

    private void getAllRecord() {
        API.getClientsRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), Arrays.asList(FastSave.getInstance().getString(CORPORATION_ID, "")), FastSave.getInstance().getString(CHOOSE_CLIENT_ID, ""))
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<ClientRecordNewResponse>() {
                    @Override
                    public void onSuccessful(Call<ClientRecordNewResponse> call, Response<ClientRecordNewResponse> response) {
                        clientRecordListAdapter.setItems(response.body().getContent());

                    }

                    @Override
                    public void onEmpty(Call<ClientRecordNewResponse> call, Response<ClientRecordNewResponse> response) {

                    }
                }));
    }

    @Override
    public void onItemClick(View view, int position) {
        FastSave.getInstance().saveObject(CLIENT_RECORD, clientRecordListAdapter.getItem(position));
        startActivity(new Intent(ClientRecordListActivity.this, SingleClientRecordActivity.class));

    }
}
