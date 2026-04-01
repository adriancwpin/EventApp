import User;

public class FacultyMember extends User {
    private int loginAttempts;

    /Constructor
    public FacultyMember(String email, String password) {
        super(email, password); 
        this.loginAttempts = 0;
    }

    /Getter
    public int getLoginAttempts() {
        return loginAttempts;
    }

    /Increment
    public void incrementLoginAttempts() {
        loginAttempts++;
    }

}
