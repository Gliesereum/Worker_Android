package com.gliesereum.coupler_worker.adapter.customadapterrecycleview.listener;

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerView | Copyrights 1/13/19.
 */

public interface OnClickItemListener<T> {
    void onClickItem(int position, T element);

    void onLongClickItem(int position, T element);
}