package com.dd7.yahn;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.dd7.yahn.service.PreferenceService;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Context mContext;
    private PreferenceService mPreferenceService;
    private EditText mEditTextMaxStories;
    private Spinner mSpinner;
    private String mSelectedPreferredCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = getApplicationContext();
        mPreferenceService = new PreferenceService(mContext);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        ImageButton fab = (ImageButton) findViewById(R.id.save_settings);
        fab.setOnClickListener(view -> savePreferences());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSpinner = (Spinner) findViewById(R.id.pref_cat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        mEditTextMaxStories = (EditText) findViewById(R.id.max_story_no);
        mEditTextMaxStories.setText(String.valueOf(mPreferenceService.getMaxStoriesSetting()));

        mSpinner.setSelection(adapter.getPosition(mPreferenceService.getPreferredCategory()));
    }

    private void savePreferences() {
        int maxStories = Integer.parseInt(mEditTextMaxStories.getText().toString());

        mPreferenceService.saveMaxStoriesSetting(maxStories);
        if (mSelectedPreferredCat != null) {
            mPreferenceService.savePreferredCateogory(mSelectedPreferredCat);
        }
        Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectedPreferredCat = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
