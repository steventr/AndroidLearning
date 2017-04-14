package itp341.truong.steven.presence;

/**
 * Created by Steven on 5/6/2016.
 */
public enum ActivityConstants {;

    public enum Actions{
        CREATE,
        UPDATE,
        DELETE
    }

    public enum Activities {
        MANAGE_CLASS_ACTIVITY,
        MANAGE_CLASS_MEMBER_ACTIVITY;

        public static Activities fromInteger(int x) {
            switch(x) {
                case 0:
                    return MANAGE_CLASS_ACTIVITY;
                case 1:
                    return MANAGE_CLASS_MEMBER_ACTIVITY;
            }
            return null;
        }

        public int toInteger() {
            switch(this) {
                case MANAGE_CLASS_ACTIVITY:
                    return 0;
                case MANAGE_CLASS_MEMBER_ACTIVITY:
                    return 1;
            }
            return -1;
        }
    }

}
