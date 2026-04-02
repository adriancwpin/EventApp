package eventApp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    /**
     * Cancel this performance by setting its status to CANCELLED
     */
    public void cancel(){
        this.status = PerformanceStatus.CANCELLED;
    }

    /**
     * Check whether the event associated with this performance is ticketed
     * @return true if event is ticketed, false otherwise
     */
    public boolean checkIfEventIsTicketed(){
        Event event = getEvent();
        if(event == null){
            return false; //null check
        }
        return event.isTicketed();
    }

    /**
     * Check whether enough tickets are available for the requested number
     * @param numTicketsRequested the number of tickets the student wants to book
     * @return true if enough tickets, false otherwise.
     */
    public boolean checkIfTicketsLeft(int numTicketsRequested){
        return (numTicketsTotal - numTicketsSold) >= numTicketsRequested;
    }

    /**
     * Returns the final tickets of the event after applying any sponsorship discount.
     * If the performance is not sponsored, then the original ticket price is displayed
     * @return the final ticket price
     */
    public double getFinalTicketPrice() {
        if(isSponsored){
            return ticketPrice - sponsoredAmount;
        }

        return ticketPrice;
    }

    /**
     * Returns the email of the organiser of the event this performance belongs to.
     * @return the organiser email, or null if no event is set
     *
     */
    public String getOrganiserEmail(){
        if(event == null){
            return null;
        }
        return event.getOrganiserEmail();
    }

    /**
     * Returns the title of the event this performance belongs to.
     * @return the event title, or "Unknown Event" if no event is set
     */
    public String getEventTitle(){
        if(event == null){
            return "Unknown Event"; //null check
        }
        return event.getTitle();
    }

    /**
     * Checks whether this performance has not yet taken place.
     * @return true if the performance has not happened yet, false otherwise
     */
    public boolean checkHasNotHappenedYet(){
        LocalDateTime pStartDateTime = getStartDateTime();

        //if start time is before now means it already happened -> false
        if(pStartDateTime.isBefore(LocalDateTime.now())){
            return false;
        }
        return true; // has not happened
    }

    /**
     * Checks whether this performance was created by the given Entertainment Provider.
     * @param email the email of the Entertianment Provider to check against
     * @return true if organiser email matches, false otherwise.
     */
    public boolean checkCreatedByEP(String email){
        //get event -> get organiser email -> compare with the EP email
        Event event = getEvent();
        String epEmail = event.getOrganiserEmail();

        if(!email.equals(epEmail)){
            return false;
        }
        return true;
    }

    /**
     * Checks whether this performance has at least one active booking.
     * @return true if there is at least one active booking,false otherwise.
     */
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

    /**
     * Return a formatted string of booking details for all active bookings.
     * Each entry contains student emails, phone number, amount paid, and number of tickets.
     * Each variables are separated by commas, with entries separated by semicolons.
     * @return
     */
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
                String[] studentDetails = booking.getStudentDetails();
                String studentEmail = studentDetails[0];
                int studentPhone = Integer.parseInt(studentDetails[1]);

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

    /**
     * Sponsor this performance by setting the sponsored flag and storing the discount amount
     * @param amount the amount to deduct from the ticket price.
     */
    public void sponsor(double amount){
        this.isSponsored = true;
        this.sponsoredAmount = amount;
    }

    /**
     * Checks whether the given student has an active booking for this performance
     * @param email the email of the student to check
     * @return true if the student has an active booking, false otherwise
     */
    //check if any booking in performance belongs to student
    public boolean checkBookedPerfByStudent(String email){
        for(Booking booking: bookings){
            //check if booking belongs to the student and its active
            if(booking.checkBookedByStudent(email) && booking.getStatus() == BookingStatus.ACTIVE){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a rating and optional comment as a review for this performance.
     * @param rating the rating given by the student, from 1-10
     * @param comment an optional comment left by the student.
     */
    public void review(int rating, String comment){
        reviewRatings.add(rating);
        reviewComments.add(comment);
    }

    /**
     * Add a booking to this performance's list of bookings.
     * @param b the booking to add
     */
    public void addBooking(Booking b){
        bookings.add(b);
    }

    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Performance ID: " + performanceID +
                " | Start: " + startDateTime.format(formatter) +
                " | End: " + endDateTime.format(formatter) +
                " | Venue: " + venueAddress +
                " | Performers: " + performerName+
                " | Capacity: " + venueCapacity +
                " | Number Ticket Available: " + (numTicketsTotal - numTicketsSold) +
                " | Sponsored Amount: £" + sponsoredAmount +
                " | Ticket Price: £" + getFinalTicketPrice() +
                " | Status: " + status;
    }


}
