package models;

import org.mongodb.morphia.annotations.Entity;
import play.data.validation.Constraints;

import java.util.List;

/**
 * Created by harshit on 26/02/17.
 */
@Entity(noClassnameStored = true)
public class Speaker extends ContentBaseModel {


    @Constraints.Required
    private MultiLingual name;
    private SpeakerType speakerType = SpeakerType.PANDIT;
    private String thumbnail;
    private String website;
    private String androidApp;
    private String iOSApp;
    private List<Media> mediaList;
    private String about;
    private KnownLocation knownLocation;
    private List<Book> bookList;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAndroidApp() {
        return androidApp;
    }

    public void setAndroidApp(String androidApp) {
        this.androidApp = androidApp;
    }

    public String getiOSApp() {
        return iOSApp;
    }

    public void setiOSApp(String iOSApp) {
        this.iOSApp = iOSApp;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public MultiLingual getName() {
        return name;
    }

    public void setName(MultiLingual name) {
        this.name = name;
    }

    public SpeakerType getSpeakerType() {
        return speakerType;
    }

    public void setSpeakerType(SpeakerType speakerType) {
        this.speakerType = speakerType;
    }

    public List<Media> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<Media> mediaList) {
        this.mediaList = mediaList;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public KnownLocation getKnownLocation() {
        return knownLocation;
    }

    public void setKnownLocation(KnownLocation knownLocation) {
        this.knownLocation = knownLocation;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
