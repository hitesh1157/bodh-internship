package common.Protocols;

import common.ActorModels.FileMessage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import play.Configuration;
import play.Logger;
import play.mvc.Http;
import utils.Utils;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

import static common.settings.S3.tempUploadDirPath;

public class DownloadProtocol {

    public static FileMessage DownloadVideoFromURL(String url, @Nonnull String filename, Configuration configuration, Map<String, String[]> headers, Http.MultipartFormData body) {
        try {
            filename = filename.trim();
            String sourceFileName = FilenameUtils.getBaseName(filename) + ".m4a";
            String myCommand = "youtube-dl -f 139 -o " + Utils.convertForShell(sourceFileName) + " " + url;
            Logger.info("command to exec : " + myCommand + " , url : " + url);
            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", myCommand}, null, new File(tempUploadDirPath));
            p.waitFor();

            String s = "";
            BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = bre.readLine()) != null) {
                Logger.info(s);
                if (s.contains("error"))
                    return null;
            }
            Path path = new File(tempUploadDirPath + sourceFileName).toPath();
            FileMessage videoFileMessage = new FileMessage(filename, configuration, headers, body, path, "file");
            return videoFileMessage;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FileMessage DownloadAudioFromURL(String url, @Nonnull String filename, Configuration configuration, Map<String, String[]> headers, Http.MultipartFormData body) {

        try {
            filename = filename.trim();
            String sourceFileName = "" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(url);
            File sourceFile = new File(tempUploadDirPath + FilenameUtils.getBaseName(sourceFileName));
            Path path = sourceFile.toPath();
            FileUtils.copyURLToFile(new URL(url), sourceFile);

            FileMessage audioFileMessage = new FileMessage(filename, configuration, headers, body, path, "file");
            return audioFileMessage;

        } catch (Exception e) {
            Utils.logException(e);
            return null;
        }
    }
}
