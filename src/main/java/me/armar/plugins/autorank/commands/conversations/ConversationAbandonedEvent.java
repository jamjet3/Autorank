package me.armar.plugins.autorank.commands.conversations;

import me.armar.plugins.autorank.language.Lang;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ExactMatchConversationCanceller;
import org.bukkit.conversations.InactivityConversationCanceller;

public class ConversationAbandonedEvent implements ConversationAbandonedListener {
    public ConversationAbandonedEvent() {
    }

    public void conversationAbandoned(org.bukkit.conversations.ConversationAbandonedEvent conversationAbandonedEvent) {
        Object conversationObject = conversationAbandonedEvent.getContext().getSessionData(AutorankConversation.CONVERSATION_IDENTIFIER);
        if (conversationObject != null) {
            AutorankConversation conversation = (AutorankConversation)conversationObject;
            Object endedSuccesfully = conversationAbandonedEvent.getContext().getSessionData(AutorankConversation.CONVERSATION_SUCCESSFUL_IDENTIFIER);
            Conversable conversable = conversationAbandonedEvent.getContext().getForWhom();
            ConversationResult result;
            if (endedSuccesfully == null) {
                result = new ConversationResult(false, conversable);
            } else {
                result = new ConversationResult((Boolean)endedSuccesfully, conversable);
            }

            result.setConversationStorage(conversationAbandonedEvent.getContext().getAllSessionData());
            ConversationCanceller canceller = conversationAbandonedEvent.getCanceller();
            if (canceller instanceof InactivityConversationCanceller) {
                ChatColor var10001 = ChatColor.GRAY;
                conversable.sendRawMessage(var10001 + Lang.NCC_CONVERSATION_HAS_ENDED_BECAUSE.getConfigValue(new Object[0]));
            } else if (canceller instanceof ExactMatchConversationCanceller) {
                ChatColor var8 = ChatColor.GRAY;
                conversable.sendRawMessage(var8 + Lang.NCC_CONVERSATION_HAS_BEEN.getConfigValue(new Object[0]));
                result.setEndedByKeyword(true);
            } else {
                ChatColor var9 = ChatColor.GRAY;
                conversable.sendRawMessage(var9 + Lang.NCC_CONVERSATION_HAS_ENDED.getConfigValue(new Object[0]));
            }

            conversation.conversationEnded(result);
        }

    }
}
