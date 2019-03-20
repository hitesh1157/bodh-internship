package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import common.settings.StartUpHandler;
import common.utils.Constants;
import common.utils.Response;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import play.libs.Json;

/**
 * Created by harshitjain on 06/07/17.
 */
public class UploaderDao extends BasicDAO<Uploader, Object> {
    private static volatile UploaderDao mInstance;
    private Datastore datastore = getDatastore();

    private UploaderDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static UploaderDao getInstance() {
        if (mInstance == null) {
            synchronized (UploaderDao.class) {
                if (mInstance == null)
                    mInstance = new UploaderDao();
            }
        }
        return mInstance;
    }

    public Uploader uploader(String id) {
        return datastore.get(Uploader.class, new ObjectId(id));
    }

    public Response getUploader(String id) {
        Uploader uploader = datastore.get(Uploader.class, new ObjectId(id));
        if (uploader != null)
            return Response.successResponse(uploader);
        else
            return Response.errorResponse("Not found");
    }

    public Response getUploaderByEmail(String email) {
        Uploader uploader = datastore.find(Uploader.class).field("email").equal(email).get();
        if (uploader != null)
            return Response.successResponse(uploader);
        else
            return Response.errorResponse("Not found");
    }

    public Response insert(Uploader uploader) {
        datastore.save(uploader);
        ObjectNode objectNode = Json.newObject();
        objectNode.put(Constants.ID, String.valueOf(uploader.id));
        return Response.successResponse(objectNode);
    }

}
