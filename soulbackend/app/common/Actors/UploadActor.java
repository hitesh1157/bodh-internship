package common.Actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import common.ActorModels.UploadFileMessage;
import common.ActorModels.UploadFilePartMessage;
import play.Logger;

import javax.inject.Singleton;
import java.io.File;

import static common.Protocols.UploadProtocols.UploadFile;
import static common.Protocols.UploadProtocols.UploadFilepart;


@Singleton
public class UploadActor extends AbstractActor {

    public static Props getProps() {
        return Props.create(UploadActor.class, UploadActor::new);
    }

    private UploadActor() {

        receive(ReceiveBuilder.
                match(UploadFileMessage.class, uploadFileMessage -> {
                    String uploadFileLocation = UploadFile(uploadFileMessage.key, new File(uploadFileMessage.filePath), uploadFileMessage.contentType, uploadFileMessage.filename, uploadFileMessage.configuration);
                    Logger.info("UploadActor : uploaded file : " + uploadFileMessage.filename);
                    if(uploadFileLocation == null) {
                        sender().tell("operation failed", self());
                    } else {
                        sender().tell(uploadFileLocation, self());
                    }
                }).
                match(UploadFilePartMessage.class, uploadFilePartMessage -> {
                    String uploadLocation = UploadFilepart(uploadFilePartMessage.filePart, uploadFilePartMessage.name, uploadFilePartMessage.configuration);
                    sender().tell(uploadLocation, self());
                }).
                matchAny(o -> Logger.info("UploadActor: received unknown message")).build()
        );
    }

}
