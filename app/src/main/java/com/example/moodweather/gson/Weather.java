package com.example.moodweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    public String status;//成功返回“ok” 失败发牛具体原因
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast") //使用不同命名要注解JSON字段和Java字段建立映射关系
    public List<Forecast> forecastList;//单日天气实体类Forecast的集合

    public List<Alarm> alarms;

}
