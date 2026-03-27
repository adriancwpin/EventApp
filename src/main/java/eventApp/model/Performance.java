package eventApp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import eventApp.enums.PerformanceStatus;

public class Performance {
    private long performanceID;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Collection<String> performerName;
    private String venueAddress;
    private int venueCapacity;
    private boolean venueIsOutdoors;
    private boolean venueAllowsSmoking;
    private int numTicketsTotal;
    private int numTicketsSold;
    private double ticketPrice;
    private boolean isSponsored;
    private double sponsoredAmount;
    private Collection<String> reviewComments;
    private Collection<Integer> reviewRatings;
    private PerformanceStatus status;
    private Event event;

    public Performance(long performanceID, LocalDateTime startDateTime, LocalDateTime endDateTime, Collection<String> performerName, String venueAddress, int venueCapacity,
                       boolean venueIsOutdoors, boolean venueAllowsSmoking, int numTicketsTotal, double ticketPrice) {
        this.performanceID = performanceID;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.performerName = performerName;
        this.venueAddress = venueAddress;
        this.venueCapacity = venueCapacity;
        this.venueIsOutdoors = venueIsOutdoors;
        this.venueAllowsSmoking = venueAllowsSmoking;
        this.numTicketsTotal = numTicketsTotal;
        this.numTicketsSold = 0;
        this.ticketPrice = ticketPrice;
        this.isSponsored = false;
        this.sponsoredAmount = 0.0;
        this.reviewComments = new ArrayList<>();
        this.reviewRatings = new ArrayList<>();
        this.status = PerformanceStatus.ACTIVE;
    }

    //getters
    public long getPerformanceID() {
        return performanceID;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public Collection<String> getPerformerName() {
        return performerName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public int getVenueCapacity() {
        return venueCapacity;
    }

    public boolean isVenueIsOutdoors() {
        return venueIsOutdoors;
    }

    public boolean isVenueAllowsSmoking() {
        return venueAllowsSmoking;
    }

    public int getNumTicketsTotal() {
        return numTicketsTotal;
    }

    public int getNumTicketsSold() {
        return numTicketsSold;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public boolean isSponsored() {
        return isSponsored;
    }

    public double getSponsoredAmount() {
        return sponsoredAmount;
    }

    public Collection<String> getReviewComments() {
        return reviewComments;
    }

    public Collection<Integer> getReviewRatings() {
        return reviewRatings;
    }

    public PerformanceStatus getStatus() {
        return status;
    }

    public void setNumTicketsSold(int numTicketsSold) {
        this.numTicketsSold = numTicketsSold;
    }

    //methods
    public void cancel(){}

    public boolean checkIfEventIsTicketed(){
        Event event = getEvent();
        return event.isTicketed();
    }

    public boolean checkIfTicketsLeft(int numTicketsRequested){
        return (numTicketsTotal - numTicketsSold) >= numTicketsRequested;
    }

    public double getFinalTicketPrice() {
        return ticketPrice;
    }

    public String getOrganiserEmail(){
        return event.getOrganiserEmail();
    }

    public String getEventTitle(){
        return event.getTitle();
    }

    public boolean checkHasNotHappenedYet(){
        return false;
    }

    public boolean checkCreatedByEP(String email){
        return false;
    }

    public boolean hasActiveBookings(){
        return false;
    }

    public String getBookingDetailsForRefund(){
        return null;
    }

    public void sponsor(double amount){}

    public void review(int rating, String comment){}

    public void addBooking(Booking b){}

    public String toString(){
        return "Performance ID: " + performanceID +
                " | Start: " + startDateTime +
                " | End: " + endDateTime +
                " | Venue: " + venueAddress +
                " | Capacity: " + venueCapacity +
                " | Number Ticket Available: " + (numTicketsTotal - numTicketsSold) +
                " | Ticket Price: " + ticketPrice +
                " | Status: " + status;
    }


}
