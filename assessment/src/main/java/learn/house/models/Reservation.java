package learn.house.models;

import learn.house.data.DataException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Objects;

public class Reservation {

    private int id;
    private Guest guest;
    private Host host;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalCost;

    public Reservation() {

    }
    public Reservation(Guest guest, Host host, LocalDate startDate, LocalDate endDate) {
        this.guest = guest;
        this.host = host;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal makeTotalCost() throws DataException {
        BigDecimal total = new BigDecimal(0);
        Host host = getHost();
        for (int i = 0; i < ChronoUnit.DAYS.between(getStartDate(), getEndDate()); i++) {
            LocalDate currentDate = getStartDate().plusDays(i);
            if (currentDate.getDayOfWeek().getValue() + 1 == Calendar.SATURDAY
                    || currentDate.getDayOfWeek().getValue() + 1 == Calendar.FRIDAY) {
                total = total.add(host.getWeekendRate());
            } else {
                total = total.add(host.getStandardRate());
            }
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id && Objects.equals(guest, that.guest) && Objects.equals(host, that.host) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(totalCost, that.totalCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guest, host, startDate, endDate, totalCost);
    }
}
