package me.armar.plugins.autorank.commands.conversations.editorcommand.assignpath;

import java.util.UUID;
import me.armar.plugins.autorank.Autorank;
import me.armar.plugins.autorank.commands.conversations.editorcommand.EditorMenuPrompt;
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

public class AssignPathPrompt extends StringPrompt {
    public static String KEY_PATH_TO_BE_ASSIGNED = "pathToBeAssigned";

    public AssignPathPrompt() {
    }

    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        String playerName = conversationContext.getSessionData(SelectPlayerPrompt.KEY_PLAYERNAME).toString();
        ChatColor var10000 = ChatColor.GOLD;
        return var10000 + Lang.NCC_WHAT_PATH_ASSIGN.getConfigValue(new Object[]{ChatColor.GRAY + playerName + ChatColor.GOLD});
    }

    public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        Path path = Autorank.getInstance().getPathManager().findPathByDisplayName(s, false);
        Conversable conversable = conversationContext.getForWhom();
        if (path == null) {
            ChatColor var10001 = ChatColor.RED;
            conversable.sendRawMessage(var10001 + Lang.NCC_THE_PATH.getConfigValue(new Object[]{ChatColor.GRAY + s + ChatColor.RED}));
            return this;
        } else {
            UUID uuid = (UUID)conversationContext.getSessionData(SelectPlayerPrompt.KEY_UUID);
            String playerName = (String)conversationContext.getSessionData(SelectPlayerPrompt.KEY_PLAYERNAME);
            if (path.isActive(uuid)) {
                conversable.sendRawMessage(Lang.NCC_IS_ALREADY.getConfigValue(ChatColor.GRAY + playerName + ChatColor.RED));
                return new EditorMenuPrompt();
            } else {
                conversationContext.setSessionData(KEY_PATH_TO_BE_ASSIGNED, path.getInternalName());
                return !path.isEligible(uuid) ? new AssignPathByForcePrompt() : END_OF_CONVERSATION;
            }
        }
    }
}
