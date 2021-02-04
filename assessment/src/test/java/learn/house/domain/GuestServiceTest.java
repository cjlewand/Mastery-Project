package learn.house.domain;

import learn.house.data.GuestRepositoryDouble;
import learn.house.models.Guest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    GuestService service = new GuestService(new GuestRepositoryDouble());

    @Test
    void shouldFindAll() {
        List<Guest> guests = service.findAll();
        assertNotNull(guests);
        assertEquals(2, guests.size());
    }

    @Test
    void shouldFindGuestById() {
        Guest expected = service.findByEmail("jj@gmail.com");
        assertNotNull(expected);
        assertEquals("John", expected.getFirstName());
        assertEquals(10000, expected.getId());
    }

    @Test
    void shouldNotFindNonExistentGuest() {
        Guest expected = service.findByEmail("InvalidEmail");
        assertNull(expected);
    }

}