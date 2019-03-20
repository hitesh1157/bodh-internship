package models;

import common.settings.StartUpHandler;
import common.utils.Response;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import utils.Utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by harshit on 11/03/17.
 */
public class UserDao extends BasicDAO<User, Object> {
    private static volatile UserDao mInstance;
    private Datastore datastore = getDatastore();

    private UserDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static UserDao getInstance() {
        if (mInstance == null) {
            synchronized (UserDao.class) {
                if (mInstance == null)
                    mInstance = new UserDao();
            }
        }
        return mInstance;
    }

    public User getUser(@Nonnull String countryCode, @Nonnull String mobile) {
        return datastore.find(User.class)
                .field("mobile").equal(mobile)
                .field("countryCode").equal(countryCode)
                .get();
    }

    public User user(String id) {
        return datastore.get(User.class, new ObjectId(id));
    }

    public User changePhoneNumber(@Nonnull String oldMobile, @Nonnull String newMobile) {
        User user = datastore.find(User.class).field("mobile").equal(oldMobile).get();
        if (user != null) {
            user.setMobile(newMobile);
            user.save();
            return user;
        }
        return null;
    }

    public User createOrUpdateUser(@Nonnull String countryCode, @Nonnull String mobile, @Nonnull String name, @Nullable String gcmToken) {
        User user = datastore.find(User.class)
                .field("mobile").equal(mobile)
                .field("countryCode").equal(countryCode)
                .get();
        if (user == null) {
            user = new User();
            user.setMobile(mobile);
            user.setName(name);
            user.setGcmToken(gcmToken);
            user.setCountryCode(countryCode);
            user.save();
        } else {
            user.setName(name);
            user.setGcmToken(gcmToken);
            user.save();
        }
        return user;
    }

    public Response updateProfile(String name, String email, String status, String profilePic, String id) {
        try {
            User user = datastore.get(User.class, new ObjectId(id));
            if (user == null)
                return Response.errorResponse("user not found");
            else {
                user.setName(name);
                user.setStatus(status);
                user.setEmail(email);
                user.setProfilePic(profilePic);
                user.save();
            }
            return Response.successResponse(datastore.get(User.class, user.id));
        } catch (Exception e) {
            Utils.logException(e);
            return Response.errorResponse(e.getMessage());
        }
    }

    public User updateGcmToken(@Nonnull String countryCode, @Nonnull String mobile, @Nonnull String gcmToken) {
        User user = datastore.find(User.class)
                .field("mobile").equal(mobile)
                .field("countryCode").equal(countryCode)
                .get();
        if (user != null) {
            user.setGcmToken(gcmToken);
            user.save();
        }
        return user;
    }

    public User updateGcmToken(@Nonnull String mobile, @Nonnull String gcmToken) {
        User user = datastore.find(User.class)
                .field("mobile").equal(mobile)
                .get();
        if (user != null) {
            user.setGcmToken(gcmToken);
            user.save();
        }
        return user;
    }

    public Response updateUserAnalytics(UserAnalytics userAnalytics) {
        UserAnalytics existingUserAnalytics = datastore.find(UserAnalytics.class).field("user").equal(userAnalytics.getUser()).get();
        if (existingUserAnalytics != null) {
            userAnalytics.setId(existingUserAnalytics.getId());
            userAnalytics.setFirstApkVersion(existingUserAnalytics.getFirstApkVersion());
            userAnalytics.setFirstAppOpened(existingUserAnalytics.getFirstAppOpened());
        }
        userAnalytics.save();
        return Response.successResponse(userAnalytics);
    }

    public Response updateUserSettings(UserSettings userSettings) {
        UserSettings existingUserSettings = datastore.find(UserSettings.class).field("user").equal(userSettings.getUser()).get();
        if (existingUserSettings != null)
            userSettings.setId(existingUserSettings.getId());
        userSettings.save();
        return Response.successResponse(userSettings);
    }

    public Response readUserSettings(String userId) {
        UserSettings userSettings = datastore.find(UserSettings.class).field("user").equal(user(userId)).get();
        if (userSettings != null)
            return Response.successResponse(userSettings);
        else
            return Response.errorResponse("Settings not saved for user");
    }
}
