package common.utils;

import play.core.enhancers.PropertiesEnhancer.GeneratedAccessor;
import play.core.enhancers.PropertiesEnhancer.RewrittenAccessor;

@GeneratedAccessor
@RewrittenAccessor
public class Response {
    private Object data;
    private Object error;
    private Boolean success;

    public static Response successResponse(Object data) {
        Response response = new Response();
        response.success = true;
        response.data = data;
        return response;
    }

    public static Response errorResponse(Object error) {
        Response response = new Response();
        response.success = false;
        response.error = error;
        return response;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getError() {
        return this.error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
