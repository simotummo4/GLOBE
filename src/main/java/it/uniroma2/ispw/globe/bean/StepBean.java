package it.uniroma2.ispw.globe.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;

import java.util.List;

public class StepBean {
    private int num;
    private List<String> city;
    private List<String> attractions;


    public int getNum() {
        return num;
    }

    public void setNum(int num) throws IncorrectDataException {
        if (num < 0 || num > 99) {
            throw new IncorrectDataException("day number not valid");
        }
        this.num = num;
    }

    public List<String> getCity() { return city; }

    public void setCity(List<String> city) throws IncorrectDataException {
        if (city == null || city.isEmpty()) {
            throw new IncorrectDataException("cities not valid");
        }
        this.city = city;
    }

    public List<String> getAttractions() { return attractions;}

    public void setAttractions(List<String> attractions) throws IncorrectDataException {
        if (attractions == null || attractions.isEmpty()) {
            throw new IncorrectDataException("attractions not valid");
        }
        this.attractions = attractions;
    }

}
