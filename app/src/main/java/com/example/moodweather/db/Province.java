package com.example.moodweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 江婷婷 on 2017/9/25.
 */

public class Province extends DataSupport {
    private int id;
    private String provinceName;
    protected int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
