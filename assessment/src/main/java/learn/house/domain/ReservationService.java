package learn.house.domain;

import learn.house.data.DataException;
import learn.house.data.GuestRepository;
import learn.house.data.HostRepository;
import learn.house.data.ReservationRepository;
import learn.house.models.Guest;
import learn.house.models.Host;
import learn.house.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ReservationService {

    private final HostRepository hostRepository;
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(HostRepository hostRepository,
                              GuestRepository guestRepository,
                              ReservationRepository reservationRepository) {
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findByHost(Host host) {
        Map<Integer, Guest> guestMap = guestRepository.findAll().stream()
                .collect(Collectors.toMap(i -> i.getId(), i -> i));
        Map<String, Host> hostMap = hostRepository.findAll().stream()
                .collect(Collectors.toMap(i -> i.getId(), i -> i));

        List<Reservation> result = reservationRepository.findByHost(host);
        for (Reservation r : result) {
            r.setGuest(guestMap.get(r.getGuest().getId()));
            r.setHost(hostMap.get(r.getHost().getId()));
        }
        return result;
    }

    public Reservation findReservationById(Host host, int id) {
        List<Reservation> reservations = findByHost(host);
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Reservation> narrowByGuest(List<Reservation> reservations, Guest guest) {
        List<Reservation> guestReservations = new ArrayList<>();
        guestReservations = reservations.stream()
                .filter(reservation -> reservation.getGuest().getId() == guest.getId())
                .collect(Collectors.toList());
        return guestReservations;
    }

    public List<Reservation> findReservationsByGuest(Guest guest) {
        List<Host> hostList = hostRepository.findAll();
        List<Reservation> reservations = new ArrayList<>();
        for (Host host : hostList) {
            List<Reservation> reservationsByHost = findByHost(host);
            reservations.addAll(reservationsByHost);
        }
        reservations = reservations.stream()
                .filter(reservation -> reservation.getGuest().getId() == guest.getId())
                .collect(Collectors.toList());

        return reservations;

    }

    public List<Reservation> findReservationsByState(String state) {
        List<Host> hostList = hostRepository.findAll();
        List<Reservation> reservations = new ArrayList<>();
        for (Host host : hostList) {
            List<Reservation> reservationsByHost = findByHost(host);
            reservations.addAll(reservationsByHost);
        }
        reservations = reservations.stream()
                .filter(reservation -> reservation.getHost().getState().equalsIgnoreCase(state))
                .collect(Collectors.toList());

        return reservations;
    }

    public Result<Reservation> add(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(reservationRepository.add(reservation));

        return result;
    }

    public Result<Reservation> update(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);

        if (!result.isSuccess()) {
            return result;
        }

        Reservation existing = reservationRepository.findByHost(reservation.getHost()).stream()
                .filter(r -> r.getId() == reservation.getId())
                .findFirst()
                .orElse(null);
        if (existing == null) {
            result.addErrorMessage("Reservation could not be found.");
            return result;
        }

        boolean success = reservationRepository.update(reservation);
        if (!success) {
            result.addErrorMessage("Our records could not find this reservation to update.");
        }

        return result;
    }

    public Result<Reservation> deleteById(Reservation reservation) throws DataException {
        Result<Reservation> result = new Result<>();

        Reservation existing = reservationRepository.findByHost(reservation.getHost()).stream()
                .filter(r -> r.getId() == reservation.getId())
                .findFirst()
                .orElse(null);
        if (existing == null) {
            result.addErrorMessage("Reservation could not be found.");
            return result;
        }

        if (existing.getEndDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Past reservations cannot be deleted.");
            return result;
        }

        boolean success = reservationRepository.deleteById(reservation.getId(), reservation.getHost());
        if (!success) {
            result.addErrorMessage("Our records could not find this reservation to delete.");
        }

        return result;
    }

    public BigDecimal makeTotalReservationCost(Reservation reservation) throws DataException {
        return reservation.makeTotalCost();
    }

    private Result<Reservation> validate(Reservation reservation) {

        Result<Reservation> result = validateNulls(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        validateFields(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateChildrenExist(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateOverlap(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        return result;
    }

    private Result<Reservation> validateNulls(Reservation reservation) {
        Result<Reservation> result = new Result<>();

        if (reservation == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }

        if (reservation.getHost() == null) {
            result.addErrorMessage("Host not found. Host is required.");
        }

        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Start Date is required.");
        }

        if (reservation.getEndDate() == null) {
            result.addErrorMessage("End Date is required.");
        }

        if (reservation.getGuest() == null) {
            result.addErrorMessage("Guest not found. Guest is required.");
        }

        return result;
    }

    private Result<Reservation> validateFields(Reservation reservation, Result<Reservation> result) {
        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Cannot make a reservation for a past date.");
        }

        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            result.addErrorMessage("Start Date must be before End Date.");
        }
        if (reservation.getStartDate().equals(reservation.getEndDate())) {
            result.addErrorMessage("Reservation must be for at least 1 night");
        }
        return result;
    }

    private Result<Reservation> validateChildrenExist(Reservation reservation, Result<Reservation> result) {
        if (guestRepository.findAll().stream()
                .filter(guest -> guest.getId() == (reservation.getGuest().getId()))
                .findFirst()
                .orElse(null) == null) {
            result.addErrorMessage("Guest does not exist.");
        }

        if (hostRepository.findAll().stream()
                .filter(h -> h.getId().equalsIgnoreCase(reservation.getHost().getId()))
                .findFirst()
                .orElse(null) == null) {
            result.addErrorMessage("Host does not exist.");
        }

        return result;
    }

    private Result<Reservation> validateOverlap(Reservation reservation, Result<Reservation> result) {
        for (Reservation r : reservationRepository.findByHost(reservation.getHost())) {
            if (reservation.getId() != r.getId()) {
                if (reservation.getStartDate().isAfter(r.getStartDate())
                        && reservation.getStartDate().isBefore(r.getEndDate())) {
                    result.addErrorMessage("Start Date overlaps with another reservation.");
                }
                if (reservation.getEndDate().isAfter(r.getStartDate())
                        && reservation.getEndDate().isBefore(r.getEndDate())) {
                    result.addErrorMessage("End Date overlaps with another reservation.");
                }
                if (reservation.getStartDate().isBefore(r.getStartDate())
                        && reservation.getEndDate().isAfter(r.getEndDate())) {
                    result.addErrorMessage("Reservation overlaps an existing reservation.");
                }
            }
        }

        return result;
    }

}
