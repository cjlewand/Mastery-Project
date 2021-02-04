package learn.house.data;

import learn.house.models.Guest;
import learn.house.models.Host;
import learn.house.models.Reservation;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;

    public ReservationFileRepository(String directory) {
        this.directory = directory;
    }

    public List<Reservation> findByHost(Host host) {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(host.getId())))) {

            // read header
            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields, host));
                }
            }
        } catch (IOException | NullPointerException ex) {

        }
        return result;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> all = findByHost(reservation.getHost());
        reservation.setId(getNextId(all));
        all.add(reservation);
        writeAll(all, reservation.getHost().getId());
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        List<Reservation> all = findByHost(reservation.getHost());
        for (int i = 0; i < all.size(); i++ ) {
            if (all.get(i).getId() == reservation.getId()) {
                all.set(i, reservation);
                writeAll(all, reservation.getHost().getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(int reservationId, Host host) throws DataException {
        List<Reservation> all = findByHost(host);
        for (int i = 0; i < all.size(); i++ ) {
            if (all.get(i).getId() == reservationId) {
                all.remove(i);
                writeAll(all, host.getId());
                return true;
            }
        }
        return false;
    }

    private int getNextId(List<Reservation> reservations) {
        int nextId = 0;
        for (Reservation reservation : reservations) {
            nextId = Math.max(nextId, reservation.getId());
        }
        return nextId + 1;
    }

    private void writeAll(List<Reservation> reservations, String id) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(id))) {

            writer.println(HEADER);

            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuest().getId(),
                reservation.getTotalCost());
    }

    private Reservation deserialize(String[] fields, Host host) {
        Reservation result = new Reservation();
        result.setId(Integer.parseInt(fields[0]));
        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));

        Guest guest = new Guest();
        guest.setId(Integer.parseInt(fields[3]));
        result.setGuest(guest);

        result.setTotalCost(new BigDecimal(fields[4]));
        result.setHost(host);
        return result;


    }
    private String getFilePath(String id) {
        return Paths.get(directory, id + ".csv").toString();
    }

}
