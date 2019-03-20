package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import common.utils.Response;
import models.Feedback;
import models.FeedbackDao;
import models.UserDao;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by harshitjain on 04/06/17.
 */
public class FeedbackController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public FeedbackController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> create() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            Feedback feedback = Json.fromJson(request, Feedback.class);
            if (request.has("userId")) {
                String userId = request.get("userId").asText();
                feedback.setUser(UserDao.getInstance().user(userId));
            }
            Response response = FeedbackDao.getInstance().insert(feedback);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }
}
