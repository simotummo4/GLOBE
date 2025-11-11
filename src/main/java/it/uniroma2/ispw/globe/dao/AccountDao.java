package it.uniroma2.ispw.globe.dao;

import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.bean.CredentialsBean;

import java.util.List;

public abstract class AccountDao {
    public abstract Account authenticate(String username, String password) throws DaoException;
    public abstract void addAccount(CredentialsBean credentials) throws DaoException;
    public abstract Account getAccount(String username) throws DaoException;
    public abstract void removeAccount(CredentialsBean credentials) throws DaoException;
    public abstract List<Agency> getAgenciesByType(List<String> types) throws DaoException;
    public abstract Agency getAgencyByProposal(String proposalID) throws DaoException;
    public abstract User getUserByProposal(String proposalID) throws DaoException;
    public abstract Agency getAgencyByRequest(String requestID) throws DaoException;
    public abstract User getUserByRequest(String requestID) throws DaoException;
    public abstract void updateAgencyRating(Agency agency) throws DaoException;
}
