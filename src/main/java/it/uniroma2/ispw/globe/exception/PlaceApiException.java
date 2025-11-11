package it.uniroma2.ispw.globe.exception;

public class PlaceApiException extends Exception {
    public PlaceApiException(String message) {
        super("Api exception:\n" + message);
    }
}
