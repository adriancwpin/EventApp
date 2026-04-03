package eventApp.controller;

import java.util.Collection;

import eventApp.model.*;
import eventApp.view.View;

public abstract class Controller{
    protected static User currentUser; //current user is stored here
    protected View view;

    /**
     * Checks if the currently logged-in user has Admin privileges.
     * @return true if the user is an admin, false otherwise.
     */

    protected boolean checkCurrentUserIsGuest(){
        return currentUser == null;
    }

    protected boolean checkCurrentUserIsAdmin(){
        return currentUser instanceof AdminStaff;
    }

    protected boolean checkCurrentUserIsStudent(){
        return currentUser instanceof Student;
    }

    protected boolean checkCurrentUserIsEntertainmentProvider(){
        return currentUser instanceof EntertainmentProvider;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }

    //helper function for getting correct input for Yes or No
    protected boolean getYesNo(String prompt){
        String input = null;
        while(input == null){
            input = view.getInput(prompt);

            if(!input.equalsIgnoreCase("Yes") &&
                !input.equalsIgnoreCase("No")){
                view.displayError("Invalid input. Please enter Yes or No.");
                input = null; // ask again
            }
        }

        return input.equalsIgnoreCase("Yes");
    }

    //private helper method for email validation using regex
    protected boolean isValidEmail(String email){
        String emailRegex = "(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        return email.matches(emailRegex);
    }

    /**
     * Display a menu of options to the user and capture their selection.
     *
     * @param <T> a generic to show the type of elements in the option collection
     * @param options a collection of items to be displayed as menu choices
     * @param prompt the text message displayed to the users before selection
     * @return the index of the selected choice or 0 if no selection was made
     */

    protected <T> int selectFromMenu(Collection<T> options,  String prompt){
        int choice = 0;
        while(choice < 1 || choice > options.size()) {
            System.out.println(); //empty line before
            System.out.println(prompt);
            System.out.println(); //empty line after
            int count = 1;
            for (T option : options) {
                System.out.println(count + "." + option);
                count++;
            }

            try {
                choice = Integer.parseInt(view.getInput("Enter Choice: ").trim());

                if (choice < 1 || choice > options.size()) {
                    view.displayError("Invalid Choice. please choose a valid number.");
                    continue;
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid Choice. please enter a valid number.");
            }
        }
        return choice;
    }
}
