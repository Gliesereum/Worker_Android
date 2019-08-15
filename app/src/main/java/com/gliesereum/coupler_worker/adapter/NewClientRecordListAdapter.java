package com.gliesereum.coupler_worker.adapter;

import android.content.Context;

import androidx.databinding.ViewDataBinding;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.adapter.customadapterrecycleview.AdapterRecyclerView;
import com.gliesereum.coupler_worker.adapter.customadapterrecycleview.viewHolder.ItemViewHolder;
import com.gliesereum.coupler_worker.databinding.ClientRecordItemBinding;
import com.gliesereum.coupler_worker.network.json.client_record_new.RecordItem;
import com.gliesereum.coupler_worker.util.Util;

public class NewClientRecordListAdapter extends AdapterRecyclerView<RecordItem> {

    private Context context;

    @Override
    public int getItemLayout(int viewType) {
        return R.layout.client_record_item;
    }

    @Override
    public int onProgressLayout() {
        return R.layout.my_custom_progress_item;
    }

    @Override
    public void onBindView(ViewDataBinding binding, ItemViewHolder viewDataBinding, int position, int viewType, RecordItem element) {
        ClientRecordItemBinding itemBinding = (ClientRecordItemBinding) binding;
        itemBinding.dataTextView.setText(Util.getStringFullDateTrue(element.getBegin()));
        itemBinding.timeTextView.setText(Util.getStringTime(element.getBegin()));
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


    public NewClientRecordListAdapter(Context context) {
        this.context = context;
    }


}
