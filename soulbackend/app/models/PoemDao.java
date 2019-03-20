package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import common.settings.StartUpHandler;
import common.utils.Constants;
import common.utils.Response;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import play.libs.Json;
import utils.Utils;

import java.util.List;

/**
 * Created by harshit on 11/03/17.
 */
public class PoemDao extends BasicDAO<Poem, Object> {
    private static volatile PoemDao mInstance;
    private Datastore datastore = getDatastore();

    private PoemDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static PoemDao getInstance() {
        if (mInstance == null) {
            synchronized (PoemDao.class) {
                if (mInstance == null)
                    mInstance = new PoemDao();
            }
        }
        return mInstance;
    }

    public Response insert(Poem poem) {
        Poem duplicatePoem = datastore.find(Poem.class).field("religion").equal(poem.getReligion()).field("name").equal(poem.getName()).get();
        if (duplicatePoem == null) {
            datastore.save(poem);
            ObjectNode objectNode = Json.newObject();
            objectNode.put(Constants.ID, String.valueOf(poem.id));
            return Response.successResponse(objectNode);
        } else {
            return Response.errorResponse("Duplicate Entry");
        }
    }

    public Response getPoem(String id) {
        Poem poem = datastore.get(Poem.class, new ObjectId(id));
        if (poem != null)
            return Response.successResponse(poem);
        else
            return Response.errorResponse("Not found");
    }

    public Response getAllPoems(String religion) {
        List<Poem> poems = datastore.find(Poem.class).field("religion").equal(Religion.fromString(religion)).asList();
        return Response.successResponse(poems);
    }

    public Response getAllPoems(String religion, String poemType) {
        List<Poem> poems = datastore.find(Poem.class).field("religion").equal(Religion.fromString(religion)).field("poemType").equal(PoemType.fromString(poemType)).asList();
        return Response.successResponse(poems);
    }

    public Response update(Poem poem, String id) {
        try {
            poem.id = new ObjectId(id);
            Key<Poem> key = datastore.merge(poem);
            return Response.successResponse(datastore.get(Poem.class, poem.id));
        } catch (Exception e) {
            Utils.logException(e);
            return Response.errorResponse(e.getMessage());
        }
    }

}
