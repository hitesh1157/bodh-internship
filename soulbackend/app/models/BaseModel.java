package models;

import common.settings.StartUpHandler;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;

import java.util.Date;

/**
 * Created by harshit on 11/03/17.
 */
public class BaseModel {
    @Id
    ObjectId id;
    private Date createdDate;
    private Date modifiedDate;

    @PrePersist
    void prePersist() {
        if (id == null)
            createdDate = new Date();
        this.modifiedDate = new Date();
    }

    public void save() {
        StartUpHandler.getDatastore().save(this);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public boolean equals(Object obj) {
        BaseModel model = (BaseModel) obj;
        if (id == null || model.id == null)
            return false;
        return model.id.equals(id);
    }
}
