package learn.house.data;

import learn.house.models.Guest;

import java.util.List;

public interface GuestRepository {

    List<Guest> findAll();

    Guest findByEmail(String email);

}
