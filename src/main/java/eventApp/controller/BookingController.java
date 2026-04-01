package eventApp.controller;

import eventApp.enums.BookingStatus;
import eventApp.external.PaymentSystem;
import eventApp.model.*;
import eventApp.view.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;


public class BookingController extends Controller {
    private long nextBookingNumber;

    private Collection<Booking> bookings;
    private Collection<Performance> performances;
    private PaymentSystem paymentSystem;

    //Constructor
    public BookingController(Collection<Booking> bookings, Collection<Performance> performances,View view,
                             PaymentSystem paymentSystem) {
        this.nextBookingNumber = 1;
        this.bookings = bookings;
        this.view = view;
        this.performances = performances;
        this.paymentSystem = paymentSystem;
    }

    //getters
    public User getCurrentUser() {
        return currentUser;
    }

    //Methods
    public void bookPerformance(){
        Performance performance = null;
        int numTicketsReq = 0;

        //Loop until the performance is found
        while (performance == null || !checkIfBookingPossible(performance, numTicketsReq)) {
            try{
                String input1 = view.getInput("Enter Performance ID (or '-1' to return back to dashboard): ");
                String input2 = view.getInput("Enter Number of tickets (or '-1' to return back to dashboard): ");
                long performanceID = Long.parseLong(input1);
                numTicketsReq = Integer.parseInt(input2);

                if(input1.trim().equals("-1") || input2.trim().equals("-1")){
                    return;
                }

                //get performance id
                performance = getPerformanceByID(performanceID);

                //if null then display error and loop again
                if(performance == null){
                    view.displayError("Performance with this ID does not exists.");
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid performance ID. Please enter a valid number.");
                performance = null;
            }
        }

        //get current user as student
        Student student = (Student) currentUser;

        //create booking
        Booking booking = new Booking (nextBookingNumber, numTicketsReq, performance.getFinalTicketPrice() * numTicketsReq,
                LocalDateTime.now());

        nextBookingNumber++;

        booking.setStudent(student);
        booking.setPerformance(performance);

        addBooking(booking);

        //add booking to performance and students
        performance.addBooking(booking);
        student.addBooking(booking);

        //collect info for payment
        String eventTitle = performance.getEventTitle();
        String studentEmail = student.getEmail();
        int studentPhone = student.getPhoneNumber();
        String epEmail = performance.getOrganiserEmail();
        double transactionAmount = performance.getFinalTicketPrice() * numTicketsReq;

        //process payment
        boolean paymentSuccess = paymentSystem.processPayment(numTicketsReq, eventTitle, studentEmail, studentPhone, epEmail, transactionAmount);

        if(!paymentSuccess){
            view.displayError("There was an issue with payment.");
            booking.cancelPaymentFailed();
            return;
        }

        //update tickets sold
        int numTicketsSold = performance.getNumTicketsSold();

        performance.setNumTicketsSold(numTicketsSold + numTicketsReq);

        //display success
        view.displaySuccess("Booking Successful");
        String bookingRecord = booking.generateBookingRecord();
        view.displayBookingRecord(bookingRecord);
        view.getInput("Press ENTER to return to dashboard... \n");
    }

    public void reviewPerformance(){
        //check if the current user is student
        if(!checkCurrentUserIsStudent()){
            view.displayError("Only student can review performances.");
            return;
        }
        Student student = (Student) currentUser;

        //find a valid performance id
        //check if the student booked this performance
        //ask for rating
        //ask for optional comment
        //success
        Performance performance = null;
        while(performance == null){
            try{
                String input = view.getInput("Enter Performance ID to review (or '-1' to return back to dashboard): ");

                if(input.trim().equals("-1")){
                    return; //return back to dashboard
                }

                long performanceID = Long.parseLong(view.getInput(input.trim()));
                performance = getPerformanceByID(performanceID);


                //performance not found
                if(performance == null){
                    view.displayError("Couldn't find any peformance with this performance ID.");
                    continue;
                }

                //check if the performance has happened
                if(performance.checkHasNotHappenedYet()){
                    view.displayError("You can only review the performance that has already taken place.");
                    performance = null;
                    continue;
                }

                //chcek if the student booked that performance
                if(!performance.checkBookedPerfByStudent(student.getEmail())){
                    view.displayError("Only student that booked the performance is eligible to review.");
                    performance = null;
                    continue;
                }

            }catch(NumberFormatException e){
                view.displayError("Invalid input. Please enter a number.");
                performance = null;
            }
        }

        //ask for rating
        int rating = 0;

        while(rating == 0){
            try{
                rating = Integer.parseInt(view.getInput("Enter rating (1-10): "));
                if(rating < 1 || rating > 10){
                    view.displayError(" Rating must be betweem 1 and 5");
                    rating = 0;
                    continue;
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid input. Please enter a number.");
                rating = 0;
            }
        }

        //ask for comment (optional)
        String comment = view.getInput("Comment on the performance (press enter to skip):  ");

        //add review to performance
        performance.review(rating, comment);

        view.displaySuccess("Review submitted successfully!");
        view.getInput("Press ENTER to return to dashboard... \n");
    }

    public void cancelBooking() {
        //has to be student
        if (!checkCurrentUserIsStudent()) {
            view.displayError("Only student can cancel booking.");
            return;
        }
        //ask for booking number
        Student student = (Student) currentUser;
        Booking booking = null;

        while (booking == null) {
            try {
                String input = view.getInput("Enter Booking Number (or '-1' to return back to dashboard: ");

                if(input.trim().equals("-1")){
                    return;
                }

                long bookingNumber = Long.parseLong(view.getInput(input.trim()));
                booking = getBookingByNumber(bookingNumber);

                if (booking == null) {
                    view.displayError("This booking number does not exists.");
                    continue;
                }
                //1a. booking does not belong to student
                if (!booking.checkBookedByStudent(student.getEmail())) {
                    view.displayError("This booking number does not belong to you. Please try again.");
                    booking = null;
                    continue;
                }

                // check if the booking still active
                if (booking.getStatus() != BookingStatus.ACTIVE) {
                    view.displayError("This booking has been cancelled. Sorry for the inconvenience.");
                    booking = null;
                    continue;
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid booking number. Please enter a valid number.");
                booking = null;
            }
        }

        //get associated performance once we get the booking ID
        Performance performance = booking.getPerformance();

        //1b. check if the booking is less than 24 hrs away
        LocalDateTime performanceTime = performance.getStartDateTime();
        LocalDateTime now = LocalDateTime.now();

        if (performanceTime.isBefore(now.plusHours(24))) {
            view.displayError("Cancellation is not allowed. The performance is less than 24 hours away.");
            return;
        }

        //refund
        String eventTitle = performance.getEventTitle();
        String studentEmail = student.getEmail();
        int studentPhone = student.getPhoneNumber();
        String epEmail = performance.getOrganiserEmail();
        double transactionAmount = booking.getAmountPaid();
        int numTicketsBought = booking.getNumTickets();

        boolean refundSuccess = paymentSystem.processRefund(numTicketsBought, eventTitle, studentEmail, studentPhone,
                epEmail, transactionAmount, "Booking cancelled by student.");

        //2a. refund failed
        if (!refundSuccess) {
            view.displayError("Refund failed. Booking is not cancelled.");
            return;
        }

        //cancel booking
        booking.cancelByStudent();

        //update ticket sold
        performance.setNumTicketsSold(performance.getNumTicketsSold() - numTicketsBought);

        view.displaySuccess("Booking cancelled successfully.");
        view.getInput("Press ENTER to return to dashboard... \n");
    }

    private void addBooking(Booking b){
        bookings.add(b);
    }

    private Performance getPerformanceByID(long performanceID){
        //loop for each performance p
        for (Performance p : performances){
            //get ID of each performance
            long ID = p.getPerformanceID();

            //opt[ID === performanceID]
            if(ID == performanceID){
                return p;
            }
        }
        return null;
    }

    private boolean checkIfBookingPossible(Performance performance, int numTickets){
        //check Event is ticketed
        boolean isTicketed = performance.checkIfEventIsTicketed();

        //alt[!isTicketed]
        if(!isTicketed){
            view.displayError("The requested performance's event is not ticketed. There is no need to book it.");
            return false;
        }
        //else check number of ticket left
        boolean enoughTicketsLeft = performance.checkIfTicketsLeft(numTickets);

        //alt [!enoughTicketsLeft]
        if(!enoughTicketsLeft){
            view.displayError("Requested performance has no tickets left.");
            return false;
        }
        return true;
    }

    private Collection<Booking> findBookingsByEventID(long eventID){
        Collection<Booking> b = new ArrayList<>();
        for(Booking booking : bookings){
            if(booking.getPerformance().getEvent().getEventID() == eventID){
                b.add(booking);
            }
        }
        return b;
    }

    private Booking getBookingByNumber(long bookingNumber){
        System.out.println("DEBUG - bookings size: " + bookings.size());
        for (Booking booking: bookings){
            System.out.println("DEBUG - checking booking: " + booking.getBookingNumber());
            if(booking.getBookingNumber() == bookingNumber){
                return booking; //found the booking
            }
        }
        return null;
    }
}
