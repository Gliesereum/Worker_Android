package com.gliesereum.coupler_worker.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.WorkerListAdapter;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.worker_new.WorkerResponse;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.BUSSINES_ID;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_WORKER_DONE;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_WORKER_FIRST_NAME;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_WORKER_ID;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_WORKER_SECOND_NAME;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_WORKER_SPACE;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOCK;

public class MasterListActivity extends AppCompatActivity implements WorkerListAdapter.ItemClickListener {

    private APIInterface API;
    private Toolbar toolbar;
    private CustomCallback customCallback;
    private RecyclerView recyclerView;
    private WorkerListAdapter workerListAdapter;
    private ImageButton lockBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_list);
        initView();
        getAllWorkers();

        if (FastSave.getInstance().getBoolean(IS_LOCK, false)) {
            new Util().lockScreen(this, this, lockBtn);
        }

    }

    private void getAllWorkers() {
        API.getAllWorkersByBusiness(FastSave.getInstance().getString(ACCESS_TOKEN, ""), FastSave.getInstance().getString(BUSSINES_ID, ""))
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<WorkerResponse>() {
                    @Override
                    public void onSuccessful(Call<WorkerResponse> call, Response<WorkerResponse> response) {
                        workerListAdapter.setItems(response.body().getContent());
                    }

                    @Override
                    public void onEmpty(Call<WorkerResponse> call, Response<WorkerResponse> response) {

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

        toolbar = findViewById(R.id.toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        workerListAdapter = new WorkerListAdapter(MasterListActivity.this);
        workerListAdapter.setClickListener(MasterListActivity.this);
        recyclerView.setAdapter(workerListAdapter);
        lockBtn = findViewById(R.id.lockBtn);
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Util().lockScreen(MasterListActivity.this, MasterListActivity.this, lockBtn);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        FastSave.getInstance().saveString(CHOOSE_WORKER_ID, workerListAdapter.getItem(position).getId());
        FastSave.getInstance().saveString(CHOOSE_WORKER_SPACE, workerListAdapter.getItem(position).getWorkingSpaceId());
        FastSave.getInstance().saveString(CHOOSE_WORKER_FIRST_NAME, workerListAdapter.getItem(position).getUser().getFirstName());
        FastSave.getInstance().saveString(CHOOSE_WORKER_SECOND_NAME, workerListAdapter.getItem(position).getUser().getMiddleName());
        FastSave.getInstance().saveBoolean(CHOOSE_WORKER_DONE, true);
        finish();

    }

}
