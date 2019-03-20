package mma;

import com.fasterxml.jackson.databind.node.ObjectNode;
import common.settings.StartUpHandler;
import common.utils.Constants;
import common.utils.Response;
import models.User;
import models.UserDao;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import play.libs.Json;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshit on 11/03/17.
 */
public class AccApplicationDao extends BasicDAO<AccApplication, Object> {
    private static volatile AccApplicationDao mInstance;
    private Datastore datastore = getDatastore();

    private AccApplicationDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static AccApplicationDao getInstance() {
        if (mInstance == null) {
            synchronized (AccApplicationDao.class) {
                if (mInstance == null)
                    mInstance = new AccApplicationDao();
            }
        }
        return mInstance;
    }

    public Response insert(AccApplication accApplication) {
        datastore.save(accApplication);
        ObjectNode objectNode = Json.newObject();
        objectNode.put(Constants.ID, String.valueOf(accApplication.getId()));
        return Response.successResponse("Your booking application has been submitted successfully, which will be processed within 7 working days.");
    }

    public Response approve(String id, int approvedRoomCount, @Nullable String reason) {
        AccApplication accApplication = datastore.get(AccApplication.class, new ObjectId(id));
        if (approvedRoomCount < 1 || approvedRoomCount > accApplication.getRequestedRoomCount())
            return Response.errorResponse("Wrong no. of rooms approved");

        accApplication.setStatus(AccApplicationStatus.APPROVED);
        accApplication.setApprovedRoomCount(approvedRoomCount);
        accApplication.setStatusChangeComment(reason);
        accApplication.setAmountToPay(approvedRoomCount * accApplication.getAccommodationType().getPrice());
        datastore.save(accApplication);
        return Response.successResponse(accApplication);
    }

    public Response reject(String id, @Nullable String reason) {
        AccApplication accApplication = datastore.get(AccApplication.class, new ObjectId(id));
        accApplication.setStatus(AccApplicationStatus.REJECTED);
        accApplication.setStatusChangeComment(reason);
        datastore.save(accApplication);
        return Response.successResponse(accApplication);
    }

    public Response cancel(String id) {
        AccApplication accApplication = datastore.get(AccApplication.class, new ObjectId(id));
        accApplication.setStatus(AccApplicationStatus.CANCELED);
        datastore.save(accApplication);
        return Response.successResponse(accApplication);
    }

    public Response getAccApplication(String id) {
        AccApplication accApplication = datastore.get(AccApplication.class, new ObjectId(id));
        if (accApplication != null)
            return Response.successResponse(accApplication);
        else
            return Response.errorResponse("Not found");
    }

    public Response getAccApplications() {
        List<AccApplication> accApplicationList = datastore.find(AccApplication.class).asList();
        return Response.successResponse(accApplicationList);
    }

    public Response getAppliedApplications() {
        List<AccApplication> accApplicationList = datastore.find(AccApplication.class).field("status").equal(AccApplicationStatus.APPLIED).asList();
        return Response.successResponse(accApplicationList);
    }

    public Response getPaidApplications() {
        List<AccApplication> accApplicationList = datastore.find(AccApplication.class).field("status").equal(AccApplicationStatus.BOOKED).asList();
        return Response.successResponse(accApplicationList);
    }

    public Response getApprovedApplications() {
        List<AccApplication> accApplicationList = datastore.find(AccApplication.class).field("status").equal(AccApplicationStatus.APPROVED).asList();
        return Response.successResponse(accApplicationList);
    }

    public Response getUserApplications(String userId) {
        User user = UserDao.getInstance().user(userId);
        if (user == null)
            return Response.errorResponse("User does not exist");

        List<AccApplicationStatus> statusList = new ArrayList<>();
        statusList.add(AccApplicationStatus.APPLIED);
        statusList.add(AccApplicationStatus.BOOKED);
        statusList.add(AccApplicationStatus.APPROVED);
        statusList.add(AccApplicationStatus.REJECTED);
        List<AccApplication> accApplicationList = datastore.find(AccApplication.class).field("user").equal(user).field("status").in(statusList).asList();
        return Response.successResponse(accApplicationList);
    }

    public boolean hasUserApplications(String userId) {
        User user = UserDao.getInstance().user(userId);
        if (user == null)
            return false;

        List<AccApplicationStatus> statusList = new ArrayList<>();
        statusList.add(AccApplicationStatus.APPLIED);
        statusList.add(AccApplicationStatus.BOOKED);
        statusList.add(AccApplicationStatus.APPROVED);
        statusList.add(AccApplicationStatus.REJECTED);
        long count = datastore.find(AccApplication.class).field("user").equal(user).field("status").in(statusList).count();
        return count > 0;
    }


}
