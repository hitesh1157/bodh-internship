package models;

import common.settings.StartUpHandler;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.PrePersist;
import play.data.validation.Constraints;

import java.util.Date;

/**
 * Created by harshitjain on 25/03/17.
 */
@Entity(noClassnameStored = true)
public class Otp {
    @Id
    private ObjectId id;

    @Constraints.Required
    @Constraints.Pattern("^[+0-9]+$")
    private String mobile;

    @Constraints.Pattern("^[0-9]{4}$")
    @Constraints.Required
    private String otp;

    @Constraints.Pattern("^[+0-9]+$")
    private String countryCode;

    @Indexed(unique = true, expireAfterSeconds = 600)
    private Date createdDate;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void save() {
        StartUpHandler.getDatastore().save(this);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @PrePersist
    void prePersist() {
        if (id == null)
            createdDate = new Date();
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object obj) {
        Otp model = (Otp) obj;
        if (id == null || model.id == null)
            return false;
        return model.id.equals(id);
    }
}
