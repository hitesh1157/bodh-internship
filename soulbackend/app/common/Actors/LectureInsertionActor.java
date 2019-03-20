package common.Actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import common.ActorModels.StoreLectureMessage;
import common.Protocols.LectureInsertionActorProtocol;
import common.utils.Response;
import play.Logger;

public class LectureInsertionActor extends AbstractActor {

    public static Props getProps() {
        return Props.create(LectureInsertionActor.class, LectureInsertionActor::new);
    }

    public LectureInsertionActor() {
        receive(ReceiveBuilder.
                match(StoreLectureMessage.class, storeLectureMessage -> {
                    Response response;
                    if (storeLectureMessage.audioDetail != null) {
                        response = LectureInsertionActorProtocol.insertAudioLecture(storeLectureMessage.audioDetail, storeLectureMessage.headers, storeLectureMessage.body);
                    } else {
                        response = LectureInsertionActorProtocol.insertVideoLecture(storeLectureMessage.videoDetail, storeLectureMessage.headers, storeLectureMessage.body);
                    }
                    sender().tell(response, self());
                }).
                matchAny(o -> Logger.info("received abcdefgh message")).build()
        );
    }
}
