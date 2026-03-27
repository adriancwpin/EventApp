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

    public Booking(long bookingNumber, int numTickets, double amountPaid, LocalDateTime bookingDateTime, BookingStatus status) {
        this.bookingNumber = bookingNumber;
        this.numTickets = numTickets;
        this.amountPaid = amountPaid;
        this.bookingDateTime = bookingDateTime;
        this.status = status;
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

    //setters
    public void setStudent(Student student) {
        this.student = student;
    }

    public void cancelByStudent(){}

    public void cancelPaymentFailed(){}

    public void cancelByProvider(){
        this.status = BookingStatus.CANCELLEDBYPROVIDER;
    }

    public boolean checkBookedByStudent(String email){
        return false;
    }

    public String getStudentDetails(){
        return student.getEmail() + "," + student.getPhoneNumber();
    }

    public String generateBookingRecord(){
        return "Booking Number: " + bookingNumber + "\n" +
                "Number of Tickets: " + numTickets + "\n" +
                "Amount Paid: £" + amountPaid + "\n" +
                "Booking DateTime: " + bookingDateTime + "\n" +
                "Status: " + status;
    }
}
