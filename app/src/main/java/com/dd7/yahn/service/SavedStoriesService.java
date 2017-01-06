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
    private String separator = System.getProperty("line.separator");


    public SavedStoriesService(Context mContext) {
        this.mContext = mContext;
    }

    public void saveItem(Item item) throws IOException {
        String id = String.valueOf(item.getId());

        if (alreadySaved(id)) {
            return;
        }

        FileOutputStream fileOutputStream = mContext.openFileOutput(FILE_SAVED_ITEMS, Context.MODE_APPEND);
        OutputStreamWriter objectOutputStream = new OutputStreamWriter(fileOutputStream);
        objectOutputStream.append(id);
        objectOutputStream.append(separator);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public void removeItem(Item item) throws IOException {
        String id = String.valueOf(item.getId());

        List<String> existing = getItems();
        existing.remove(id);

        FileOutputStream fileOutputStream = mContext.openFileOutput(FILE_SAVED_ITEMS, Context.MODE_PRIVATE);
        OutputStreamWriter objectOutputStream = new OutputStreamWriter(fileOutputStream);
        objectOutputStream.append("");
        objectOutputStream.flush();

        for (String line : existing) {
            objectOutputStream.write(line);
        }

        objectOutputStream.close();
        fileOutputStream.close();
    }

    public List<String> getItems() throws IOException {
        List<String> savedIds = new ArrayList<>();

        FileInputStream fileInputStream = mContext.openFileInput(FILE_SAVED_ITEMS);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while ( (line = bufferedReader.readLine()) != null) {
            savedIds.add(line);
        }

        bufferedReader.close();
        inputStreamReader.close();
        fileInputStream.close();
        return savedIds;
    }

    private boolean alreadySaved(String id) throws IOException{
        List<String> existing = getItems();
        if (existing.contains(id)) {
            return true;
        } else {
            return false;
        }
    }
}
