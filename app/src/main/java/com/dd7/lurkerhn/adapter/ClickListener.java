package com.dd7.lurkerhn.adapter;

import android.view.View;
import com.dd7.lurkerhn.rest.model.Item;

import java.util.List;

public interface ClickListener {

    void onItemClick(int position, View v, List<Item> items);

    void onItemLongClick(int position, View v, List<Item> items);
}