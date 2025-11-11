package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.exception.*;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.bean.CredentialsBean;
import it.uniroma2.ispw.globe.dao.AccountDao;
import it.uniroma2.ispw.globe.engineering.Persistence;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;

import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.engineering.Persistence.IN_MEMORY;
import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;

public class LogInController{

    private static final String GUEST = "guest";

    public String logIn(CredentialsBean credentials) throws FailedOperationException, DuplicateItemException {
        try {
            if (credentials.getType()!=null && credentials.getType().equals(GUEST)) {
                Persistence.getInstance().setType(IN_MEMORY);
            } else {
                Persistence.getInstance().setDefault();
            }

            AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

            if (credentials.getType()!=null && credentials.getType().equals(GUEST)) {
                accountDao.addAccount(credentials);
            }
            Account account = accountDao.authenticate(credentials.getUsername(), credentials.getPassword());

            if (account != null) {
                return SessionManager.getInstance().addSession(account);
            }
            return null;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Log in");
        }
    }

    public void signIn(CredentialsBean credentials) throws FailedOperationException, DuplicateItemException {
        if (credentials.getType()!=null && credentials.getType().equals(GUEST)) {
            Persistence.getInstance().setType(IN_MEMORY);
        } else {
            Persistence.getInstance().setDefault();
        }

        try {
            AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
            accountDao.addAccount(credentials);

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Sign in");
        }
    }

    public void logOut(String sessionId) throws FailedOperationException, IncorrectDataException {
        try {
            Account account = SessionManager.getInstance().getSession(sessionId).getAccount();
            if (account.getType().equals(GUEST)) {
                CredentialsBean credentialsBean = new CredentialsBean();
                credentialsBean.setUsername(account.getUsername());
                credentialsBean.setPassword(account.getPassword());
                credentialsBean.setType(account.getType());

                AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
                accountDao.removeAccount(credentialsBean);
            }
            SessionManager.getInstance().removeSession(sessionId);
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Log out");
        }
    }

    public String getUserType(String sessionID) {
        Account account = SessionManager.getInstance().getSession(sessionID).getAccount();
        if (account != null) {
            return account.getType();
        }
        return null;
    }
}