import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegistrationUtilityTest {

    @Test
    public void testRegisterFacultyMember_found() {
        RegistrationUtility regUtil = new RegistrationUtility();
        FacultyMember fm = regUtil.registerFacultyMember("professor1@uni.ac.uk");

        assertNotNull(fm, "FacultyMember should be created");
        assertEquals("professor1@uni.ac.uk", fm.getEmail());
        assertEquals("password1", fm.getPassword());
    }

    @Test
    public void testRegisterFacultyMember_notFound() {
        RegistrationUtility regUtil = new RegistrationUtility();
        FacultyMember fm = regUtil.registerFacultyMember("unknown@uni.ac.uk");

        assertNull(fm, "FacultyMember should be null if email not in file");
    }
}
