package learn.house.data;

import learn.house.models.Host;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostFileRepository implements HostRepository {

    //private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";
    private final String filePath;
    private static final String DELIMITER = ",";

    public HostFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Host> findAll() {
        ArrayList<Host> hosts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            // read header
            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields. length == 10) {
                    hosts.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {

        }
        return hosts;
    }

    @Override
    public Host findByEmail(String email) {
        return findAll().stream()
                .filter(host -> host.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    private Host deserialize(String [] fields) {
        Host result = new Host();
        result.setId(fields[0]);
        result.setLastName(preserveString(fields[1]));
        result.setEmail(preserveString(fields[2]));
        result.setPhoneNumber(preserveString((fields[3])));
        result.setAddress(preserveString(fields[4]));
        result.setCity(preserveString(fields[5]));
        result.setState(preserveString(fields[6]));
        result.setPostalCode(preserveString(fields[7]));
        result.setStandardRate(new BigDecimal(fields[8]));
        result.setWeekendRate(new BigDecimal(fields[9]));
        return result;
    }


    private String preserveString(String string) {
        return string.replace("@@@",DELIMITER);
    }
}


