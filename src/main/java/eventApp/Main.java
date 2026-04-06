package eventApp;

import eventApp.controller.MenuController;
import eventApp.external.*;
import eventApp.view.*;

public class Main {
    public static void main(String[] args) {
        View view = new TextUserInterface();
        PaymentSystem paymentSystem = new MockPaymentSystem();
        VerificationService verificationService = new MockVerificationService();
        MenuController menuctrl = new MenuController(view, paymentSystem, verificationService);
        menuctrl.mainMenu();
    }
}