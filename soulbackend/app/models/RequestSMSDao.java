package models;

import common.settings.StartUpHandler;
import common.utils.Response;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import play.Configuration;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import utils.Utils;

import javax.inject.Inject;
import java.util.Date;

import static common.utils.TriggerTransactionalSMS.send;
import static common.utils.TriggerTransactionalSMS.sendviaSNS;


public class RequestSMSDao extends BasicDAO<RequestSMS, Object> {

    private static volatile RequestSMSDao mInstance;
    private Datastore datastore = getDatastore();
    private WSClient wsClient;
    private Configuration configuration;

    private RequestSMSDao(WSClient wsClient, Configuration configuration) {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
        this.wsClient = wsClient;
        this.configuration = configuration;
    }

    @Inject
    public static RequestSMSDao getInstance(WSClient wsClient, Configuration configuration) {
        if (mInstance == null) {
            synchronized (RequestSMSDao.class) {
                if (mInstance == null)
                    mInstance = new RequestSMSDao(wsClient, configuration);
            }
        }
        return mInstance;
    }

    public Response insert(RequestSMS phone) {
        RequestSMS duplicatePhone = datastore.find(RequestSMS.class).field("phone").equal(phone.getPhone()).get();
        Date date = new Date();
        String message = "जय जिनेन्द्र !!\n" +
                "यह अप्प डाउनलोड करे और जैन धर्म पर 30 से भी अधिक विषयों जैसे कर्म सिद्धांत, समयसार आदि पर प्रवचन सुन कर धर्म लाभ ले|\n" +
                "अप्प का लिंक : https://z5nq4.app.goo.gl/JdE5";
        String countryCode = phone.getCountryCode();
        Response response = Response.errorResponse("Bad request.");

        if (duplicatePhone == null) {
          
            if(countryCode == null || countryCode.equals("+91") || countryCode.equals("91")) {
                send(countryCode + phone.getPhone(), message, wsClient,configuration);

                phone.setDate(date);
                datastore.save(phone);
                return Response.successResponse("Message has been sent.");
            } else {
                sendviaSNS(countryCode + phone.getPhone(), message,configuration);

                phone.setDate(date);
                datastore.save(phone);
                return Response.successResponse("Message has been sent.");
            }

        } else{
            long timeDifference = 0;
            long diffHours = 0;
            int diffDays = 0;
            try {
                Date oldDate = duplicatePhone.getDate();

                timeDifference = date.getTime() - oldDate.getTime();
                diffHours = timeDifference / (60 * 60 * 1000);
                diffDays = (int) ((date.getTime() - oldDate.getTime()) / (1000 * 60 * 60 * 24));

                if(diffDays > 0){
                    if(countryCode == null || countryCode.equals("+91") || countryCode.equals("91")) {
                        send(countryCode + phone.getPhone(), message, wsClient,configuration);

                        phone.setDate(date);
                        datastore.save(phone);
                        return Response.successResponse("Message has been sent.");
                    } else {
                        sendviaSNS(countryCode + phone.getPhone(), message,configuration);

                        phone.setDate(date);
                        datastore.save(phone);
                        return Response.successResponse("Message has been sent.");
                    }

                }
              
            } catch (Exception e) {
                Utils.logException(e);
                return response;
            }

            return Response.errorResponse("Message not sent. Please try again in " + String.valueOf(diffHours) + " hrs.");
        }

    }


}
