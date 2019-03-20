package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import common.utils.Response;
import models.CategoryDao;
import models.Credit;
import models.Temple;
import models.TempleDao;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by harshitjain on 23/09/17.
 */
public class TempleController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public TempleController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> create() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            Temple temple = Json.fromJson(request, Temple.class);
            Response response = TempleDao.getInstance().insert(temple);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAllTemples() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = TempleDao.getInstance().getAllTemples();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }
}
