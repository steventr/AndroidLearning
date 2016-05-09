package itp341.truong.steven.presence.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import itp341.truong.steven.presence.*;
import itp341.truong.steven.presence.Class;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyMemberContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Member> ITEMS = new ArrayList<Member>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Member> ITEM_MAP = new HashMap<String, Member>();

    private static final int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            Member m  = createDummyItem(i);
            m.pin = "1234";
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(itp341.truong.steven.presence.Member item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.memberID, item);
    }

    private static Member createDummyItem(int position) {
        if (position == COUNT) {
            return new Member("PAST_END");
        }
        return new Member("Test");
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
