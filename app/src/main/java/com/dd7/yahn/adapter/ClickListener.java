package com.dd7.yahn.adapter;

import android.view.View;
import com.dd7.yahn.rest.model.Item;

import java.util.List;

public interface ClickListener {

    void onItemClick(int position, View v, List<Item> items);

    void onItemLongClick(int position, View v, List<Item> items);
}