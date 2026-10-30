package com.thekirankumar.youtubeauto.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Lightweight phone launcher for CarStream Pro.
 *
 * The original phone-side WebView is intentionally skipped because it relies on
 * a legacy browser stack. Android Auto playback continues to use CarService and
 * WebViewCarFragment independently.
 */
public class MainPhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, SettingsPhoneActivity.class));
        finish();
    }
}
