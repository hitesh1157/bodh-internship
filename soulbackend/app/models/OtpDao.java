package models;

import common.settings.StartUpHandler;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import utils.Utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by harshit on 11/03/17.
 */
public class OtpDao extends BasicDAO<Otp, Object> {
    private static volatile OtpDao mInstance;
    private Datastore datastore = getDatastore();

    private OtpDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static OtpDao getInstance() {
        if (mInstance == null) {
            synchronized (OtpDao.class) {
                if (mInstance == null)
                    mInstance = new OtpDao();
            }
        }
        return mInstance;
    }

    public boolean validate(@Nonnull String countryCode,@Nonnull String mobile, @Nullable String otpValue) {
            Otp otp = datastore.find(Otp.class)
                    .field("mobile").equal(mobile)
                    .field("countryCode").equal(countryCode)
                    .get();

        if (otp != null) {
            return otp.getOtp().equals(otpValue);
        }
        return false;
    }

    public String createOtp(@Nonnull String countryCode , @Nonnull String mobile) {
        String otpValue = getOtp(countryCode , mobile);
        if (otpValue == null) {
            otpValue = Utils.generateOtp();
            Otp otp = new Otp();
            otp.setCountryCode(countryCode);
            otp.setMobile(mobile);
            otp.setOtp(otpValue);
            datastore.save(otp);
        }
        return otpValue;
    }

    private String getOtp(@Nonnull String countryCode , @Nonnull String mobile) {
        Otp otp = datastore.find(Otp.class)
                .field("mobile").equal(mobile)
                .field("countryCode").equal(countryCode)
                .get();
        if (otp != null)
            return otp.getOtp();
        return null;
    }

    public void invalidateOtp(@Nonnull String countryCode ,@Nonnull String mobile) {
        Otp otp = datastore.find(Otp.class)
                .field("mobile").equal( mobile)
                .field("countryCode").equal( countryCode)
                .get();
        datastore.delete(otp);
    }
}
