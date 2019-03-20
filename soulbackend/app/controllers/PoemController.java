package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import common.settings.S3;
import common.utils.Response;
import models.*;
import play.api.Application;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.Utils;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by harshit on 11/03/17.
 */
public class PoemController extends Controller {
    private HttpExecutionContext ec;
    private Application application;

    @Inject
    public PoemController(HttpExecutionContext ec, Application application) {
        this.ec = ec;
        this.application = application;
    }

    public CompletableFuture<Result> create() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            Poem poem = Json.fromJson(request, Poem.class);
            if (request().headers().containsKey("uploaderId")) {
                String uploaderId = request().headers().get("uploaderId")[0];
                Uploader uploader = UploaderDao.getInstance().uploader(uploaderId);
                poem.setUploadedBy(uploader);
            }
            Response response = PoemDao.getInstance().insert(poem);
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
            Poem poem = Json.fromJson(request, Poem.class);
            Response response = PoemDao.getInstance().update(poem, id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> read(String id) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = PoemDao.getInstance().getPoem(id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAll(String religion) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = PoemDao.getInstance().getAllPoems(religion);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getTypes(String religion) {
        return CompletableFuture.supplyAsync(() -> {

            Response response = Response.successResponse(PoemType.values());
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getPoems(String religion, String poemType) {
        return CompletableFuture.supplyAsync(() -> {

            Response response = PoemDao.getInstance().getAllPoems(religion, poemType);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> createWithAudioUrl() {
        return CompletableFuture.supplyAsync(() -> {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            String[] urls = (String[]) body.asFormUrlEncoded().get("url");
            String url = urls != null ? urls[0] : null;
            if (url == null)
                return badRequest(Json.toJson(Response.errorResponse("Url is missing")));
            String[] filenames = (String[]) body.asFormUrlEncoded().get("name");
            String filename = filenames != null ? filenames[0] : null;

            String nameHi = ((String[]) body.asFormUrlEncoded().get("name-hi"))[0];
            String nameEn = ((String[]) body.asFormUrlEncoded().get("name-en"))[0];

            AudioDetail audioDetail = S3.downloadConvertAndUpload(url, filename);
            if (audioDetail == null || audioDetail.getLocation() == null)
                return badRequest("upload error");


            String text = ((String[]) body.asFormUrlEncoded().get("text"))[0];
            String type = ((String[]) body.asFormUrlEncoded().get("type"))[0];
            if (type == null || type.isEmpty())
                return badRequest("poem type is missing");
            PoemType poemType = PoemType.fromString(type);

            MultiLingual multiLingual = new MultiLingual();
            multiLingual.setEn(nameEn);
            multiLingual.setHi(nameHi);

            Poem poem = new Poem();
            poem.setName(multiLingual);
            poem.setSource(url);
            poem.setAudio(audioDetail.getLocation());
            poem.setPoemType(poemType);
            poem.setText(text);

            if (request().headers().containsKey("uploaderId")) {
                String uploaderId = request().headers().get("uploaderId")[0];
                Uploader uploader = UploaderDao.getInstance().uploader(uploaderId);
                poem.setUploadedBy(uploader);
            }
            Response response = PoemDao.getInstance().insert(poem);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

}
