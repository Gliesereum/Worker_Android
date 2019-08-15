package com.gliesereum.coupler_worker.adapter;

import android.content.Context;

import androidx.databinding.ViewDataBinding;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.customadapterrecycleview.AdapterRecyclerView;
import com.gliesereum.coupler_worker.adapter.customadapterrecycleview.viewHolder.ItemViewHolder;
import com.gliesereum.coupler_worker.databinding.RecordItemBinding;
import com.gliesereum.coupler_worker.network.json.carwash.WorkersItem;
import com.gliesereum.coupler_worker.network.json.client_record_new.RecordItem;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

import static com.gliesereum.coupler_worker.util.Constants.WORKERS_MAP;

public class NewRecordListAdapter extends AdapterRecyclerView<RecordItem> {

    private Context context;

    @Override
    public int getItemLayout(int viewType) {
        return R.layout.record_item;
    }

    @Override
    public int onProgressLayout() {
        return R.layout.my_custom_progress_item;
    }

    @Override
    public void onBindView(ViewDataBinding binding, ItemViewHolder viewDataBinding, int position, int viewType, RecordItem element) {
        RecordItemBinding itemBinding = (RecordItemBinding) binding;
        itemBinding.dataTextView.setText(Util.getStringFullDateTrue(element.getBegin()));
        itemBinding.timeTextView.setText(Util.getStringTime(element.getBegin()));
        Map<String, WorkersItem> workersMap = FastSave.getInstance().getObject(WORKERS_MAP, new TypeToken<Map<String, WorkersItem>>() {
        });
        WorkersItem workersItem = workersMap.get(element.getWorkerId());
        if (workersItem != null && workersItem.getUser() != null && workersItem.getUser().getFirstName() != null) {
            itemBinding.masterTextView.setText(workersItem.getUser().getFirstName());
        } else {
            itemBinding.masterTextView.setText("");
        }

        if (element.getClient() != null) {
            if (element.getClient().getFirstName() != null && !element.getClient().getFirstName().equals("")) {
                itemBinding.firstName.setText(element.getClient().getFirstName() + " " + element.getClient().getMiddleName());
            } else {
                itemBinding.firstName.setText("");
            }
        } else {
            itemBinding.firstName.setText("Клиент");
        }


        if (element.getStatusRecord().equals("CANCELED")) {
            itemBinding.statusTextView.setText("Отменена");
            itemBinding.statusImg.setImageResource(R.drawable.ic_outline_block_24px);
        } else {
            switch (element.getStatusProcess()) {
                case "WAITING":
                    itemBinding.statusTextView.setText("В ожидании");
                    itemBinding.statusImg.setImageResource(R.drawable.ic_outline_av_timer_24px);
                    break;
                case "IN_PROCESS":
                    itemBinding.statusTextView.setText("В процессе");
                    itemBinding.statusImg.setImageResource(R.drawable.ic_outline_pause_circle_outline_24px);
                    break;
                case "COMPLETED":
                    itemBinding.statusTextView.setText("Завершена");
                    itemBinding.statusImg.setImageResource(R.drawable.ic_outline_check_circle_outline_24px);
                    break;
                default:
                    itemBinding.statusTextView.setText("");
                    itemBinding.statusImg.setImageResource(R.drawable.ic_outline_block_24px);
                    break;
            }
        }

    }


    public NewRecordListAdapter(Context context) {
        this.context = context;
    }


}
