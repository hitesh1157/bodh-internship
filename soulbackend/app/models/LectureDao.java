package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import common.settings.S3;
import common.settings.StartUpHandler;
import common.utils.Constants;
import common.utils.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;
import org.gagravarr.opus.tools.OpusInfoTool;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import play.Logger;
import play.libs.Json;
import utils.Utils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static common.settings.S3.tempUploadDirPath;

/**
 * Created by harshit on 11/03/17.
 */
public class LectureDao extends BasicDAO<Lecture, Object> {
    private static volatile LectureDao mInstance;
    private Datastore datastore = getDatastore();

    private LectureDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static LectureDao getInstance() {
        if (mInstance == null) {
            synchronized (LectureDao.class) {
                if (mInstance == null)
                    mInstance = new LectureDao();
            }
        }
        return mInstance;
    }

    public Response insert(Lecture lecture) {
        datastore.save(lecture);
        ObjectNode objectNode = Json.newObject();
        objectNode.put(Constants.ID, String.valueOf(lecture.id));
        return Response.successResponse(objectNode);
    }

    public Response insertWithMedia(Lecture lecture) {
        datastore.save(lecture);
        ObjectNode objectNode = Json.newObject();
        objectNode.put(Constants.ID, String.valueOf(lecture.id));
        return Response.successResponse(objectNode);
    }

    public Response getLecture(String id) {
        Lecture lecture = datastore.get(Lecture.class, new ObjectId(id));
        if (lecture != null)
            return Response.successResponse(lecture);
        else
            return Response.errorResponse("Not found");
    }

    public Response getAllLectures() {
        List<Lecture> lectures = datastore.find(Lecture.class).asList();
        return Response.successResponse(lectures);
    }

    public Response getAllLectures(String religion) {
        List<Lecture> lectures = datastore.find(Lecture.class).field("religion").equal(Religion.fromString(religion)).asList();
        return Response.successResponse(lectures);
    }

    public Response getUnassignedLectures() {
        List<Lecture> lectures = datastore.find(Lecture.class).asList();
        List<Playlist> playlists = datastore.find(Playlist.class).asList();
        for (Playlist playlist : playlists)
            lectures.removeAll(playlist.getLectures());
        return Response.successResponse(lectures);
    }

    public Response getUnassignedLectures(String religion) {
        List<Lecture> lectures = datastore.find(Lecture.class).field("religion").equal(Religion.fromString(religion)).asList();
        List<Playlist> playlists = datastore.find(Playlist.class).field("religion").equal(Religion.fromString(religion)).asList();
        for (Playlist playlist : playlists)
            lectures.removeAll(playlist.getLectures());
        return Response.successResponse(lectures);
    }

    public Response eligibleForPlaylist(String playlistId) {
        Playlist playlist = datastore.get(Playlist.class, new ObjectId(playlistId));
        List<Lecture> lectures = datastore.find(Lecture.class).field("religion").equal(playlist.getReligion()).asList();
        List<Playlist> playlists = datastore.find(Playlist.class).field("religion").equal(playlist.getReligion()).asList();
        playlists.remove(playlist);
        for (Playlist pl : playlists)
            lectures.removeAll(pl.getLectures());
        return Response.successResponse(lectures);
    }

    public Integer calculateDuration(@Nullable Lecture lecture) {
        if (lecture == null || lecture.getAudio() == null)
            return null;
        try {
            String sourceFileName = "" + System.currentTimeMillis();
            File sourceFile = new File(tempUploadDirPath + FilenameUtils.getBaseName(sourceFileName));

            FileUtils.copyURLToFile(new URL(lecture.getAudio()), sourceFile);
            Integer duration = OpusInfoTool.getAudioLengthInMills(sourceFile);
            lecture.setDurationInMillisec(duration);
            datastore.merge(lecture);

            FileUtils.deleteQuietly(sourceFile);
            return duration;
        } catch (IOException e) {
            Utils.logException(e);
            return null;
        }

    }

    public Response update(Lecture lecture, String id) {
        try {
            lecture.id = new ObjectId(id);
            Key<Lecture> key = datastore.merge(lecture);
            return Response.successResponse(datastore.get(Lecture.class, lecture.id));
        } catch (Exception e) {
            Utils.logException(e);
            return Response.errorResponse(e.getMessage());
        }
    }

    public Response removeAudioKitkat() {
        List<Lecture> lectures = datastore.find(Lecture.class).asList();
        for (Lecture lecture : lectures) {
            if (lecture.getAudioForKitkat() != null) {
                lecture.setAudioForKitkat("");
                datastore.merge(lecture);
            }
        }
        return Response.successResponse("done");
    }

    public Response addMp3Audio() {
        List<Lecture> lectures = datastore.find(Lecture.class).asList();
        int countConverted = 0;
        int lectureSourceNullCount = 0;
        int countCopied = 0;
        int countNotConverted = 0;
        for (Lecture lecture : lectures) {
            if (lecture.getAudioForKitkat() == null || lecture.getAudioForKitkat().isEmpty()) {
                if (lecture.getSource() == null) {
                    lectureSourceNullCount++;
                } else {
                    if (FilenameUtils.getExtension(lecture.getSource()).equalsIgnoreCase("mp3") ||
                            FilenameUtils.getExtension(lecture.getSource()).equalsIgnoreCase("wma") ||
                            FilenameUtils.getExtension(lecture.getSource()).equalsIgnoreCase("m4a") ||
                            FilenameUtils.getExtension(lecture.getSource()).equalsIgnoreCase("wav")) {
                        countCopied++;
                        lecture.setAudioForKitkat(lecture.getSource());
                        datastore.merge(lecture);
                    } else if (lecture.getSource().contains("https://www.youtube.com/")) {
                        try {
                            String url = lecture.getAudio();
                            String filename = FilenameUtils.getBaseName(lecture.getAudio()).split("_")[0];
                            Logger.info(filename);
                            String sourceFileName = FilenameUtils.getBaseName(filename) + ".opus";
                            File sourceFile = new File(tempUploadDirPath + sourceFileName);
                            FileUtils.copyURLToFile(new URL(url), sourceFile);

                            String destMp3File = tempUploadDirPath + FilenameUtils.getBaseName(filename) + ".mp3";
                            S3.convertToMp3(tempUploadDirPath + sourceFileName, destMp3File);
                            String locationMp3 = S3.uploadFile("file", new File(destMp3File), "audio/mp3", FilenameUtils.getBaseName(filename) + ".mp3");
                            lecture.setAudioForKitkat(locationMp3);
                            datastore.merge(lecture);
                            Logger.info(lecture.getId() + " : " + locationMp3);
                            countConverted++;
                        } catch (Exception e) {
                            Utils.logException(e);
                        }
                    }
//                    else if (FilenameUtils.getExtension(lecture.getSource()).equalsIgnoreCase("wma") ||
//                            FilenameUtils.getExtension(lecture.getSource()).equalsIgnoreCase("m4a") ||
//                            FilenameUtils.getExtension(lecture.getSource()).equalsIgnoreCase("wav")) {
//                        countConverted++;
//                        try {
//                            String url = lecture.getSource();
//                            String filename = FilenameUtils.getBaseName(lecture.getAudio()).split("_")[0];
//                            Logger.debug(filename);
//                            String sourceFileName = "" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(url);
//                            File sourceFile = new File(tempUploadDirPath + FilenameUtils.getBaseName(sourceFileName));
//                            FileUtils.copyURLToFile(new URL(url), sourceFile);
//
//                            String destMp3File = tempUploadDirPath + FilenameUtils.getBaseName(sourceFileName) + ".mp3";
//                            S3.convertToMp3(sourceFile.getPath(), destMp3File);
//                            String locationMp3 = S3.uploadFile("file", new File(destMp3File), "audio/mp3", FilenameUtils.getBaseName(filename) + ".mp3");
//                            lecture.setAudioForKitkat(locationMp3);
//                            datastore.merge(lecture);
//                            S3.removeFiles(destMp3File, tempUploadDirPath + sourceFileName);
//                        } catch (Exception e) {
//                            Utils.logException(e);
//                        }
//                    }
                    else {
                        Logger.info("not converted for source : " + lecture.getSource());
                        countNotConverted++;
                    }
                }
            }
        }
        return Response.successResponse("lectureSourceNullCount : " + lectureSourceNullCount + ", countConverted : " + countConverted + ", " + "countCopied : " + countCopied + ", countNotConverted : " + countNotConverted);
    }

}
