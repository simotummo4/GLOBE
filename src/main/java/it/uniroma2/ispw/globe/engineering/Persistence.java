package it.uniroma2.ispw.globe.engineering;

import it.uniroma2.ispw.globe.dao.DaoFactory;
import it.uniroma2.ispw.globe.dao.db.InDbDaoFactory;
import it.uniroma2.ispw.globe.dao.fs.InFSDaoFactory;
import it.uniroma2.ispw.globe.dao.memory.InMemoryDaoFactory;

public class Persistence {
    private static Persistence instance = null;

    public static final String IN_MEMORY = "MEMORY";
    public static final String IN_DATABASE = "DB";
    public static final String IN_FILESYSTEM = "FS";

    private DaoFactory daoFactory;

    private String type;
    private String defaultType;

    private Persistence() {}

    public static Persistence getInstance() {
        if (instance == null) {
            instance = new Persistence();
        }
        return instance;
    }

    public DaoFactory getFactory() {
        return daoFactory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        if (type.equals(IN_DATABASE)) {
            daoFactory = InDbDaoFactory.getInstance();
        } else if (type.equals(IN_MEMORY)) {
            daoFactory = InMemoryDaoFactory.getInstance();
        } else if (type.equals(IN_FILESYSTEM)) {
            daoFactory = InFSDaoFactory.getInstance();
        }
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }

    public void setDefault() {
        setType(defaultType);
    }
}

