package models;

import play.data.validation.Constraints;

/**
 * Created by harshitjain on 10/08/17.
 */
public class Festival {
    @Constraints.Required
    @Constraints.Pattern(value = "\\d{2}\\/\\d{2}\\/\\d{4}")
    private String date;
    @Constraints.Required
    private MultiLingual name;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MultiLingual getName() {
        return name;
    }

    public void setName(MultiLingual name) {
        this.name = name;
    }
}
