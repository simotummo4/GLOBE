package it.uniroma2.ispw.globe.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;

import java.util.List;

public class CredentialsBean {
    private String username;
    private String password;
    private String type;
    private String description;
    private String paymentCredentials;
    private List<String> preferences;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws IncorrectDataException {
        if (username == null || username.isEmpty()) {
            throw new IncorrectDataException("Username not valid");
        }
        this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) throws IncorrectDataException {
        if (password == null || password.length()<8) {
            throw new IncorrectDataException("Password not valid (must be at least 8 characters)");
        }
        this.password = password;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getDescription() { return description;}

    public void setDescription(String description) throws IncorrectDataException {
        if (description == null || description.isEmpty()) {
            throw new IncorrectDataException("Description not valid");
        }
        this.description = description;
    }

    public String getPaymentCredentials() { return paymentCredentials; }

    public void setPaymentCredentials(String paymentCredentials) throws IncorrectDataException {
        if (paymentCredentials == null || !paymentCredentials.matches("\\d{16}")) {
            throw new IncorrectDataException("PaymentCredentials not valid (must be 16 number)");
        }
        this.paymentCredentials = paymentCredentials;
    }

    public List<String> getPreferences() { return preferences; }

    public void setPreferences(List<String> preferences) throws IncorrectDataException {
        if (preferences == null || preferences.isEmpty()) {
            throw new IncorrectDataException("Preferences not valid");
        }
        this.preferences = preferences;
    }
}
