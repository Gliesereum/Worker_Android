package com.gliesereum.coupler_worker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler_worker.R;

import java.util.ArrayList;
import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder> {

    private List<String> allServiceList = new ArrayList<>();
    //    private Map<String, String> carWashNameMap = new HashMap<>();
    private Context context;
    private int i = 0;
    private ItemClickListener mClickListener;

    @NonNull
    @Override
    public ServiceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_item, parent, false);
        return new ServiceListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceListAdapter.ViewHolder holder, int position) {
        holder.bind(allServiceList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return allServiceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView serviceTextView;
        private TextView count;

        public ViewHolder(View itemView) {
            super(itemView);
            serviceTextView = itemView.findViewById(R.id.serviceTextView);
            count = itemView.findViewById(R.id.count);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public void bind(String name, int position) {
            serviceTextView.setText(name);
            count.setText(String.valueOf(position + 1));
        }
    }

    public void setItems(List<String> services) {
        allServiceList.addAll(services);
        notifyDataSetChanged();
    }

    public ServiceListAdapter() {
    }

    public ServiceListAdapter(Context context) {
        this.context = context;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public String getItem(int id) {
        return allServiceList.get(id);
    }
}
