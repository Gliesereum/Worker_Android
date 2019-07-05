package com.gliesereum.coupler_worker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.network.json.client.ClientResponse;

import java.util.ArrayList;
import java.util.List;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ViewHolder> {

    private List<ClientResponse> allClientsList = new ArrayList<>();
    //    private Map<String, String> carWashNameMap = new HashMap<>();
    private Context context;
    private int i = 0;
    private ItemClickListener mClickListener;

    @NonNull
    @Override
    public ClientListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_item, parent, false);
        return new ClientListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientListAdapter.ViewHolder holder, int position) {
        holder.bind(allClientsList.get(position));
    }

    @Override
    public int getItemCount() {
        return allClientsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView firstName;
        private TextView dataTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.firstName);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public void bind(ClientResponse recordInfo) {
            firstName.setText(recordInfo.getFirstName() + " " + recordInfo.getMiddleName());
            dataTextView.setText(recordInfo.getPhone());
        }
    }

    public void setItems(List<ClientResponse> cars) {
        allClientsList.addAll(cars);
        notifyDataSetChanged();
    }

    public ClientListAdapter() {
    }

    public ClientListAdapter(Context context) {
        this.context = context;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public ClientResponse getItem(int id) {
        return allClientsList.get(id);
    }
}
