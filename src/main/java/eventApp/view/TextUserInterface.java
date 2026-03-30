package eventApp.view;

import eventApp.view.View;
import java.util.Scanner;
import java.util.Collection;

public class TextUserInterface implements View{
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";
    private final Scanner scanner = new Scanner(System.in);


    @Override
    public String getInput(String inputPrompt){
        System.out.print(ANSI_YELLOW + inputPrompt + ANSI_RESET);
        return scanner.nextLine();
    }

    @Override
    public void displaySuccess(String successMessage) {
        System.out.println(); //empty line before
        System.out.println(ANSI_GREEN + "[SUCCESS]" + successMessage + ANSI_RESET);
        System.out.println(); //after
    }

    @Override
    public void displayError(String errorMessage) {
        System.out.println(); //empty line before
        System.out.println(ANSI_RED + "[ERROR]" + errorMessage +ANSI_RESET);
        System.out.println(); //empty line after
    }

    @Override
    public void displayListOfPerformances(Collection<String> listOfPerformanceInfo){
        System.out.println("=== [List of Performances] ===");
        int count = 1;
        for(String info : listOfPerformanceInfo){
            System.out.println(count + "." + info);
            count++;
        }
    }

    @Override
    public void displaySpecificPerformance(String performanceInfo){
        System.out.println("=== [Performance Info] ===");
        System.out.println(performanceInfo);
    }

    @Override
    public void displayBookingRecord(String bookingRecord){
        System.out.println("=== [Booking Record] ===");
        System.out.println(bookingRecord);
    }
}
