package common.ActorModels;

import play.Configuration;
import play.mvc.Http;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class DownloadAudioFileMessage {
    public String url, filename;
    public Configuration configuration;
    public Map<String, String[]> headers = new HashMap<String, String[]>();
    public Http.MultipartFormData body;

    public DownloadAudioFileMessage(String url, @Nonnull String filename, Configuration configuration, Map<String, String[]> headers, Http.MultipartFormData body) {
        this.url = url;
        this.filename = filename;
        this.configuration = configuration;
        this.headers.putAll(headers);
        this.body = body;
    }
}
