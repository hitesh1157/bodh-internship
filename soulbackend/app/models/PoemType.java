package models;

/**
 * Created by harshitjain on 20/08/17.
 */
public enum PoemType {
    AARTI, BHAJAN, BHAKTI, CHALISA, POOJA, STUTI, STOTRA;

    public static PoemType fromString(String poemType) {
        poemType = poemType.toUpperCase();
        switch (poemType) {
            case "AARTI":
                return AARTI;
            case "BHAJAN":
                return BHAJAN;
            case "BHAKTI":
                return BHAKTI;
            case "CHALISA":
                return CHALISA;
            case "POOJA":
                return POOJA;
            case "STUTI":
                return STUTI;
            case "STOTRA":
                return STOTRA;
        }
        return null;
    }
}
