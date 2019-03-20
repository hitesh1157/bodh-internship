package models;

import org.mongodb.morphia.annotations.Entity;
import play.data.validation.Constraints;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshitjain on 10/08/17.
 */
@Entity(noClassnameStored = true)
public class FestivalList extends BaseModel {
    @Constraints.Required
    private String year;
    @Constraints.Required
    private List<Festival> festivals = new ArrayList<>();

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<Festival> getFestivals() {
        return festivals;
    }

    public void setFestivals(List<Festival> festivals) {
        this.festivals = festivals;
    }
}
