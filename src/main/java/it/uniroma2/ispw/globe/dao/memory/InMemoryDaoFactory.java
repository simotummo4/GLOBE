package it.uniroma2.ispw.globe.dao.memory;

import it.uniroma2.ispw.globe.dao.*;


public class InMemoryDaoFactory extends DaoFactory {
    private static InMemoryDaoFactory instance = null;

    private InMemoryDaoFactory() {}

    public static InMemoryDaoFactory getInstance() {
        if (instance == null) {
            instance = new InMemoryDaoFactory();
        }
        return instance;
    }
    @Override
    public AccountDao getAccountDao() {
        return InMemoryAccountDao.getInstance();
    }

    @Override
    public AttractionDao getAttractionDao() {
        return InMemoryAttractionDao.getInstance();
    }

    @Override
    public CityDao getCityDao() {
        return InMemoryCityDao.getInstance();
    }

    @Override
    public DayDao getDayDao() {
        return InMemoryDayDao.getInstance();
    }

    @Override
    public ItineraryDao getItineraryDao() {
        return InMemoryItineraryDao.getInstance();
    }

    @Override
    public ProposalDao getProposalDao() { return InMemoryProposalDao.getInstance(); }

    @Override
    public RequestDao getRequestDao() {
        return InMemoryRequestDao.getInstance();
    }

    @Override
    public FlightDao getFlightDao() {
        return InMemoryFlightDao.getInstance();
    }

    @Override
    public AccommodationDao getAccommodationDao() {
        return InMemoryAccommodationDao.getInstance();
    }
}
