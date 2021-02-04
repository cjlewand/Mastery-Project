package learn.house.data;

import learn.house.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    static final String SEED_PATH = "./data/guest-seed.csv";
    static final String TEST_PATH = "./data/guest-test.csv";

    GuestFileRepository repository = new GuestFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAllTenGuests() {
        List<Guest> result = repository.findAll();
        assertNotNull(result);
        assertEquals(10, result.size());
    }

    @Test
    void shouldFindByValidEmail() {
        Guest result = repository.findByEmail("kcurson5@youku.com");
        assertNotNull(result);
        assertEquals("Kenn", result.getFirstName());
        assertEquals("Curson", result.getLastName());
    }

    @Test
    void shouldNotFindByInvalidEmail() {
        Guest result = repository.findByEmail("InvalidEmail");
        assertNull(result);
    }
}