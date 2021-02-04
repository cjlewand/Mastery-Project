package learn.house.domain;

import learn.house.data.GuestRepository;
import learn.house.models.Guest;

import java.util.List;

public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public List<Guest> findAll() {
        return repository.findAll();
    }

    public Guest findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
