package learn.house.data;

import learn.house.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository{

    public static final Guest GUEST = makeGuest();
    public static final Guest GUEST_2 = makeSecondGuest();

    private final ArrayList<Guest> guests = new ArrayList<>();

    public GuestRepositoryDouble() {
        guests.add(GUEST);
        guests.add(GUEST_2);
    }

    @Override
    public List<Guest> findAll() {
        return guests;
    }

    @Override
    public Guest findByEmail(String email) {
        return guests.stream()
                .filter(guest -> guest.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    private static Guest makeGuest() {
        Guest guest = new Guest();
        guest.setId(10000);
        guest.setFirstName("John");
        guest.setLastName("Johnson");
        guest.setEmail("jj@gmail.com");
        guest.setPhoneNumber("(414) 538-4858");
        guest.setState("WI");
        return guest;
    }

    private static Guest makeSecondGuest() {
        Guest guest = new Guest();
        guest.setId(10001);
        guest.setFirstName("Bill");
        guest.setLastName("Johnson");
        guest.setEmail("bj@gmail.com");
        guest.setPhoneNumber("(414) 642-8457");
        guest.setState("WI");
        return guest;
    }

}
