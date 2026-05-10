package me.armar.plugins.autorank.hooks.quests;

import java.util.UUID;
import me.armar.plugins.utils.pluginlibrary.hooks.QuestsHook;

public class Quests implements QuestsPlugin {
    private final QuestsHook questsHook;

    public Quests(QuestsHook hook) {
        this.questsHook = hook;
    }

    public int getNumberOfActiveQuests(UUID uuid) {
        return this.questsHook.getNumberOfActiveQuests(uuid);
    }

    public int getNumberOfCompletedQuests(UUID uuid) {
        return this.questsHook.getNumberOfCompletedQuests(uuid);
    }

    public boolean hasCompletedQuest(UUID uuid, String questName) {
        return this.questsHook.isQuestCompleted(uuid, questName);
    }
}