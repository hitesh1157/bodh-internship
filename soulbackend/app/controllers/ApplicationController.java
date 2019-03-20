package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.settings.StartUpHandler;
import common.utils.Response;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class ApplicationController extends Controller {

    public Result index() {
        return health();
    }

    public Result health() {
        Response response = new Response();
        try {
            JsonNode data = new ObjectMapper().createObjectNode();
            response.setSuccess(true);
            try {
                if (StartUpHandler.getDatastore().getMongo().getAddress() != null) {
                    ((ObjectNode) data).put("mongo", "Mongo is Up");
                }
            } catch (Exception ex) {
                response.setSuccess(false);
                ((ObjectNode) data).put("mongo", "Mongo is Down");
            }

            response.setData(data);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return internalServerError(Json.toJson(response));
            }

        } catch (Exception ex) {
            Logger.error("Error while checking health : ", ex);
            response.setSuccess(false);
            response.setError(ex.getMessage());
            return internalServerError(Json.toJson(response));
        }
    }

}
