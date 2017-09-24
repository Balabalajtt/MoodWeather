package com.example.moodweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 江婷婷 on 2017/9/25.
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
