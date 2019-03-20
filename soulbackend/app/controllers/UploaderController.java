package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import common.utils.Response;
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
public class UploaderController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public UploaderController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> create() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            Uploader uploader = Json.fromJson(request, Uploader.class);

            Response response = UploaderDao.getInstance().insert(uploader);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }

        }, ec.current());
    }

    public CompletableFuture<Result> read(String id) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = UploaderDao.getInstance().getUploader(id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getByEmail(String email) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = UploaderDao.getInstance().getUploaderByEmail(email);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }
}
