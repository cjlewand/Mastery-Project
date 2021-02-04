package learn.house.data;

import learn.house.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    static final String SEED_PATH = "./data/host-seed.csv";
    static final String TEST_PATH = "./data/host-test.csv";

    HostFileRepository repository = new HostFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAllTenHosts() {
        List<Host> result = repository.findAll();
        assertEquals(10, result.size());
    }

    @Test
    void shouldFindOneByEmail() {
        Host result = repository.findByEmail("charley4@apple.com");
        assertNotNull(result);
        assertEquals("Harley", result.getLastName());
    }
    @Test
    void shouldNotFindWithInvalidEmail() {
        Host result = repository.findByEmail("invalidEmail");
        assertNull(result);
    }
}