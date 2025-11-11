package it.uniroma2.ispw.globe.dao.fs;

import it.uniroma2.ispw.globe.dao.*;


public class InFSDaoFactory extends DaoFactory {
    private static InFSDaoFactory instance = null;

    private InFSDaoFactory() {}

    public static InFSDaoFactory getInstance() {
        if (instance == null) {
            instance = new InFSDaoFactory();
        }
        return instance;
    }
    @Override
    public ItineraryDao getItineraryDao() {
        return new InFSItineraryDao();
    }

    @Override
    public ProposalDao getProposalDao() { return new InFSProposalDao(); }

    @Override
    public RequestDao getRequestDao() {
        return new InFSRequestDao();
    }

    @Override
    public FlightDao getFlightDao() {
        return new InFSFlightDao();
    }

    @Override
    public AccommodationDao getAccommodationDao() {
        return new InFSAccommodationDao();
    }

    @Override
    public AccountDao getAccountDao() {
        return new InFSAccountDao();
    }

    @Override
    public AttractionDao getAttractionDao() {
        return new InFSAttractionDao();
    }

    @Override
    public CityDao getCityDao() {
        return new InFSCityDao();
    }

    @Override
    public DayDao getDayDao() {
        return new InFSDayDao();
    }
}