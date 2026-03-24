package eventApp.external;

public interface PaymentSystem {
    boolean processPayment(int numTickets, String eventTitle, String studentEmail, int studentPhone, String epEmail,
                           double transactionAmount);

    boolean processRefund(int numTickets, String eventTitle, String studentEmail, int studentPhone, String epEmail,
                          double transactionAmount, String organiserMsg);
}
