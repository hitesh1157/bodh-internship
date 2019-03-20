package models;

public enum Religion {
    JAINISM, BUDDHISM, HINDUISM, SIKHISM;

    public static Religion fromString(String religionName) {
        religionName = religionName.toUpperCase();
        switch (religionName) {
            case "JAINISM":
                return JAINISM;
            case "BUDDHISM":
                return BUDDHISM;
            case "HINDUISM":
                return HINDUISM;
            case "SIKHISM":
                return SIKHISM;
        }
        return JAINISM;
    }
}