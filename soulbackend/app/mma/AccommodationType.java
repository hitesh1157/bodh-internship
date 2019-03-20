package mma;

import models.BaseModel;
import models.MultiLingual;
import org.mongodb.morphia.annotations.Entity;

/**
 * Created by harshitjain on 11/11/17.
 */
@Entity(noClassnameStored = true)
public class AccommodationType extends BaseModel {
    private MultiLingual type;
    private MultiLingual subType;
    private MultiLingual description;
    private int price;
    private boolean showInApplication = false;

    public MultiLingual getType() {
        return type;
    }

    public void setType(MultiLingual type) {
        this.type = type;
    }

    public MultiLingual getSubType() {
        return subType;
    }

    public void setSubType(MultiLingual subType) {
        this.subType = subType;
    }

    public MultiLingual getDescription() {
        return description;
    }

    public void setDescription(MultiLingual description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isShowInApplication() {
        return showInApplication;
    }

    public void setShowInApplication(boolean showInApplication) {
        this.showInApplication = showInApplication;
    }
}
