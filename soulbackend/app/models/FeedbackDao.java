package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import common.settings.StartUpHandler;
import common.utils.Constants;
import common.utils.Response;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import play.libs.Json;

/**
 * Created by harshit on 11/03/17.
 */
public class FeedbackDao extends BasicDAO<Feedback, Object> {
    private static volatile FeedbackDao mInstance;
    private Datastore datastore = getDatastore();

    private FeedbackDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static FeedbackDao getInstance() {
        if (mInstance == null) {
            synchronized (FeedbackDao.class) {
                if (mInstance == null)
                    mInstance = new FeedbackDao();
            }
        }
        return mInstance;
    }

    public Response insert(Feedback feedback) {
        datastore.save(feedback);
        ObjectNode objectNode = Json.newObject();
        objectNode.put(Constants.ID, String.valueOf(feedback.id));
        return Response.successResponse(objectNode);
    }
}
