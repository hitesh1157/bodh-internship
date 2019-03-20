package common.ActorModels;

import play.Configuration;
import play.Logger;
import play.mvc.Http;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileMessage {
    public String filename, key;
    public Path path;
    public Configuration configuration;
    public Http.MultipartFormData body;
    public Map<String, String[]> headers = new HashMap<String, String[]>();

    public FileMessage(@Nonnull String filename, Configuration configuration, Map<String, String[]> headers, Http.MultipartFormData body, Path path, String key) {
        this.filename = filename;
        this.configuration = configuration;
        this.headers = headers;
        this.body = body;
        this.path = path;
        this.key = key;
    }
}