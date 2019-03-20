package models;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static common.utils.Constants.SCALETYPE_CENTER_CROP;
import static common.utils.Constants.TRANSPARENT;

public class Media {
    String type;
    String bgColor;
    String url;
    String scaleType;

    private Media() {

    }

    public static Media createPhoto(@Nonnull String url, @Nullable String scaleType, @Nullable String bgColor) {
        Media media = new Media();
        media.type = "photo";
        media.url = url;

        if (bgColor != null)
            media.bgColor = bgColor;
        else
            media.bgColor = TRANSPARENT;

        if(scaleType!=null)
            media.scaleType = scaleType;
        else
            media.scaleType = SCALETYPE_CENTER_CROP;

        return media;
    }

    public static Media createVideo(@Nonnull String url, @Nullable String bgColor) {
        Media media = new Media();
        media.type = "video";
        media.url = url;
        if (bgColor != null)
            media.bgColor = bgColor;
        else
            media.bgColor = TRANSPARENT;

        return media;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScaleType() {
        return scaleType;
    }

    public void setScaleType(String scaleType) {
        this.scaleType = scaleType;
    }
}
