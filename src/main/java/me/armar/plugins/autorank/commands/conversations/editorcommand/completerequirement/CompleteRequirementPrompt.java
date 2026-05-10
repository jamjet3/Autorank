package me.armar.plugins.autorank.commands.conversations.editorcommand.completerequirement;

import me.armar.plugins.autorank.Autorank;
import me.armar.plugins.autorank.language.Lang;
import me.armar.plugins.autorank.pathbuilder.Path;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompleteRequirementPrompt extends StringPrompt {
    public static String KEY_PATH_OF_REQUIREMENT = "pathOfRequirement";

    public CompleteRequirementPrompt() {
    }

    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        ChatColor var10000 = ChatColor.GOLD;
        return var10000 + Lang.NCC_WHAT_PATH.getConfigValue(new Object[0]);
    }

    public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        Path path = Autorank.getInstance().getPathManager().findPathByDisplayName(s, false);
        Conversable conversable = conversationContext.getForWhom();
        if (path == null) {
            ChatColor var10001 = ChatColor.RED;
            conversable.sendRawMessage(var10001 + Lang.NCC_THE_PATH.getConfigValue(new Object[]{ChatColor.GRAY + s + ChatColor.RED}));
            return this;
        } else {
            conversationContext.setSessionData(KEY_PATH_OF_REQUIREMENT, path.getInternalName());
            return new CompleteRequirementRequestRequirementIdPrompt();
        }
    }
}