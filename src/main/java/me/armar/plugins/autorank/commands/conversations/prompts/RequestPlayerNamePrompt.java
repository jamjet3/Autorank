package me.armar.plugins.autorank.commands.conversations.prompts;

import me.armar.plugins.autorank.Autorank;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RequestPlayerNamePrompt extends ValidatingPrompt {
    private final String message;
    private String PLAYERNAME_KEY;
    private final Prompt nextPrompt;

    public RequestPlayerNamePrompt(String message, String key, Prompt nextPrompt) {
        this.PLAYERNAME_KEY = "playerName";
        this.message = message;
        if (key != null) {
            this.PLAYERNAME_KEY = key;
        }

        this.nextPrompt = nextPrompt == null ? Prompt.END_OF_CONVERSATION : nextPrompt;
    }

    public RequestPlayerNamePrompt(String message, Prompt nextPrompt) {
        this(message, null, nextPrompt);
    }

    public RequestPlayerNamePrompt(String message) {
        this(message, null);
    }

    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return this.message;
    }

    protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
        // Use PlayerLookupService so the validator recognises both Java and
        // Bedrock-prefixed names without producing synthetic UUIDs.
        // If the service isn't reachable (very early load), reject the input
        // rather than falling back to Bukkit.getOfflinePlayer(String) — that
        // is the unsafe legacy path this whole change is replacing.
        Autorank autorank = (Autorank) Bukkit.getPluginManager().getPlugin("Autorank");
        if (autorank == null || autorank.getPlayerLookupService() == null) {
            return false;
        }
        return autorank.getPlayerLookupService().resolveOnlineOrCached(s).isPresent();
    }

    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
        conversationContext.setSessionData(this.PLAYERNAME_KEY, s);
        return this.nextPrompt;
    }
}