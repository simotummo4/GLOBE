package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.bean.CredentialsBean;
import it.uniroma2.ispw.globe.dao.AccountDao;
import it.uniroma2.ispw.globe.engineering.Persistence;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static it.uniroma2.ispw.globe.constants.UserType.USER;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class LogInControllerTest {

    // test by Simone Tummolo 0309116

    @Test
    void testLogInCorrect() throws IncorrectDataException, FailedOperationException, DuplicateItemException {
        LogInController logInController = new LogInController();

        CredentialsBean credentialsBean = new CredentialsBean();
        credentialsBean.setUsername("userTest");
        credentialsBean.setPassword("passwordU");
        credentialsBean.setType(USER);

        String sessionID = logInController.logIn(credentialsBean);
        Assertions.assertEquals(credentialsBean.getUsername(), SessionManager.getInstance().getSession(sessionID).getAccount().getUsername());
    }

    @Test
    void testLogInIncorrect() throws IncorrectDataException, FailedOperationException, DuplicateItemException {
        LogInController logInController = new LogInController();

        CredentialsBean credentialsBean = new CredentialsBean();
        credentialsBean.setUsername("userIncorrect");
        credentialsBean.setPassword("passwordIncorrect");
        credentialsBean.setType(USER);

        String sessionID = logInController.logIn(credentialsBean);
        assertNull(sessionID);
    }

    @Test
    void testSignInDuplicate() throws IncorrectDataException {
        LogInController logInController = new LogInController();

        CredentialsBean credentialsBean = new CredentialsBean();
        credentialsBean.setUsername("userTest");
        credentialsBean.setPassword("passwordTest");
        credentialsBean.setType(USER);

        String errorMess = "";
        try {
            logInController.signIn(credentialsBean);
        } catch (DuplicateItemException | FailedOperationException e) {
            errorMess = e.getMessage();
        }
        Assertions.assertEquals("Duplicate item", errorMess);
    }



    @Test
    void testSignInCorrect() throws IncorrectDataException, FailedOperationException, DuplicateItemException, DaoException {
        LogInController logInController = new LogInController();
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

        CredentialsBean credentialsBean = new CredentialsBean();
        credentialsBean.setUsername("userCorrect");
        credentialsBean.setPassword("passwordU");
        credentialsBean.setType(USER);

        logInController.signIn(credentialsBean);
        Account account = accountDao.getAccount("userCorrect");
        assertNotNull(account);
    }


    @Test
    void testGetUserTypeCorrect() throws DaoException {
        LogInController logInController = new LogInController();
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

        Account account = accountDao.getAccount("userTest");
        String sessionID = SessionManager.getInstance().addSession(account);

        String type = logInController.getUserType(sessionID);

        Assertions.assertEquals(USER, type);
    }






    @BeforeAll
    static void provideAccount() throws IncorrectDataException, DaoException {
        Persistence.getInstance().setType(Persistence.IN_MEMORY);
        Persistence.getInstance().setDefaultType(Persistence.IN_MEMORY);

        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

        CredentialsBean userCredentialsBean = new CredentialsBean();
        userCredentialsBean.setUsername("userTest");
        userCredentialsBean.setPassword("passwordU");
        userCredentialsBean.setType(USER);
        userCredentialsBean.setPaymentCredentials("1234567890123456");
        if (accountDao.getAccount("userTest") == null) {
            accountDao.addAccount(userCredentialsBean);
        }
    }
}