package models;

import org.mongodb.morphia.annotations.Entity;
import play.data.validation.Constraints;

/**
 * Created by harshitjain on 26/03/17.
 * UPLOADER + INDIVIDUAL - Content uploaded by him is visible
 * UPLOADER + RELIGIOUS - Content uploaded by him is visible + Categories and speakers available in platform is also visible
 * ADMIN - Everything is visible
 */
@Entity(noClassnameStored = true)
public class Uploader extends BaseModel {
    @Constraints.Required
    private String name;
    @Constraints.Required
    private Religion religion;
    @Constraints.Required
    private AccountType accountType;
    @Constraints.Required
    private AccessLevel accessLevel;
    private String profilePic;
    @Constraints.Email
    private String email;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public enum AccountType {
        INDIVIDUAL, RELIGIOUS
    }

    public enum AccessLevel {
        ADMIN, UPLOADER
    }
}
