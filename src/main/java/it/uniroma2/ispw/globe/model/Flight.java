package it.uniroma2.ispw.globe.model;

public class Flight {
    private String id;
    private double departureTime;
    private double arrivalTime;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public double getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
