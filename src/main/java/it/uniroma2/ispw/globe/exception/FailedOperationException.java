package it.uniroma2.ispw.globe.exception;

public class FailedOperationException extends Exception{
    public FailedOperationException(String message) {
        super(message + " operation failed");
    }
}
