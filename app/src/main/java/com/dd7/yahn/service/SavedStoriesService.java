package com.dd7.yahn.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.dd7.yahn.rest.model.Item;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SavedStoriesService {

    private static final String FILE_SAVED_ITEMS = "file_saved_items";
    private static final String SAVED_ITEMS = "SAVED_ITEMS";

    private Context mContext;
    private Gson mGson;


    public SavedStoriesService(Context mContext) {
        this.mContext = mContext;
    }

    public void saveItem(Item item) throws IOException {
        List<Item> items;
        try {
            items = getItems();
        } catch (ClassNotFoundException e) {
            items = new ArrayList<>();
            Log.e("SAVEITEMS","Could not get saved items:" + e.getMessage());
        }
        items.add(item);
        FileOutputStream fileOutputStream = mContext.openFileOutput(FILE_SAVED_ITEMS, Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(items);
        objectOutputStream.close();
        fileOutputStream.close();

    }

    public void removeItem(Item item) throws IOException {
        List<Item> items;
        try {
            items = getItems();
            items.remove(item);
        } catch (ClassNotFoundException e) {
            Log.e("SAVEITEMS","Could not get saved items:" + e.getMessage());
        }
    }

    public List<Item> getItems() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = mContext.openFileInput(FILE_SAVED_ITEMS);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        List<Item> items = (List<Item>) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return items;
    }

    private void getSavedItems() {

    }
}
