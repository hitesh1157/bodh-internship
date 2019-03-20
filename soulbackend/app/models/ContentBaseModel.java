package models;

import jdk.nashorn.internal.ir.annotations.Reference;

/**
 * Created by harshit on 12/03/17.
 */
public class ContentBaseModel extends BaseModel {
    private PublishType publishType = PublishType.PUBLISHED_UNVERIFIED;
    private Religion religion = Religion.JAINISM;
    @Reference
    private Uploader uploadedBy;
    private Sect sect;

    public Sect getSect() {
        return sect;
    }

    public void setSect(Sect sect) {
        this.sect = sect;
    }

    public Uploader getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Uploader uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public PublishType getPublishType() {
        return publishType;
    }

    public void setPublishType(PublishType publishType) {
        this.publishType = publishType;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }


}
