package eventApp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import eventApp.enums.BookingStatus;
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
    //Performance has 1-* relationship with Booking
    private Collection<Booking> bookings;

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
        this.bookings = new ArrayList<>();
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

    public Collection<Booking> getBookings() {
        return bookings;
    }

    //methods
    public void cancel(){
        this.status = PerformanceStatus.CANCELLED;
    }

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
        LocalDateTime pStartDateTime = getStartDateTime();

        //if start time is before now means it already happened -> false
        if(pStartDateTime.isBefore(LocalDateTime.now())){
            return false;
        }
        return true; // has not happened
    }

    public boolean checkCreatedByEP(String email){
        //get event -> get organiser email -> compare with the EP email
        Event event = getEvent();
        String epEmail = event.getOrganiserEmail();

        if(!email.equals(epEmail)){
            return false;
        }
        return true;
    }

    public boolean hasActiveBookings(){
        //iterate the bookings
        //check if at least one of them is ACTIVE
        //return
        for(Booking booking: bookings){
            if(booking.getStatus() == BookingStatus.ACTIVE){
                return true;
            }
        }
        return false;
    }

    public String getBookingDetailsForRefund(){
        //get booking
        //booking = getBooking
        //for each booking that is not cancelled
        // get student details ( using String Builder)
        //return
        StringBuilder details = new StringBuilder();

        for(Booking booking: bookings){
            //only include ACTIVE bookings that havent been cancelled
            if(booking.getStatus() == BookingStatus.ACTIVE){
                //get student details
                String studentDetails = booking.getStudentDetails();
                String [] parts = studentDetails.split(",");
                String studentEmail = parts[0];
                int studentPhone = Integer.parseInt(parts[1]);

                //append all the booking details
                details.append(studentEmail).append(",")
                        .append(studentPhone).append(",")
                        .append(booking.getAmountPaid()).append(",")
                        .append(booking.getNumTickets()).append(";");
            }
        }
        //convert StringBuilder to String
        return details.toString();
    }

    public void sponsor(double amount){}

    public void review(int rating, String comment){}

    public void addBooking(Booking b){
        bookings.add(b);
    }

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
