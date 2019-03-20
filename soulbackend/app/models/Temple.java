package models;

import org.mongodb.morphia.geo.Point;
import play.data.validation.Constraints;

/**
 * Created by harshitjain on 11/11/17.
 */
public class Temple extends BaseModel {
    @Constraints.Required
    private MultiLingual name;
    private MultiLingual description;
    @Constraints.Required
    private Point location;
    private MultiLingual address;
    private Sect sect;
    private String[] urls;

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public MultiLingual getName() {
        return name;
    }

    public void setName(MultiLingual name) {
        this.name = name;
    }

    public MultiLingual getDescription() {
        return description;
    }

    public void setDescription(MultiLingual description) {
        this.description = description;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public MultiLingual getAddress() {
        return address;
    }

    public void setAddress(MultiLingual address) {
        this.address = address;
    }

    public Sect getSect() {
        return sect;
    }

    public void setSect(Sect sect) {
        this.sect = sect;
    }
}
