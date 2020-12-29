package com.example.quickdustinfo.model.dust_material;

public class Dust {
    private Station station;
    private String timeObservation;
    private pm10 pm10;

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getTimeObservation() {
        return timeObservation;
    }

    public void setTimeObservation(String timeObservation) {
        this.timeObservation = timeObservation;
    }

    public com.example.quickdustinfo.model.dust_material.pm10 getPm10() {
        return pm10;
    }

    public void setPm10(com.example.quickdustinfo.model.dust_material.pm10 pm10) {
        this.pm10 = pm10;
    }
}
