package com.gliesereum.coupler_worker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.NewClientRecordListAdapter;
import com.gliesereum.coupler_worker.adapter.customadapterrecycleview.listener.OnClickItemListener;
import com.gliesereum.coupler_worker.adapter.customadapterrecycleview.listener.OnLoadMoreListener;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.client_record_new.ClientRecordNewResponse;
import com.gliesereum.coupler_worker.network.json.client_record_new.RecordItem;
import com.gliesereum.coupler_worker.network.json.record.RecordsSearchBody;
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

public class ClientRecordListActivity extends AppCompatActivity {

    private APIInterface API;
    private CustomCallback customCallback;
    private ImageView backBtn;
    private TextView bussinesName;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    //    private ClientRecordListAdapter clientRecordListAdapter;
    private NewClientRecordListAdapter newClientRecordListAdapter;
    private ImageButton lockBtn;
    private RecordsSearchBody recordsSearchBody;
    private boolean loadingFlag = true;
    private Integer page = 0;
    private SwipeRefreshLayout swipeRefreshLayout;


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
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        clientRecordListAdapter = new ClientRecordListAdapter(ClientRecordListActivity.this);
//        clientRecordListAdapter.setClickListener(ClientRecordListActivity.this);
//        recyclerView.setAdapter(clientRecordListAdapter);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
//                startActivity(new Intent(MainActivity.this, MainActivity.class));
                newClientRecordListAdapter.removeAll();
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
        newClientRecordListAdapter = new NewClientRecordListAdapter(this);
        newClientRecordListAdapter.endLessScrolled(recyclerView);
        recyclerView.setAdapter(newClientRecordListAdapter);

        newClientRecordListAdapter.setOnClickItemListener(recyclerView, new OnClickItemListener<RecordItem>() {
            @Override
            public void onClickItem(int position, RecordItem element) {
                if (element != null) {
                    FastSave.getInstance().saveObject(CLIENT_RECORD, element);
                    startActivity(new Intent(ClientRecordListActivity.this, SingleClientRecordActivity.class));
                }
            }

            @Override
            public void onLongClickItem(int position, RecordItem element) {

            }
        });

        newClientRecordListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadingFlag = false;
                newClientRecordListAdapter.showLoading();
                ++page;
                getAllRecord();
            }
        });


        lockBtn = findViewById(R.id.lockBtn);
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Util().lockScreen(ClientRecordListActivity.this, ClientRecordListActivity.this, lockBtn);
            }
        });
    }

    private void getAllRecord() {
        if (page == 0) {
            recordsSearchBody = new RecordsSearchBody();
//        recordsSearchBody.setFrom(FastSave.getInstance().getLong(FROM_DATE, 0));
//        recordsSearchBody.setTo(FastSave.getInstance().getLong(TO_DATE, 0));
            recordsSearchBody.setClientIds(Arrays.asList(FastSave.getInstance().getString(CHOOSE_CLIENT_ID, "")));
            recordsSearchBody.setCorporationId(FastSave.getInstance().getString(CORPORATION_ID, ""));
//        recordsSearchBody.setProcesses(FastSave.getInstance().getObjectsList(STATUS_FILTER, String.class));
            recordsSearchBody.setSize(20);
            recordsSearchBody.setPage(page);

            API.getAllRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), recordsSearchBody)
                    .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<ClientRecordNewResponse>() {
                        @Override
                        public void onSuccessful(Call<ClientRecordNewResponse> call, Response<ClientRecordNewResponse> response) {
//                        clientRecordListAdapter.setItems(response.body().getContent());
                            newClientRecordListAdapter.addItems(response.body().getContent());
                            loadingFlag = true;
                        }

                        @Override
                        public void onEmpty(Call<ClientRecordNewResponse> call, Response<ClientRecordNewResponse> response) {
                            Toast.makeText(ClientRecordListActivity.this, "Список заказов пуст", Toast.LENGTH_SHORT).show();
                        }
                    }));
        } else {
            recordsSearchBody = new RecordsSearchBody();
//        recordsSearchBody.setFrom(FastSave.getInstance().getLong(FROM_DATE, 0));
//        recordsSearchBody.setTo(FastSave.getInstance().getLong(TO_DATE, 0));
            recordsSearchBody.setClientIds(Arrays.asList(FastSave.getInstance().getString(CHOOSE_CLIENT_ID, "")));
            recordsSearchBody.setCorporationId(FastSave.getInstance().getString(CORPORATION_ID, ""));
//        recordsSearchBody.setProcesses(FastSave.getInstance().getObjectsList(STATUS_FILTER, String.class));
            recordsSearchBody.setSize(20);
            recordsSearchBody.setPage(page);

            API.getAllRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), recordsSearchBody)
                    .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<ClientRecordNewResponse>() {
                        @Override
                        public void onSuccessful(Call<ClientRecordNewResponse> call, Response<ClientRecordNewResponse> response) {
//                        clientRecordListAdapter.setItems(response.body().getContent());
                            newClientRecordListAdapter.addItems(response.body().getContent());
                            loadingFlag = true;
                        }

                        @Override
                        public void onEmpty(Call<ClientRecordNewResponse> call, Response<ClientRecordNewResponse> response) {
                            Toast.makeText(ClientRecordListActivity.this, "Список заказов пуст", Toast.LENGTH_SHORT).show();
                        }
                    }));
        }

    }

//    private void getAllRecord() {
//        API.getClientsRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), Arrays.asList(FastSave.getInstance().getString(CORPORATION_ID, "")), FastSave.getInstance().getString(CHOOSE_CLIENT_ID, ""))
//                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<ClientRecordNewResponse>() {
//                    @Override
//                    public void onSuccessful(Call<ClientRecordNewResponse> call, Response<ClientRecordNewResponse> response) {
//                        clientRecordListAdapter.setItems(response.body().getContent());
//
//                    }
//
//                    @Override
//                    public void onEmpty(Call<ClientRecordNewResponse> call, Response<ClientRecordNewResponse> response) {
//
//                    }
//                }));
//    }

//    @Override
//    public void onItemClick(View view, int position) {
//        FastSave.getInstance().saveObject(CLIENT_RECORD, clientRecordListAdapter.getItem(position));
//        startActivity(new Intent(ClientRecordListActivity.this, SingleClientRecordActivity.class));
//
//    }
}
