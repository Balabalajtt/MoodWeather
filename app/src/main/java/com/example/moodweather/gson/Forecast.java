package com.example.moodweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 江婷婷 on 2017/9/25.
 * 单日天气实体类
 */

public class Forecast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    private class Temperature {
        public String max;
        public String min;
    }

    private class More {
        @SerializedName("txt_d")
        public String info;
    }
}
