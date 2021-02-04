package learn.house.data;

import learn.house.models.Guest;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuestFileRepository implements GuestRepository{

    //private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";
    private final String filePath;
    private static final String DELIMITER = ",";

    public GuestFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Guest> findAll() {
        ArrayList<Guest> guests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            // read header
            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields. length == 6) {
                    guests.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {

        }
        return guests;
    }

    @Override
    public Guest findByEmail(String email) {
        return findAll().stream()
                .filter(guest -> guest.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setId(Integer.parseInt(fields[0]));
        result.setFirstName(preserveString(fields[1]));
        result.setLastName(preserveString(fields[2]));
        result.setEmail(preserveString(fields[3]));
        result.setPhoneNumber(preserveString(fields[4]));
        result.setState(preserveString(fields[5]));
        return result;
    }

    private String preserveString(String string) {
        return string.replace("@@@",DELIMITER);
    }
}
