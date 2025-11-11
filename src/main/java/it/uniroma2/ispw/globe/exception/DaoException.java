package it.uniroma2.ispw.globe.exception;

public class DaoException extends Exception {

    public static final int GENERAL = 1;
    public static final int DUPLICATE = 2;

    private final int type;

    public DaoException(String message, int type) {
        super("Dao exception:\n " + message);
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
