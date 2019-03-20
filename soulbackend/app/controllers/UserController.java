package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import common.utils.Response;
import models.User;
import models.UserAnalytics;
import models.UserDao;
import models.UserSettings;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by harshitjain on 27/03/17.
 */
public class UserController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public UserController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> changeMobileNumber() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            String oldMobile = request.get("oldMobile").asText();
            String newMobile = request.get("newMobile").asText();
            if (oldMobile != null && newMobile != null) {
                User user = UserDao.getInstance().changePhoneNumber(oldMobile, newMobile);
                if (user != null) {
                    return ok(Json.toJson(Response.successResponse(user)));
                } else {
                    return ok(Json.toJson(Response.errorResponse(null)));
                }
            } else {
                return badRequest();
            }
        }, ec.current());
    }

    public CompletableFuture<Result> updateGcmToken() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            String mobile = request.get("mobile").asText();
            String gcmToken = request.get("gcmToken").asText();
            String countryCode = null;
            if (request.has("countryCode")) {
                countryCode = request.get("countryCode").asText();
                if (mobile != null && gcmToken != null && countryCode != null) {
                    User user = UserDao.getInstance().updateGcmToken(countryCode, mobile, gcmToken);
                    if (user != null) {
                        return ok(Json.toJson(Response.successResponse(user)));
                    } else {
                        return ok(Json.toJson(Response.errorResponse(null)));
                    }
                } else {
                    return badRequest();
                }
            } else {
                if (mobile != null && gcmToken != null) {
                    User user = UserDao.getInstance().updateGcmToken(mobile, gcmToken);
                    if (user != null) {
                        return ok(Json.toJson(Response.successResponse(user)));
                    } else {
                        return ok(Json.toJson(Response.errorResponse(null)));
                    }
                } else {
                    return badRequest();
                }
            }
        }, ec.current());
    }

    public CompletableFuture<Result> updateUserAnalytics() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            UserAnalytics userAnalytics = Json.fromJson(request, UserAnalytics.class);
            if (request().headers().containsKey("userId")) {
                String userId = request().headers().get("userId")[0];
                userAnalytics.setUser(UserDao.getInstance().user(userId));
                Response response = UserDao.getInstance().updateUserAnalytics(userAnalytics);
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

    public CompletableFuture<Result> updateUserSettings() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            UserSettings userSettings = Json.fromJson(request, UserSettings.class);
            if (request().headers().containsKey("userId")) {
                String userId = request().headers().get("userId")[0];
                userSettings.setUser(UserDao.getInstance().user(userId));
                Response response = UserDao.getInstance().updateUserSettings(userSettings);
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

    public CompletableFuture<Result> readUserSettings() {
        return CompletableFuture.supplyAsync(() -> {
            if (request().headers().containsKey("userId")) {
                String userId = request().headers().get("userId")[0];
                Response response = UserDao.getInstance().readUserSettings(userId);
                return ok(Json.toJson(response));
            } else {
                return badRequest("userId is missing");
            }

        }, ec.current());
    }

//    public CompletableFuture<Result> update(String id) {
//        return CompletableFuture.supplyAsync(() -> {
//            JsonNode request = request().body().asJson();
//            User user = Json.fromJson(request, User.class);
//            Response response = UserDao.getInstance().update(user, id);
//            if (response.getSuccess()) {
//                return ok(Json.toJson(response));
//            } else {
//                return badRequest(Json.toJson(response));
//            }
//        }, ec.current());
//    }

    public CompletableFuture<Result> updateProfile(String id) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            String status = null, name = null, email = null, profilePic = null;
            if (request.has("name"))
                name = request.get("name").asText();
            if (request.has("status"))
                status = request.get("status").asText();
            if (request.has("email"))
                email = request.get("email").asText();
            if (request.has("profilePic"))
                profilePic = request.get("profilePic").asText();
            Response response = UserDao.getInstance().updateProfile(name, email, status, profilePic, id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }
}
