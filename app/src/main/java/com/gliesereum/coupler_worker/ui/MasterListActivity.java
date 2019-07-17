package com.gliesereum.coupler_worker.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.ClientListAdapter;
import com.gliesereum.coupler_worker.adapter.WorkerListAdapter;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.worker.WorkerResponse;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.CARWASH_ID;

public class MasterListActivity extends AppCompatActivity implements ClientListAdapter.ItemClickListener, WorkerListAdapter.ItemClickListener {

    private APIInterface API;
    private Toolbar toolbar;
    private CustomCallback customCallback;
    private RecyclerView recyclerView;
    private WorkerListAdapter workerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_list);
        initView();
        getAllWorkers();

    }

    private void getAllWorkers() {
        API.getAllWorkersByBusiness(FastSave.getInstance().getString(ACCESS_TOKEN, ""), FastSave.getInstance().getString(CARWASH_ID, ""))
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<List<WorkerResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<WorkerResponse>> call, Response<List<WorkerResponse>> response) {
                        workerListAdapter.setItems(response.body());
                    }

                    @Override
                    public void onEmpty(Call<List<WorkerResponse>> call, Response<List<WorkerResponse>> response) {

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
    }

    @Override
    public void onItemClick(View view, int position) {
        workerListAdapter.getItem(position).getId();
    }

}
