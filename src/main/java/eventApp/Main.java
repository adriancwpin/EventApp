package eventApp;

import eventApp.controller.MenuController;
import eventApp.external.*;
import eventApp.view.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("App starting..."); // ← add this

        View view = new TextUserInterface();
        PaymentSystem paymentSystem = new MockPaymentSystem();
        VerificationService verificationService = new MockVerificationService();
        System.out.println("Controllers initialising..."); // ← add this

        MenuController menuctrl = new MenuController(view, paymentSystem, verificationService);
        System.out.println("Starting main menu..."); // ← add this

        menuctrl.mainMenu();
    }
}