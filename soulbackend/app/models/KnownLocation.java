package models;

import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.geo.Point;
import org.mongodb.morphia.geo.PointBuilder;

import java.util.Date;

public class KnownLocation {
    Point location;
    Address address;
    Date updatedOn;
    @Reference
    User user;

    public KnownLocation() {
        location = PointBuilder.pointBuilder().latitude(78).longitude(79).build();
        address = new Address();
        address.setAddressLine1("line1");
        address.setAddressLine2("line2");
        address.setCity("Bangalore");
        address.setCountry("India");
        address.setPincode("311001");
        address.setState("Karnataka");
        updatedOn = new Date();
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
