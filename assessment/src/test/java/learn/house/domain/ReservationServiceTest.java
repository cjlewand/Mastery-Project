package learn.house.domain;

import learn.house.data.DataException;
import learn.house.data.GuestRepositoryDouble;
import learn.house.data.HostRepositoryDouble;
import learn.house.data.ReservationRepositoryDouble;
import learn.house.models.Guest;
import learn.house.models.Host;
import learn.house.models.Reservation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    LocalDate futureDate = LocalDate.now().plusMonths(2);

    private final Host HOST = makeHost();

    ReservationService service = new ReservationService(
            new HostRepositoryDouble(),
            new GuestRepositoryDouble(),
            new ReservationRepositoryDouble());


    @Test
    void shouldFindReservationsByHost() {
        List<Reservation> result = service.findByHost(HOST);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldFindReservationById() throws DataException {
        List<Reservation> result = service.findByHost(HOST);
        Reservation expected = service.findReservationById(HOST, 1);
        assertNotNull(expected);
        assertEquals("John", expected.getGuest().getFirstName());
    }

    @Test
    void shouldFindByState() {
        List<Reservation> result = service.findReservationsByState("NY");
        assertNotNull(result);
        assertEquals(1,result.size());
    }

    @Test
    void shouldFindReservationsByGuest() throws DataException {
        Guest guest = new Guest();
        guest.setId(10000);
        List<Reservation> result = service.findReservationsByGuest(guest);
        assertEquals(1, result.size());
        Reservation actual = result.stream()
                .findFirst()
                .orElse(null);
        assertTrue(actual.getGuest().getPhoneNumber().equalsIgnoreCase("(414) 538-4858"));
    }

    @Test
    void shouldNarrowByGuest() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        Guest guest = new Guest();
        guest.setId(10001);
        reservation.setGuest(guest);
        reservation.setStartDate(futureDate);
        reservation.setEndDate(futureDate.plusWeeks(2));
        reservation.setTotalCost(new BigDecimal("400"));
        reservation.setHost(HOST);
        service.add(reservation);

        List<Reservation> result = service.findByHost(HOST);
        result = service.narrowByGuest(result, guest);
        assertEquals(1, result.size());
    }

    @Test
    void shouldAddValidReservation() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        Guest guest = new Guest();
        guest.setId(10000);
        reservation.setGuest(guest);
        reservation.setStartDate(futureDate);
        reservation.setEndDate(futureDate.plusWeeks(2));
        reservation.setTotalCost(new BigDecimal("400"));
        reservation.setHost(HOST);

        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(2, result.getPayload().getId());
    }

    @Test
    void shouldNotAddForGuestThatDoesNotExist() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        Guest guest = new Guest();
        guest.setId(1000000000);
        reservation.setGuest(guest);
        reservation.setStartDate(futureDate);
        reservation.setEndDate(futureDate.plusWeeks(2));
        reservation.setTotalCost(new BigDecimal("400"));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddForHostThatDoesNotExist() throws DataException{
        Reservation reservation = new Reservation();
        reservation.setId(2);
        Guest guest = new Guest();
        guest.setId(10000);
        reservation.setGuest(guest);
        reservation.setStartDate(futureDate);
        reservation.setEndDate(futureDate.plusWeeks(2));
        reservation.setTotalCost(new BigDecimal("400"));

        Host host = new Host();
        host.setId("100000000");
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

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddNullReservation() throws DataException {
        Reservation reservation = new Reservation();

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }
    @Test
    void shouldNotAddOverlapingStartDate() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        Guest guest = new Guest();
        guest.setId(10000);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.now().plusDays(3));
        reservation.setEndDate(LocalDate.now().plusDays(10));
        reservation.setTotalCost(new BigDecimal("400"));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationThatEngulfsOther() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        Guest guest = new Guest();
        guest.setId(150);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.now().plusWeeks(1));
        reservation.setEndDate(LocalDate.now().plusWeeks(2));
        reservation.setTotalCost(new BigDecimal("400"));
        service.update(reservation);

        Reservation reservation2 = new Reservation();
        reservation.setId(2);
        Guest guest2 = new Guest();
        guest.setId(10000);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.now().plusDays(4));
        reservation.setEndDate(LocalDate.now().plusWeeks(3));
        reservation.setTotalCost(new BigDecimal("400"));

        Result<Reservation> result = service.add(reservation2);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationInsideExistingReservation() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        Guest guest = new Guest();
        guest.setId(150);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.now().plusWeeks(1));
        reservation.setEndDate(LocalDate.now().plusWeeks(8));
        reservation.setTotalCost(new BigDecimal("400"));
        service.update(reservation);

        Reservation reservation2 = new Reservation();
        reservation.setId(2);
        Guest guest2 = new Guest();
        guest.setId(10000);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.now().plusWeeks(4));
        reservation.setEndDate(LocalDate.now().plusWeeks(5));
        reservation.setTotalCost(new BigDecimal("400"));

        Result<Reservation> result = service.add(reservation2);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddEndDateBeforeStartDate() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        Guest guest = new Guest();
        guest.setId(10000);
        reservation.setGuest(guest);
        reservation.setStartDate(futureDate.plusWeeks(2));
        reservation.setEndDate(futureDate);
        reservation.setTotalCost(new BigDecimal("400"));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddPastStartDateReservation() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        Guest guest = new Guest();
        guest.setId(10000);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.now().minusWeeks(2));
        reservation.setEndDate(LocalDate.now());
        reservation.setTotalCost(new BigDecimal("400"));
        reservation.setHost(HOST);

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("Cannot make a reservation for a past date."));
    }

    @Test
    void shouldUpdateExistingReservation() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        Guest guest = new Guest();
        guest.setId(10000);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.now().plusWeeks(10));
        reservation.setEndDate(LocalDate.now().plusWeeks(11));
        reservation.setTotalCost(new BigDecimal("400"));
        reservation.setHost(HOST);

        Result<Reservation> result = service.update(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateNullReservation() throws DataException {
        Reservation reservation = new Reservation();

        Result<Reservation> result = service.update(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateOverlappingReservation() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        Guest guest = new Guest();
        guest.setId(10000);
        reservation.setGuest(guest);
        reservation.setStartDate(futureDate);
        reservation.setEndDate(futureDate.plusWeeks(2));
        reservation.setTotalCost(new BigDecimal("400"));
        reservation.setHost(HOST);

        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());

        reservation.setStartDate(LocalDate.now().plusDays(4));
        reservation.setEndDate(LocalDate.now().plusWeeks(2));

        result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("Start Date overlaps with another reservation."));
    }

    @Test
    void shouldNotUpdateStartBeforeEnd() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        Guest guest = new Guest();
        guest.setId(10000);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.now().plusWeeks(10));
        reservation.setEndDate(LocalDate.now().plusWeeks(8));
        reservation.setTotalCost(new BigDecimal("400"));

        Result<Reservation> result = service.update(reservation);
        assertFalse(result.isSuccess());
    }


    @Test
    void shouldDeleteById() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1);

        Result<Reservation> result = service.deleteById(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotDeleteNonExistentId() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(100000);

        Result<Reservation> result = service.deleteById(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("Reservation could not be found."));
    }


    @Test
    void shouldMakeCorrectTotalCost() throws DataException {
        Reservation reservation = service.findByHost(HOST).stream()
                .findFirst()
                .orElse(null);
        BigDecimal result = service.makeTotalReservationCost(reservation);
        assertEquals(new BigDecimal(1250), result);

    }

    private Host makeHost() {
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
        return host;
    }
}