package eventApp.model;

public class Student extends User{
    private String name;
    private int phoneNumber;
    private StudentPreferences preferences;

    //Constructor
    public Student(String name, int phoneNumber, String email, String password) {
        super(email, password);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.preferences = new StudentPreferences();
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

    public void addBooking(Booking booking) {}
}
