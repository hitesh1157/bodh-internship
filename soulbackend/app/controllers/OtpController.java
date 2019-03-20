package controllers;

import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.databind.JsonNode;
import common.utils.Response;
import models.OtpDao;
import models.User;
import models.UserDao;
import play.Configuration;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

import static common.utils.TriggerTransactionalSMS.send;
import static common.utils.TriggerTransactionalSMS.sendviaSNS;

/**
 * Created by harshitjain on 25/03/17.
 */
public class OtpController extends Controller {
    private HttpExecutionContext ec;
    private WSClient wsClient;
    private Configuration configuration;
    public static final String AWS_ACCESS_KEY = "aws.access.key";
    public static final String AWS_SECRET_KEY = "aws.secret.key";

    @Inject
    public OtpController(HttpExecutionContext ec, WSClient wsClient, Configuration configuration) {
        this.ec = ec;
        this.wsClient = wsClient;
        this.configuration = configuration;
    }

    public CompletableFuture<Result> requestOtp(final String countryCode, final String mobile) {
        return CompletableFuture.supplyAsync(() -> {
            String code = countryCode == null ? "+91" : countryCode;

            // AWS API for Transactional SMS outside India.
            String otp = OtpDao.getInstance().createOtp(code, mobile);
            String message = "Hi! Your Bodh OTP is " + otp + ".";
            String phoneNumber = code + mobile;
            PublishResult result = sendviaSNS(phoneNumber, message, configuration);

            return ok(Json.toJson(Response.successResponse(otp)));
            // if (countryCode.equals("+91") || countryCode.equals("91") || countryCode == null) {
            //     String otp = OtpDao.getInstance().createOtp(countryCode, mobile);
            //     String message = "Hi! Your Bodh OTP is " + otp + ".";
            //     send(mobile, message, wsClient, configuration);

            //     return ok(Json.toJson(Response.successResponse(otp)));
            // } else {
            //     // AWS API for Transactional SMS outside India.
            //     String otp = OtpDao.getInstance().createOtp(countryCode, mobile);
            //     String message = "Hi! Your Bodh OTP is " + otp + ".";
            //     String phoneNumber = countryCode + mobile;
            //     PublishResult result = sendviaSNS(phoneNumber, message, configuration);

            //     return ok(Json.toJson(Response.successResponse(otp)));
            // }
        }, ec.current());
    }


    public CompletableFuture<Result> loginByOtp() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            String countryCode = null;
            if (request.has("countryCode")) {
                countryCode = request.get("countryCode").asText();
            } else {
                countryCode = "+91";
            }
            String mobile = request.get("mobile").asText();
            String otp = request.get("otp").asText();
            String name = request.get("name").asText();
            String gcmToken = null;
            if (request.has("gcmToken")) {
                gcmToken = request.get("gcmToken").asText();
            }
            if (mobile != null) {
                boolean valid = OtpDao.getInstance().validate(countryCode, mobile, otp);
                if (valid) {
                    User user = UserDao.getInstance().createOrUpdateUser(countryCode, mobile, name, gcmToken);
                    OtpDao.getInstance().invalidateOtp(countryCode, mobile);
                    return ok(Json.toJson(Response.successResponse(user)));
                } else {
                    return ok(Json.toJson(Response.errorResponse(null)));
                }
            } else {
                return badRequest();
            }
        }, ec.current());
    }
}
