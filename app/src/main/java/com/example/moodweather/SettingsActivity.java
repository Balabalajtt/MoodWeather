package com.example.moodweather;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class SettingsActivity extends AppCompatActivity {

    private Button backButton;
    private TextView tempUnit;
    private TextView updateFrequence;
    private TextView about;


    private int tempUnitChoice;
    private int frequenceChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//使活动布局显示在状态栏上面
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tempUnitChoice = preferences.getInt("tempUnitChoice", 0);
        frequenceChoice = preferences.getInt("frequenceChoice", 2);


        backButton = (Button) findViewById(R.id.setting_back_button);
        tempUnit = (TextView) findViewById(R.id.temp_unit);
        updateFrequence = (TextView) findViewById(R.id.update_frequence);
        about = (TextView) findViewById(R.id.about);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tempUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = {"℃ - 摄氏度","℉ - 华氏度"};
                String title = "温度单位";
                showSingleAlertDialog(items, title, tempUnitChoice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        tempUnitChoice = index;
                        SharedPreferences.Editor editor = PreferenceManager.
                                getDefaultSharedPreferences(SettingsActivity.this).
                                edit();
                        editor.putInt("tempUnitChoice", tempUnitChoice);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
            }
        });

        updateFrequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = {"实时（较费流量）", "四小时", "八小时", "一天", "从不（省流量模式）"};
                String title = "自动更新频率";
                showSingleAlertDialog(items, title, (int) frequenceChoice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        frequenceChoice = index;
                        SharedPreferences.Editor editor = PreferenceManager.
                                getDefaultSharedPreferences(SettingsActivity.this).
                                edit();
                        editor.putInt("frequenceChoice", frequenceChoice);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SettingsActivity.this);
                alertBuilder.setMessage("\n\t\t\t\t\t天气数据来源于和天风气\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tcreated by jtt\n");
                alertDialog = alertBuilder.create();
                alertDialog.show();
                WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                params.width = 900;
                params.height = 450 ;
                alertDialog.getWindow().setAttributes(params);
            }
        });

    }

    private AlertDialog alertDialog;
    public void showSingleAlertDialog(final String[] items, String title, int initChoice, DialogInterface.OnClickListener listener){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(title);
        alertBuilder.setSingleChoiceItems(items, initChoice, listener);
        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
