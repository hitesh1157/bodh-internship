package models;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;
import play.data.validation.Constraints;

/**
 * Created by harshit on 11/03/17.
 */
@Entity(noClassnameStored = true)
public class Lecture extends ContentBaseModel {
    @Constraints.Required
    private MultiLingual name;

    @Reference
    private Speaker speaker;
    private String thumbnail;
    @Constraints.Required
    private String audio;
    private String audioForKitkat;
    private String video;
    private String source;
    private Integer durationInMillisec;

    public String getAudioForKitkat() {
        return audioForKitkat;
    }

    public void setAudioForKitkat(String audioForKitkat) {
        this.audioForKitkat = audioForKitkat;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getDurationInMillisec() {
        return durationInMillisec;
    }

    public void setDurationInMillisec(Integer durationInMillisec) {
        this.durationInMillisec = durationInMillisec;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public MultiLingual getName() {
        return name;
    }

    public void setName(MultiLingual name) {
        this.name = name;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
