package com.example.moodweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 江婷婷 on 2017/9/25.
 */

public class Basic {
    //JSON字段不适合作为JAVA字段命名时使用注解建立映射关系
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    private class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
