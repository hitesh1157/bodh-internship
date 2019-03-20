package common.ActorModels;

import play.Configuration;
import play.mvc.Http;

import javax.annotation.Nullable;

public class UploadFilePartMessage {
    public Http.MultipartFormData.FilePart filePart;
    public String name;
    public Configuration configuration;

    public UploadFilePartMessage(Http.MultipartFormData.FilePart filePart, @Nullable String name, Configuration configuration){
        this.filePart = filePart;
        this.name = name;
        this.configuration = configuration;
    }
}
