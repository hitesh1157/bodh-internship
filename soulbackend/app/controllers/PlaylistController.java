package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import common.utils.Response;
import models.*;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by harshit on 11/03/17.
 */
public class PlaylistController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public PlaylistController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> create() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            Playlist playlist = Json.fromJson(request, Playlist.class);
            if (request.has("speakerId")) {
                String speakerId = request.get("speakerId").asText();
                playlist.setSpeaker(SpeakerDao.getInstance().speaker(speakerId));
            }
            if (request().headers().containsKey("uploaderId")) {
                String uploaderId = request().headers().get("uploaderId")[0];
                Uploader uploader = UploaderDao.getInstance().uploader(uploaderId);
                playlist.setUploadedBy(uploader);
            }
            Response response = PlaylistDao.getInstance().insert(playlist);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> update(String id) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            Playlist playlist = Json.fromJson(request, Playlist.class);
            if (request.has("speakerId")) {
                String speakerId = request.get("speakerId").asText();
                playlist.setSpeaker(SpeakerDao.getInstance().speaker(speakerId));
            }
            Response response = PlaylistDao.getInstance().update(playlist, id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> read(String id) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = PlaylistDao.getInstance().getPlaylist(id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAllPlaylists() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = PlaylistDao.getInstance().getAllPlaylists();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAll(String religion) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = PlaylistDao.getInstance().getAllPlaylists(religion);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getSpeakerPlaylists(String speakerId) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = PlaylistDao.getInstance().getSpeakerPlaylists(speakerId);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> lectures(String id) {
        return CompletableFuture.supplyAsync(() -> {
            List<PublishType> publishTypeList = Arrays.asList(PublishType.PUBLISHED_UNVERIFIED, PublishType.PUBLISHED_VERIFIED);
//            if (request().headers().containsKey("mobile")) { TODO : enable when content is sorted
//                String mobileNumber = request().headers().get("mobile")[0];
//                if (mobileNumber != null && UserDao.getInstance().getUser(mobileNumber).getUserType() == User.UserType.EDITOR) {
//                    publishTypeList = Arrays.asList(PublishType.values());
//                }
//            }
            Response response = PlaylistDao.getInstance().getLectures(id, publishTypeList);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> add(String id) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            if (request.has("lectures")) {
                JsonNode lectures = request.get("lectures");
                List<String> lectureIds = new ArrayList<String>();
                for (final JsonNode objNode : lectures) {
                    lectureIds.add(objNode.asText());
                }
                Response response = PlaylistDao.getInstance().add(lectureIds, id);
                if (response.getSuccess()) {
                    return ok(Json.toJson(response));
                } else {
                    return badRequest(Json.toJson(response));
                }
            } else {
                return badRequest();
            }
        }, ec.current());
    }

    public CompletableFuture<Result> remove(String id) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            if (request.has("lectures")) {
                JsonNode lectures = request.get("lectures");
                List<String> lectureIds = new ArrayList<String>();
                for (final JsonNode objNode : lectures) {
                    lectureIds.add(objNode.asText());
                }
                Response response = PlaylistDao.getInstance().remove(lectureIds, id);
                if (response.getSuccess()) {
                    return ok(Json.toJson(response));
                } else {
                    return badRequest(Json.toJson(response));
                }
            } else {
                return badRequest();
            }
        }, ec.current());
    }
}
