package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import common.settings.StartUpHandler;
import common.utils.Constants;
import common.utils.Response;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import play.libs.Json;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshit on 11/03/17.
 */
public class PlaylistDao extends BasicDAO<Playlist, Object> {
    private static volatile PlaylistDao mInstance;
    private Datastore datastore = getDatastore();

    private PlaylistDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static PlaylistDao getInstance() {
        if (mInstance == null) {
            synchronized (PlaylistDao.class) {
                if (mInstance == null)
                    mInstance = new PlaylistDao();
            }
        }
        return mInstance;
    }

    public Response insert(Playlist playlist) {
        datastore.save(playlist);
        calculatePlaylistDuration(playlist);
        ObjectNode objectNode = Json.newObject();
        objectNode.put(Constants.ID, String.valueOf(playlist.id));
        return Response.successResponse(objectNode);
    }

    public Response getPlaylist(String id) {
        Playlist playlist = datastore.get(Playlist.class, new ObjectId(id));
        if (playlist != null)
            return Response.successResponse(playlist);
        else
            return Response.errorResponse("Not found");
    }

    public Response refreshAllPlaylists() {
        List<Playlist> playlists = datastore.find(Playlist.class).asList();
        for (Playlist playlist : playlists) {
            calculatePlaylistDuration(playlist);
        }
        return Response.successResponse(null);
    }

    public Response refreshContentMetadata() {
        List<Playlist> playlists = datastore.find(Playlist.class).asList();
        for (Playlist playlist : playlists) {
            playlist.setSect(Sect.DIGAMBAR);
            datastore.merge(playlist);
        }

        List<Category> categories = datastore.find(Category.class).asList();
        for (Category category : categories) {
            category.setSect(Sect.DIGAMBAR);
            datastore.merge(category);
        }

        List<Lecture> lectures = datastore.find(Lecture.class).asList();
        for (Lecture lecture : lectures) {
            lecture.setSect(Sect.DIGAMBAR);
            datastore.merge(lecture);
        }

        List<Speaker> speakers = datastore.find(Speaker.class).asList();
        for (Speaker speaker : speakers) {
            speaker.setSect(Sect.DIGAMBAR);
            datastore.merge(speaker);
        }

        return Response.successResponse(null);
    }

    private void calculatePlaylistDuration(Playlist playlist) {
        int duration = 0;
        List<Lecture> lectures = playlist.getLectures();
        for (Lecture lecture : lectures) {
            if (lecture.getDurationInMillisec() != null)
                duration += lecture.getDurationInMillisec();
            else if (lecture.getAudio() != null) {
                Integer lectureDuration = LectureDao.getInstance().calculateDuration(lecture);
                if (lectureDuration != null)
                    duration += lectureDuration;
            }
        }
        playlist.setDurationInMillisec(duration == 0 ? null : duration);
        datastore.merge(playlist);
    }

    public Response getAllPlaylists() {
        List<Playlist> playlists = datastore.find(Playlist.class).asList();
        return Response.successResponse(playlists);
    }

    public Response getAllPlaylists(String religion) {
        List<Playlist> playlists = datastore.find(Playlist.class).field("religion").equal(Religion.fromString(religion)).asList();
        return Response.successResponse(playlists);
    }

    public Response getSpeakerPlaylists(String speakerId) {
        Speaker speaker = SpeakerDao.getInstance().speaker(speakerId);
        List<Playlist> playlists = datastore.find(Playlist.class).field("speaker").equal(speaker).asList();
        return Response.successResponse(playlists);
    }

    public Response getLectures(String id, List<PublishType> values) {
        Playlist playlist = datastore.get(Playlist.class, new ObjectId(id));
        List<Lecture> lectures = playlist.getLectures();
        List<Lecture> finalLectues = new ArrayList<>();
        for (Lecture lecture : lectures) {
            if (values.contains(lecture.getPublishType())) {
                finalLectues.add(lecture);
            }
        }
        return Response.successResponse(finalLectues);
    }

    public Response update(Playlist playlist, String id) {
        try {
            playlist.id = new ObjectId(id);
            Key<Playlist> key = datastore.merge(playlist);
            calculatePlaylistDuration(playlist);
            return Response.successResponse(datastore.get(Playlist.class, playlist.id));
        } catch (Exception e) {
            Utils.logException(e);
            return Response.errorResponse(e.getMessage());
        }
    }

    public Response add(List<String> lectureIds, String id) {
        Playlist playlist = datastore.get(Playlist.class, new ObjectId(id));
        List<Lecture> lectures = playlist.getLectures();
        List<String> lectureStrings = playlist.getLectureIds();
        for (String lectureId : lectureIds) {
            if (!lectureStrings.contains(lectureId)) {
                Lecture lecture = datastore.get(Lecture.class, new ObjectId(lectureId));
                if (lecture != null)
                    lectures.add(lecture);
            }
        }
        playlist.setLectures(lectures);
        datastore.save(playlist);
        calculatePlaylistDuration(playlist);
        return Response.successResponse(playlist);
    }

    public Response remove(List<String> lectureIds, String id) {
        Playlist playlist = datastore.get(Playlist.class, new ObjectId(id));
        List<Lecture> lectures = playlist.getLectures();
        for (String lectureId : lectureIds) {
            Lecture lecture = datastore.get(Lecture.class, new ObjectId(lectureId));
            if (lecture != null) {
                lectures.remove(lecture);
            }
        }
        playlist.setLectures(lectures);
        datastore.save(playlist);
        calculatePlaylistDuration(playlist);
        return Response.successResponse(playlist);
    }

    public Response assignDefaultPlaylistType() {
        List<Playlist> playlists = datastore.find(Playlist.class).asList();
        for (Playlist playlist : playlists) {
            playlist.setPlaylistType(PlaylistType.ACADEMIC);
            datastore.merge(playlist);
        }
        return Response.successResponse(null);
    }

}
