package it.uniroma2.ispw.globe.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;

public class PaymentBean {
    private String payerUsername;
    private String payeeUsername;
    private double amount;

    public String getPayerUsername() {
        return payerUsername;
    }
    public void setPayerUsername(String payerUsername) {
        this.payerUsername = payerUsername;
    }
    public String getPayeeUsername() {
        return payeeUsername;
    }
    public void setPayeeUsername(String payeeUsername) {
        this.payeeUsername = payeeUsername;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) throws IncorrectDataException {
        if (amount < 0) {
            throw new IncorrectDataException("Payment amount not valid");
        }
        this.amount = amount;
    }
}
