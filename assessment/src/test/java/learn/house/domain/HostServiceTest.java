package learn.house.domain;

import learn.house.data.HostRepositoryDouble;
import learn.house.models.Guest;
import learn.house.models.Host;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    HostService service = new HostService( new HostRepositoryDouble());

    @Test
    void shouldFindAll() {
        List<Host> hosts = service.findAll();
        assertNotNull(hosts);
        assertEquals(1, hosts.size());
    }

    @Test
    void shouldFindGuestById() {
        Host expected = service.findByEmail("jacobs@gmail.com");
        assertNotNull(expected);
        assertEquals("Jacobs", expected.getLastName());
        assertEquals("10000", expected.getId());
        assertEquals("NY", expected.getState());
    }

    @Test
    void shouldNotFindNonExistentGuest() {
        Host expected = service.findByEmail("InvalidEmail");
        assertNull(expected);
    }

}