package common.ActorModels;

import play.Configuration;

import javax.annotation.Nonnull;

public class UploadFileMessage {
    public String filename, filePath, key, contentType;
    public Configuration configuration;

    public UploadFileMessage(@Nonnull String key, @Nonnull String filename, @Nonnull String destPath, @Nonnull Configuration configuration, @Nonnull String contentType) throws InterruptedException {
        this.key = key;
        this.filename = filename;
        this.filePath = destPath;
        this.configuration = configuration;
        this.contentType = contentType;
    }
}