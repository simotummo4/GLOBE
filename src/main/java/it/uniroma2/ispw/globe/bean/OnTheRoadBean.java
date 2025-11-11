package it.uniroma2.ispw.globe.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;

public class OnTheRoadBean {
    private String mode;
    private double dayDrivingHours;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) throws IncorrectDataException {
        if (mode == null || mode.isEmpty()) {
            throw new IncorrectDataException("On the road mode not valid");
        }
        this.mode = mode;
    }

    public double getDayDrivingHours() {
        return dayDrivingHours;
    }

    public void setDayDrivingHours(double dayDrivingHours) throws IncorrectDataException {
        if (dayDrivingHours < 0) {
            throw new IncorrectDataException("On the road day driving hours not valid");
        }
        this.dayDrivingHours = dayDrivingHours;
    }
}
