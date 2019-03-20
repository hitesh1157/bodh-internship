package common.Protocols;

import play.Logger;
import utils.Utils;

public class RemovalProtocol {

    public static String removeFiles(String... paths) {
        try {
            String myCommand = "rm";
            for (String path : paths) {
                myCommand += " " + Utils.convertForShell(path);
            }
            Logger.info("command to exec : " + myCommand);
            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", myCommand});
            p.waitFor();
            return "ok";

        } catch (Exception e) {
            Utils.logException(e);
            return null;
        }
    }
}
