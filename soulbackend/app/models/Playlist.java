package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;
import play.data.validation.Constraints;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshit on 12/03/17.
 */
@Entity(noClassnameStored = true)
public class Playlist extends ContentBaseModel {
    @Constraints.Required
    private MultiLingual name;
    @Reference
    private Speaker speaker;
    @Reference
    private List<Lecture> lectures = new ArrayList<>();
    private Integer durationInMillisec;
    private String thumbnail;
    private String document;
    private String introduction;
    private PlaylistType playlistType;
    @Reference
    private User verifiedBy;

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
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

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public PlaylistType getPlaylistType() {
        return playlistType;
    }

    public void setPlaylistType(PlaylistType playlistType) {
        this.playlistType = playlistType;
    }

    @JsonIgnore
    public List<Lecture> getLectures() {
        return lectures;
    }

    public List<String> getLectureIds() {
        List<String> list = new ArrayList<>();
        for (Lecture lecture : lectures)
            list.add(lecture.getId().toString());
        return list;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public void setLectureIds(List<String> lectureIds) {
        for (String lectureId : lectureIds) {
            Lecture lecture = LectureDao.getInstance().get(new ObjectId(lectureId));
            if (lecture != null) {
                lectures.add(lecture);
            }
        }
    }

    public Integer getDurationInMillisec() {
        return durationInMillisec;
    }

    public void setDurationInMillisec(Integer durationInMillisec) {
        this.durationInMillisec = durationInMillisec;
    }

}
