package models;

import play.data.validation.Constraints;

/**
 * Created by harshitjain on 13/04/17.
 */
public class MultiLingual {
    @Constraints.MinLength(3)
    private String en; //english
    @Constraints.MinLength(3)
    private String hi; //hindi
    @Constraints.MinLength(3)
    private String gu; //gujrati
    @Constraints.MinLength(3)
    private String mr; //marathi
    @Constraints.MinLength(3)
    private String kn; //kannada

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getHi() {
        return hi;
    }

    public void setHi(String hi) {
        this.hi = hi;
    }

    public String getGu() {
        return gu;
    }

    public void setGu(String gu) {
        this.gu = gu;
    }

    public String getMr() {
        return mr;
    }

    public void setMr(String mr) {
        this.mr = mr;
    }

    public String getKn() {
        return kn;
    }

    public void setKn(String kn) {
        this.kn = kn;
    }
}
