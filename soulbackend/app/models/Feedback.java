package models;

import jdk.nashorn.internal.ir.annotations.Reference;
import play.data.validation.Constraints;

/**
 * Created by harshitjain on 04/06/17.
 */
public class Feedback extends BaseModel {
    @Constraints.Required
    private String feedback;
    private String entityId;
    @Constraints.Required
    private EntityType entityType;
    @Constraints.Required
    @Reference
    private User user;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum EntityType {
        PLAYLIST, LECTURE, CONTACT;
    }
}
