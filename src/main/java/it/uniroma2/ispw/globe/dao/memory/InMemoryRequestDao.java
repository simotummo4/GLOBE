package it.uniroma2.ispw.globe.dao.memory;

import it.uniroma2.ispw.globe.dao.RequestDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Request;
import it.uniroma2.ispw.globe.model.User;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;

public class InMemoryRequestDao extends RequestDao {

    private static InMemoryRequestDao instance = null;

    private List<Request> requests = new ArrayList<>();

    private InMemoryRequestDao() {}

    public static InMemoryRequestDao getInstance() {
        if (instance == null) {
            instance = new InMemoryRequestDao();
        }
        return instance;
    }

    @Override
    public void addAgencyRequest(Request request, User user, List<Agency> agencies) throws DaoException {
        if (getRequest(request.getId()) == null) {
            requests.add(request);
            user.getRequests().add(request);
            for (Agency agency : agencies) {
                agency.getRequests().add(request);
            }
        } else {
            throw new DaoException("addAgencyRequest", DUPLICATE);
        }
    }

    @Override
    public Request getRequest(String requestId) {
        for (Request request : requests) {
            if (request.getId().equals(requestId)) {
                return request;
            }
        }
        return null;
    }

    @Override
    public void updateRequest(Request request) {
        // not necessary InMemory
    }
}
