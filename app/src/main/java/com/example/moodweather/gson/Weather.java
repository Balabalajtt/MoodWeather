package com.example.moodweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 江婷婷 on 2017/9/25.
 * 总实体类
 */

public class Weather {

    public String status;//成功返回“ok” 失败发牛具体原因
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
