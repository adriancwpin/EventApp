package eventApp.model;

public class AdminStaff extends User{
    private String name;

    public AdminStaff(String email, String password, String name) {
        super(email, password);
        this.name = name;
    }

    /**
     * Get the name of the admin staff
     * @return name of the admin staff
     */
    public String getName() {
        return name;
    }
}
