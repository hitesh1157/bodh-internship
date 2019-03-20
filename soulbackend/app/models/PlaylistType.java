package models;

/**
 * Created by harshitjain on 12/10/17.
 */
public enum PlaylistType {
    ACADEMIC, GENERIC;

    public static PlaylistType fromString(String playlistType) {
        playlistType = playlistType.toUpperCase();
        switch (playlistType) {
            case "ACADEMIC":
                return ACADEMIC;
            case "GENERIC":
                return GENERIC;
        }
        return null;
    }
}
