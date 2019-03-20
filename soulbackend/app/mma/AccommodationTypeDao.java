package mma;

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
 * Created by harshit on 11/03/17.
 */
public class AccommodationTypeDao extends BasicDAO<AccommodationType, Object> {
    private static volatile AccommodationTypeDao mInstance;
    private Datastore datastore = getDatastore();

    private AccommodationTypeDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static AccommodationTypeDao getInstance() {
        if (mInstance == null) {
            synchronized (AccommodationTypeDao.class) {
                if (mInstance == null)
                    mInstance = new AccommodationTypeDao();
            }
        }
        return mInstance;
    }

    public Response insert(AccommodationType accommodationType) {
        datastore.save(accommodationType);
        ObjectNode objectNode = Json.newObject();
        objectNode.put(Constants.ID, String.valueOf(accommodationType.getId()));
        return Response.successResponse(objectNode);
    }

    public Response getAccommodationType(String id) {
        AccommodationType accommodationType = datastore.get(AccommodationType.class, new ObjectId(id));
        if (accommodationType != null)
            return Response.successResponse(accommodationType);
        else
            return Response.errorResponse("Not found");
    }

    public AccommodationType accommodationType(String id) {
        return datastore.get(AccommodationType.class, new ObjectId(id));
    }

    public Response getBookingAccommodationType() {
        List<AccommodationType> accommodationTypeList = datastore.find(AccommodationType.class).field("showInApplication").equal(true).asList();
        return Response.successResponse(accommodationTypeList);
    }
}
