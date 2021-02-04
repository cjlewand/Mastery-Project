package learn.house.data;

import learn.house.models.Guest;
import learn.house.models.Host;
import learn.house.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    static final String SEED_PATH = "./data/reservation-seed.csv";
    static final String TEST_PATH = "./data/reservation_data_test/2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c.csv";
    static final String TEST_DIR_PATH = "./data/reservation_data_test";

    final Host testHost = new Host();


    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);

        testHost.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
    }

    @Test
    void shouldFindByHost() {
        List<Reservation> result = repository.findByHost(testHost);
        assertNotNull(result);
        assertEquals(13, result.size());
    }

    @Test
    void shouldAddNewReservation() throws DataException {
        Reservation expected = new Reservation();
        expected.setStartDate(LocalDate.of(2020,12,01));
        expected.setEndDate(LocalDate.of(2020,12,13));
        Guest guest = new Guest();
        guest.setId(1);
        expected.setGuest(guest);
        expected.setTotalCost(new BigDecimal("300"));
        expected.setHost(testHost);

        expected = repository.add(expected);

        assertEquals(14, expected.getId());
    }

    @Test
    void shouldUpdateExistingReservation() throws DataException {
        Reservation expected = new Reservation();
        expected.setId(1);
        expected.setStartDate(LocalDate.of(2030,01,01));
        expected.setEndDate(LocalDate.of(2030,2,1));
        Guest guest = new Guest();
        guest.setId(1);
        expected.setGuest(guest);
        expected.setTotalCost(new BigDecimal("300"));
        expected.setHost(testHost);

        boolean success = repository.update(expected);

        assertTrue(success);
    }

    @Test
    void shouldNotUpdateNonExistentReservation() throws DataException {
        Reservation expected = new Reservation();
        expected.setId(100000);
        expected.setStartDate(LocalDate.of(2030,01,01));
        expected.setEndDate(LocalDate.of(2030,2,1));
        Guest guest = new Guest();
        guest.setId(1);
        expected.setGuest(guest);
        expected.setTotalCost(new BigDecimal("300"));
        expected.setHost(testHost);

        boolean success = repository.update(expected);
        assertFalse(success);
    }

    @Test
    void shouldDeleteExistingReservation() throws DataException {
        Reservation expected = new Reservation();
        expected.setId(1);

        boolean success = repository.deleteById(expected.getId(), testHost);
        assertTrue(success);
    }

    @Test
    void shouldNotDeleteNonExistentReservation() throws DataException {
        Reservation expected = new Reservation();
        expected.setId(100000);

        boolean success = repository.deleteById(expected.getId(), testHost);
        assertFalse(success);
    }

}