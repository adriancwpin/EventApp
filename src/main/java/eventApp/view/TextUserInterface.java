package eventApp.view;

import eventApp.view.View;
import java.util.Scanner;
import java.util.Collection;

public class TextUserInterface implements View{
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getInput(String inputPrompt){
        System.out.print(inputPrompt);
        return scanner.nextLine();
    }

    @Override
    public void displaySuccess(String successMessage) {
        System.out.println(); //empty line before
        System.out.println("[SUCCESS]" + successMessage);
        System.out.println(); //after
    }

    @Override
    public void displayError(String errorMessage) {
        System.out.println(); //empty line before
        System.out.println("[ERROR]" + errorMessage);
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
