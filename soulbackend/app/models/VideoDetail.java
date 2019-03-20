package models;

/**
 * Created by harshitjain on 02/06/17.
 */
public class VideoDetail {
    private String audioLocation;
    private String audioMp3Location;
    private String videoLocation;
    private Integer durationInMillisec;

    public VideoDetail(String audioLocation, String audioMp3Location, String videoLocation, Integer durationInMillisec) {
        this.audioLocation = audioLocation;
        this.audioMp3Location = audioMp3Location;
        this.videoLocation = videoLocation;
        this.durationInMillisec = durationInMillisec;
    }

    public String getAudioMp3Location() {
        return audioMp3Location;
    }

    public void setAudioMp3Location(String audioMp3Location) {
        this.audioMp3Location = audioMp3Location;
    }

    public String getAudioLocation() {
        return audioLocation;
    }

    public void setAudioLocation(String audioLocation) {
        this.audioLocation = audioLocation;
    }

    public String getVideoLocation() {
        return videoLocation;
    }

    public void setVideoLocation(String videoLocation) {
        this.videoLocation = videoLocation;
    }

    public Integer getDurationInMillisec() {
        return durationInMillisec;
    }

    public void setDurationInMillisec(Integer durationInMillisec) {
        this.durationInMillisec = durationInMillisec;
    }


}
