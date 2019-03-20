package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import common.utils.Response;
import models.RequestSMS;
import models.RequestSMSDao;
import play.Configuration;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;


/**
 * Created by hitesh1157 on 11/09/17.
 */
public class RequestSMSController extends Controller {
    private HttpExecutionContext ec;
    private WSClient wsClient;
    private Configuration configuration;

    @Inject
    public RequestSMSController(HttpExecutionContext ec, WSClient wsClient, Configuration configuration) {
        this.ec = ec;
        this.wsClient = wsClient;
        this.configuration = configuration;
    }

    public CompletableFuture<Result> requestHandler() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            RequestSMS requestSMS = Json.fromJson(request, RequestSMS.class);

            Response response = RequestSMSDao.getInstance(wsClient, configuration).insert(requestSMS);


            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }

        }, ec.current());
    }

}
