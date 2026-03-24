package eventApp.external;

public class MockPaymentSystem implements PaymentSystem {

    @Override
    public boolean processPayment(int numTickets, String eventTitle, String studentEmail, int studentPhone, String epEmail,
                           double transactionAmount){
        return true;
    }

    @Override
    public boolean processRefund(int numTickets, String eventTitle, String studentEmail, int studentPhone, String epEmail,
                          double transactionAmount, String organiserMsg){
        return true;
    }
}
