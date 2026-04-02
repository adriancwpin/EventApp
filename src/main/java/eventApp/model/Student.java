package eventApp.model;

import java.util.ArrayList;
import java.util.Collection;

public class Student extends User{
    private String name;
    private int phoneNumber;
    private StudentPreferences preferences;
    private Collection <Booking> bookings;

    //Constructor
    public Student(String email, String password, String name, int phoneNumber) {
        super(email, password);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.preferences = new StudentPreferences();
        this.bookings = new ArrayList<>();
    }

    //Getters
    public String getName() {
        return name;
    }
    public int getPhoneNumber() {
        return phoneNumber;
    }
    public StudentPreferences getPreferences() {
        return preferences;
    }

    /**
     * Adds a booking to this student's list of bookings.
     * @param booking the booking to add
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
}
