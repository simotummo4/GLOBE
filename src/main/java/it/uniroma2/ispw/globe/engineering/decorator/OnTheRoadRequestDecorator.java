package it.uniroma2.ispw.globe.engineering.decorator;

import it.uniroma2.ispw.globe.model.*;

public class OnTheRoadRequestDecorator extends RequestDecorator {
    private String travelMode;
    private double dayDrivingHours;
    // attributi

    public OnTheRoadRequestDecorator(Request request) {
        super(request);
    }

    //get set attributi

    public String getTravelMode() {
        return travelMode;
    }
    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public double getDayDrivingHours() {
        return dayDrivingHours;
    }
    public void setDayDrivingHours(double dayDrivingHours) {
        this.dayDrivingHours = dayDrivingHours;
    }
}
