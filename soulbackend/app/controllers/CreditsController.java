package controllers;

import common.utils.Response;
import models.Credit;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by harshitjain on 23/09/17.
 */
public class CreditsController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public CreditsController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> getBannerCredits() {
        return CompletableFuture.supplyAsync(() -> {
            Credit credit = new Credit();
            credit.setThumbnail("https://s3.ap-south-1.amazonaws.com/soul-media-prod/ic_mma.png");
            credit.setText("Planning to attend <b>MahaMastakAbhisheka</b>? Tap here for online room booking");
            credit.setType("banner");
            Response response = Response.successResponse(credit);
            return ok(Json.toJson(response));
        }, ec.current());
    }

    public CompletableFuture<Result> getScreenCredits() {
        return CompletableFuture.supplyAsync(() -> {
            Credit credit = new Credit();
            credit.setThumbnail("http://www.tworld.com/janwild/wp-content/themes/tworld_agent_theme/assets/img/default-profile-pic.png");
            credit.setText("Sponsor Name");
            credit.setType("screen");
            Response response = Response.successResponse(credit);
            return ok(Json.toJson(response));
        }, ec.current());
    }
}
