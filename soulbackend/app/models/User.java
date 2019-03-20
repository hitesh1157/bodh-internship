package models;

import org.mongodb.morphia.annotations.Entity;
import play.data.validation.Constraints;

/**
 * Created by harshitjain on 26/03/17.
 */
@Entity(noClassnameStored = true)
public class User extends BaseModel {
    @Constraints.Required
    @Constraints.Pattern("^[0-9]+$")
    private String mobile;

    @Constraints.Pattern("^[+0-9]+$")
    private String countryCode;

    @Constraints.Required
    private String name;
    private String gcmToken;
    private UserType userType = UserType.NORMAL;
    private String profilePic;
    private Religion religion = Religion.JAINISM;
    private String status;
    @Constraints.Email
    private String email;
    private Address address;

    @Constraints.Pattern("^[0-9]{8}$")
    private String dob; // Format -  DDMMYYYY

    public Address getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public enum UserType {
        NORMAL, EDITOR
    }

    public enum GENDER {
        MALE, FEMALE
    }
}
