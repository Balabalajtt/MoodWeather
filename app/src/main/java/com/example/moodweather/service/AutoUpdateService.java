package com.example.moodweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.moodweather.gson.HeWeather5;
import com.example.moodweather.util.HttpUtil;
import com.example.moodweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    long triggerAtTime;
    int choice;
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int anHour = 60 * 60 * 1000;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        choice = preferences.getInt("frequenceChoice", 1);
        Log.d("lalall", "onStartCommand: " + choice);
        switch (choice) {
            case 0:
                triggerAtTime = SystemClock.elapsedRealtime() + 5 * 60 * 1000;
                break;
            case 1:
                triggerAtTime = SystemClock.elapsedRealtime() + 4 * anHour;
                break;
            case 2:
                triggerAtTime = SystemClock.elapsedRealtime() + 8 * anHour;
                break;
            case 3:
                triggerAtTime = SystemClock.elapsedRealtime() + 24 * anHour;
                break;
            case 4:
                triggerAtTime = -1;
                break;
        }

        if (triggerAtTime != -1) {

            updateWeather();

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent i = new Intent(this, AutoUpdateService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
            alarmManager.cancel(pendingIntent);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);

        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather", null);
        if (weatherString != null) {
            HeWeather5 heWeather5 = Utility.handleWeatherResponse(weatherString);
            String weatherId = heWeather5.getBasic().getId();
            String weatherUrl = "https://free-api.heweather.com/v5/weather?city="
                    + weatherId + "&key=46c949f3635e455c890828d1ba60311c";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    HeWeather5 heWeather5 = Utility.handleWeatherResponse(responseText);
                    if (heWeather5 != null && "ok".equals(heWeather5.getStatus())) {
                        SharedPreferences.Editor editor = PreferenceManager.
                                getDefaultSharedPreferences(AutoUpdateService.this).
                                edit();
                        editor.putString("weather", responseText);
                        editor.apply();
                    }
                }
            });
        }
    }
}
