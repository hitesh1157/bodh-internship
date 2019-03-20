package common.settings;

import akka.stream.Materializer;
import play.Logger;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;
import play.routing.Router.Tags;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class RoutedLoggingFilter extends Filter {

    Materializer mat;

    @Inject
    public RoutedLoggingFilter(Materializer mat) {
        super(mat);
        this.mat = mat;
    }

    @Override
    public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> nextFilter, Http.RequestHeader requestHeader) {
        long startTime = System.currentTimeMillis();
        return nextFilter.apply(requestHeader).thenApply(result -> {
            Map<String, String> tags = requestHeader.tags();
            String actionMethod = tags.get(Tags.ROUTE_CONTROLLER) +
                    "." + tags.get(Tags.ROUTE_ACTION_METHOD);
            long endTime = System.currentTimeMillis();
            long requestTime = endTime - startTime;
//            akka.util.ByteString body = play.core.j.JavaResultExtractor.getBody(result, 10000l, mat);

//            Logger.info("{} {} ({}) took {}ms and returned {} {}\n",
//                    requestHeader.method(), requestHeader.uri(), actionMethod, requestTime, result.status(), body.decodeString("UTF-8"));
            Logger.info("{} {} ({}) took {}ms and returned {}\n",
                    requestHeader.method(), requestHeader.uri(), actionMethod, requestTime, result.status());

            return result.withHeader("Request-Time", "" + requestTime);
        });
    }
}