package utils;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import play.Logger;
import play.api.Application;
import play.api.Play;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by harshitjain on 25/03/17.
 */
public class Utils {
    public static String generateOtp() {
        StringBuilder generatedToken = new StringBuilder();
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            for (int i = 0; i < 4; i++) {
                generatedToken.append(number.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            logException(e);
        }
        return generatedToken.toString();
    }

    public static String convertForShell(String str) {
        return str.replace(" ", "\\ ")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("[", "\\[")
                .replace("&", "\\&")
                .replace("]", "\\]");
    }

    public static String convertForWget(String str) {
        return str.replace("(", "\\(")
                .replace(")", "\\)");
    }

    public static void logException(Exception e) {
        e.printStackTrace();
        Logger.error("Error : ", e);
    }

    public static String htmlToString(String location, Application application) {
        File file = Play.application(application).getFile(location.trim());
        String html, str = null;
        try {
            FileReader reader = new FileReader(file);
            html = IOUtils.toString(reader);
            Document document = Jsoup.parse(html);
            document.outputSettings(new Document.OutputSettings().prettyPrint(true));//makes html() preserve linebreaks and spacing
//                document.select("br").append("\\n");
//                document.select("p").prepend("\\n\\n");
            String s = document.html();
            str = Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
            str = str.replaceAll(" +", " ")
                    .replaceAll("\\r", "\n")
                    .replaceAll("\\n ", "\n")
                    .replaceAll("(\\n)+", "\n")
                    .replaceAll("&nbsp;", "");
            str = str.trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

}
