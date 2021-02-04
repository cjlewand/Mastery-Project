package learn.house.data;

import learn.house.models.Host;

import java.util.List;

public interface HostRepository {

    Host findByEmail(String email);

    List<Host> findAll();

}
