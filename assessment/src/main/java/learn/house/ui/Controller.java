package learn.house.ui;

import learn.house.data.DataException;
import learn.house.domain.GuestService;
import learn.house.domain.HostService;
import learn.house.domain.ReservationService;
import learn.house.domain.Result;
import learn.house.models.Guest;
import learn.house.models.Host;
import learn.house.models.Reservation;

import java.util.List;
import java.util.NoSuchElementException;

public class Controller {

    private final HostService hostService;
    private final GuestService guestService;
    private final ReservationService reservationService;
    private final View view;

    public Controller(HostService hostService, GuestService guestService, ReservationService reservationService, View view) {
        this.hostService = hostService;
        this.guestService = guestService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run()  {
        view.displayHeader("Welcome to 'Don't Wreck My House'");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye");
    }

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_HOST:
                    viewReservationsByHost();
                    break;
                case VIEW_RESERVATIONS_GUEST:
                    viewReservationsByGuest();
                    break;
                case VIEW_RESERVATIONS_STATE:
                    viewReservationsByState();
                    break;
                case CREATE_RESERVATION:
                    makeReservation();
                    break;
                case UPDATE_RESERVATION:
                    updateReservation();
                    break;
                case DELETE_RESERVATION:
                    deleteReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
        view.displayHeader("See you next time!");
    }

    public void viewReservationsByHost() throws DataException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_HOST.getMessage());
        String email = view.getHostEmail();
        if (email.equals("0")) {
            return;
        }
        Host host = hostService.findByEmail(email);
        if (host == null) {
            view.displayStatus(false, "Host Not Found.");
            return;
        }
        List<Reservation> reservations = reservationService.findByHost(host);
        view.displayReservations(reservations, host);
        view.enterToContinue();
    }

    public void viewReservationsByGuest() throws DataException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_GUEST.getMessage());
        String guestEmail = view.getGuestEmail();
        if (guestEmail.equals("0")) {
            return;
        }
        Guest guest = guestService.findByEmail(guestEmail);
        if (guest == null) {
            view.displayStatus(false, "Guest could not be found.");
            return;
        }
        List<Reservation> result = reservationService.findReservationsByGuest(guest);
        if (result == null || result.isEmpty()) {
            view.displayHeader("No Reservations Found");
            return;
        }
        boolean futureOnly = view.displayFutureOnly();
        if (futureOnly) {
            view.displayFutureReservations(result, guest);
        } else if (!futureOnly) {
            view.displayReservations(result, guest);
        }
        view.enterToContinue();
    }

    public void viewReservationsByState() throws DataException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_STATE.getMessage());
        String state = view.getState();
        List<Reservation> result = reservationService.findReservationsByState(state);
        if (result.size() == 0) {
            view.displayHeader("No Reservations found for " + state);
            return;
        }
        boolean futureOnly = view.displayFutureOnly();
        if (futureOnly) {
            view.displayFutureReservations(result, state);
        } else if (!futureOnly) {
            view.displayReservations(result, state);
        }
        view.enterToContinue();
    }

    public void makeReservation() throws DataException {
        view.displayHeader(MainMenuOption.CREATE_RESERVATION.getMessage());
        String hostEmail = view.getHostEmail();
        if (hostEmail.equals("0")) {
            return;
        }
        Host host = hostService.findByEmail(hostEmail);
        if (host == null) {
            view.displayStatus(false, "Host was not found.");
            return;
        }
        String guestEmail = view.getGuestEmail();
        if (guestEmail.equals("0")) {
            return;
        }
        Guest guest = guestService.findByEmail(guestEmail);
        if (guest == null) {
            view.displayStatus(false, "Guest was not found.");
            return;
        }
        List<Reservation> reservations = reservationService.findByHost(host);
        view.displayFutureReservations(reservations, host);

        Reservation reservation = view.makeReservation(host, guest);
        reservation.setTotalCost(reservationService.makeTotalReservationCost(reservation));
        boolean confirm = view.displaySummary(reservation);
        if (confirm) {
            Result<Reservation> result = reservationService.add(reservation);
            if (!result.isSuccess()) {
                view.displayStatus(false, result.getErrorMessages());
            } else {
                String successMessage = String.format("Reservation %s created.", result.getPayload().getId());
                view.displayStatus(true, successMessage);
            }
        }
    }

    public void updateReservation() throws DataException {
        view.displayHeader(MainMenuOption.UPDATE_RESERVATION.getMessage());
        String hostEmail = view.getHostEmail();
        if (hostEmail.equals("0")) {
            return;
        }
        Host host = hostService.findByEmail(hostEmail);
        if (host == null) {
            view.displayStatus(false, "Host was not found.");
            return;
        }
        String guestEmail = view.getGuestEmail();
        if (guestEmail.equals("0")) {
            return;
        }
        Guest guest = guestService.findByEmail(guestEmail);
        if (guest == null) {
            view.displayStatus(false, "Guest was not found.");
            return;
        }
        List<Reservation> reservations = reservationService.findByHost(host);
        reservations = reservationService.narrowByGuest(reservations, guest);
        int id = view.selectReservation(reservations, host);
        if (id == 0) {
            return;
        }
        Reservation reservation = reservationService.findReservationById(host, id);
        reservation = view.updateReservation(reservation);
        reservation.setTotalCost(reservationService.makeTotalReservationCost(reservation));
        boolean confirm = view.displaySummary(reservation);
        if (confirm) {
            Result<Reservation> result = reservationService.update(reservation);
            if (!result.isSuccess()) {
                view.displayStatus(false, result.getErrorMessages());
            } else {
                String successMessage = String.format("Reservation %s updated.", reservation.getId());
                view.displayStatus(true, successMessage);
            }
        }
    }

    public void deleteReservation() throws DataException {
        view.displayHeader(MainMenuOption.DELETE_RESERVATION.getMessage());
        String hostEmail = view.getHostEmail();
        if (hostEmail.equals("0")) {
            return;
        }
        Host host = hostService.findByEmail(hostEmail);
        if (host == null) {
            view.displayStatus(false, "Host was not found.");
            return;
        }
        String guestEmail = view.getGuestEmail();
        if (guestEmail.equals("0")) {
            return;
        }
        Guest guest = guestService.findByEmail(guestEmail);
        if (guest == null) {
            view.displayStatus(false, "Guest was not found.");
            return;
        }
        List<Reservation> reservations = reservationService.findByHost(host);
        reservations = reservationService.narrowByGuest(reservations, guest);
        int id = view.selectReservation(reservations, host);
        if (id == 0) {
            return;
        }
        Reservation reservation = reservationService.findReservationById(host, id);
        Result<Reservation> result = reservationService.deleteById(reservation);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            String successMessage = String.format("Reservation %s deleted.", reservation.getId());
            view.displayStatus(true, successMessage);
        }
    }
}
