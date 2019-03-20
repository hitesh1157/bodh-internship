package mma;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.utils.Response;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by harshitjain on 11/11/17.
 */
public class MmaContentController extends Controller {
    private HttpExecutionContext ec;

    @Inject
    public MmaContentController(HttpExecutionContext ec) {
        this.ec = ec;
    }

    public CompletableFuture<Result> getHomePageContent() {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode jsonNode = Json.parse("{\n" +
                    "        \"media\":[\n" +
                    "        {\n" +
                    "        \"url\":\"https://i2.wp.com/www.mahamasthakabhisheka.com/wp-content/uploads/2017/06/Shravanabelagola-Mahamasthakabhisheka-Mahotsava-Bahubali-Swamy-1.jpg?w=800\",\n" +
                    "        \"scaleType\":\"centreCrop\",\n" +
                    "        \"type\":\"photo\",\n" +
                    "        \"bgColor\":\"#ffffff\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "        \"url\":\"https://i0.wp.com/www.mahamasthakabhisheka.com/wp-content/uploads/2017/03/Shravanabelagola-Bahubali-Mahamasthakabhisheka-Jain-Banner-02.jpg?w=1286\",\n" +
                    "        \"scaleType\":\"centreCrop\",\n" +
                    "        \"type\":\"photo\",\n" +
                    "        \"bgColor\":\"#ffffff\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "        \"url\":\"https://s3.ap-south-1.amazonaws.com/mahamastakabhishek/mmaBanner2.png\",\n" +
                    "        \"scaleType\":\"fitCenter\",\n" +
                    "        \"type\":\"photo\",\n" +
                    "        \"bgColor\":\"#ffffff\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "        \"url\":\"https://s3.ap-south-1.amazonaws.com/mahamastakabhishek/introVideo.mp4\",\n" +
                    "        \"type\":\"video\",\n" +
                    "        \"bgColor\":\"#000000\"\n" +
                    "        }\n" +
                    "        ],\n" +
                    "        \"accommodationText\":{\n" +
                    "        \"en\":\"11 Nagars has been established for accommodation of pilgrims during MahaMastakAbhishek. 4 type of rooms are available.\",\n" +
                    "        \"hi\":\"महा मास्टक अभिषेक के दौरान तीर्थयात्रियों के आवास के लिए 11 नगर स्थापित किए गए हैं। 4 प्रकार के कमरे उपलब्ध हैं}\"\n" +
                    "        }\n" +
                    "}");

            if (request().headers().containsKey("userId")) {
                String userId = request().headers().get("userId")[0];
                ((ObjectNode) jsonNode).put("showViewStatus", AccApplicationDao.getInstance().hasUserApplications(userId));
            }
            Response response = Response.successResponse(jsonNode);
            return ok(Json.toJson(response));
        }, ec.current());
    }

}

//{
//        "media":[
//        {
//        "url":"https://i2.wp.com/www.mahamasthakabhisheka.com/wp-content/uploads/2017/06/Shravanabelagola-Mahamasthakabhisheka-Mahotsava-Bahubali-Swamy-1.jpg?w=800",
//        "scaleType":"centreCrop",
//        "type":"photo",
//        "bgColor":"#ffffff"
//        },
//        {
//        "url":"https://i0.wp.com/www.mahamasthakabhisheka.com/wp-content/uploads/2017/03/Shravanabelagola-Bahubali-Mahamasthakabhisheka-Jain-Banner-02.jpg?w=1286",
//        "scaleType":"centreCrop",
//        "type":"photo",
//        "bgColor":"#ffffff"
//        },
//        {
//        "url":"https://s3.ap-south-1.amazonaws.com/mahamastakabhishek/mmaBanner2.png",
//        "scaleType":"fitCenter",
//        "type":"photo",
//        "bgColor":"#ffffff"
//        },
//        {
//        "url":"https://s3.ap-south-1.amazonaws.com/mahamastakabhishek/introVideo.mp4",
//        "type":"video",
//        "bgColor":"#ffffff"
//        }
//        ],
//        "accommodationText":{
//        "en":"11 Nagars has been established for accommodation of pilgrims during MahaMastakAbhishek. 4 type of rooms are available.",
//        "hi":"महा मास्टक अभिषेक के दौरान तीर्थयात्रियों के आवास के लिए 11 नगर स्थापित किए गए हैं। 4 प्रकार के कमरे उपलब्ध हैं}"
//        }
//}
