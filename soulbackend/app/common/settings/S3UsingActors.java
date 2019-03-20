package common.settings;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import common.ActorModels.*;
import common.Actors.*;
import common.Protocols.ConvertProtocol;
import common.Protocols.RemovalProtocol;
import common.utils.Response;
import models.AudioDetail;
import models.VideoDetail;
import org.apache.commons.io.FilenameUtils;
import org.gagravarr.opus.tools.OpusInfoTool;
import play.Configuration;
import play.Logger;
import play.mvc.Http;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static akka.pattern.PatternsCS.ask;
import static common.settings.S3.tempUploadDirPath;

@Singleton
public class S3UsingActors {
    private static ActorRef downloadActor, convertToMp3Actor, convertToOpusActor, uploadActor, lectureInsertionActor;
    public static Map<String, Object> map = new HashMap<String, Object>();
    private static Configuration conf;

    @Inject
    public S3UsingActors(Configuration configuration) {
        conf = configuration;

    }

    public static class ActorSysContainer {
        private ActorSystem sys;

        private ActorSysContainer() {
            sys = ActorSystem.create("sys");
        }

        public ActorSystem getSystem() {
            return sys;
        }

        private static ActorSysContainer instance = null;

        public static synchronized ActorSysContainer getInstance() {
            if (instance == null) {
                instance = new ActorSysContainer();
            }
            return instance;
        }
    }

    public static Response downloadVideoFile(String url, @Nonnull String filename, Map<String, String[]> headers, Http.MultipartFormData body) throws InterruptedException {
        DownloadVideoFileMessage downloadVideoFileMessage = new DownloadVideoFileMessage(url, filename, conf, headers, body);
        if (downloadActor == null) {
            downloadActor = ActorSysContainer.getInstance().getSystem().actorOf(DownloadActor.getProps(), "DownloadFile");
        }
        CompletableFuture<Object> downloadFileMessage = ask(downloadActor, downloadVideoFileMessage, 1000000).toCompletableFuture();
        Object videoFileObject = (Object) downloadFileMessage.join();

        FileMessage videoFileMessage;
        if (videoFileObject instanceof String) {
            return Response.errorResponse("video file is corrupted");
        } else {
            videoFileMessage = (FileMessage) videoFileObject;
        }

        String destFileName = tempUploadDirPath + FilenameUtils.getBaseName(videoFileMessage.path.toString());
        String baseExtension = "." + FilenameUtils.getExtension(videoFileMessage.path.toString());

        if (convertToOpusActor == null) {
            convertToOpusActor = ActorSysContainer.getInstance().getSystem().actorOf(ConvertToOpusActor.getProps(), "convertToOpusActor");
        }
        if (convertToMp3Actor == null) {
            convertToMp3Actor = ActorSysContainer.getInstance().getSystem().actorOf(ConvertToMp3Actor.getProps(), "convertToMp3Actor");
        }
        CompletableFuture<Object> convertToOpus = ask(convertToOpusActor, videoFileMessage, 1000000).toCompletableFuture();
        CompletableFuture<Object> convertToMP3 = ask(convertToMp3Actor, videoFileMessage, 1000000).toCompletableFuture();
        String locationMP3 = (String) convertToMP3.join();
        String locationOpus = (String) convertToOpus.join();
        Logger.info("S3UsingActors : locationOpus : " + locationOpus);
        Logger.info("S3UsingActors : locationMP3 : " + locationMP3);

        if (!locationMP3.startsWith("http")) {
            RemovalProtocol.removeFiles(destFileName + ".opus", destFileName + ".mp3", destFileName + baseExtension);
            return Response.errorResponse(locationMP3);
        }
        if (!locationOpus.startsWith("http")) {
            RemovalProtocol.removeFiles(destFileName + ".opus", destFileName + ".mp3", destFileName + baseExtension);
            return Response.errorResponse(locationOpus);
        }

        String destOpusFile = destFileName + ".opus";
        File audioDestinationFile = new File(destOpusFile);
        Integer duration = OpusInfoTool.getAudioLengthInMills(audioDestinationFile);
        VideoDetail videoDetail = new VideoDetail(locationOpus, locationMP3, null, duration);

        if (lectureInsertionActor == null) {
            lectureInsertionActor = ActorSysContainer.getInstance().getSystem().actorOf(LectureInsertionActor.getProps(), "lectureInsertionActor");
        }
        StoreLectureMessage storeLectureMessage = new StoreLectureMessage(videoDetail, headers, body);
        CompletableFuture<Object> insertResponse = ask(lectureInsertionActor, storeLectureMessage, 10000).toCompletableFuture();
        Response response = (Response) insertResponse.join();
        Logger.info("S3UsingActors : lecture created");

        RemovalProtocol.removeFiles(destFileName + ".opus", destFileName + ".mp3", destFileName + baseExtension);
        return response;
    }

    public static Response downloadAudioFile(String url, @Nonnull String filename, Map<String, String[]> headers, Http.MultipartFormData body) {
        DownloadAudioFileMessage downloadAudioFileMessage = new DownloadAudioFileMessage(url, filename, conf, headers, body);
        if (downloadActor == null) {
            downloadActor = ActorSysContainer.getInstance().getSystem().actorOf(DownloadActor.getProps(), "DownloadFile");
            Logger.info("downloadAudioFile downloadActor path : " + downloadActor.path());
        }
        CompletableFuture<Object> fileMessage = ask(downloadActor, downloadAudioFileMessage, 1000000).toCompletableFuture();
        Object audioFileObject = (Object) fileMessage.join();

        FileMessage audioFileMessage;
        if (audioFileObject instanceof String) {
            return Response.errorResponse("video file is corrupted");
        } else {
            audioFileMessage = (FileMessage) audioFileObject;
        }
        Logger.info("audio file saved successfully... in S3UsingActors");

        String destFileName = tempUploadDirPath + FilenameUtils.getBaseName(audioFileMessage.path.toString());

        if (convertToOpusActor == null) {
            convertToOpusActor = ActorSysContainer.getInstance().getSystem().actorOf(ConvertToOpusActor.getProps(), "convertToOpusActor");
            Logger.info("downloadAudioFile convertToOpusActor path : " + convertToOpusActor.path());
        }
        CompletableFuture<Object> convertToOpus = ask(convertToOpusActor, audioFileMessage, 1000000).toCompletableFuture();

        String locationMP3;
        if (FilenameUtils.getExtension(url).equalsIgnoreCase("mp3")) {
            locationMP3 = url;
        } else {
            if (convertToMp3Actor == null) {
                convertToMp3Actor = ActorSysContainer.getInstance().getSystem().actorOf(ConvertToMp3Actor.getProps(), "convertToMp3Actor");
                Logger.info("downloadAudioFile convertToMp3Actor path : " + convertToMp3Actor.path());
            }
            CompletableFuture<Object> convertToMP3 = ask(convertToMp3Actor, audioFileMessage, 1000000).toCompletableFuture();
            locationMP3 = (String) convertToMP3.join();
        }

        String locationOpus = (String) convertToOpus.join();
        Logger.info("S3UsingActors : locationOpus : " + locationOpus);
        Logger.info("S3UsingActors : locationMP3 : " + locationMP3);

        if (!locationMP3.startsWith("http")) {
            RemovalProtocol.removeFiles(destFileName + ".opus", destFileName + ".mp3", destFileName);
            return Response.errorResponse(locationMP3);
        }
        if (!locationOpus.startsWith("http")) {
            RemovalProtocol.removeFiles(destFileName + ".opus", destFileName + ".mp3", destFileName);
            return Response.errorResponse(locationOpus);
        }

        String destOpusFile = destFileName + ".opus";
        File audioDestinationFile = new File(destOpusFile);
        Integer duration = OpusInfoTool.getAudioLengthInMills(audioDestinationFile);
        AudioDetail audioDetail = new AudioDetail(locationOpus, locationMP3, duration);

        if (lectureInsertionActor == null) {
            lectureInsertionActor = ActorSysContainer.getInstance().getSystem().actorOf(LectureInsertionActor.getProps(), "lectureInsertionActor");
            Logger.info("downloadAudioFile lectureInsertionActor path : " + lectureInsertionActor.path());
        }
        StoreLectureMessage storeLectureMessage = new StoreLectureMessage(audioDetail, headers, body);
        CompletableFuture<Object> insertResponse = ask(lectureInsertionActor, storeLectureMessage, 10000).toCompletableFuture();
        Response response = (Response) insertResponse.join();
        Logger.info("S3UsingActors : lecture created");

        RemovalProtocol.removeFiles(destFileName + ".opus", destFileName + ".mp3", destFileName);
        return response;
    }

    public static Response dropzonedAudioFile(Http.MultipartFormData.FilePart filepart, @Nonnull String filename, Map<String, String[]> headers, Http.MultipartFormData body) throws InterruptedException {
        FileMessage audioFileMessage = ConvertProtocol.convertAudioFilepartToFile(filepart, filename, conf, headers, body);
        if (audioFileMessage == null) {
            return Response.errorResponse("audio file is corrupted");
        }
        Logger.info("audio file saved successfully... in S3UsingActors");

        String destFileName = tempUploadDirPath + FilenameUtils.getBaseName(audioFileMessage.path.toString());
        String baseExtension = "." + FilenameUtils.getExtension(audioFileMessage.path.toString());

        if (convertToOpusActor == null) {
            convertToOpusActor = ActorSysContainer.getInstance().getSystem().actorOf(ConvertToOpusActor.getProps(), "convertToOpusActor");
        }
        if (convertToMp3Actor == null) {
            convertToMp3Actor = ActorSysContainer.getInstance().getSystem().actorOf(ConvertToMp3Actor.getProps(), "convertToMp3Actor");
        }
        CompletableFuture<Object> convertToOpus = ask(convertToOpusActor, audioFileMessage, 1000000).toCompletableFuture();
        CompletableFuture<Object> convertToMP3 = ask(convertToMp3Actor, audioFileMessage, 1000000).toCompletableFuture();
        String locationMP3 = (String) convertToMP3.join();
        String locationOpus = (String) convertToOpus.join();
        Logger.info("S3UsingActors : locationOpus : " + locationOpus);
        Logger.info("S3UsingActors : locationMP3 : " + locationMP3);

        if (!locationMP3.startsWith("http")) {
            RemovalProtocol.removeFiles(destFileName + ".opus", destFileName + ".mp3", destFileName + baseExtension);
            return Response.errorResponse(locationMP3);
        }
        if (!locationOpus.startsWith("http")) {
            RemovalProtocol.removeFiles(destFileName + ".opus", destFileName + ".mp3", destFileName + baseExtension);
            return Response.errorResponse(locationOpus);
        }


        String destOpusFile = destFileName + ".opus";
        File audioDestinationFile = new File(destOpusFile);
        Integer duration = OpusInfoTool.getAudioLengthInMills(audioDestinationFile);
        AudioDetail audioDetail = new AudioDetail(locationOpus, locationMP3, duration);

        if (lectureInsertionActor == null) {
            lectureInsertionActor = ActorSysContainer.getInstance().getSystem().actorOf(LectureInsertionActor.getProps(), "lectureInsertionActor");
        }
        StoreLectureMessage storeLectureMessage = new StoreLectureMessage(audioDetail, headers, body);
        CompletableFuture<Object> insertResponse = ask(lectureInsertionActor, storeLectureMessage, 10000).toCompletableFuture();
        Response response = (Response) insertResponse.join();
        Logger.info("S3UsingActors : lecture created");

        RemovalProtocol.removeFiles(destFileName + ".opus", destFileName + ".mp3", destFileName + baseExtension);
        return response;
    }

    public static String DirectUpload(Http.MultipartFormData.FilePart filePart, @Nullable String name) {
        if (uploadActor == null) {
            uploadActor = ActorSysContainer.getInstance().getSystem().actorOf(UploadActor.getProps(), "DirectUpload");
        }

        Logger.info("Uploading...");
        UploadFilePartMessage uploadFilePartMessage = new UploadFilePartMessage(filePart, name, conf);
        CompletableFuture<Object> getLocation = ask(uploadActor, uploadFilePartMessage, 1000000).toCompletableFuture();
        String uploadLocation = (String) getLocation.join();

        return uploadLocation;
    }

}