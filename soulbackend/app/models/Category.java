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
public class Category extends ContentBaseModel {
    @Constraints.Required
    private MultiLingual name;

    @Reference
    private List<Playlist> playlists = new ArrayList<>();
    private String thumbnail;
    private int position = 10000;
    private String document;
    private String introduction;
    private int difficultyLevel = -1;
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

    @JsonIgnore
    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public List<String> getPlaylistIds() {
        List<String> list = new ArrayList<>();
        for (Playlist playlist : playlists)
            list.add(playlist.getId().toString());
        return list;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public void setPlaylistIds(List<String> playlistIds) {
        for (String playlistId : playlistIds) {
            Playlist playlist = PlaylistDao.getInstance().get(new ObjectId(playlistId));
            if (playlist != null) {
                playlists.add(playlist);
            }
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}
