package it.uniroma2.ispw.globe.exception;

public class AttractionNotAddedException extends Exception{
    public AttractionNotAddedException(String message) {
        super("Related cities are not presented.\nAttraction not added to the list of attractions:\n " + message);
    }
}
