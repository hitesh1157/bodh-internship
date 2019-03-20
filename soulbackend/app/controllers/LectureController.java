package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import common.settings.S3UsingActors;
import common.settings.StartUpHandler;
import common.utils.Response;
import models.*;
import play.Logger;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by harshit on 11/03/17.
 */
public class LectureController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public LectureController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> create() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            Lecture lecture = Json.fromJson(request, Lecture.class);
            if (request.has("speakerId")) {
                String speakerId = request.get("speakerId").asText();
                lecture.setSpeaker(SpeakerDao.getInstance().speaker(speakerId));
            }
            if (request().headers().containsKey("uploaderId")) {
                String uploaderId = request().headers().get("uploaderId")[0];
                Uploader uploader = UploaderDao.getInstance().uploader(uploaderId);
                lecture.setUploadedBy(uploader);
            }
            Response response = LectureDao.getInstance().insert(lecture);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    @With(AccessLoggingAction.class)
    public CompletableFuture<Result> update(String id) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            Lecture lecture = Json.fromJson(request, Lecture.class);
            Response response = LectureDao.getInstance().update(lecture, id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> read(String id) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = LectureDao.getInstance().getLecture(id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAllLectures() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = LectureDao.getInstance().getAllLectures();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getAll(String religion) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = LectureDao.getInstance().getAllLectures(religion);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getUnassignedLectures(String religion) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = LectureDao.getInstance().getUnassignedLectures(religion);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> eligibleForPlaylist(String playlistId) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = LectureDao.getInstance().eligibleForPlaylist(playlistId);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    @With(AccessLoggingAction.class)
    public CompletableFuture<Result> createWithVideoUrl() {
        return CompletableFuture.supplyAsync(() -> {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            String[] urls = (String[]) body.asFormUrlEncoded().get("url");
            String url = urls != null ? urls[0] : null;
            if (url == null) {
                return badRequest(Json.toJson(Response.errorResponse("Url is missing")));
            }
            String[] filenames = (String[]) body.asFormUrlEncoded().get("name");
            String filename = filenames != null ? filenames[0] : null;
            if (filename == null) {
                return badRequest("filename is missing");
            }
            Logger.info("createWithVideoUrl : " + url + " : " + filename);
            Response response = null;
            try {
                response = S3UsingActors.downloadVideoFile(url, filename, request().headers(), body);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            if (videoDetail == null || videoDetail.getAudioLocation() == null)
//                return badRequest("upload error");
//
//            String thumbnailLocation = null;
//            Http.MultipartFormData.FilePart thumbnailFilepart = body.getFile("thumbnail");
//            if (thumbnailFilepart != null) {
//                if (!StartUpHandler.getContentSet().contains(thumbnailFilepart.getContentType())) {
//                    return badRequest(Json.toJson(Response.errorResponse("content is not accepted")));
//                }
//                thumbnailLocation = S3.upload(thumbnailFilepart, null);
//            }
//
//            String nameHi = ((String[]) body.asFormUrlEncoded().get("name-hi"))[0];
//            String nameEn = ((String[]) body.asFormUrlEncoded().get("name-en"))[0];
//            String speakerId = ((String[]) body.asFormUrlEncoded().get("speakerId"))[0];
//            Speaker speaker = SpeakerDao.getInstance().speaker(speakerId);
//            if (speaker == null)
//                return badRequest("Invalid speakerId");
//
//            MultiLingual multiLingual = new MultiLingual();
//            multiLingual.setEn(nameEn);
//            multiLingual.setHi(nameHi);
//
//            Lecture lecture = new Lecture();
//            lecture.setName(multiLingual);
//            lecture.setSource(url);
//            lecture.setThumbnail(thumbnailLocation);
//            lecture.setSpeaker(SpeakerDao.getInstance().speaker(speakerId));
//            lecture.setAudio(videoDetail.getAudioLocation());
//            lecture.setAudioForKitkat(videoDetail.getAudioMp3Location());
//            lecture.setVideo(videoDetail.getVideoLocation());
//            lecture.setDurationInMillisec(videoDetail.getDurationInMillisec());
//            if (request().headers().containsKey("uploaderId")) {
//                String uploaderId = request().headers().get("uploaderId")[0];
//                Uploader uploader = UploaderDao.getInstance().uploader(uploaderId);
//                lecture.setUploadedBy(uploader);
//            }
//            Response response = LectureDao.getInstance().insert(lecture);
            if (response != null && response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    @With(AccessLoggingAction.class)
    public CompletableFuture<Result> createWithAudioUrl() {
        return CompletableFuture.supplyAsync(() -> {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            String[] urls = (String[]) body.asFormUrlEncoded().get("url");
            String url = urls != null ? urls[0] : null;
            if (url == null)
                return badRequest(Json.toJson(Response.errorResponse("Url is missing")));
            String[] filenames = (String[]) body.asFormUrlEncoded().get("name");
            String filename = filenames != null ? filenames[0] : null;
            if (filename == null) {
                return badRequest("filename is missing");
            }

            Response response = null;
            response = S3UsingActors.downloadAudioFile(url, filename, request().headers(), body);

            if (response != null && response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> createWithAudio() {
        return CompletableFuture.supplyAsync(() -> {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart filepart = body.getFile("audio");
            if (filepart == null) {
                return badRequest(Json.toJson(Response.errorResponse("file part is missing")));
            }
            if (!StartUpHandler.getContentSet().contains(filepart.getContentType())) {
                return badRequest(Json.toJson(Response.errorResponse("content is not accepted")));
            }
            String[] filenames = (String[]) body.asFormUrlEncoded().get("name");
            String filename = filenames != null ? filenames[0] : null;
            if (filename == null) {
                return badRequest("filename is missing");
            }

            Response response = null;
            try {
                response = S3UsingActors.dropzonedAudioFile(filepart, filename, request().headers(), body);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            if (audioDetail == null || audioDetail.getLocation() == null)
//                return badRequest("upload error");
////
//            String thumbnailLocation = null;
//            Http.MultipartFormData.FilePart thumbnailFilepart = body.getFile("thumbnail");
//            if (thumbnailFilepart != null) {
//                if (!StartUpHandler.getContentSet().contains(thumbnailFilepart.getContentType())) {
//                    return badRequest(Json.toJson(Response.errorResponse("content is not accepted")));
//                }
//                thumbnailLocation = S3.upload(thumbnailFilepart, null);
////                thumbnailLocation = S3UsingActors.DirectUpload(filepart, null);
//            }
//
//            String nameHi = ((String[]) body.asFormUrlEncoded().get("name-hi"))[0];
//            String nameEn = ((String[]) body.asFormUrlEncoded().get("name-en"))[0];
//            String speakerId = ((String[]) body.asFormUrlEncoded().get("speakerId"))[0];
//
//            MultiLingual multiLingual = new MultiLingual();
//            multiLingual.setEn(nameEn);
//            multiLingual.setHi(nameHi);
//
//            HashMap<Object, Object> lectureMeta = new HashMap<>();
//            lectureMeta.put("multiLingual", multiLingual);
//            lectureMeta.put("thumbnailLocation", thumbnailLocation);
//            lectureMeta.put("speaker", SpeakerDao.getInstance().speaker(speakerId));
//            lectureMeta.put("audioDetailLocation", audioDetail.getLocation());
//            lectureMeta.put("audioForKitkat", audioDetail.getMp3Location());
//            lectureMeta.put("durationInMs", audioDetail.getDurationInMillisec());
//
//
//            Lecture lecture = new Lecture();
//            lecture.setName(multiLingual);
//            lecture.setThumbnail(thumbnailLocation);
//            lecture.setSpeaker(SpeakerDao.getInstance().speaker(speakerId));
//            lecture.setAudio(audioDetail.getLocation());
//            lecture.setAudioForKitkat(audioDetail.getMp3Location());
//            lecture.setDurationInMillisec(audioDetail.getDurationInMillisec());
//            if (request().headers().containsKey("uploaderId")) {
//                String uploaderId = request().headers().get("uploaderId")[0];
//                lectureMeta.put("uploader", UploaderDao.getInstance().uploader(uploaderId));
////                Uploader uploader = UploaderDao.getInstance().uploader(uploaderId);
////                lecture.setUploadedBy(uploader);
//            }


//            Response response = S3UsingActors.InsertLecture(lectureMeta);
//            Response response = LectureDao.getInstance().insert(lecture);


            if (response != null && response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }

        }, ec.current());
    }
}

class AccessLoggingAction extends Action.Simple {

    private Logger.ALogger accessLogger = Logger.of("application");

    public CompletionStage<Result> call(Http.Context ctx) {
        final Http.Request request = ctx.request();
        accessLogger.info("method={} uri={}", request.method(), request.uri());

        return delegate.call(ctx);
    }
}