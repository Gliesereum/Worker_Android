package com.gliesereum.coupler_worker.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.ClientListAdapter;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.client.ClientResponse;
import com.gliesereum.coupler_worker.util.ErrorHandler;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.CARWASH_ID;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_DONE;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_FIRST_NAME;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_ID;
import static com.gliesereum.coupler_worker.util.Constants.CHOOSE_CLIENT_SECOND_NAME;

public class ClientsListActivity extends AppCompatActivity implements ClientListAdapter.ItemClickListener {

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
    private ClientListAdapter clientListAdapter;
    private List<ClientResponse> clientsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_list);
        initView();
        getAllClients();
    }

    private void getAllClients() {
        API.getAllClientsByBusiness(FastSave.getInstance().getString(ACCESS_TOKEN, ""), Arrays.asList(FastSave.getInstance().getString(CARWASH_ID, "")))
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<List<ClientResponse>>() {
                    @Override
                    public void onSuccessful(Call<List<ClientResponse>> call, Response<List<ClientResponse>> response) {
                        clientsList = response.body();
                        if (clientsList != null && clientsList.size() > 0) {
                            clientListAdapter.setItems(clientsList);
                        }
                    }

                    @Override
                    public void onEmpty(Call<List<ClientResponse>> call, Response<List<ClientResponse>> response) {

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        clientListAdapter = new ClientListAdapter(ClientsListActivity.this);
        clientListAdapter.setClickListener(ClientsListActivity.this);
        recyclerView.setAdapter(clientListAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        FastSave.getInstance().saveString(CHOOSE_CLIENT_ID, clientListAdapter.getItem(position).getId());
        FastSave.getInstance().saveString(CHOOSE_CLIENT_FIRST_NAME, clientListAdapter.getItem(position).getFirstName());
        FastSave.getInstance().saveString(CHOOSE_CLIENT_SECOND_NAME, clientListAdapter.getItem(position).getMiddleName());
        FastSave.getInstance().saveBoolean(CHOOSE_CLIENT_DONE, true);
        finish();
    }
}
