package common.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;
import play.libs.Json;

public class JavaJsonCustomObjectMapper extends ObjectMapper {

    public JavaJsonCustomObjectMapper() {
        ObjectMapper mapper = Json.newDefaultMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SimpleModule module = new SimpleModule("ObjectIdModule");
        module.addSerializer(ObjectId.class, new ObjectIdSerializer());
        mapper.registerModule(module);

        Json.setObjectMapper(mapper);
    }

}