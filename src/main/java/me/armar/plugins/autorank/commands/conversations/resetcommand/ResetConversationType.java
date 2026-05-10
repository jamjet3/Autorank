package me.armar.plugins.autorank.commands.conversations.resetcommand;

import me.armar.plugins.autorank.commands.conversations.prompts.RequestPlayerNamePrompt;
import me.armar.plugins.autorank.language.Lang;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResetConversationType extends FixedSetPrompt {
    public static String RESET_TYPE = "resetType";
    public static String RESET_ACTIVE_PROGRESS = "active progress";
    public static String RESET_ALL_PROGRESS = "all progress";
    public static String RESET_COMPLETED_PATHS = "completed paths";
    public static String RESET_ACTIVE_PATHS = "active paths";

    public ResetConversationType() {
        super(RESET_ACTIVE_PROGRESS, RESET_COMPLETED_PATHS, RESET_ACTIVE_PATHS, RESET_ALL_PROGRESS);
    }

    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
        conversationContext.setSessionData(RESET_TYPE, s);
        String requestPlayerMessage;
        if (s.equals(RESET_COMPLETED_PATHS)) {
            ChatColor var10000 = ChatColor.DARK_AQUA;
            requestPlayerMessage = var10000 + Lang.NCC_OF_WHICH_PLAYER_COMPLETED.getConfigValue(new Object[0]);
        } else if (s.equals(RESET_ACTIVE_PATHS)) {
            ChatColor var4 = ChatColor.DARK_AQUA;
            requestPlayerMessage = var4 + Lang.NCC_OF_WHICH_PLAYER_ACTIVE.getConfigValue(new Object[0]);
        } else if (s.equals(RESET_ALL_PROGRESS)) {
            ChatColor var5 = ChatColor.DARK_AQUA;
            requestPlayerMessage = var5 + Lang.NCC_OF_WHICH_PLAYER_ALL_PROGRESS.getConfigValue(new Object[0]);
        } else {
            ChatColor var6 = ChatColor.DARK_AQUA;
            requestPlayerMessage = var6 + Lang.NCC_OF_WHICH_PLAYER_ACTIVE_PROGRESS.getConfigValue(new Object[0]);
        }

        return new RequestPlayerNamePrompt(requestPlayerMessage, new ResetConfirmation());
    }

    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        ChatColor var10000 = ChatColor.DARK_AQUA;
        return var10000 + Lang.NCC_WHAT_DO_YOU.getConfigValue(new Object[0]) + ChatColor.RED + this.formatFixedSet();
    }
}
