package com.example.Rzesny.shoppinglistapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.Rzesny.shoppinglistapp.R;
import com.example.Rzesny.shoppinglistapp.Utils.SharedPreferencesUtils;
import com.example.Rzesny.shoppinglistapp.Utils.ThemeUtils;

public class OptionsActivity extends AppCompatActivity {

    public static boolean configurationChanged = false;
    Button applyChanges;
    RadioButton whiteRadio;
    RadioButton darkRadio;
    RadioButton polishRadio;
    RadioButton englishRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_options);
        applyChanges = findViewById(R.id.applyChangesButton);
        whiteRadio = findViewById(R.id.radio_white);
        darkRadio = findViewById(R.id.radio_dark);
        polishRadio = findViewById(R.id.radio_polish);
        englishRadio = findViewById(R.id.radio_english);
        if(SharedPreferencesUtils.appLanguage.equals("en"))
        {
            englishRadio.setChecked(true);
        }
        if(SharedPreferencesUtils.appLanguage.equals("pl"))
        {
            polishRadio.setChecked(true);
        }
        if(ThemeUtils.cTheme==ThemeUtils.BLACK)
        {
            darkRadio.setChecked(true);
        }
        if(ThemeUtils.cTheme==ThemeUtils.GREY)
        {
            whiteRadio.setChecked(true);
        }
    }

    public void onApplyChangesButtonClicked(View view) {
        configurationChanged=true;
        if(englishRadio.isChecked()){
            SharedPreferencesUtils.saveLocale(this,"en");
            SharedPreferencesUtils.loadAndSetLocale(this);
            recreate();
        }
        if(polishRadio.isChecked()){
            SharedPreferencesUtils.saveLocale(this,"pl");
            SharedPreferencesUtils.loadAndSetLocale(this);
            recreate();
        }

        if(darkRadio.isChecked()){
            SharedPreferencesUtils.saveTheme(this,ThemeUtils.BLACK);
            ThemeUtils.cTheme=ThemeUtils.BLACK;
            recreate();
        }
        if(whiteRadio.isChecked()){
            SharedPreferencesUtils.saveTheme(this,ThemeUtils.GREY);
            ThemeUtils.cTheme=ThemeUtils.GREY;
            recreate();
        }
    }
}