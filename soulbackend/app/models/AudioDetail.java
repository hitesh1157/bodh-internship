package models;

/**
 * Created by harshitjain on 02/06/17.
 */
public class AudioDetail {
    private String location;
    private String mp3Location;
    private Integer durationInMillisec;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getDurationInMillisec() {
        return durationInMillisec;
    }

    public void setDurationInMillisec(Integer durationInMillisec) {
        this.durationInMillisec = durationInMillisec;
    }

    public String getMp3Location() {
        return mp3Location;
    }

    public void setMp3Location(String mp3Location) {
        this.mp3Location = mp3Location;
    }

    public AudioDetail(String location, String mp3Location, Integer durationInMillisec) {

        this.location = location;
        this.mp3Location = mp3Location;
        this.durationInMillisec = durationInMillisec;
    }
}
