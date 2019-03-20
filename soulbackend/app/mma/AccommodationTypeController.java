package mma;

import com.fasterxml.jackson.databind.JsonNode;
import common.utils.Response;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by harshit on 26/02/17.
 */
public class AccommodationTypeController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public AccommodationTypeController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> create() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode request = request().body().asJson();
            AccommodationType accommodationType = Json.fromJson(request, AccommodationType.class);
            Response response = AccommodationTypeDao.getInstance().insert(accommodationType);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }

        }, ec.current());
    }

    public CompletableFuture<Result> read(String id) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = AccommodationTypeDao.getInstance().getAccommodationType(id);
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

    public CompletableFuture<Result> getBookingAccType() {
        return CompletableFuture.supplyAsync(() -> {
            Response response = AccommodationTypeDao.getInstance().getBookingAccommodationType();
            if (response.getSuccess()) {
                return ok(Json.toJson(response));
            } else {
                return badRequest(Json.toJson(response));
            }
        }, ec.current());
    }

}
