package it.uniroma2.ispw.globe.controller.applicationcontroller;

public class PaymentApi {

    // classe Mock che simula il comportamento dell'api
    public boolean processPayment(double amount, String payerPaymentCredentials, String payeePaymentCredentials){
        return amount < 1000 && payerPaymentCredentials != null && payeePaymentCredentials != null;
    }
}
