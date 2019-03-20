package common.Protocols;

import common.ActorModels.FileMessage;
import common.ActorModels.UploadFileMessage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import play.Configuration;
import play.Logger;
import play.mvc.Http;
import utils.Utils;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import static common.settings.S3.tempUploadDirPath;

public class ConvertProtocol {

    public static UploadFileMessage convertToOpus(String key, @Nonnull String filename, String sourcePath, String destPath, Configuration configuration) {
        try {
            String myCommand = "ffmpeg -i " + Utils.convertForShell(sourcePath) + " -f wav - | opusenc --bitrate 48 - " + Utils.convertForShell(destPath);
            Logger.info("command to exec : " + myCommand);
            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", myCommand});
            p.waitFor();

            String s = "";
            BufferedReader bre = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));


            Logger.info("Error stream");
            while ((s = bre.readLine()) != null) {
                Logger.info(s);
                if (s.contains("error"))
                    return null;
            }

            UploadFileMessage uploadFileMessage = new UploadFileMessage(key, FilenameUtils.getBaseName(filename) + ".opus", destPath, configuration, "audio/opus");
            return uploadFileMessage;
        } catch (Exception e) {
            Utils.logException(e);
            return null;
        }
    }

    public static UploadFileMessage convertToMP3(String key, @Nonnull String filename, String sourcePath, String destPath, Configuration configuration) {
        try {
            if (sourcePath.equals(destPath)) {
                //do nothing
            } else if (FilenameUtils.getExtension(sourcePath).equalsIgnoreCase("mp3") && !sourcePath.equals(destPath)) {
                FileUtils.copyFile(new File(sourcePath), new File(destPath));
            } else {
                String myCommand = "ffmpeg -i " + Utils.convertForShell(sourcePath) + " " + Utils.convertForShell(destPath);
                Logger.info("command to exec : " + myCommand);
                Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", myCommand});
                p.waitFor();

                String s = "";
                BufferedReader bre = new BufferedReader
                        (new InputStreamReader(p.getErrorStream()));


                Logger.info("Error stream");
                while ((s = bre.readLine()) != null) {
                    Logger.info(s);
                    if (s.contains("error"))
                        return null;
                }

            }
            UploadFileMessage uploadFileMessage = new UploadFileMessage(key, FilenameUtils.getBaseName(filename) + ".mp3", destPath, configuration, "audio/mp3");
            return uploadFileMessage;
        } catch (Exception e) {
            Utils.logException(e);
            return null;
        }
    }

    public static FileMessage convertAudioFilepartToFile(Http.MultipartFormData.FilePart filepart, @Nonnull String filename, Configuration configuration, Map<String, String[]> headers, Http.MultipartFormData body) {
        try {
            filename = filename.trim();
            Path path = new File(tempUploadDirPath + filepart.getFilename()).toPath();
            Files.copy(new FileInputStream((File) filepart.getFile()), path, StandardCopyOption.REPLACE_EXISTING);

            FileMessage audioFileMessage = new FileMessage(filename, configuration, headers, body, path, filepart.getKey());
            return audioFileMessage;
        } catch (Exception e) {
            Utils.logException(e);
            return null;
        }
    }
}
