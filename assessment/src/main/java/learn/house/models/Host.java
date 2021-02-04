package learn.house.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Host {

    private String id;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private BigDecimal standardRate;
    private BigDecimal weekendRate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public BigDecimal getStandardRate() {
        return standardRate;
    }

    public void setStandardRate(BigDecimal standardRate) {
        this.standardRate = standardRate;
    }

    public BigDecimal getWeekendRate() {
        return weekendRate;
    }

    public void setWeekendRate(BigDecimal weekendRate) {
        this.weekendRate = weekendRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Host host = (Host) o;
        return Objects.equals(id, host.id) && Objects.equals(lastName, host.lastName) && Objects.equals(email, host.email) && Objects.equals(phoneNumber, host.phoneNumber) && Objects.equals(address, host.address) && Objects.equals(city, host.city) && Objects.equals(state, host.state) && Objects.equals(postalCode, host.postalCode) && Objects.equals(standardRate, host.standardRate) && Objects.equals(weekendRate, host.weekendRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, email, phoneNumber, address, city, state, postalCode, standardRate, weekendRate);
    }
}
