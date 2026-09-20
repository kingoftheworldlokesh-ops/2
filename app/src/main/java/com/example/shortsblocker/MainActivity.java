package com.example.shortsblocker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        Button btnSetup = new Button(this);
        btnSetup.setText("1. Enable Accessibility Permission");
        btnSetup.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        layout.addView(btnSetup);

        SharedPreferences prefs = getSharedPreferences("BlockerPrefs", Context.MODE_PRIVATE);

        CheckBox cbAllowFirst = new CheckBox(this);
        cbAllowFirst.setText("Watch only the first short video");
        cbAllowFirst.setChecked(prefs.getBoolean("ALLOW_FIRST_SHORT", true));
        cbAllowFirst.setOnCheckedChangeListener((b, isChecked) ->
            prefs.edit().putBoolean("ALLOW_FIRST_SHORT", isChecked).apply()
        );
        layout.addView(cbAllowFirst);

        setContentView(layout);
    }
}
