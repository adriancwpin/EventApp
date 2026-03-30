package eventApp.model;

import java.util.ArrayList;
import java.util.List;

public class EntertainmentProvider extends User {
    private String orgName;
    private String businessNumber;
    private String name;
    private String description;
    private List<Event> events;

    public EntertainmentProvider(String email, String password, String orgName, String businessNumber,
                                 String name, String description) {
        super(email, password);
        this.orgName = orgName;
        this.businessNumber = businessNumber;
        this.name = name;
        this.description = description;
        this.events = new ArrayList<>();
    }

    public String getOrgName() {
        return orgName;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public String getName() {
        return name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public String getDescription() {
        return description;
    }

    public void addEvent(Event event) {}
}
