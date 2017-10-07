package com.example.moodweather.gson;

/**
 * Created by 江婷婷 on 2017/10/7.
 */

public class Daily_forecast {
    private Astro astro;

    private Cond cond;

    private String date;

    private String hum;

    private String pcpn;

    private String pop;

    private String pres;

    private Tmp tmp;

    private String uv;

    private String vis;

    private Wind wind;

    public void setAstro(Astro astro) {
        this.astro = astro;
    }

    public Astro getAstro() {
        return this.astro;
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

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public String getPcpn() {
        return this.pcpn;
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

    public void setTmp(Tmp tmp) {
        this.tmp = tmp;
    }

    public Tmp getTmp() {
        return this.tmp;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getUv() {
        return this.uv;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public String getVis() {
        return this.vis;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Wind getWind() {
        return this.wind;
    }

    public class Astro {
        private String mr;

        private String ms;

        private String sr;

        private String ss;

        public void setMr(String mr) {
            this.mr = mr;
        }

        public String getMr() {
            return this.mr;
        }

        public void setMs(String ms) {
            this.ms = ms;
        }

        public String getMs() {
            return this.ms;
        }

        public void setSr(String sr) {
            this.sr = sr;
        }

        public String getSr() {
            return this.sr;
        }

        public void setSs(String ss) {
            this.ss = ss;
        }

        public String getSs() {
            return this.ss;
        }

    }

    public class Cond {
        private String code_d;

        private String code_n;

        private String txt_d;

        private String txt_n;

        public void setCode_d(String code_d) {
            this.code_d = code_d;
        }

        public String getCode_d() {
            return this.code_d;
        }

        public void setCode_n(String code_n) {
            this.code_n = code_n;
        }

        public String getCode_n() {
            return this.code_n;
        }

        public void setTxt_d(String txt_d) {
            this.txt_d = txt_d;
        }

        public String getTxt_d() {
            return this.txt_d;
        }

        public void setTxt_n(String txt_n) {
            this.txt_n = txt_n;
        }

        public String getTxt_n() {
            return this.txt_n;
        }

    }

    public class Tmp {
        private String max;

        private String min;

        public void setMax(String max) {
            this.max = max;
        }

        public String getMax() {
            return this.max;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getMin() {
            return this.min;
        }

    }

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

}
