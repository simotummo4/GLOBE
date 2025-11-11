package it.uniroma2.ispw.globe.engineering.decorator;

import it.uniroma2.ispw.globe.model.*;

public class NatureRequestDecorator extends RequestDecorator {
    private double trekkingDistance;
    private String trekkingDifficulty;
    //attributi

    public NatureRequestDecorator(Request request) {
        super(request);
    }
    //get set attributi

    public double getTrekkingDistance() {
        return this.trekkingDistance;
    }
    public void setTrekkingDistance(double trekkingDistance) {
        this.trekkingDistance = trekkingDistance;
    }

    public String getTrekkingDifficulty() {
        return this.trekkingDifficulty;
    }
    public void setTrekkingDifficulty(String trekkingDifficulty) {
        this.trekkingDifficulty = trekkingDifficulty;
    }
}
