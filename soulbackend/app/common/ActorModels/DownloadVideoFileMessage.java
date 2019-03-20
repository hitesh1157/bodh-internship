package common.ActorModels;

import play.Configuration;
import play.mvc.Http;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class DownloadVideoFileMessage {
    public String url, filename;
    public Configuration configuration;
    public HashMap<String, String[]> headers = new HashMap<String, String[]>();
    public Http.MultipartFormData body ;

    public DownloadVideoFileMessage(String url, @Nonnull String filename, Configuration configuration, Map<String, String[]> headers, Http.MultipartFormData body) {
        this.url = url;
        this.filename = filename;
        this.configuration = configuration;
        this.headers.putAll(headers);
        this.body = body;
    }
}
