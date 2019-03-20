package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import common.utils.Response;
import models.*;
import play.api.Application;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Utils;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by harshitjain on 16/09/17.
 */
public class DataModificationController extends Controller {
    private HttpExecutionContext ec;
    private Application application;

    @Inject
    public DataModificationController(HttpExecutionContext ec, Application application) {
        this.ec = ec;
        this.application = application;
    }

    public CompletableFuture<Result> removeAudioMp3() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = LectureDao.getInstance().removeAudioKitkat();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> assignPlaylistType() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = PlaylistDao.getInstance().assignDefaultPlaylistType();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }


    public CompletableFuture<Result> convertToMp3() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = LectureDao.getInstance().addMp3Audio();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> refreshPlaylist() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = PlaylistDao.getInstance().refreshAllPlaylists();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> refreshContentMetadata() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = PlaylistDao.getInstance().refreshContentMetadata();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> addAarti() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            JsonNode node = request.get("aarti");
            if (node.isArray()) {
                for (JsonNode objNode : node) {
                    Poem poem = new Poem();
                    poem.setPoemType(PoemType.AARTI);
                    String str = Utils.htmlToString(objNode.get("path").asText(), application);
                    poem.setText(str);
                    poem.setPosition(objNode.get("order").asInt());
                    MultiLingual name = new MultiLingual();
                    name.setEn(objNode.get("name").asText());
                    name.setHi(objNode.get("name_h").asText());
                    poem.setName(name);
                    PoemDao.getInstance().insert(poem);
                }
            }

            return ok();
        }, ec.current());
    }

    public CompletableFuture<Result> addBhajan() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            JsonNode node = request.get("bhajan");
            if (node.isArray()) {
                for (JsonNode objNode : node) {
                    Poem poem = new Poem();
                    poem.setPoemType(PoemType.BHAJAN);
                    String str = Utils.htmlToString(objNode.get("path").asText(), application);
                    poem.setText(str);
                    MultiLingual name = new MultiLingual();
                    name.setEn(objNode.get("name").asText());
                    name.setHi(objNode.get("name_h").asText());
                    poem.setName(name);
                    PoemDao.getInstance().insert(poem);
                }
            }
            return ok();
        }, ec.current());
    }

    public CompletableFuture<Result> addBhakti() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            JsonNode node = request.get("bhakti");
            if (node.isArray()) {
                for (JsonNode objNode : node) {
                    Poem poem = new Poem();
                    poem.setPoemType(PoemType.BHAKTI);
                    String str = Utils.htmlToString(objNode.get("path").asText(), application);
                    poem.setText(str);
                    MultiLingual name = new MultiLingual();
                    name.setEn(objNode.get("name").asText());
                    name.setHi(objNode.get("name_h").asText());
                    poem.setName(name);
                    PoemDao.getInstance().insert(poem);
                }
            }
            return ok();
        }, ec.current());
    }

    public CompletableFuture<Result> addChalisa() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            JsonNode node = request.get("chalisa");
            if (node.isArray()) {
                for (JsonNode objNode : node) {
                    Poem poem = new Poem();
                    poem.setPoemType(PoemType.CHALISA);
                    poem.setPosition(objNode.get("order").asInt());
                    String str = Utils.htmlToString(objNode.get("path").asText(), application);
                    poem.setText(str);
                    MultiLingual name = new MultiLingual();
                    name.setEn(objNode.get("name").asText());
                    name.setHi(objNode.get("name_h").asText());
                    poem.setName(name);
                    PoemDao.getInstance().insert(poem);
                }
            }
            return ok();
        }, ec.current());
    }


    public CompletableFuture<Result> addPooja() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            JsonNode node = request.get("puja");
            if (node.isArray()) {
                for (JsonNode objNode : node) {
                    Poem poem = new Poem();
                    poem.setPoemType(PoemType.POOJA);
                    poem.setPosition(objNode.get("order").asInt());
                    String str = Utils.htmlToString(objNode.get("path").asText(), application);
                    poem.setText(str);
                    MultiLingual name = new MultiLingual();
                    name.setEn(objNode.get("name").asText());
                    name.setHi(objNode.get("name_h").asText());
                    poem.setName(name);
                    PoemDao.getInstance().insert(poem);
                }
            }
            return ok();
        }, ec.current());
    }

    public CompletableFuture<Result> addStotra() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            JsonNode node = request.get("stotra");
            if (node.isArray()) {
                for (JsonNode objNode : node) {
                    Poem poem = new Poem();
                    poem.setPoemType(PoemType.STOTRA);
                    String str = Utils.htmlToString(objNode.get("path").asText(), application);
                    poem.setText(str);
                    MultiLingual name = new MultiLingual();
                    name.setEn(objNode.get("name").asText());
                    name.setHi(objNode.get("name_h").asText());
                    poem.setName(name);
                    PoemDao.getInstance().insert(poem);
                }
            }
            return ok();
        }, ec.current());
    }


    public CompletableFuture<Result> addStuti() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            JsonNode node = request.get("stuti");
            if (node.isArray()) {
                for (JsonNode objNode : node) {
                    Poem poem = new Poem();
                    poem.setPoemType(PoemType.STUTI);
                    String str = Utils.htmlToString(objNode.get("path").asText(), application);
                    poem.setText(str);
                    MultiLingual name = new MultiLingual();
                    name.setEn(objNode.get("name").asText());
                    name.setHi(objNode.get("name_h").asText());
                    poem.setName(name);
                    PoemDao.getInstance().insert(poem);
                }
            }
            return ok();
        }, ec.current());
    }

}
