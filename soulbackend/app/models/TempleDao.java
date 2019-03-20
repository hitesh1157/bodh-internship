package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import common.settings.StartUpHandler;
import common.utils.Constants;
import common.utils.Response;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import play.libs.Json;

import java.util.List;

/**
 * Created by harshitjain on 06/07/17.
 */
public class TempleDao extends BasicDAO<Temple, Object> {
    private static volatile TempleDao mInstance;
    private Datastore datastore = getDatastore();

    private TempleDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static TempleDao getInstance() {
        if (mInstance == null) {
            synchronized (TempleDao.class) {
                if (mInstance == null)
                    mInstance = new TempleDao();
            }
        }
        return mInstance;
    }

    public Temple temple(String id) {
        return datastore.get(Temple.class, new ObjectId(id));
    }

    public Response getTemple(String id) {
        Temple temple = datastore.get(Temple.class, new ObjectId(id));
        if (temple != null)
            return Response.successResponse(temple);
        else
            return Response.errorResponse("Not found");
    }

    public Response insert(Temple temple) {
        datastore.save(temple);
        ObjectNode objectNode = Json.newObject();
        objectNode.put(Constants.ID, String.valueOf(temple.id));
        return Response.successResponse(objectNode);
    }

    public Response getAllTemples() {
        List<Temple> temples = datastore.find(Temple.class).asList();
        return Response.successResponse(temples);
    }

}
