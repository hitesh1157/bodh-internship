package models;

import org.mongodb.morphia.annotations.Entity;
import play.data.validation.Constraints;

/**
 * Created by harshit on 12/03/17.
 */
@Entity(noClassnameStored = true)
public class Poem extends ContentBaseModel {
    @Constraints.Required
    private MultiLingual name;
    private int position = 10000;
    private String document;
    private String audio;
    private String video;
    private String text;
    @Constraints.Required
    private PoemType poemType;
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MultiLingual getName() {
        return name;
    }

    public void setName(MultiLingual name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PoemType getPoemType() {
        return poemType;
    }

    public void setPoemType(PoemType poemType) {
        this.poemType = poemType;
    }
}
