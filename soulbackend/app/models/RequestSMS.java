package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;
import play.data.validation.Constraints;

import java.util.Date;

/**
 * Created by hitesh1157 on 11/09/17.
 */
@Entity(noClassnameStored = true)
public class RequestSMS extends BaseModel {
    @Constraints.Required
    private String phone;
    private Date date;
    private String countryCode;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

}
