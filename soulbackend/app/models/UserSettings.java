package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshitjain on 28/06/17.
 */
public class UserSettings extends BaseModel {
    @Reference
    private User user;
    private String language = "en";
    private boolean autoDownloadEnable = false;
    private List<ObjectId> favouriteSpeakers = new ArrayList<ObjectId>();
    private Sect sect;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isAutoDownloadEnable() {
        return autoDownloadEnable;
    }

    public void setAutoDownloadEnable(boolean autoDownloadEnable) {
        this.autoDownloadEnable = autoDownloadEnable;
    }

    public List<ObjectId> getFavouriteSpeakers() {
        return favouriteSpeakers;
    }

    public void setFavouriteSpeakers(List<ObjectId> favouriteSpeakers) {
        this.favouriteSpeakers = favouriteSpeakers;
    }

    public Sect getSect() {
        return sect;
    }

    public void setSect(Sect sect) {
        this.sect = sect;
    }
}
