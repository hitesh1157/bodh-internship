package models.response;

import models.Playlist;
import models.Speaker;

import java.util.List;

public class SpeakerDetail {
    Speaker speaker;
    List<Playlist> playlists;

    public SpeakerDetail(Speaker speaker, List<Playlist> playlists) {
        this.speaker = speaker;
        this.playlists = playlists;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
}
