package me.armar.plugins.autorank.commands.conversations.resetcommand;

import me.armar.plugins.autorank.commands.conversations.AutorankConversation;
import me.armar.plugins.autorank.commands.conversations.prompts.ConfirmPrompt;
import me.armar.plugins.autorank.commands.conversations.prompts.ConfirmPromptCallback;
import me.armar.plugins.autorank.language.Lang;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ResetConfirmation extends MessagePrompt {
    ResetConfirmation() {
    }

    protected @Nullable Prompt getNextPrompt(final @NotNull ConversationContext conversationContext) {
        ChatColor var10000 = ChatColor.DARK_AQUA;
        Lang var10001 = Lang.NCC_ARE_YOU_SURE_RESET;
        Object[] var10002 = new Object[1];
        ChatColor var10005 = ChatColor.GOLD;
        var10002[0] = var10005 + conversationContext.getSessionData("playerName").toString() + ChatColor.DARK_AQUA;
        String message = var10000 + var10001.getConfigValue(var10002);
        String resetType = conversationContext.getSessionData(ResetConversationType.RESET_TYPE).toString();
        if (resetType.equals(ResetConversationType.RESET_COMPLETED_PATHS)) {
            message = message.replace("%type%", Lang.NCC_COMPLETED.getConfigValue());
        } else if (resetType.equals(ResetConversationType.RESET_ACTIVE_PATHS)) {
            message = message.replace("%type%", Lang.NCC_ACTIVE.getConfigValue());
        } else if (resetType.equals(ResetConversationType.RESET_ALL_PROGRESS)) {
            message = message.replace("%type%", Lang.NCC_ALL_PROGRESS.getConfigValue());
        } else {
            message = message.replace("%type%", Lang.NCC_ACTIVE_PROGRESS.getConfigValue());
        }

        return new ConfirmPrompt(message, new ConfirmPromptCallback() {
            public void promptConfirmed() {
                conversationContext.setSessionData(AutorankConversation.CONVERSATION_SUCCESSFUL_IDENTIFIER, true);
            }

            public void promptDenied() {
                conversationContext.setSessionData(AutorankConversation.CONVERSATION_SUCCESSFUL_IDENTIFIER, false);
            }
        });
    }

    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return "";
    }
}
