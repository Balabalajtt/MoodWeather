package com.example.moodweather.gson;

public class Aqi {
    private City city;

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return this.city;
    }

    public class City {
        private String aqi;

        private String pm10;

        private String pm25;

        private String qlty;

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getAqi() {
            return this.aqi;
        }

        public void setPm10(String pm10) {
            this.pm10 = pm10;
        }

        public String getPm10() {
            return this.pm10;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public String getPm25() {
            return this.pm25;
        }

        public void setQlty(String qlty) {
            this.qlty = qlty;
        }

        public String getQlty() {
            return this.qlty;
        }

    }

}
