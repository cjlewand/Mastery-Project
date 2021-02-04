package learn.house.data;

import learn.house.models.Guest;
import learn.house.models.Host;
import learn.house.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryDouble implements ReservationRepository {

    private final ArrayList<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble() {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        Guest guest = new Guest();
        guest.setId(10000);
        guest.setFirstName("John");
        guest.setLastName("Johnson");
        guest.setEmail("jj@gmail.com");
        guest.setPhoneNumber("(414) 538-4858");
        guest.setState("WI");
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusWeeks(1));
        reservation.setTotalCost(new BigDecimal("400"));

        Host host = new Host();
        host.setId("10000");
        host.setLastName("Jacobs");
        host.setEmail("jacobs@gmail.com");
        host.setPhoneNumber("(414) 998-8899");
        host.setAddress("12 Maple St");
        host.setPostalCode("55555");
        host.setCity("Riverdale");
        host.setState("NY");
        host.setStandardRate(new BigDecimal("150"));
        host.setWeekendRate(new BigDecimal("250"));

        reservation.setHost(host);
        reservations.add(reservation);
    }
    @Override
    public List<Reservation> findByHost(Host host) {
        return reservations;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        reservation.setId(2);
        reservations.add(reservation);
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        return true;
    }

    @Override
    public boolean deleteById(int reservationId, Host host) throws DataException {
        return true;
    }
}
