package com.example.moodweather.gson;

/**
 * Created by 江婷婷 on 2017/9/25.
 */

public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
