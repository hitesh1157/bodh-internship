package common.Actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import common.ActorModels.DownloadAudioFileMessage;
import common.ActorModels.DownloadVideoFileMessage;
import common.ActorModels.FileMessage;
import common.Protocols.DownloadProtocol;
import play.Logger;

import javax.inject.Singleton;

@Singleton
public class DownloadActor extends AbstractActor {

    public static Props getProps() {
        return Props.create(DownloadActor.class, DownloadActor::new);
    }

    private DownloadActor() {

        receive(ReceiveBuilder.
                match(DownloadVideoFileMessage.class, downloadVideoFileMessage -> {
                    FileMessage videoFileMessage = DownloadProtocol.DownloadVideoFromURL(downloadVideoFileMessage.url, downloadVideoFileMessage.filename, downloadVideoFileMessage.configuration, downloadVideoFileMessage.headers, downloadVideoFileMessage.body);
                    if (videoFileMessage != null) {
                        sender().tell(videoFileMessage, self());
                    } else {
                        sender().tell("video download failed", self());
                    }
                }).
                match(DownloadAudioFileMessage.class, downloadAudioFileMessage -> {
                    FileMessage audioFileMessage = DownloadProtocol.DownloadAudioFromURL(downloadAudioFileMessage.url, downloadAudioFileMessage.filename, downloadAudioFileMessage.configuration, downloadAudioFileMessage.headers, downloadAudioFileMessage.body);
                    if (audioFileMessage != null) {
                        sender().tell(audioFileMessage, self());
                    } else {
                        sender().tell("audio download failed", self());
                    }
                }).
                matchAny(o -> Logger.info("received unknown message")).build()
        );
    }
}