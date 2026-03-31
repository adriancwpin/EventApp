package eventApp.controller;

import eventApp.external.VerificationService;
import eventApp.model.User;
import eventApp.view.View;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;

public abstract class SystemInitialisation {
    protected UserController userController;
    protected Collection<User> users;
    protected View view;
    protected VerificationService verificationService;

    @BeforeEach
    void setup(){
        users = new ArrayList<>();

        // create a mock version of view
        view = mock(View.class);
        verificationService = mock(VerificationService.class);
        userController = new UserController(users, view, null);
        userController.setCurrentUser(null);
    }
}
