package com.gliesereum.coupler_worker.adapter;

import android.content.Context;

import androidx.databinding.ViewDataBinding;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.customadapterrecycleview.AdapterRecyclerView;
import com.gliesereum.coupler_worker.adapter.customadapterrecycleview.viewHolder.ItemViewHolder;
import com.gliesereum.coupler_worker.databinding.ClientItemBinding;
import com.gliesereum.coupler_worker.network.json.client_new.ClientItem;

public class NewClientListAdapter extends AdapterRecyclerView<ClientItem> {

    private Context context;

    @Override
    public int getItemLayout(int viewType) {
        return R.layout.client_item;
    }

    @Override
    public int onProgressLayout() {
        return R.layout.my_custom_progress_item;
    }

    @Override
    public void onBindView(ViewDataBinding binding, ItemViewHolder viewDataBinding, int position, int viewType, ClientItem element) {
        ClientItemBinding itemBinding = (ClientItemBinding) binding;
        itemBinding.firstName.setText(element.getFirstName() + " " + element.getMiddleName());
        itemBinding.dataTextView.setText(element.getPhone());
    }


    public NewClientListAdapter(Context context) {
        this.context = context;
    }


}
