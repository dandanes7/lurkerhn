package com.dd7.yahn;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.dd7.yahn.service.PreferenceService;

public class SettingsActivity extends AppCompatActivity {

    private Context mContext;
    private PreferenceService mPreferenceService;
    private EditText mEditTextMaxStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = getApplicationContext();
        mPreferenceService = new PreferenceService(mContext);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEditTextMaxStories = (EditText) findViewById(R.id.max_story_no);
        mEditTextMaxStories.setText(String.valueOf(mPreferenceService.getMaxStoriesSetting()));


        ImageButton fab = (ImageButton) findViewById(R.id.save_settings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int val = Integer.parseInt(mEditTextMaxStories.getText().toString());
                mPreferenceService.saveMaxStoriesSetting(val);
                Toast.makeText(mContext, "Saved",Toast.LENGTH_SHORT).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
