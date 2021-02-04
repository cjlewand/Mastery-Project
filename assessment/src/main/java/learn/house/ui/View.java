package learn.house.ui;

import com.sun.tools.javac.Main;
import learn.house.data.DataException;
import learn.house.models.Guest;
import learn.house.models.Host;
import learn.house.models.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class View {

    private final ConsoleIO io;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getMessage());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String prompt = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(prompt, min, max));
    }

    public String getHostEmail() {
        return io.readRequiredString("Please Enter Host Email [0 to exit]: ");
    }

    public String getGuestEmail() {
        return io.readRequiredString("Please enter Guest Email [0 to exit]: ");
    }

    public String getState() {
        String state;
        do {
            state = io.readRequiredString("Please Enter two letter state abbreviation: ");
        } while (state.length() != 2);
        return state;
    }

    public Reservation makeReservation(Host host, Guest guest) {
        Reservation reservation = new Reservation();
        LocalDate startDate = io.readRequiredLocalDate("Enter Start Date (MM/dd/yyyy): ");
        reservation.setStartDate(startDate);
        LocalDate endDate = io.readRequiredLocalDate("Enter End Date (MM/dd/yyyy): ");
        reservation.setEndDate(endDate);
        reservation.setHost(host);
        reservation.setGuest(guest);
        return reservation;
    }

    public int selectReservation(List<Reservation> reservations, Host host) {
        displayFutureReservations(reservations, host);
        int id = 0;
        try {
            id = io.readInt("Enter Reservation ID [0 to exit]: ", reservations.stream().min(Comparator.comparing(Reservation::getId))
                            .get().getId(),
                    reservations.stream().max(Comparator.comparing(Reservation::getId))
                            .get().getId());
        } catch (NoSuchElementException ex) {

        }
        return id;
    }

    public Reservation updateReservation(Reservation reservation) {
        displayHeader("Editing Reservation " + reservation.getId());
        LocalDate newStartDate = io.readLocalDate("Start Date (" + reservation.getStartDate().format(formatter) +  "): ");
        if (newStartDate != null) {
            reservation.setStartDate(newStartDate);
        }
        LocalDate newEndDate = io.readLocalDate("End Date (" + reservation.getEndDate().format(formatter) +  "): ");
        if (newEndDate != null) {
            reservation.setEndDate(newEndDate);
        }
        return reservation;
    }

    public boolean displaySummary(Reservation reservation) {
        displayHeader("Summary");
        io.println("Start Date: " + reservation.getStartDate().format(formatter));
        io.println("End Date: " + reservation.getEndDate().format(formatter));
        io.println("Total: $" + reservation.getTotalCost());
        return io.readBoolean("Is this okay? [y/n]: ");
    }




    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    public void displayReservations(List<Reservation> reservations, Host host) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No Reservations found.");
            return;
        }
        displayHeader("Reservations for " + host.getLastName() + " (" + host.getCity() + ", " + host.getState() + ")");
        reservations = reservations.stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
        displayAllHostReservations(reservations);
    }

    // overload for state output
    public void displayReservations(List<Reservation> reservations, String state) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No Reservations found.");
            return;
        }
        displayHeader("Reservations in " + state);
        reservations = reservations.stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
        displayAllStateReservations(reservations);
    }
    // overload for guest output
    public void displayReservations(List<Reservation> reservations, Guest guest) {
        if (reservations == null || reservations.isEmpty() ) {
            io.println("No Reservations found.");
            return;
        }
        displayHeader("Reservations for " + guest.getFirstName() + " " + guest.getLastName() + " (" + guest.getEmail() + ")");
        reservations = reservations.stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
        displayAllGuestReservations(reservations);
    }

    public void displayFutureReservations(List<Reservation> reservations, Host host) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No Reservations found.");
            return;
        }
        displayHeader("Reservations for " + host.getLastName() + " (" + host.getCity() + ", " + host.getState() + ")");
        reservations = reservations.stream()
                .filter(reservation -> reservation.getEndDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
        displayAllHostReservations(reservations);
    }
    // overload for guest output
    public void displayFutureReservations(List<Reservation> reservations, Guest guest) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No Reservations found.");
            return;
        }
        displayHeader("Reservations for " + guest.getFirstName() + " " + guest.getLastName() + " (" + guest.getEmail() + ")");
        reservations = reservations.stream()
                .filter(reservation -> reservation.getEndDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
        displayAllGuestReservations(reservations);
    }

    public void displayFutureReservations(List<Reservation> reservations, String state) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No Reservations found.");
            return;
        }
        displayHeader("Reservations in " + state);
        reservations = reservations.stream()
                .filter(reservation -> reservation.getEndDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
        displayAllStateReservations(reservations);
    }

    public void displayAllHostReservations(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No Reservations found.");
        }
        for (Reservation r : reservations) {
            io.printf("Reservation Id: %s, %s - %s, Guest: %s, %s, Email: %s%n",
                    r.getId(),
                    r.getStartDate().format(formatter),
                    r.getEndDate().format(formatter),
                    r.getGuest().getLastName(),
                    r.getGuest().getFirstName(),
                    r.getGuest().getEmail());
            io.println("-".repeat(200));
        }
    }

    public void displayAllGuestReservations(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No Reservations found.");
        }
        for (Reservation r : reservations) {
            io.printf("Reservation Id: %s, %s - %s, Location: %s, %s, Host Last Name: %s, Host Email: %s%n",
                    r.getId(),
                    r.getStartDate().format(formatter),
                    r.getEndDate().format(formatter),
                    r.getHost().getCity(),
                    r.getHost().getState(),
                    r.getHost().getLastName(),
                    r.getHost().getEmail());
            io.println("-".repeat(200));
        }
    }

    public void displayAllStateReservations(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No Reservations found.");
        }
        for (Reservation r : reservations) {
            io.printf("Reservation Id: %s, %s - %s, City: %s, " +
                            "Guest Last Name: %s, Guest Email: %s, Host Last Name: %s, Host Email: %s%n",
                    r.getId(),
                    r.getStartDate().format(formatter),
                    r.getEndDate().format(formatter),
                    r.getHost().getCity(),
                    r.getGuest().getLastName(),
                    r.getGuest().getEmail(),
                    r.getHost().getLastName(),
                    r.getHost().getEmail());
            io.println("-".repeat(200));
        }
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success!" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    public boolean displayFutureOnly() {
        return io.readBoolean("Would you like to see only future reservations? [y/n]: ");
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

}
