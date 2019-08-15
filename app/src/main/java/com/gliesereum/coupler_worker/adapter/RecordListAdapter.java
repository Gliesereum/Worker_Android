package com.gliesereum.coupler_worker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.network.json.carwash.WorkersItem;
import com.gliesereum.coupler_worker.network.json.client_record_new.RecordItem;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gliesereum.coupler_worker.util.Constants.WORKERS_MAP;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    private List<RecordItem> allRecordList = new ArrayList<>();
    private Context context;
    private int i = 0;
    private ItemClickListener mClickListener;


    @NonNull
    @Override
    public RecordListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);
        return new RecordListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordListAdapter.ViewHolder holder, int position) {
        holder.bind(allRecordList.get(position));
    }

    @Override
    public int getItemCount() {
        return allRecordList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView dataTextView;
        private TextView timeTextView;
        private TextView statusTextView;
        private TextView firstName;
        private TextView masterTextView;
        private ImageView statusImg;



        public ViewHolder(View itemView) {
            super(itemView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            masterTextView = itemView.findViewById(R.id.masterTextView);
            firstName = itemView.findViewById(R.id.firstName);
            statusImg = itemView.findViewById(R.id.statusImg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public void bind(RecordItem recordInfo) {
            dataTextView.setText(Util.getStringFullDateTrue(recordInfo.getBegin()));
            timeTextView.setText(Util.getStringTime(recordInfo.getBegin()));
            Map<String, WorkersItem> workersMap = FastSave.getInstance().getObject(WORKERS_MAP, new TypeToken<Map<String, WorkersItem>>() {
            });
            WorkersItem workersItem = workersMap.get(recordInfo.getWorkerId());
            if (workersItem != null && workersItem.getUser() != null && workersItem.getUser().getFirstName() != null) {
                masterTextView.setText(workersItem.getUser().getFirstName());
            } else {
                masterTextView.setText("");
            }

            if (recordInfo.getClient() != null) {
                if (recordInfo.getClient().getFirstName() != null && !recordInfo.getClient().getFirstName().equals("")) {
                    firstName.setText(recordInfo.getClient().getFirstName() + " " + recordInfo.getClient().getMiddleName());
                } else {
                    firstName.setText("");
                }
            } else {
                firstName.setText("Клиент");
            }


            if (recordInfo.getStatusRecord().equals("CANCELED")) {
                statusTextView.setText("Отменена");
                statusImg.setImageResource(R.drawable.ic_outline_block_24px);
            } else {
                switch (recordInfo.getStatusProcess()) {
                    case "WAITING":
                        statusTextView.setText("В ожидании");
                        statusImg.setImageResource(R.drawable.ic_outline_av_timer_24px);
                        break;
                    case "IN_PROCESS":
                        statusTextView.setText("В процессе");
                        statusImg.setImageResource(R.drawable.ic_outline_pause_circle_outline_24px);
                        break;
                    case "COMPLETED":
                        statusTextView.setText("Завершена");
                        statusImg.setImageResource(R.drawable.ic_outline_check_circle_outline_24px);
                        break;
                    default:
                        statusTextView.setText("");
                        statusImg.setImageResource(R.drawable.ic_outline_block_24px);
                        break;
                }
            }
//            recordId.setText(String.valueOf(i));
        }
    }

    public void setItems(List<RecordItem> cars) {
        allRecordList.addAll(cars);
        notifyDataSetChanged();
    }

    public RecordListAdapter() {
    }

    public RecordListAdapter(Context context) {
        this.context = context;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public RecordItem getItem(int id) {
        return allRecordList.get(id);
    }
}
