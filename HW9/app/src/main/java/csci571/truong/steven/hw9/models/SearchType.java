package csci571.truong.steven.hw9.models;

/**
 * Created by Steven on 4/17/2017.
 */

public enum SearchType {
    USER, PAGE, EVENTS, PLACE, GROUP;

    public static String toString(SearchType type) {
        switch (type) {
            case USER:
                return "user";
            case PAGE:
                return "page";
            case EVENTS:
                return "event";
            case PLACE:
                return "place";
            case GROUP:
                return "group";
            default:
                return "SHOULDNT HAPPEN";
        }
    }
    public static int toInteger(SearchType type) {
        switch (type) {
            case USER:
                return 0;
            case PAGE:
                return 1;
            case EVENTS:
                return 2;
            case PLACE:
                return 3;
            case GROUP:
                return 4;
            default:
                return -1;
        }
    }
}
