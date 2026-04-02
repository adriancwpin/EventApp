package eventApp.model;

import eventApp.enums.EventType;

import java.util.ArrayList;
import java.util.Collection;
import java.time.LocalDateTime;
import java.util.List;

public class Event {
    private long eventID;
    private String title;
    private EventType type;
    private boolean isTicketed;
    private List<Performance> performances;
    private String organiserEmail;
    private String organiserName;

    public Event(long eventID, String title, EventType type, boolean isTicketed,String organiserEmail, String organiserName) {
        this.eventID = eventID;
        this.title = title;
        this.type = type;
        this.isTicketed = isTicketed;
        this.performances = new ArrayList<Performance>();
        this.organiserEmail = organiserEmail;
        this.organiserName = organiserName;
    }

    public long getEventID() { return eventID; }
    public String getTitle() { return title; }
    public EventType getType() { return type; }
    public boolean isTicketed() { return isTicketed; }

    //Methods

    /**
     * Create a new Performance for this event and adds it to the performance collection list.
     *
     * @param performanceID the unique ID for this performance
     * @param startDateTime the start date and time of the performance
     * @param endDateTime the end date and time of the performance
     * @param performerNames the names of the performers
     * @param venueAddress the address of the venue
     * @param venueCapacity the maximum capacity of the venue
     * @param venueisOutdoors whether the venue is outdoors
     * @param venueAllowsSmoking whether smoking is allowed
     * @param numTickets number of tickets available
     * @param ticketPrice price of the ticket
     * @return null if a performance already exists at the same start and end times, otherwise p.
     */
    public Performance createPerformance(long performanceID, LocalDateTime startDateTime, LocalDateTime endDateTime, Collection<String> performerNames,
                                         String venueAddress, int venueCapacity, boolean venueisOutdoors, boolean venueAllowsSmoking,
                                         int numTickets, double ticketPrice ) {

        //check for time clash
        if(hasPerformanceAtSameTimes(startDateTime, endDateTime)) {
            return null;
        }

       Performance p = new Performance(performanceID, startDateTime, endDateTime, performerNames, venueAddress,
               venueCapacity, venueisOutdoors, venueAllowsSmoking, numTickets, ticketPrice);

       p.setEvent(this); //so the performance know which event it belongs to
       addPerformance(p);
       return p;
    }

    /**
     * Returns the specific performance based on the id
     *
     * @param performanceID unique identification of the performance
     * @return the performance if the performance matches the id, null otherwise
     */
    public Performance getPerformanceByID(long performanceID) {
        for(Performance p : performances) {
            if(p.getPerformanceID() == performanceID) {
                return p;
            }
        };
        return null;
    }

    /**
     * Returns a collection of summary strings for all performances on the given date.
     * Each string contains the performance details formatted for display.
     *
     * @param searchDateTime  date to search for performances on
     * @return a collection of performance summary strings, empty if none found
     */
    public Collection<String> getInfoOfPerformancesOnDate(LocalDateTime searchDateTime) {
        Collection<String> searched = new ArrayList<>();

        for (Performance p : performances) {
            //check if performance is on the same date
            if(p.getStartDateTime().toLocalDate().equals(searchDateTime.toLocalDate())) {
                searched.add(p.toString());
            }
        }
        return searched;
    }

    private String getOrganiserName(){
        return organiserName;
    };

    /**
     * Returns the email of the organiser of this event.
     *
     * @return the organiser email
     */
    public String getOrganiserEmail(){
        return organiserEmail;
    }

    /**
     * Calculates and returns the average rating across all reviews of all performances.
     * Returns 0.0 if no reviews have been submitted.
     *
     * @return the average rating as a double
     */
    public double getAverageRatingOfPerformances(){
        int totalRating = 0;
        int i = 0;

        for (Performance p : performances) {
            for(int rating : p.getReviewRatings()){
                totalRating += rating;
                i ++;
            }
        }
        //prevent zero division
        if(i == 0){
            return 0.0;
        }

        return (double) totalRating / i;
    }

    /**
     * Returns a collection of all review strings across all performances of this event.
     * Each string includes the performance ID, rating, and comment.
     *
     * @return a collection of review strings, empty if no reviews exist
     */
    public Collection<String> getAllPerformanceReviews(){
        Collection<String> reviews = new ArrayList<>();

        for (Performance p : performances) {
            List<Integer> ratings = new ArrayList<>(p.getReviewRatings());
            List<String> comments = new ArrayList<>(p.getReviewComments());

            for(int i = 0; i < ratings.size(); i++){
                reviews.add("Performance ID: " + p.getPerformanceID() +
                        " Rating: " + ratings.get(i) +
                        " Comment: " + comments.get(i));
            }
        }
        return reviews;
    }

    private boolean hasPerformanceAtSameTimes(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        for (Performance p : performances) {
            //check if existing performance has the same time slot as the previous performance
            if(p.getStartDateTime().equals(startDateTime) && p.getEndDateTime().equals(endDateTime)) {
                return true; //clash found
            }
        }
        return false;
    }
    /**
     * Checks whether an event with the same title already has a performance at the same time.
     * Used to prevent duplicate performances being created for the same event.
     *
     * @param title the title to check against
     * @param startDateTime the start time to check
     * @param endDateTime the end time to check
     * @return true if the title matches and a time clash exists, false otherwise
     */
    //helper function to check if there is any time clash within the same event -> 2 same event titles
    public boolean hasSameTitleAndTime (String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        //check same title
        if(!this.title.equalsIgnoreCase(title)) {
            return false;
        }

        //check time clashes
        return hasPerformanceAtSameTimes(startDateTime, endDateTime);
    }

    private void addPerformance(Performance p){
        performances.add(p);
    }

    public String toString() {
        return null;
    }
}
