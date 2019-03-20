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
public class CategoryController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public CategoryController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> create() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            Category category = Json.fromJson(request, Category.class);
            if (request().headers().containsKey("uploaderId")) {
                String uploaderId = request().headers().get("uploaderId")[0];
                Uploader uploader = UploaderDao.getInstance().uploader(uploaderId);
                category.setUploadedBy(uploader);
            }
            Response response = CategoryDao.getInstance().insert(category);
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
            Category category = Json.fromJson(request, Category.class);
            Response response = CategoryDao.getInstance().update(category, id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> reorder(){
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            JsonNode ids = request.get("Ids");

            Response response = Response.errorResponse("Bad request.");
            List<String> categoryIds = new ArrayList<String>();
            if(ids.isArray()){
                for(JsonNode objNode : ids){
                    categoryIds.add(objNode.asText());
                }
                response = CategoryDao.getInstance().updateOrder(categoryIds);
            }

            if(response.getSuccess()){
                return ok(Json.toJson(response));
            } else{
                return badRequest(Json.toJson(response));
            }


        }, ec.current());
    }

    public CompletableFuture<Result> read(String id) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = CategoryDao.getInstance().getCategory(id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAllCategories() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = CategoryDao.getInstance().getAllCategories();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAll(String religion) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = CategoryDao.getInstance().getAllCategories(religion);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAllPublished(String religion) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = CategoryDao.getInstance().getAllPublishedCategories(religion);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAllPublishedSect(String religion, String sect) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = CategoryDao.getInstance().getAllPublishedCategories(religion, sect);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> playlists(String id) {
        return CompletableFuture.supplyAsync(() -> {
            List<PublishType> publishTypeList = Arrays.asList(PublishType.PUBLISHED_UNVERIFIED, PublishType.PUBLISHED_VERIFIED);
//            if (request().headers().containsKey("mobile")) { TODO : enable when content is sorted
//                String mobileNumber = request().headers().get("mobile")[0];
//                if (mobileNumber != null && UserDao.getInstance().getUser(mobileNumber).getUserType() == User.UserType.EDITOR) {
//                    publishTypeList = Arrays.asList(PublishType.values());
//                }
//            }
            Response response = CategoryDao.getInstance().getPlaylists(id, publishTypeList);
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
            if (request.has("playlists")) {
                JsonNode playlists = request.get("playlists");
                List<String> playlistIds = new ArrayList<String>();
                for (final JsonNode objNode : playlists) {
                    playlistIds.add(objNode.asText());
                }
                Response response = CategoryDao.getInstance().add(playlistIds, id);
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
            if (request.has("playlists")) {
                JsonNode playlists = request.get("playlists");
                List<String> playlistIds = new ArrayList<String>();
                for (final JsonNode objNode : playlists) {
                    playlistIds.add(objNode.asText());
                }
                Response response = CategoryDao.getInstance().remove(playlistIds, id);
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
