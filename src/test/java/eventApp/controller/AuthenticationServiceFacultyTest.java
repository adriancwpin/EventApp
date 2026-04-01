import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    public void testLogin_existingFacultyUser() {
        AuthService auth = new AuthService();

        // create user
        boolean success = auth.login("professor1@uni.ac.uk", "password1");
        assertTrue(success, "Login should succeed and create the faculty account");

        // already existing user
        boolean success2 = auth.login("professor1@uni.ac.uk", "password1");
        assertTrue(success2, "Login should succeed for existing faculty");
    }

    @Test
    public void testLogin_wrongPassword() {
        AuthService auth = new AuthService();
        boolean success = auth.login("professor1@uni.ac.uk", "wrongpass");
        assertFalse(success, "Login should fail for wrong password");
    }

    @Test
    public void testLogin_unknownEmail() {
        AuthService auth = new AuthService();
        boolean success = auth.login("unknown@uni.ac.uk", "any");
        assertFalse(success, "Login should fail if email not in system or faculty file");
    }
}
