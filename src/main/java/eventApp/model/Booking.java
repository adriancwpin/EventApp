package eventApp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    //Methods

    /**
     * Cancels this booking when the student requests cancellation
     * Set the booking status to CANCELLEDBYSTUDENT.
     */
    public void cancelByStudent(){
        this.status = BookingStatus.CANCELLEDBYSTUDENT;
    }

    /**
     * Cancels this booking when payment fails during the booking process.
     * Sets the booking status to CANCELLEDPAYMENTFAILED.
     */
    public void cancelPaymentFailed(){
        this.status = BookingStatus.PAYMENTFAILED;
    }

    /**
     * Cancels this booking when the Entertainment Provider cancels the performance.
     * Sets the booking status to CANCELLEDBYPROVIDER.
     */
    public void cancelByProvider(){
        this.status = BookingStatus.CANCELLEDBYPROVIDER;
    }

    /**
     * Checks whether this booking was made by the student with the given email.
     *
     * @param email the email of the student to check against
     * @return true if the booking belongs to the student, false otherwise
     */
    public boolean checkBookedByStudent(String email){
        return student.getEmail().equals(email);
    }

    /**
     * Returns the student's email and phone number as a string array.
     * Returns empty strings if no student is associated with this booking.
     *
     * @return a string array where index 0 is the email and index 1 is the phone number
     */
    public String[] getStudentDetails(){
        if(student == null){
            return new String[]{"", "0"};
        }
        return new String[]{student.getEmail().trim(), String.valueOf(student.getPhoneNumber()).trim()};
    }

    /**
     * Generates a formatted booking record summarising the booking details.
     * Includes booking number, performance title, date, venue, tickets, amount paid,
     * booking date, and current status.
     *
     * @return a formatted string representing the booking record
     */
    public String generateBookingRecord() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();

        sb.append("==============================\n");
        sb.append("       BOOKING RECORD         \n");
        sb.append("==============================\n");
        sb.append("Booking Number: ").append(bookingNumber).append("\n");
        sb.append("Performance:    ").append(performance.getEventTitle()).append("\n");
        sb.append("Date & Time:    ").append(performance.getStartDateTime().format(formatter)).append("\n");
        sb.append("Venue:          ").append(performance.getVenueAddress()).append("\n");
        sb.append("Tickets:        ").append(numTickets).append("\n");
        sb.append("Amount Paid:    £").append(String.format("%.2f", amountPaid)).append("\n");
        sb.append("Booked On:      ").append(bookingDateTime.format(formatter)).append("\n");
        sb.append("Status:         ").append(status).append("\n");
        sb.append("==============================");

        return sb.toString();
    }
}
