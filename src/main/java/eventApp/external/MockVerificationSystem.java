package eventApp.external;

public class MockVerificationSystem implements VerificationSystem {

    @Override
    public boolean verifyEntertainmentProvider(String businessRegistrationNumber){
        // verifies successfully
        return true;
    }
}
