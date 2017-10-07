package com.example.moodweather.gson;

/**
 * Created by 江婷婷 on 2017/10/7.
 */

public class Hourly_forecast {
    private Cond cond;

    private String date;

    private String hum;

    private String pop;

    private String pres;

    private String tmp;

    private Wind wind;

    public class Wind {
        private String deg;

        private String dir;

        private String sc;

        private String spd;

        public void setDeg(String deg) {
            this.deg = deg;
        }

        public String getDeg() {
            return this.deg;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public String getDir() {
            return this.dir;
        }

        public void setSc(String sc) {
            this.sc = sc;
        }

        public String getSc() {
            return this.sc;
        }

        public void setSpd(String spd) {
            this.spd = spd;
        }

        public String getSpd() {
            return this.spd;
        }

    }

    public void setCond(Cond cond) {
        this.cond = cond;
    }

    public Cond getCond() {
        return this.cond;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getHum() {
        return this.hum;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getPop() {
        return this.pop;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public String getPres() {
        return this.pres;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getTmp() {
        return this.tmp;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Wind getWind() {
        return this.wind;
    }

    public class Cond {
        private String code;

        private String txt;

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getTxt() {
            return this.txt;
        }

    }

}
