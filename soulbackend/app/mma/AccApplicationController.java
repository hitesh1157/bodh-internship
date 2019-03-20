package mma;

import com.fasterxml.jackson.databind.JsonNode;
import common.utils.Response;
import models.UserDao;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by harshit on 26/02/17.
 */
public class AccApplicationController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public AccApplicationController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> create() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            AccApplication accApplication = Json.fromJson(request, AccApplication.class);
            if (request().headers().containsKey("userId")) {
                String userId = request().headers().get("userId")[0];
                accApplication.setUser(UserDao.getInstance().user(userId));
            }
            Response response = AccApplicationDao.getInstance().insert(accApplication);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }

        }, ec.current());
    }

    public CompletableFuture<Result> read(String id) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = AccApplicationDao.getInstance().getAccApplication(id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAllApplications() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = AccApplicationDao.getInstance().getAccApplications();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAppliedApplications() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = AccApplicationDao.getInstance().getAppliedApplications();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getApprovedApplications() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = AccApplicationDao.getInstance().getApprovedApplications();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getPaidApplications() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = AccApplicationDao.getInstance().getPaidApplications();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getUserApplications() {
        return CompletableFuture.supplyAsync(() -> {
            if (request().headers().containsKey("userId")) {
                String userId = request().headers().get("userId")[0];
                Response response = AccApplicationDao.getInstance().getUserApplications(userId);
                if (response.getSuccess()) {
                    return ok(Json.toJson(response));
                } else {
                    return badRequest(Json.toJson(response));
                }
            } else {
                return badRequest("userId is missing");
            }

        }, ec.current());
    }

    public CompletableFuture<Result> reject(String id) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            String reason = null;
            if (request != null && request.has("reason"))
                reason = request.get("reason").asText();
            Response response = AccApplicationDao.getInstance().reject(id, reason);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }


    public CompletableFuture<Result> approve(String id) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            String reason = null;
            int approveRoomCount = 0;
            if (request.has("reason"))
                reason = request.get("reason").asText();
            approveRoomCount = request.get("approveRoomCount").asInt();
            Response response = AccApplicationDao.getInstance().approve(id, approveRoomCount, reason);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }


    public CompletableFuture<Result> cancel(String id) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = AccApplicationDao.getInstance().cancel(id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

}
