package it.uniroma2.ispw.globe.engineering.session;

import it.uniroma2.ispw.globe.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static SessionManager instance = null;

    private Map<String,Session> sessions = new HashMap<>();

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public String addSession(Account account) {
        String sessionId = UUID.randomUUID().toString();
        if (sessions.containsKey(sessionId)) {
            return null;
        }
        sessions.put(sessionId, new Session(sessionId, account));
        return sessionId;
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }
}
