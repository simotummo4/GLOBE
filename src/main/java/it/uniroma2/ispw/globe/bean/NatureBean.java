package it.uniroma2.ispw.globe.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;

public class NatureBean {
    private String difficulty;
    private double trekkingDistance;

    public double getTrekkingDistance() {
        return trekkingDistance;
    }

    public void setTrekkingDistance(double trekkingDistance) throws IncorrectDataException {
        if (trekkingDistance < 0) {
            throw new IncorrectDataException("Nature trekking distance not valid");
        }
        this.trekkingDistance = trekkingDistance;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) throws IncorrectDataException {
        if (difficulty == null || difficulty.isEmpty()) {
            throw new IncorrectDataException("Nature difficulty not valid");
        }
        this.difficulty = difficulty;
    }
}
