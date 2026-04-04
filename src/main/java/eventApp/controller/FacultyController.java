import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private List<User> users;
    private RegistrationUtility regUtil;

    public AuthService() {
        users = new ArrayList<>();
        regUtil = new RegistrationUtility();
    }

    public boolean login(String email, String password) {
        //check if user already exists
        User user = findUserByEmail(email);

        if (user == null) {
            //check if email exists in faculty file
            FacultyMember newFaculty = regUtil.registerFacultyMember(email);
            if (newFaculty != null) {
                //do we need to check if the password matches too ?
                    users.add(newFaculty);
                    user = newFaculty;
                } 
          
            } else {
                return false; // email not found anywhere
            }
        }

        // 3. Check password for existing user
            return user.getPassword().equals(password);
         //this bracket isn't working lol

    private User findUserByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equals(email)) return u;
        }
        return null;
    }
