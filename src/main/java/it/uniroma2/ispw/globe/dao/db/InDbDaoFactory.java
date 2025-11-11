package it.uniroma2.ispw.globe.dao.db;

import it.uniroma2.ispw.globe.dao.*;

public class InDbDaoFactory extends DaoFactory {
    private static InDbDaoFactory instance = null;

    private InDbDaoFactory() {}

    public static InDbDaoFactory getInstance() {
        if (instance == null) {
            instance = new InDbDaoFactory();
        }
        return instance;
    }
    @Override
    public ItineraryDao getItineraryDao() {
        return new InDbItineraryDao();
    }

    @Override
    public ProposalDao getProposalDao() { return new InDbProposalDao(); }

    @Override
    public RequestDao getRequestDao() {
        return new InDbRequestDao();
    }

    @Override
    public FlightDao getFlightDao() {
        return new InDbFlightDao();
    }

    @Override
    public AccommodationDao getAccommodationDao() {
        return new InDbAccommodationDao();
    }

    @Override
    public AccountDao getAccountDao() {
        return new InDbAccountDao();
    }

    @Override
    public AttractionDao getAttractionDao() {
        return new InDbAttractionDao();
    }

    @Override
    public CityDao getCityDao() {
        return new InDbCityDao();
    }

    @Override
    public DayDao getDayDao() {
        return new InDbDayDao();
    }
}
