package controllers;

import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class QuillController extends Controller {
    private HttpExecutionContext ec;
    @Inject
    private WSClient wsClient;

    @Inject
    public QuillController(HttpExecutionContext ec, WSClient wsClient) {
        this.ec = ec;
        this.wsClient = wsClient;
    }

    public CompletionStage<Result> reqGet(String name) {
        if (name == null || name.isEmpty())
            return CompletableFuture.supplyAsync(() -> ok(""));

        String lang = request().getQueryString("lang");
        String urlRequest = "http://xlit.quillpad.in/quillpad_backend2/processWordJSON?lang=" + lang + "&inString=" + name;
        return wsClient.url(urlRequest).get().thenApply(response ->
                ok(response.getBody())
        );
    }


}
