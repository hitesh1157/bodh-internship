package models;

import common.settings.StartUpHandler;
import common.utils.Response;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * Created by harshit on 11/03/17.
 */
public class FestivalListDao extends BasicDAO<FestivalList, Object> {
    private static volatile FestivalListDao mInstance;
    private Datastore datastore = getDatastore();

    private FestivalListDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static FestivalListDao getInstance() {
        if (mInstance == null) {
            synchronized (FestivalListDao.class) {
                if (mInstance == null)
                    mInstance = new FestivalListDao();
            }
        }
        return mInstance;
    }

    public Response getFestivalList(String year) {
        FestivalList festivalList = datastore.find(FestivalList.class).field("year").equal(year).get();
        return Response.successResponse(festivalList);
    }
}
