package learn.house.data;

import learn.house.models.Host;
import learn.house.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findByHost(Host host);

    Reservation add(Reservation reservation) throws DataException;

    boolean update(Reservation reservation) throws DataException;

    boolean deleteById(int reservationId, Host host) throws DataException;
}
