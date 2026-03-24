package eventApp.model;

public class AdminStaff extends User{
    private String name;

    public AdminStaff(String name, String email, String password) {
        super(email, password);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
