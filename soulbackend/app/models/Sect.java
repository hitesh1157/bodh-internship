package models;

public enum Sect {
    DIGAMBAR, SVETAMBAR, BOTH;

    public static Sect fromString(String sect) {
        sect = sect.toUpperCase();
        switch (sect) {
            case "DIGAMBAR":
                return DIGAMBAR;
            case "SVETAMBAR":
            case "SWETAMBAR":
                return SVETAMBAR;
        }
        return BOTH;
    }
}