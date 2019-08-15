package com.gliesereum.coupler_worker.adapter.customadapterrecycleview.viewHolder;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerView | Copyrights 1/1/19.
 */


public class ProgressViewHolder extends RecyclerView.ViewHolder {

    ViewDataBinding progressBinding;

    public ProgressViewHolder(ViewDataBinding viewDataBinding) {
        super(viewDataBinding.getRoot());
        progressBinding = viewDataBinding;
    }
}