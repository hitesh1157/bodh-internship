package common.Actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import common.ActorModels.FileMessage;
import common.ActorModels.UploadFileMessage;
import common.Protocols.ConvertProtocol;
import common.settings.S3UsingActors;
import org.apache.commons.io.FilenameUtils;
import play.Logger;

import java.util.concurrent.CompletableFuture;

import static akka.pattern.PatternsCS.ask;
import static common.settings.S3.tempUploadDirPath;

public class ConvertToMp3Actor extends AbstractActor {
    private static ActorRef uploadActor;

    public static Props getProps() {
        return Props.create(ConvertToMp3Actor.class, ConvertToMp3Actor::new);
    }

    private ConvertToMp3Actor() {
        receive(ReceiveBuilder.
                match(FileMessage.class, fileMessage -> {
                    String destFileName = tempUploadDirPath + FilenameUtils.getBaseName(fileMessage.path.toString()) + ".mp3";
                    UploadFileMessage uploadFileMessage = ConvertProtocol.convertToMP3(fileMessage.key, fileMessage.filename, fileMessage.path.toString(), destFileName, fileMessage.configuration);

                    if (uploadFileMessage != null) {
                        Logger.info("ConvertToMp3Actor : converted : " + destFileName);
                        if (uploadActor == null) {
                            uploadActor = S3UsingActors.ActorSysContainer.getInstance().getSystem().actorOf(UploadActor.getProps(), "uploadMp3");
                            Logger.info("ConvertToMp3Actor uploadActor path : " + uploadActor.path());
                        }
                        CompletableFuture<Object> uploadMp3File = ask(uploadActor, uploadFileMessage, 1000000).toCompletableFuture();
                        String locationMP3 = (String) uploadMp3File.join();
                        sender().tell(locationMP3, self());
                    } else {
                        sender().tell("converting to mp3 failed", self());
                    }

                }).
                matchAny(o -> Logger.info("ConvertToMp3Actor: received unknown message")).build()
        );
    }
}