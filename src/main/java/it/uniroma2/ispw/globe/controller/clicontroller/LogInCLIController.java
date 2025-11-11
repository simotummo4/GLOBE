package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.bean.CredentialsBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import static it.uniroma2.ispw.globe.constants.ItineraryType.*;
import static it.uniroma2.ispw.globe.constants.UserType.*;

public class LogInCLIController {

    private static final String CHOICE_ERROR = "ERROR: Invalid option\n";
    private static final String ERROR = "ERROR: ";

    private static final String PASSWORD_REQUEST = "Please enter Password: ";

    public void start() {
        while(true) {
            System.out.println("# LOG IN #");
            int choice;
            choice = showMenu();

            switch(choice) {
                case 1 -> logIn();
                case 2 -> signIn();
                case 3 -> logInAsGuest();
                case 4 -> {
                    System.exit(0);
                    return;
                }
                default -> System.out.println(CHOICE_ERROR);
            }
        }
    }

    public int showMenu() {
        System.out.println("Welcome, what do you want do?");
        System.out.println("1 -> Log In");
        System.out.println("2 -> Sign In");
        System.out.println("3 -> Log In as Guest ");
        System.out.println("4 -> Quit");

        Scanner input = new Scanner(System.in);
        String strChoice;
        int choice;
        while (true) {
            System.out.print("Please enter your choice: ");
            strChoice = input.nextLine();
            if (!strChoice.isEmpty() && strChoice.matches("[1-4]")) {
                choice = Integer.parseInt(strChoice);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        return choice;
    }

    public void signIn() {
        System.out.println("# SIGN IN #");
        System.out.println("Who are you? User or Agency?");

        Scanner input = new Scanner(System.in);
        String response;
        while (true) {
            response = input.nextLine();
            if (response.equalsIgnoreCase(USER) || response.equalsIgnoreCase(AGENCY)) {
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        System.out.println();
        if (response.equalsIgnoreCase(USER)) {
            userSignIn();
        }
        else if (response.equalsIgnoreCase(AGENCY)) {
            agencySignIn();
        }
    }

    public void userSignIn() {
        CredentialsBean credentialsBean = getCredentials(false);

        try {
            credentialsBean.setType(USER);
            new LogInController().signIn(credentialsBean);
        } catch (FailedOperationException | DuplicateItemException e) {
            System.out.println(ERROR + e.getMessage());
            System.out.println();
        }
    }

    public void agencySignIn() {
        String description;
        List<String> preferences;

        CredentialsBean credentials = getCredentials(false);

        Scanner input = new Scanner(System.in);

        preferences = getAgencyPreferences();

        while (true){
            System.out.print("Please enter a description for your agency: ");
            description = input.nextLine();
            try {
                credentials.setDescription(description);
                break;
            } catch (IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
                System.out.println();
            }
        }

        try {
            credentials.setPreferences(preferences);

            new LogInController().signIn(credentials);

        } catch (IncorrectDataException | FailedOperationException | DuplicateItemException e) {
            System.out.println(ERROR + e.getMessage());
            System.out.println();
        }
    }

    public List<String> getAgencyPreferences() {
        List<String> preferences = new ArrayList<>();

        Scanner input = new Scanner(System.in);

        System.out.println("-> Select your preferences? (select stop to terminate)\n");
        System.out.println("1 -> "+ON_THE_ROAD);
        System.out.println("2 -> "+NATURE);
        System.out.println("3 -> "+CULTURE);
        System.out.println("4 -> "+RELAX);
        System.out.println("5 -> "+CITY);
        System.out.println("6 -> stop");

        String strChoice;
        int choice;
        boolean stop = false;
        while (!stop) {
            while (true) {
                System.out.print("Please enter your choice: ");
                strChoice = input.nextLine();
                if (!strChoice.isEmpty() && strChoice.matches("[1-6]")) {
                    System.out.println("*********************************");
                    choice = Integer.parseInt(strChoice);
                    break;
                }
                System.out.println(CHOICE_ERROR);
            }
            switch(choice) {
                case 1 -> preferences.add(ON_THE_ROAD);
                case 2 -> preferences.add(NATURE);
                case 3 -> preferences.add(CULTURE);
                case 4 -> preferences.add(RELAX);
                case 5 -> preferences.add(CITY);
                case 6 -> stop = true;
                default -> System.out.println(CHOICE_ERROR);
            }
        }

        return preferences;
    }

    public void logIn() {

        String sessionId;
        CredentialsBean credentials;
        try {
            credentials = getCredentials(true);
            sessionId = new LogInController().logIn(credentials);
        } catch (FailedOperationException | DuplicateItemException e) {
            System.out.println(ERROR + e.getMessage());
            System.out.println();
            return;
        }

        if (sessionId != null) {
            String type = new LogInController().getUserType(sessionId);
            System.out.println("Hello "+ credentials.getUsername());

            if (type.equals(USER)) {
                ManageItineraryCLIController controller = new ManageItineraryCLIController(sessionId);
                controller.start();
            } else {
                ManageRequestCLIController controller = new ManageRequestCLIController(sessionId);
                controller.start();
            }
        } else {
            System.out.println("You are not logged in");
        }
    }

    public void logInAsGuest() {

        CredentialsBean credentials = new CredentialsBean();
        String sessionId;
        try {
            credentials.setUsername(UUID.randomUUID().toString().substring(0,8));
            credentials.setPassword(UUID.randomUUID().toString().substring(0,8));
            credentials.setType(GUEST);
            sessionId = new LogInController().logIn(credentials);
        } catch (IncorrectDataException | FailedOperationException | DuplicateItemException e) {
            System.out.println(ERROR + e.getMessage());
            System.out.println();
            return;
        }

        if (sessionId != null) {
            ManageItineraryCLIController controller = new ManageItineraryCLIController(sessionId);
            controller.start();
        } else {
            System.out.println("ERROR\n");
        }
    }

    public CredentialsBean getCredentials(boolean isLogin) {
        Scanner input = new Scanner(System.in);

        String username;
        String password;
        String paymentCredentials;
        CredentialsBean credentials = new CredentialsBean();

        while (true){
            System.out.print("Please enter Username: ");
            username = input.nextLine();
            try {
                credentials.setUsername(username);
                break;
            } catch (IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
                System.out.println();
            }
        }

        while (true){
            System.out.print(PASSWORD_REQUEST);
            password = input.nextLine();
            try {
                credentials.setPassword(password);
                break;
            } catch (IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
                System.out.println();
            }
        }
        if (isLogin) {
            return credentials;
        }

        while (true){
            System.out.print("Please enter payment credentials: ");
            paymentCredentials = input.nextLine();
            try {
                credentials.setPaymentCredentials(paymentCredentials);
                break;
            } catch (IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
                System.out.println();
            }
        }
        return credentials;
    }
}
