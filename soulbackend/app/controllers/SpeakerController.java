package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import common.utils.Response;
import models.Speaker;
import models.SpeakerDao;
import models.Uploader;
import models.UploaderDao;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by harshit on 26/02/17.
 */
public class SpeakerController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public SpeakerController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> create() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            Speaker speaker = Json.fromJson(request, Speaker.class);
            if (request().headers().containsKey("uploaderId")) {
                String uploaderId = request().headers().get("uploaderId")[0];
                Uploader uploader = UploaderDao.getInstance().uploader(uploaderId);
                speaker.setUploadedBy(uploader);
            }
            Response response = SpeakerDao.getInstance().insert(speaker);
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
            Speaker speaker = Json.fromJson(request, Speaker.class);
            Response response = SpeakerDao.getInstance().update(speaker, id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> read(String id) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = SpeakerDao.getInstance().getSpeaker(id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAllSpeakers() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = SpeakerDao.getInstance().getAllSpeakers();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getSpeakersWithPlaylist() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = SpeakerDao.getInstance().getSpeakersWithPlaylist();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getSpeakersWithPlaylistSect(String sect) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = SpeakerDao.getInstance().getSpeakersWithPlaylist(sect);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAll(String religion) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = SpeakerDao.getInstance().getAllSpeakers(religion);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getPopularSpeakers(String religion, String sect) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = SpeakerDao.getInstance().getPopularSpeakers(religion, sect);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getSpeakerDetail(String id) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = SpeakerDao.getInstance().getSpeakerDetail(id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }
}
