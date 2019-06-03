package com.gliesereum.coupler_worker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.network.json.record.AllRecordResponse;
import com.gliesereum.coupler_worker.util.Util;

import java.util.ArrayList;
import java.util.List;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    private List<AllRecordResponse> allRecordList = new ArrayList<>();
    //    private Map<String, String> carWashNameMap = new HashMap<>();
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
        private TextView secondName;


        public ViewHolder(View itemView) {
            super(itemView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            firstName = itemView.findViewById(R.id.firstName);
            secondName = itemView.findViewById(R.id.secondName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public void bind(AllRecordResponse recordInfo) {
            dataTextView.setText(Util.getStringDate(recordInfo.getBegin()));
            timeTextView.setText(Util.getStringTime(recordInfo.getBegin()));
            if (recordInfo.getClient().getFirstName() != null && !recordInfo.getClient().getFirstName().equals("")) {
                firstName.setText(recordInfo.getClient().getFirstName());
            } else {
                firstName.setText("");
            }
            if (recordInfo.getClient().getMiddleName() != null && !recordInfo.getClient().getMiddleName().equals("")) {
                secondName.setText(recordInfo.getClient().getMiddleName());
            } else {
                secondName.setText("");
            }
            if (recordInfo.getStatusRecord().equals("CANCELED")) {
                statusTextView.setText("Отменена");
                statusTextView.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
            } else {
                switch (recordInfo.getStatusProcess()) {
                    case "WAITING":
                        statusTextView.setText("В ожидании");
                        statusTextView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                        break;
                    case "IN_PROCESS":
                        statusTextView.setText("В процессе");
                        statusTextView.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
                        break;
                    case "COMPLETED":
                        statusTextView.setText("Завершена");
                        statusTextView.setTextColor(context.getResources().getColor(android.R.color.holo_green_light
                        ));
                        break;
                    default:
                        statusTextView.setText("");
                        break;
                }
            }
//            recordId.setText(String.valueOf(i));
        }
    }

    public void setItems(List<AllRecordResponse> cars) {
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

    public AllRecordResponse getItem(int id) {
        return allRecordList.get(id);
    }
}
