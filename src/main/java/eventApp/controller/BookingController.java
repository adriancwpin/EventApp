package eventApp.controller;

import eventApp.enums.BookingStatus;
import eventApp.external.PaymentSystem;
import eventApp.model.*;
import eventApp.view.*;

import java.time.LocalDateTime;
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
                long performanceID = Long.parseLong(view.getInput("Enter booking info (Performance ID): "));
                numTicketsReq = Integer.parseInt(view.getInput("Enter booking info (Number of tickets): "));

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
                LocalDateTime.now(), BookingStatus.ACTIVE);

        nextBookingNumber++;

        booking.setStudent(student);

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
    }

    public void reviewPerformance(){}

    public void cancelBooking(){}

    private void addBooking(Booking b){}

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
        return null;
    }

    private Booking getBookingByNumber(long bookingNumber){
        return null;
    }
}
