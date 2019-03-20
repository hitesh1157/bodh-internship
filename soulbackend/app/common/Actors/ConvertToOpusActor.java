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

public class ConvertToOpusActor extends AbstractActor {
    private static ActorRef uploadActor;

    public static Props getProps() {
        return Props.create(ConvertToOpusActor.class, ConvertToOpusActor::new);
    }

    private ConvertToOpusActor() {
        receive(ReceiveBuilder.
                match(FileMessage.class, fileMessage -> {
                    String destFileName = tempUploadDirPath + FilenameUtils.getBaseName(fileMessage.path.toString()) + ".opus";
                    UploadFileMessage uploadFileMessage = ConvertProtocol.convertToOpus(fileMessage.key, fileMessage.filename, fileMessage.path.toString(), destFileName, fileMessage.configuration);
                    if (uploadFileMessage != null) {
                        Logger.info("ConvertToOpusActor : converted : " + destFileName);

                        if (uploadActor == null) {
                            uploadActor = S3UsingActors.ActorSysContainer.getInstance().getSystem().actorOf(UploadActor.getProps(), "uploadOpus");
                            Logger.info("ConvertToOpusActor uploadActor path : " + uploadActor.path());
                        }
                        CompletableFuture<Object> uploadOpusFile = ask(uploadActor, uploadFileMessage, 1000000).toCompletableFuture();
                        String locationOpus = (String) uploadOpusFile.join();
                        sender().tell(locationOpus, self());
                    } else {
                        sender().tell("converting to opus failed", self());
                    }

                }).
                matchAny(o -> Logger.info("ConvertToOpusActor: received unknown message")).build()
        );
    }
}