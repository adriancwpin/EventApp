package eventApp.controller;

import eventApp.enums.BookingStatus;
import eventApp.enums.EventType;
import eventApp.external.PaymentSystem;
import eventApp.model.*;
import eventApp.view.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class EventPerformanceController extends Controller{
    private long nextEventID;
    private long nextPerformanceID;

    private Collection<Event>events;
    private Collection<Performance> performances;
    private PaymentSystem paymentSystem;

    //Constructor
    public EventPerformanceController(Collection<Event> events, Collection<Performance> performances,View view,
                                      PaymentSystem paymentSystem) {
        this.events = events;
        this.performances = performances;
        this.view = view;
        this.paymentSystem = paymentSystem;
        this.nextEventID = 1;
        this.nextPerformanceID = 1;
    }

    //Methods

    /**
     *Creates a new event with one or more performances for the logged-in entertainment provider
     *
     * Prompt the EP for event details (title, type, is ticketed and collect
     * details for each performance (dates, venue, performers, tickets).
     * validates that end time is after start time, no time clashes, and
     * ticket count does not exceed venue capacity.
     *
     * @return the new created Event, or null if creation failed.
     */
    public Event createEvent() {
        //Check if the current user is EP
        if(!checkCurrentUserIsEntertainmentProvider()){
            view.displayError("Only Entertainment Providers can be create events.");
            return null;
        }

        EntertainmentProvider ep = (EntertainmentProvider)currentUser;

        //ask for event details(title, type, isTicketed)
        String title = null;
        while(title == null || title.trim().isEmpty()){

            title = (view.getInput("\nEnter Event Title: ").trim());

            if(title.isEmpty()){
                view.displayError("Event title cannot be empty");
                continue;
            }
        }

        //show the event type menu
        List<EventType> options = new ArrayList<>(Arrays.asList(EventType.values()));
        int choices = selectFromMenu(options, "\n=== [Select Event Type]===\n");

        EventType selectedEvent = options.get(choices - 1);

        //check if isTicketed
        String ticketed = null;
        while(ticketed == null){
            ticketed = view.getInput("\nIs this event ticketed? (Yes/No): ");
            if(!ticketed.equalsIgnoreCase("Yes") && !ticketed.equalsIgnoreCase("No")){
                view.displayError("Invalid input. Please enter Yes or No.");
                ticketed = null; //ask again
            }
        }
        boolean isTicketed = ticketed.equalsIgnoreCase("Yes");

        //create new Event with nextEVentID
        Event event = new Event(nextEventID, title, selectedEvent, isTicketed, ep.getEmail(), ep.getName());
        nextEventID++;

       //ask how many performances
        int numPerformances = 0;
        while(numPerformances <= 0){
            try{
                numPerformances = Integer.parseInt(view.getInput("\nNumber of performances: "));

                if(numPerformances <= 0){
                    view.displayError("Number of performance must be greater than 0.");
                    continue;
                }
            } catch (NumberFormatException e){
                view.displayError("Invalid input. Please enter a valid number.");
            }
        }

        //list the performances
        for (int i = 0; i < numPerformances; i++) {
            System.out.println("\n === Performance " + (i + 1) + " === \n");

            //get date , time and venue
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime startDateTime = null;
            while(startDateTime == null){
                try{
                    String start_date = view.getInput("Enter Start Date (dd/MM/yyyy): ");
                    String start_time = view.getInput("Enter Start Time (HH:mm): ");
                    startDateTime = LocalDateTime.parse(start_date + " " + start_time, formatter);
                } catch (DateTimeParseException e){
                    view.displayError("Invalid date/time format. Please use dd/MM/yyyy HH:mm format.");
                }
            }

            LocalDateTime endDateTime = null;
            while(endDateTime == null){
                try{
                    String end_date = view.getInput("Enter End Date (dd/MM/yyyy): ");
                    String end_time = view.getInput("Enter End Time (HH:mm): ");
                    endDateTime = LocalDateTime.parse(end_date + " " + end_time, formatter);
                    if(endDateTime.isBefore(startDateTime)){
                        view.displayError("End time must be after start time!");
                        endDateTime = null;
                    }
                } catch (DateTimeParseException e){
                    view.displayError("Invalid date/time format. Please use yyyy-MM-ddTHH:mm format.");
                }
            }

            //check if there is overlapping event title and time clashes
            Event existingEvent = getEventByTitle(title);

            if(existingEvent != null &&
                    existingEvent.hasSameTitleAndTime(title, startDateTime, endDateTime)){
                view.displayError("An event with this name has already exists at the same time!");
                i--; //ask again
                continue;
            }

            //get venue
            String venueAddress = null;
            while(venueAddress == null || venueAddress.trim().isEmpty()){
                venueAddress = view.getInput("\nEnter Venue Address: ").trim();

                if(venueAddress.isEmpty()){
                    view.displayError("Venue Address cannot be empty.");
                    continue;
                }
            }
            int venueCapacity = 0;
            while(venueCapacity <= 0){
                try{
                    venueCapacity = Integer.parseInt(view.getInput("\nEnter Venue Capacity: "));
                    if(venueCapacity <= 0){
                        view.displayError("Capacity must be greater than 0.");
                        continue; //ask again
                    }
                } catch (NumberFormatException e){
                    view.displayError("Please enter a valid number.");
                }
            }

            boolean venueIsOutdoors = getYesNo("\nIs the venue outdoor? (Yes/No): ");
            boolean venueAllowsSmoking = getYesNo("\nIs smoking allowed? (Yes/No): ");

            //get performer names
            int numPerformers = 0;
            while (numPerformers <= 0){
                try {
                    numPerformers = Integer.parseInt(view.getInput("\nNumber of performers: "));

                    if(numPerformers <= 0){
                        view.displayError("Number of performer must be greater than 0");
                        continue;
                    }
                } catch (NumberFormatException e){
                    view.displayError("Invalid input. Please enter a valid number.");
                }
            }
            Collection<String> performerNames = new ArrayList<>();

            for(int j = 0; j < numPerformers; j++){
                performerNames.add(view.getInput("\nEnter Performance " + (j + 1) + " name: "));
            }

            //ticket details if ticketed
            int numTickets = 0;
            double ticketPrice = 0.0;

            if(isTicketed){
                while (numTickets <= 0 || ticketPrice <= 0 || numTickets > venueCapacity){
                    try{
                        numTickets = Integer.parseInt(view.getInput("\nNumber of tickets: "));
                        ticketPrice = Double.parseDouble(view.getInput("\nEnter Ticket Price: "));

                        if(numTickets <= 0){
                            view.displayError("Number of ticket must be at least 1.");
                            continue;
                        } else if (numTickets > venueCapacity) {
                            view.displayError("Number of ticket must be less than venueCapacity.");
                            continue;
                        }

                        if(ticketPrice <= 0){
                            view.displayError("Price of the ticket must be greater than 0");
                            continue;
                        }
                    }catch (NumberFormatException e){
                        view.displayError("Invalid input. Please enter a number.");
                    }
                }
            }

            //create performance
            Performance performance = event.createPerformance(nextPerformanceID, startDateTime, endDateTime, performerNames,
                    venueAddress, venueCapacity, venueIsOutdoors, venueAllowsSmoking, numTickets, ticketPrice);

            //check time clashes
            if(performance == null){
                view.displayError("A performance already exists at this time");
                i--; //ask again
                continue;
            }

            nextPerformanceID++;
            addPerformance(performance);
        }

        //add event to collection
        addEvent(event);
        ep.addEvent(event);

        view.displaySuccess("\nEvent '" + title + "' created successfully! \n");

        //press enter to return to dashboard
        view.getInput("Press ENTER to return to dashboard...\n");
        return event;
    }

    /**
     * Searches for performance on a given date across all events in the system.
     *
     * Prompts the user to enter a date and displays all performance scheduled
     * on that date. If the current user is a student, results are sorted so that performances matching their preferences
     * appear first.
     *
     * Display an error if no performances are found on the given date.
     */
    public void searchForPerformances(){
        //ask for a date to search
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime searchDate = null;
        while(searchDate == null){
            try{
                String date = view.getInput("\nEnter Search Date of Performance (dd/MM/yyyy): ");
                searchDate = LocalDateTime.parse(date + " 00:00", formatter);
            } catch (DateTimeParseException e){
                view.displayError("Invalid date/time format. Please use dd/MM/yyyy format.");
            }
        }
        //search all events for performances on that date
        Collection<String> searched  = new ArrayList<>();

        for (Event e: events){
            Collection<String> performanceOnDate = e.getInfoOfPerformancesOnDate(searchDate);
            searched.addAll(performanceOnDate);
        }

        //case where no performance is found
        if(searched.isEmpty()){
            view.displayError("No performances found on this date!");
            view.getInput("Press ENTER to return to dashboard...\n");
            return;
        }
        //if student then sort their preferences first
        if(checkCurrentUserIsStudent()) {
            Student student = (Student) currentUser;
            searched = sortByStudentPreferences(student, searched);
        }
        //if EP/Admin, then just show everything
        //display the performance list
        view.displayListOfPerformances(searched);
        view.getInput("Press ENTER to return to dashboard...\n");
    }

    /**
     * Display detailed information about a specific performance.
     * Prompts the user to enter a performance ID and displays full details
     * including venue, timings, performers, ticket availability, ticket price,
     * average rating, and individual reviews.
     * Displays an error if the performance ID is not found.
     * The user can enter -1 to return to the dashboard without viewing.
     */
    public void viewPerformance(){
        //give performanceID
        //if incorrect ID, system asks for it again
        Performance performance = null;
        while(performance == null){
            try{
                String input = view.getInput("\nEnter Performance ID (or '-1' to return): ");
                long performanceID = Long.parseLong(input);

                if(input.trim().equals("-1")){
                    return;
                }

                performance = getPerformanceByID(performanceID);
                if(performance == null){
                    view.displayError("Performance ID not found, please try again.");
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid ID, please enter a number.");
            }
        }
        //choose the event based on the performanceID
        Event event = getEventByPerformanceID(performance.getPerformanceID());

        //if incorrect ID, system asks for it again
        //give all the details of that specific performance
        //ticket availability, event average review, list of individual reviews
        view.displaySpecificPerformance(buildPerformanceInfo(performance, event));
        view.getInput("Press ENTER to return to dashboard...\n");
    }

    /**
     * Cancels a performance and refunds all affected students.
     *
     * Only the Entertainment Provider who owns the performance may cancel it.
     * The performance must not have already taken place. If there are active bookings,
     * refund are processed via the payment system before cancellation proceeds. A
     * cancellation message must be provided for affected students.
     *
     * Displays an error and returns early if:
     * The current user is not an Entertainment Provider
     * The performance does not belong to the logged-in EP
     * The performance has already taken place
     * A refund fails for any active booking
     *
     */
    public void cancelPerformance(){
        //only EP is allowed to cancel performance
        if(!checkCurrentUserIsEntertainmentProvider()){
            view.displayError("Only Entertainment Provider can cancel performance.");
            return;
        }

        EntertainmentProvider ep = (EntertainmentProvider) currentUser;

        //performance is null and loop until valid performance is found
        Performance performance = null;

        while(performance == null || !performance.checkCreatedByEP(ep.getEmail()) || !performance.checkHasNotHappenedYet()){
            try{
                String input = view.getInput("Enter ID of performance to cancel (or '-1' to return): ");
                if(input.trim().equals("-1")){
                    return;
                }

                long performanceID = Long.parseLong(input);
                performance = getPerformanceByID(performanceID);

                //performance not found
                if(performance == null){
                    view.displayError("Performance with given number does not exists.");
                    continue;
                }

                //check created by EP
                if(!performance.checkCreatedByEP(ep.getEmail())){
                    view.displayError("The performance with given number does not belong to you.");
                    continue;
                }

                //check if the event has happened
                boolean hasNotHappenedYet = performance.checkHasNotHappenedYet();

                if(!hasNotHappenedYet){
                    view.displayError("Performance cannot be cancelled as it has already happened.");
                    continue;
                }
            }catch (NumberFormatException e){
                view.displayError("Invalid ID, please enter a number.");
                performance = null;
            }
        }

        //organiser message is null
        String organiserMessage = null;
        while(organiserMessage == null){
            organiserMessage = view.getInput("Provide a cancellation message for affected students: ");

            if(organiserMessage == null || organiserMessage.isEmpty()){
                view.displayError("Provide a cancellation message for affected students.");
                organiserMessage = null;
            }
        }

        //check if there's any active bookings
        if(performance.hasActiveBookings()){
            //get event title and ep email
            String eventTitle = performance.getEventTitle();
            String epEmail = ep.getEmail();

            //get booking details for refund
            String bookingDetailsForRefund = performance.getBookingDetailsForRefund();

            if(!bookingDetailsForRefund.isEmpty()) {
                //Split ";" to get each of the booking details
                String[] bookingDetails = bookingDetailsForRefund.split(";");
                for (String bd : bookingDetails) {
                    String[] parts = bd.split(",");
                    String studentEmail = parts[0];
                    int studentPhone = Integer.parseInt(parts[1]);
                    double transcationAmount = Double.parseDouble(parts[2]);
                    int numTickets = Integer.parseInt(parts[3]);

                    boolean refundSuccess = paymentSystem.processRefund(numTickets, eventTitle, studentEmail, studentPhone,
                            epEmail, transcationAmount, organiserMessage);

                    if (!refundSuccess) {
                        view.displayError("There was an issue with a refund. The performance cannot be cancelled.");
                        return;
                    }
                }
            }
        }

        //success
        //cancel by the ep and set status to CANCELLED
        for(Booking booking : performance.getBookings()){
            if(booking.getStatus() == BookingStatus.ACTIVE){
                booking.cancelByProvider();
            }
        }

        //cancel the performance
        performance.cancel();

        view.displaySuccess("Cancellation Successful.");
        view.getInput("Press ENTER to return to dashboard... \n");
    }


    private boolean checkIfSponsorshipPossible(Performance performance, double amount){
        boolean isTicketed = performance.checkIfEventIsTicketed();

        //check if the event is already sponsored
        if(performance.isSponsored()){
            view.displayError("This performance is already sponsored.");
            return false;
        }

        //check if the event is isTicketed
        if(!isTicketed){
            view.displayError("The requested performance's event is non ticketed. It cannot be sponsored.");
            return false;
        }

        //get ticket price
        double ticketPrice = performance.getTicketPrice();

        if(amount < 0 || amount > ticketPrice){
            view.displayError("The amount provided is invalid.");
            return false;
        }

        return true;
    }

    /**
     * Sponsors a performance by reducing its ticket price by a given amount.
     * Only Admin Staff may sponsor a performance. The performance must be ticketed
     * and not already sponsored. The sponsorship amount must be non-negative and
     * no greater than the current ticket price.
     * Displays an error and returns early if:
     * The current user is not Admin Staff
     * The performance is already sponsored
     * The event is not ticketed
     * The sponsorship amount is invalid
     * The performance ID is not found
     *
     */
    public void sponsorPerformance(){
        //Only admin staff can sponsor performance
        if(!checkCurrentUserIsAdmin()){
            view.displayError("Only Admin can sponsor performance.");
            return;
        }

        AdminStaff adminStaff = (AdminStaff) currentUser;
        Performance performance = null;
        boolean possible = false;
        double sponsorshipAmount = 0;
        while(performance == null || !possible){
            try{
                String input1 = view.getInput("Enter Performance ID (or '-1' to return back to dashboard.): ");
                String input2 = view.getInput("Enter sponsorship amount (or '-1' to return back to dashboard.): £");

                if(input1.trim().equals("-1") || input2.trim().equals("-1")){
                    return;
                }

                long performanceID = Long.parseLong(input1);
                sponsorshipAmount = Double.parseDouble(input2);

                performance = getPerformanceByID(performanceID);

                //performance not found
                if(performance == null){
                    view.displayError("Performance with given number does not exists.");
                    continue;
                }

                //check if sponsorship is possible
                possible  = checkIfSponsorshipPossible(performance, sponsorshipAmount);
            } catch (NumberFormatException e){
                view.displayError("Invalid input, please enter a number.");
                performance = null;
                possible = false;
            }
        }

        //sponsor
        performance.sponsor(sponsorshipAmount);
        view.displaySuccess("Sponsorship Successful.");
        view.getInput("Press ENTER to return to dashboard... \n");
    }

    private void addEvent(Event e){
        events.add(e);
    }

    private void addPerformance(Performance p){
        performances.add(p);
    }

    //find and return an Event by its ID
    //return null if no event with the given ID is found

    private Event getEventByID(long EventID){
        for (Event e: events){
            if(e.getEventID() == EventID){
                return e;
            }
        }
        return null;
    }

    //for view performance
    private String buildPerformanceInfo(Performance performance, Event event){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();

        //event details
        sb.append("\n===EVENT DETAILS===\n");
        sb.append("Event Title: ").append(event.getTitle()).append("\n");
        sb.append("Event Type: ").append(event.getType()).append("\n");

        //performance details
        sb.append("\n=== Performance Details ===\n");
        sb.append("Performance ID: ").append(performance.getPerformanceID()).append("\n");
        sb.append("Start Time: ").append(performance.getStartDateTime().format(formatter)).append("\n");
        sb.append("End Time: ").append(performance.getEndDateTime().format(formatter)).append("\n");
        sb.append("Venue: ").append(performance.getVenueAddress()).append("\n");
        sb.append("Venue Capacity: ").append(performance.getVenueCapacity()).append("\n");
        sb.append("Outdoors: ").append(performance.isVenueIsOutdoors()).append("\n");
        sb.append("Smoking: ").append(performance.isVenueAllowsSmoking()).append("\n");
        sb.append("Performers: ").append(performance.getPerformerName()).append("\n");
        sb.append("Status: ").append(performance.getStatus()).append("\n");

        //ticket availability
        if(event.isTicketed()){
            sb.append("\n=== Ticket Details ===\n");
            sb.append("Number of Ticket Available: ").append(performance.getNumTicketsTotal() -
                    performance.getNumTicketsSold()).append("\n");
            sb.append("Ticket Price:£ ").append(performance.getFinalTicketPrice()).append("\n");
        }

        //average event rating
        sb.append("\n=== Review ===\n");
        sb.append("Average Rating: "). append(event.getAverageRatingOfPerformances()).append("\n");

        //individual reviews
        sb.append("\n=== Individual Reviews ===\n");
        Collection<String> reviews = event.getAllPerformanceReviews();
        if(reviews.isEmpty()){
            sb.append("No comment for this event.\n");
        } else{
            for(String review: reviews){
                sb.append(review).append("\n");
            }
        }

        return sb.toString(); //convert StringBuilder to String
    }
    private Event getEventByPerformanceID(long performanceID){
        for(Event event: events){
            if(event.getPerformanceByID(performanceID) != null){
                return event;
            }
        }
        return null;
    }

    private Event getEventByTitle(String title){
        for(Event e : events){
            if(e.getTitle().equalsIgnoreCase(title)){
                return e;
            }
        }
        return null;
    }

    private Performance getPerformanceByID(long PerformanceID){
        for (Performance p: performances){
            if(p.getPerformanceID() == PerformanceID){
                return p;
            }
        }
        return null;
    }

    private Collection<String> sortByStudentPreferences(Student student, Collection<String> performance){
        //get student preferences
        StudentPreferences preferences = student.getPreferences();

        Collection<String> preferred = new ArrayList<>();
        Collection<String> others = new ArrayList<>();

        for (String p : performance){
            //check if performance match the preferences
            if((preferences.preferMusicEvents && p.contains("MUSIC")) ||
                    (preferences.preferTheatreEvents && p.contains("THEATRE")) ||
                    (preferences.preferDanceEvents && p.contains("DANCE")) ||
                    (preferences.preferMovieEvents && p.contains("MOVIE")) ||
                    (preferences.preferSportsEvents && p.contains("SPORTS"))){
                preferred.add(p);
            }else{
                others.add(p);
            }
        }

        //preferred first then others
        preferred.addAll(others);
        return preferred;
    }

    public Collection<Performance> getPerformances() {
        return performances;
    }
}
