package miniRPG.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleLog {

    private final List<String> entries = new ArrayList<>();

    public void add(String message) {
        if (message != null && !message.isEmpty()) {
            entries.add(message);
        }
    }

    public List<String> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public void clear() {
        entries.clear();
    }
}
