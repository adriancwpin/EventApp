package eventApp.controller;

import java.util.Collection;

import eventApp.model.*;
import eventApp.view.View;

public abstract class Controller{
    public static User currentUser; // This is not static so bookingcontroller and usercontorller have different variables of currentuser
    // for example, usercontroller set currentuser to a
    // eventperformance controller set currentuser to b
    // usercontrollers current user will be a and eventperfromance controller currentuser is b
    // if static, its like a global variable, when usercontroller change, all classes would see the change, so you dont need the other classes to like keep parity with  the other currentusers
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

    /**
     * Display a menu of options to the user and capture their selection.
     *
     * @param <T> a generic to show the type of elements in the option collection
     * @param options a collection of items to be displayed as menu choices
     * @param prompt the text message displayed to the users before selection
     * @return the index of the selected choice or 0 if no selection was made
     */

    protected <T> int selectFromMenu(Collection<T> options,  String prompt){
        System.out.println(prompt);
        int count = 1;
        for (T option : options){
            System.out.println(count + "." + option);
            count++;
        }

        //choose the options
        int choice = Integer.parseInt(view.getInput("Enter Choice: "));
        if (choice < 1 || choice > options.size()){
            view.displayError("Invalid Choice. please try again.");
            return selectFromMenu(options, prompt);
        }
        return choice;
    }
}
