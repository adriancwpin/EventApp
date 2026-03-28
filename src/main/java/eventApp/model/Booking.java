package eventApp.model;

import java.time.LocalDateTime;
import eventApp.enums.BookingStatus;

public class Booking {
    private long bookingNumber;
    private int numTickets;
    private double amountPaid;
    private LocalDateTime bookingDateTime;
    private BookingStatus status;
    private Student student; //Booking need reference to Student
    private Performance performance; //store performance reference in booking

    public Booking(long bookingNumber, int numTickets, double amountPaid, LocalDateTime bookingDateTime) {
        this.bookingNumber = bookingNumber;
        this.numTickets = numTickets;
        this.amountPaid = amountPaid;
        this.bookingDateTime = bookingDateTime;
        this.status = BookingStatus.ACTIVE;
    }

    public long getBookingNumber() {
        return bookingNumber;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Performance getPerformance(){
        return performance;
    }

    //setters
    public void setStudent(Student student) {
        this.student = student;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public void cancelByStudent(){
        this.status = BookingStatus.CANCELLEDBYSTUDENT;
    }

    public void cancelPaymentFailed(){}

    public void cancelByProvider(){
        this.status = BookingStatus.CANCELLEDBYPROVIDER;
    }

    public boolean checkBookedByStudent(String email){
        return student.getEmail().equals(email);
    }

    public String getStudentDetails(){
        return student.getEmail() + "," + student.getPhoneNumber();
    }

    public String generateBookingRecord(){
        return "Booking Number: " + bookingNumber + "\n" +
                "Booking Date: " + bookingDateTime + "\n" +
                "Number of Tickets: " + numTickets + "\n" +
                "Amount Paid: £" + amountPaid + "\n" +
                "Booking DateTime: " + bookingDateTime + "\n" +
                "Status: " + status;
    }
}
