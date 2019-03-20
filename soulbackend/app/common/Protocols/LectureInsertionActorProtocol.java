package common.Protocols;

import common.settings.S3UsingActors;
import common.settings.StartUpHandler;
import common.utils.Response;
import models.*;
import play.mvc.Http;

import java.util.Map;

public class LectureInsertionActorProtocol {

    public static Response insertVideoLecture(VideoDetail videoDetail, Map<String, String[]> headers, Http.MultipartFormData body) {
        if (videoDetail == null || videoDetail.getAudioLocation() == null)
            return Response.errorResponse("upload error.");

        else {
            String[] urls = (String[]) body.asFormUrlEncoded().get("url");
            String url = urls != null ? urls[0] : null;
            if (url == null) {
                return Response.errorResponse("Url is missing");

            } else {

                String thumbnailLocation = null;
                Http.MultipartFormData.FilePart thumbnailFilepart = body.getFile("thumbnail");
                boolean hasValidContent = false;
                if (thumbnailFilepart != null) {
                    hasValidContent = !StartUpHandler.getContentSet().contains(thumbnailFilepart.getContentType());
                    if (hasValidContent) {
                        return Response.errorResponse("content is not accepted");
                    }
                    thumbnailLocation = S3UsingActors.DirectUpload(thumbnailFilepart, null);
                }

                String speakerId = ((String[]) body.asFormUrlEncoded().get("speakerId"))[0];
                Speaker speaker = SpeakerDao.getInstance().speaker(speakerId);
                if (!hasValidContent && speaker != null) {
                    String nameHi = ((String[]) body.asFormUrlEncoded().get("name-hi"))[0];
                    String nameEn = ((String[]) body.asFormUrlEncoded().get("name-en"))[0];

                    MultiLingual multiLingual = new MultiLingual();
                    multiLingual.setEn(nameEn);
                    multiLingual.setHi(nameHi);

                    Lecture lecture = new Lecture();
                    lecture.setName(multiLingual);
                    lecture.setSource(url);
                    lecture.setThumbnail(thumbnailLocation);
                    lecture.setSpeaker(SpeakerDao.getInstance().speaker(speakerId));
                    lecture.setAudio(videoDetail.getAudioLocation());
                    lecture.setAudioForKitkat(videoDetail.getAudioMp3Location());
                    lecture.setVideo(videoDetail.getVideoLocation());
                    lecture.setDurationInMillisec(videoDetail.getDurationInMillisec());
                    if (headers.containsKey("uploaderId")) {
                        String uploaderId = headers.get("uploaderId")[0];
                        Uploader uploader = UploaderDao.getInstance().uploader(uploaderId);
                        lecture.setUploadedBy(uploader);
                    }
                    return LectureDao.getInstance().insert(lecture);
                } else {
                    return Response.errorResponse("Invalid speakerId");
                }
            }
        }
    }

    public static Response insertAudioLecture(AudioDetail audioDetail, Map<String, String[]> headers, Http.MultipartFormData body) {
        if (audioDetail == null || audioDetail.getLocation() == null)
            return Response.errorResponse("upload error.");
        else {
            String thumbnailLocation = null;
            Http.MultipartFormData.FilePart thumbnailFilepart = body.getFile("thumbnail");
            boolean hasValidContent = false;
            if (thumbnailFilepart != null) {
                hasValidContent = !StartUpHandler.getContentSet().contains(thumbnailFilepart.getContentType());
                if (hasValidContent) {
                    return Response.errorResponse("content is not accepted");
                }
                thumbnailLocation = S3UsingActors.DirectUpload(thumbnailFilepart, null);
            }

            if (!hasValidContent) {
                String nameHi = ((String[]) body.asFormUrlEncoded().get("name-hi"))[0];
                String nameEn = ((String[]) body.asFormUrlEncoded().get("name-en"))[0];
                String speakerId = ((String[]) body.asFormUrlEncoded().get("speakerId"))[0];

                MultiLingual multiLingual = new MultiLingual();
                multiLingual.setEn(nameEn);
                multiLingual.setHi(nameHi);

                Lecture lecture = new Lecture();
                lecture.setName(multiLingual);
                lecture.setThumbnail(thumbnailLocation);
                lecture.setSpeaker(SpeakerDao.getInstance().speaker(speakerId));
                lecture.setAudio(audioDetail.getLocation());
                lecture.setAudioForKitkat(audioDetail.getMp3Location());
                lecture.setDurationInMillisec(audioDetail.getDurationInMillisec());

                if (headers.containsKey("uploaderId")) {
                    String uploaderId = headers.get("uploaderId")[0];
                    Uploader uploader = UploaderDao.getInstance().uploader(uploaderId);
                    lecture.setUploadedBy(uploader);
                }

                return LectureDao.getInstance().insert(lecture);
            } else {
                return Response.errorResponse("error");
            }
        }
    }
}
