package me.armar.plugins.autorank.commands.conversations.editorcommand.completerequirement;

import java.util.UUID;
import me.armar.plugins.autorank.Autorank;
import me.armar.plugins.autorank.commands.conversations.editorcommand.SelectPlayerPrompt;
import me.armar.plugins.autorank.language.Lang;
import me.armar.plugins.autorank.pathbuilder.Path;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompleteRequirementRequestRequirementIdPrompt extends StringPrompt {
    public static String KEY_REQUIREMENT_TO_BE_COMPLETED = "requirementToBeCompleted";

    public CompleteRequirementRequestRequirementIdPrompt() {
    }

    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        ChatColor var10000 = ChatColor.GOLD;
        return var10000 + Lang.NCC_WHAT_REQUIREMENT_ID.getConfigValue(new Object[0]);
    }

    public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        Path path = Autorank.getInstance().getPathManager().findPathByInternalName(conversationContext.getSessionData(CompleteRequirementPrompt.KEY_PATH_OF_REQUIREMENT).toString(), false);
        Conversable conversable = conversationContext.getForWhom();
        int requirementId = Integer.parseInt(s.trim());
        UUID uuid = (UUID)conversationContext.getSessionData(SelectPlayerPrompt.KEY_UUID);
        String playerName = (String)conversationContext.getSessionData(SelectPlayerPrompt.KEY_PLAYERNAME);
        if (path.getRequirement(requirementId) == null) {
            ChatColor var8 = ChatColor.RED;
            Lang var10002 = Lang.NCC_THAT_REQUIREMENT;
            Object[] var10003 = new Object[1];
            ChatColor var10006 = ChatColor.GRAY;
            var10003[0] = var10006 + path.getDisplayName();
            conversable.sendRawMessage(var8 + var10002.getConfigValue(var10003));
            return this;
        } else if (path.hasCompletedRequirement(uuid, requirementId)) {
            ChatColor var10001 = ChatColor.RED;
            conversable.sendRawMessage(var10001 + Lang.NCC_HAS_ALREADY.getConfigValue(new Object[]{ChatColor.GRAY + playerName}));
            return this;
        } else {
            conversationContext.setSessionData(KEY_REQUIREMENT_TO_BE_COMPLETED, requirementId);
            return END_OF_CONVERSATION;
        }
    }
}
