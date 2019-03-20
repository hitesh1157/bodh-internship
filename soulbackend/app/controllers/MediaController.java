package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import common.settings.S3;
import common.settings.StartUpHandler;
import common.utils.Response;
import models.AudioDetail;
import models.VideoDetail;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by harshit on 13/03/17.
 */
public class MediaController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public MediaController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> uploadThumbnail() {
        return CompletableFuture.supplyAsync(() -> {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart filepart = body.getFile("file");
            if (filepart == null) {
                return badRequest(Json.toJson(Response.errorResponse("file part is missing")));
            }
            if (!StartUpHandler.getContentSet().contains(filepart.getContentType())) {
                return badRequest(Json.toJson(Response.errorResponse("content is not accepted")));
            }
            String[] filenames = (String[]) body.asFormUrlEncoded().get("name");
            String filename = filenames != null ? filenames[0] : null;
            String location = S3.upload(filepart, filename);

            return ok(Json.toJson(Response.successResponse(location)));

        }, ec.current());
    }

    public CompletableFuture<Result> copyAudioFromUrl() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            String filename = request.get("filename").asText();
            String url = request.get("url").asText();

            AudioDetail audioDetail = S3.downloadConvertAndUpload(url, filename);
            if (audioDetail == null || audioDetail.getLocation() == null)
                return badRequest("upload error");
            else
                return ok(Json.toJson(Response.successResponse(audioDetail)));

        }, ec.current());
    }

    public CompletableFuture<Result> uploadAudio() {
        return CompletableFuture.supplyAsync(() -> {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart filepart = body.getFile("file");
            if (filepart == null) {
                return badRequest(Json.toJson(Response.errorResponse("file part is missing")));
            }
            if (!StartUpHandler.getContentSet().contains(filepart.getContentType())) {
                return badRequest(Json.toJson(Response.errorResponse("content is not accepted : " + filepart.getContentType())));
            }
            String[] filenames = (String[]) body.asFormUrlEncoded().get("name");
            String filename = filenames != null ? filenames[0] : null;

            AudioDetail audioDetail = S3.convertAndUpload(filepart, filename);
            if (audioDetail == null || audioDetail.getLocation() == null)
                return badRequest("upload error");
            else
                return ok(Json.toJson(Response.successResponse(audioDetail)));

        }, ec.current());
    }

    public CompletableFuture<Result> copyVideoFromUrl() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            String filename = request.get("filename").asText();
            String url = request.get("url").asText();

            VideoDetail videoDetail = S3.downloadConvertAndUploadVideo(url, filename);
            if (videoDetail == null || videoDetail.getVideoLocation() == null)
                return badRequest("upload error");
            else
                return ok(Json.toJson(Response.successResponse(videoDetail)));

        }, ec.current());
    }

//    public CompletableFuture<Result> uploadVideo() {
//        return CompletableFuture.supplyAsync(() -> {
//            Http.MultipartFormData body = request().body().asMultipartFormData();
//            Http.MultipartFormData.FilePart filepart = body.getFile("file");
//            if (filepart == null) {
//                return badRequest(Json.toJson(Response.errorResponse("file part is missing")));
//            }
//            if (!StartUpHandler.getContentSet().contains(filepart.getContentType())) {
//                return badRequest(Json.toJson(Response.errorResponse("content is not accepted")));
//            }
//            String[] filenames = (String[]) body.asFormUrlEncoded().get("name");
//            String filename = filenames != null ? filenames[0] : null;
//            String location = S3.upload(filepart, filename);
//
//            return ok(Json.toJson(Response.successResponse(location)));
//
//        }, ec.current());
//    }
}
