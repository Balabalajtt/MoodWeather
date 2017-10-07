package com.example.moodweather.util;

import android.text.TextUtils;

import com.example.moodweather.db.City;
import com.example.moodweather.db.County;
import com.example.moodweather.db.Province;
import com.example.moodweather.gson.HeWeather5;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 工具类解析JSON数据
 */

public class Utility {

    /**
     * 解析服务器返回的省级数据
     * 组装成实体类存入数据库
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {//判断response是否为空 ""/null
            try {
                //JSONObject解析JSON数据
                JSONArray allProvince = new JSONArray(response);
                for (int i = 0; i < allProvince.length(); i++) {
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();//将数据存储在数据库中
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }


    /**
     * 解析服务器返回的市级数据
     * 组装成实体类存入数据库
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCity = new JSONArray(response);
                for (int i = 0; i < allCity.length(); i++) {
                    JSONObject cityObject = allCity.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 解析服务器返回的区县级数据
     * 组装成实体类存入数据库
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounty = new JSONArray(response);
                for (int i = 0; i < allCounty.length(); i++) {
                    JSONObject countyObject = allCounty.getJSONObject(i);
                    County city = new County();
                    city.setCountyName(countyObject.getString("name"));
                    city.setWeatherId(countyObject.getString("weather_id"));
                    city.setCityId(cityId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * GSON解析天气数据
     * 将返回的JSON数据解析成 Weather实体类
     */
    public static HeWeather5 handleWeatherResponse (String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            //看返回数据HeWeather是一个数组 数组只有一个元素就是想要的
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, HeWeather5.class);//解析到Weather实体类
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
