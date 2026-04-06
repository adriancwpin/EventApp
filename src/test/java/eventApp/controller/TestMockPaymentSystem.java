package eventApp.controller;

import eventApp.external.MockPaymentSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class TestMockPaymentSystem {
    private MockPaymentSystem mockPaymentSystem;

    private String studentEmail = "student@test.com";
    private int studentPhone = 1234567890;
    private String epEmail = "ep@test.com";
    private String eventTitle = "Test";

    @BeforeEach
    void setUp() {
        mockPaymentSystem = new MockPaymentSystem();
    }

    //Process payment
    @Test
    @DisplayName("processPayment() succeeds with valid input")
    void processPaymentSuccess() {
        boolean succeed = mockPaymentSystem.processPayment(1,eventTitle,studentEmail,studentPhone,epEmail,10);
        assertTrue(succeed,
                "processPayment() succeeds with valid input");
    }

    @Test
    @DisplayName("processPayment() fails when numTicket is 0")
    void processPaymentFailsWhenNumTicketIsZero() {
        boolean result = mockPaymentSystem.processPayment(0,eventTitle,studentEmail,studentPhone,epEmail,10);
        assertFalse(result, "processPayment() fails when numTicket is 0");
    }

    @Test
    @DisplayName("processPayment fails when the numTicket is negative")
    void processPaymentFailsWhenTheNumTicketIsNegative() {
        boolean result = mockPaymentSystem.processPayment(-1,eventTitle,studentEmail,studentPhone,epEmail,10);
        assertFalse(result, "processPayment() fails when the numTicket is negative");
    }

    @Test
    @DisplayName("processPayment() fails when the transaction amount is 0")
    void processPaymentFailsWhenTheTransactionAmountIsZero() {
        boolean result  = mockPaymentSystem.processPayment(1,eventTitle,studentEmail,studentPhone,epEmail,0);
        assertFalse(result, "processPayment() fails when the transaction amount is 0");
    }

    @Test
    @DisplayName("processPayment() fails when the transaction amount is negative")
    void processPaymentFailsWhenTheTransactionAmountIsNegative() {
        boolean result = mockPaymentSystem.processPayment(1,eventTitle,studentEmail,studentPhone,epEmail,-10);
        assertFalse(result, "processPayment() fails when the transaction amount is negative");
    }

    @Test
    @DisplayName("processPayment() fails when the student email is null")
    void processPaymentFailsWhenTheStudentEmailIsNull() {
        boolean result = mockPaymentSystem.processPayment(1,eventTitle,null,studentPhone,epEmail,10);
        assertFalse(result, "processPayment() fails when the student email is null");
    }

    @Test
    @DisplayName("processPayment() fails when the event title is null")
    void processPaymentFailsWhenTheEventTitleIsNull() {
        boolean result = mockPaymentSystem.processPayment(1, null, studentEmail,studentPhone,epEmail,10);
        assertFalse(result, "processPayment() fails when the event title is null");
    }

    @Test
    @DisplayName("processPayment() fails when the ep email is null")
    void processPaymentFailsWhenTheEpEmailIsNull() {
        boolean result = mockPaymentSystem.processPayment(1,eventTitle,eventTitle,studentPhone,null,10);
        assertFalse(result, "processPayment() fails when the ep email is null");
    }

    @Test
    @DisplayName("processPayment() succeeds when the student email is empty ")
    void processPaymentSucceedsWhenTheStudentEmailIsEmpty() {
        boolean succeed = mockPaymentSystem.processPayment(1,eventTitle,"",studentPhone,epEmail,10);
        assertTrue(succeed, "processPayment() succeeds when the student email is empty because mock payment only check null");
    }

    @Test
    @DisplayName("processPayment() succeeds when the event title is empty ")
    void processPaymentSucceedsWhenTheEventTitleIsEmpty() {
        boolean succeed = mockPaymentSystem.processPayment(1,"",studentEmail,studentPhone,epEmail,10);
        assertTrue(succeed, "processPayment() succeeds when the event title is empty because mock payment only check null");
    }

    @Test
    @DisplayName("processPayment() succeeds when the ep email is empty ")
    void processPaymentSucceedsWhenTheEpEmailIsEmpty() {
        boolean succeed = mockPaymentSystem.processPayment(1,eventTitle,studentEmail,studentPhone,"",10);
        assertTrue(succeed, "processPayment() succeeds when the ep email is empty because mock payment only check null");
    }

    @Test
    @DisplayName("processPayment() succeeds with large number of tickets")
    void processPaymentSucceedsWithLargeNumberOfTickets() {
        boolean succeed = mockPaymentSystem.processPayment(1000,eventTitle,studentEmail,studentPhone,epEmail,10000);
        assertTrue(succeed, "processPayment() succeeds with large number of tickets");
    }

    @Test
    @DisplayName("processPayment() succeeds with decimal ticket price")
    void processPaymentSucceedsWithDecimalTicketPrice() {
        boolean succeed = mockPaymentSystem.processPayment(1,eventTitle,studentEmail,studentPhone,epEmail,6.70);
        assertTrue(succeed, "processPayment() succeeds with decimal ticket price");
    }

    //processRefund()
    @Test
    @DisplayName("processRefund() succeeds with valid input")
    void processPaymentSucceedsWithValidInput() {
        boolean succeed = mockPaymentSystem.processRefund(1, eventTitle, studentEmail, studentPhone, epEmail, 10, "cancelled");
        assertTrue(succeed, "processRefund() succeeds with valid input");
    }

    @Test
    @DisplayName("processRefund() fails when the studentEmail is null")
    void processRefundFailsWhenTheStudentEmailIsNull() {
        boolean result = mockPaymentSystem.processRefund(1, eventTitle, null, studentPhone, epEmail, 10, "cancelled");
        assertFalse(result, "processRefund() fails when the student email is null");
    }

    @Test
    @DisplayName("processRefund() fails when the event title is null")
    void processRefundFailsWhenTheEventTitleIsNull() {
        boolean result = mockPaymentSystem.processRefund(1, null, studentEmail, studentPhone, epEmail, 10, "cancelled");
        assertFalse(result, "processRefund() fails when the event title is null");
    }

    @Test
    @DisplayName("processRefund() fails when the ep email is null")
    void processRefundFailsWhenTheEpEmailIsNull() {
        boolean result = mockPaymentSystem.processRefund(1, eventTitle, studentEmail, studentPhone, null, 10, "cancelled");
        assertFalse(result, "processRefund() fails when the ep email is null");
    }

    @Test
    @DisplayName("processRefund() fails when the number of ticket is 0")
    void processRefundFailsWhenTheNumberOfTicketIsZero() {
        boolean result = mockPaymentSystem.processRefund(0, eventTitle, studentEmail, studentPhone, epEmail, 10, "cancelled");
        assertFalse(result, "processRefund() fails when the number of ticket is 0");
    }

    @Test
    @DisplayName("processRefund() fails when the number of ticket is negative")
    void processRefundFailsWhenTheNumberOfTicketIsNegative() {
        boolean result = mockPaymentSystem.processRefund(-1, eventTitle, studentEmail, studentPhone, epEmail, 10, "cancelled");
        assertFalse(result, "processRefund() fails when the number of ticket is negative");
    }

    @Test
    @DisplayName("processRefund() fails when the transaction amount is zero")
    void processRefundFailsWhenTheTransactionAmountIsZero() {
        boolean result = mockPaymentSystem.processRefund(1, eventTitle, studentEmail, studentPhone, epEmail, 0, "cancelled");
        assertFalse(result, "processRefund() fails when the transaction amount is zero");
    }

    @Test
    @DisplayName("processRefund() fails when the transaction amount is negative")
    void processRefundFailsWhenTheTransactionAmountIsNegative() {
        boolean result = mockPaymentSystem.processRefund(1, eventTitle, studentEmail, studentPhone, epEmail, -10, "cancelled");
        assertFalse(result, "processRefund() fails when the transaction amount is negative");
    }

    @Test
    @DisplayName("processRefund() succeeds when the student email is empty")
    void processRefundSucceeedsWhenTheStudentEmailIsEmpty() {
        boolean succeed = mockPaymentSystem.processRefund(1, eventTitle, "", studentPhone, epEmail, 10, "cancelled");
        assertTrue(succeed, "processRefund() succeeeds when the student email is empty because mock payment only check null");
    }

    @Test
    @DisplayName("processRefund() succeeds when the event title is empty")
    void processRefundSucceeedsWhenTheEventTitleIsEmpty() {
        boolean succeed = mockPaymentSystem.processRefund(1, "", studentEmail, studentPhone, epEmail, 10, "cancelled");
        assertTrue(succeed, "processRefund() succeeds when the event title is empty because mock payment only check null");
    }

    @Test
    @DisplayName("processRefund() succeeds when the ep email is empty")
    void processRefundSucceeedsWhenTheEpEmailIsEmpty() {
        boolean succeed = mockPaymentSystem.processRefund(1, eventTitle, studentEmail, studentPhone, "", 10, "cancelled");
        assertTrue(succeed, "processRefund() succeeds when the ep email is empty because mock payment only check null");
    }

    @Test
    @DisplayName("processRefund() succeeds when the message is empty")
    void processRefundSucceeedsWhenTheMessageIsEmpty() {
        boolean succeed = mockPaymentSystem.processRefund(1, eventTitle, studentEmail, studentPhone, epEmail, 10, "");
        assertTrue(succeed, "processRefund() succeeds when the message is empty");
    }

    @Test
    @DisplayName("processRefund() succeeds with decimal transaction amount")
    void processRefundSucceeedsWithDecimalTransactionAmount() {
        boolean succeed = mockPaymentSystem.processRefund(1, eventTitle, studentEmail, studentPhone, epEmail, 6.70, "cancelled");
        assertTrue(succeed, "processRefund() succeeds with decimal transaction amount");
    }
}
