package common.ActorModels;

import models.AudioDetail;
import models.VideoDetail;
import play.mvc.Http;

import java.util.Map;

public class StoreLectureMessage {
    public VideoDetail videoDetail = null;
    public AudioDetail audioDetail = null;
    public Map<String, String[]> headers;
    public Http.MultipartFormData body;

    public StoreLectureMessage(AudioDetail audioDetail, Map<String, String[]> headers, Http.MultipartFormData body) {
        this.audioDetail = audioDetail;
        this.headers = headers;
        this.body = body;
    }

    public StoreLectureMessage(VideoDetail videoDetail, Map<String, String[]> headers, Http.MultipartFormData body) {
        this.videoDetail = videoDetail;
        this.headers = headers;
        this.body = body;
    }
}