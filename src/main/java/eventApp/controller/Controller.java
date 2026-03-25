package eventApp.controller;

import java.util.Collection;
import eventApp.model.User;

public abstract class Controller{
    protected User currentUser;

    /**
     * Checks if the currently logged-in user has Admin privileges.
     * @return true if the user is an admin, false otherwise.
     */

    protected boolean checkCurrentUserIsAdmin(){
        return false;
    }

    protected boolean checkCurrentUserIsGuest(){
        return false;
    }

    protected boolean checkCurrentUserIsStudent(){
        return false;
    }

    protected boolean checkCurrentUserIsEntertainmentProvider(){
        return false;
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
        return 0;
    }
}
