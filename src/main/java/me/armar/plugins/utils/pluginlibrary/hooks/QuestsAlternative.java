package me.armar.plugins.utils.pluginlibrary.hooks;

import java.util.UUID;

// Stubbed: LMBishop Quests not used on Pinecraft Equestrian and JAR not available locally.
public class QuestsAlternative extends LibraryHook {
    public QuestsAlternative() {
    }

    public boolean isHooked() {
        return false;
    }

    public boolean hook() {
        return false;
    }

    public int getNumberOfCompletedQuests(UUID uuid) {
        return -1;
    }

    public int getNumberOfActiveQuests(UUID uuid) {
        return -1;
    }

    public boolean isQuestCompleted(UUID uuid, String questName) {
        return false;
    }
}
