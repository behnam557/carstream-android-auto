package com.thekirankumar.youtubeauto.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainPhoneActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(24), dp(20), dp(24));
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        TextView title = new TextView(this);
        title.setText("CarStream Pro");
        title.setTextSize(28);
        title.setPadding(0, 0, 0, dp(20));
        root.addView(title);

        CheckBox adBlock = new CheckBox(this);
        adBlock.setText("YouTube ad filtering");
        adBlock.setTextSize(18);
        adBlock.setChecked(prefs.getBoolean("pref_pro_adblock", true));
        root.addView(adBlock);

        CheckBox autoFullscreen = new CheckBox(this);
        autoFullscreen.setText("Auto full-screen video");
        autoFullscreen.setTextSize(18);
        autoFullscreen.setChecked(prefs.getBoolean("pref_pro_auto_fullscreen", true));
        root.addView(autoFullscreen);

        TextView modeLabel = new TextView(this);
        modeLabel.setText("Video display mode");
        modeLabel.setTextSize(18);
        modeLabel.setPadding(0, dp(20), 0, dp(8));
        root.addView(modeLabel);

        final Spinner displayMode = new Spinner(this);
        final String[] labels = new String[]{"Fit", "Fill", "Crop"};
        final String[] values = new String[]{"contain", "fill", "cover"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        displayMode.setAdapter(adapter);

        String savedMode = prefs.getString("pref_pro_display_mode", "cover");
        int selected = 2;
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(savedMode)) {
                selected = i;
                break;
            }
        }
        displayMode.setSelection(selected);
        root.addView(displayMode);

        TextView note = new TextView(this);
        note.setText("These settings are used by the Android Auto player.");
        note.setTextSize(14);
        note.setPadding(0, dp(24), 0, 0);
        root.addView(note);

        adBlock.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean("pref_pro_adblock", isChecked).apply());

        autoFullscreen.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean("pref_pro_auto_fullscreen", isChecked).apply());

        displayMode.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                prefs.edit().putString("pref_pro_display_mode", values[position]).apply();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        setContentView(root);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
