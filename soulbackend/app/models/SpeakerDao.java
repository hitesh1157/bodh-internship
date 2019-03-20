package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import common.settings.StartUpHandler;
import common.utils.Constants;
import common.utils.Response;
import models.response.SpeakerDetail;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Meta;
import org.mongodb.morphia.query.Query;
import play.libs.Json;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by harshit on 10/03/17.
 */
public class SpeakerDao extends BasicDAO<Speaker, Object> {
    private static volatile SpeakerDao mInstance;
    private Datastore datastore = getDatastore();

    private SpeakerDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static SpeakerDao getInstance() {
        if (mInstance == null) {
            synchronized (SpeakerDao.class) {
                if (mInstance == null)
                    mInstance = new SpeakerDao();
            }
        }
        return mInstance;
    }

    public Response insert(Speaker speaker) {
        datastore.save(speaker);
        ObjectNode objectNode = Json.newObject();
        objectNode.put(Constants.ID, String.valueOf(speaker.id));
        return Response.successResponse(objectNode);
    }

    public Response getSpeaker(String id) {
        Speaker speaker = datastore.get(Speaker.class, new ObjectId(id));
        if (speaker != null)
            return Response.successResponse(speaker);
        else
            return Response.errorResponse("Not found");
    }

    public Response getSpeakerDetail(String id) {
        Speaker speaker = datastore.get(Speaker.class, new ObjectId(id));
        if (speaker != null){
            List<Playlist> playlists = datastore.find(Playlist.class).field("speaker").equal(speaker)
                    .project("speaker", false)
                    .project("createdDate", false)
                    .project("modifiedDate", false)
                    .project("religion", false)
                    .project("publishType", false)
                    .project("sect", false)
                    .asList();
            SpeakerDetail speakerDetail =  new SpeakerDetail(speaker, playlists);
            return Response.successResponse(speakerDetail);
        }
        else
            return Response.errorResponse("Not found");
    }


    public Response getAllSpeakers() {
        List<Speaker> speakers = datastore.find(Speaker.class).asList();
        return Response.successResponse(speakers);
    }

    public Response getSpeakersWithPlaylist() {
        List<Speaker> speakers = datastore.find(Speaker.class).asList();
        List<Speaker> speakerList = new ArrayList<>();
        List<PublishType> publishTypeList = Arrays.asList(PublishType.PUBLISHED_UNVERIFIED, PublishType.PUBLISHED_VERIFIED);
        for (Speaker speaker : speakers) {
            long count = datastore.find(Playlist.class).field("publishType").in(publishTypeList).field("speaker").equal(speaker).count();
            if (count > 0)
                speakerList.add(speaker);
        }
        return Response.successResponse(speakerList);
    }

    public Response getSpeakersWithPlaylist(String sect) {
        Query<Speaker> query = datastore.find(Speaker.class).project("name", true).project("thumbnail", true);
        if(Sect.fromString(sect) != Sect.BOTH){
            query = query.field("sect").equal(Sect.fromString(sect));
        }
        List<Speaker> speakers = query.asList();
        List<Speaker> speakerList = new ArrayList<>();
        List<PublishType> publishTypeList = Arrays.asList(PublishType.PUBLISHED_UNVERIFIED, PublishType.PUBLISHED_VERIFIED);
        for (Speaker speaker : speakers) {
            long count = datastore.find(Playlist.class).field("publishType").in(publishTypeList).field("speaker").equal(speaker).count();
            if (count > 0) {
                speakerList.add(speaker);
            }
        }
        return Response.successResponse(speakerList);
    }

    public Response getAllSpeakers(String religion) {
        List<Speaker> speakers = datastore.find(Speaker.class).field("religion").equal(Religion.fromString(religion)).asList();
        return Response.successResponse(speakers);
    }

    public Response getPopularSpeakers(String religion, String sect) {
        List<Speaker> speakers = new ArrayList<>();
        switch (Religion.fromString(religion)) {
            case JAINISM:
                switch (Sect.fromString(sect)) {
                    case DIGAMBAR:
                        speakers.addAll(getDigambarPopularSpeakers());
                        break;
                    case SVETAMBAR:
                        speakers.addAll(getSvetambarPopularSpeakers());
                        break;
                    case BOTH:
                        speakers.addAll(getDigambarPopularSpeakers());
                        speakers.addAll(getSvetambarPopularSpeakers());
                        break;
                }
                break;
        }
        return Response.successResponse(speakers);
    }

    private List<Speaker> getDigambarPopularSpeakers() {
        List<Speaker> speakers = new ArrayList<>();
        speakers.add(SpeakerDao.getInstance().speaker("59279f43cb6cd909ac7b5b8a"));
        speakers.add(SpeakerDao.getInstance().speaker("59279e26cb6cd909ac7b5b89"));
        speakers.add(SpeakerDao.getInstance().speaker("5927a099cb6cd909ac7b5b8b"));
        speakers.add(SpeakerDao.getInstance().speaker("59279e26cb6cd909ac7b5b89"));
        return speakers;
    }

    private List<Speaker> getSvetambarPopularSpeakers() {
        List<Speaker> speakers = new ArrayList<>();
        return speakers;
    }

    public Speaker speaker(String id) {
        return datastore.get(Speaker.class, new ObjectId(id));
    }

    public Response update(Speaker speaker, String id) {
        try {
            speaker.id = new ObjectId(id);
            Key<Speaker> key = datastore.merge(speaker);
            return Response.successResponse(datastore.get(Speaker.class, speaker.id));
        } catch (Exception e) {
            Utils.logException(e);
            return Response.errorResponse(e.getMessage());
        }
    }
}
