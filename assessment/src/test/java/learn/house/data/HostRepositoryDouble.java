package learn.house.data;

import learn.house.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    public final static Host HOST = makeHost();

    private final ArrayList<Host> hosts = new ArrayList<Host>();

    public HostRepositoryDouble() {
        hosts.add(HOST);
    }
    @Override
    public Host findByEmail(String email) {
        return hosts.stream()
                .filter(guest -> guest.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Host> findAll() {
        return hosts;
    }

    private static Host makeHost() {
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
