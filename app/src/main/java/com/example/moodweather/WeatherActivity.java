package com.example.moodweather;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodweather.gson.Alarms;
import com.example.moodweather.gson.Daily_forecast;
import com.example.moodweather.gson.HeWeather5;
import com.example.moodweather.service.AutoUpdateService;
import com.example.moodweather.util.HttpUtil;
import com.example.moodweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
//    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView dressText;
    private TextView sportText;
    private Button settingsButton;
    private ImageView imageView;

    public SwipeRefreshLayout swipeRefresh;

    private String mWeatherId;

    public DrawerLayout drawerLayout;

    private int tempUnitChoice = 0;
    private int frequenceChoice = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //初始
        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tempUnitChoice = preferences.getInt("tempUnitChoice", 0);
        frequenceChoice = preferences.getInt("frequenceChoice", 2);
        Log.d("lalalll", "onCreate: " + tempUnitChoice + frequenceChoice);

        //如果有天气数据就直接显示否则去通过MainActivity传来的weather_id获取天气数据
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather", null);
        if (weatherString != null) {
            HeWeather5 heWeather5 = Utility.handleWeatherResponse(weatherString);
            mWeatherId = heWeather5.getBasic().getId();
            showWeatherInfo(heWeather5);
        } else {
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);//获取天气数据
        }
    }

    /**
     * 初始化view
     */
    private void initViews() {

        //设置透明actionbar
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//使活动布局显示在状态栏上面
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
//        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        dressText = (TextView) findViewById(R.id.drsg_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        imageView = (ImageView) findViewById(R.id.degree_image);

        //侧滑
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Button navButton = (Button) findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(Color.CYAN);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        settingsButton = (Button) findViewById(R.id.setting);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this, SettingsActivity.class);
                startActivity(intent);
//                finish();
            }
        });
    }



    /**
     * 根据 weatherId获取天气数据
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "https://free-api.heweather.com/v5/weather?city="
                + weatherId + "&key=46c949f3635e455c890828d1ba60311c";

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final HeWeather5 heWeather5 = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("v5", "run: " + responseText + (heWeather5 == null));
                        if (heWeather5 != null && "ok".equals(heWeather5.getStatus())) {
                            //存文件
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).
                                    edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(heWeather5);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败",
                                Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }



    /**
     * 获取天气数据后展示
     */
    private void showWeatherInfo(HeWeather5 heWeather5) {
        String cityName = heWeather5.getBasic().getCity();
//        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = heWeather5.getNow().getTmp();
        String weatherInfo = heWeather5.getNow().getCond().getTxt();
        if (heWeather5.getAlarms() != null && !heWeather5.getAlarms().isEmpty()) {
            Log.d("kkk", "showWeatherInfo: " + heWeather5.getAlarms().get(0));
            for (Alarms alarm : heWeather5.getAlarms()) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle(alarm.getTitle())
                        .setContentText(alarm.getLevel() + " " + alarm.getType())
                        .setWhen(System.currentTimeMillis())
                        .build();
                manager.notify(heWeather5.getAlarms().indexOf(alarm), notification);
            }
        }
        switch (weatherInfo) {
            case "晴":
                imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.sunny));
                break;
            case "多云":
            case "晴间多云":
            case "少云":
                imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.cloudy));
                break;
            case "小雨":
            case "中雨":
            case "大雨":
            case "暴雨":
            case "阵雨":
            case "强阵雨":
                imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.rain));
                break;
            case "阴":
                imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.overcast));
                break;
            case "雷阵雨":
                imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.thunder));
                break;
            case "雨夹雪":
            case "雨雪天气":
                imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.snow));
                break;
        }

        titleCity.setText(cityName);
//        titleUpdateTime.setText("更新时间" + updateTime);

        if (tempUnitChoice == 1) {
            degreeText.setText(huaShiDu(degree) + "°");
        } else {
            degreeText.setText(degree + "°");
        }

        //添加listView
        forecastLayout.removeAllViews();
        for (Daily_forecast forecast : heWeather5.getDaily_forecast()) {
            View view = LayoutInflater.from(this).inflate(
                    R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.getDate());
            infoText.setText(forecast.getCond().getTxt_d());
            if (tempUnitChoice == 1) {
                if (heWeather5.getDaily_forecast().indexOf(forecast) == 0) {
                    weatherInfoText.setText(weatherInfo + "   " + huaShiDu(forecast.getTmp().getMin()) + "°/" + huaShiDu(forecast.getTmp().getMax()) + "℉");
                }
                maxText.setText(huaShiDu(forecast.getTmp().getMax()) + "℉");
                minText.setText(huaShiDu(forecast.getTmp().getMin()) + "℉");
            } else {
                if (heWeather5.getDaily_forecast().indexOf(forecast) == 0) {
                    weatherInfoText.setText(weatherInfo + "   " + forecast.getTmp().getMin() + "°/" + forecast.getTmp().getMax() + "℃");
                }
                maxText.setText(forecast.getTmp().getMax() + "℃");
                minText.setText(forecast.getTmp().getMin() + "℃");
            }
            forecastLayout.addView(view);
        }

        if (heWeather5.getAqi() != null) {
            aqiText.setText(heWeather5.getAqi().getCity().getAqi());
            aqiText.setTextSize(40);
            pm25Text.setText(heWeather5.getAqi().getCity().getPm25());
            pm25Text.setTextSize(40);
        } else {
            aqiText.setText("暂无数据");
            aqiText.setTextSize(20);
            pm25Text.setText("暂无数据");
            pm25Text.setTextSize(20);
        }
        String comfort = "舒适指数：" + heWeather5.getSuggestion().getComf().getTxt();
        String dress = "穿衣指数：" + heWeather5.getSuggestion().getDrsg().getTxt();
        String sport = "运动建议：" + heWeather5.getSuggestion().getSport().getTxt();
        comfortText.setText(comfort);
        dressText.setText(dress);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, AutoUpdateService.class);
//        intent.putExtra("hour", frequenceChoice);
        startService(intent);
    }

    private int huaShiDu(String degree) {
        return (int) (Integer.parseInt(degree) * 1.8 + 32);
    }

    public void setWeatherId(String weatherId) {
        mWeatherId = weatherId;
    }
}
