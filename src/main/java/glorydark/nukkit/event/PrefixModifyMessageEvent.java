package glorydark.nukkit.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.data.MessageDecorationType;
import glorydark.nukkit.data.PrefixData;

public class PrefixModifyMessageEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected PrefixData prefixData;

    protected String displayedPrefix;

    protected String messageModifier = "";

    public PrefixModifyMessageEvent(Player player, String prefixIdentifier){
        this.player = player;
        this.prefixData = PrefixAPI.getPrefixData(prefixIdentifier);
        this.displayedPrefix = PrefixAPI.getPlayerPrefixData(player.getName()).getDisplayedPrefix();
        if(this.prefixData != null){
            StringBuilder messageModifierBuilder = new StringBuilder();
            for(MessageDecorationType type: this.prefixData.getMessageDecorationTypes()){
                switch (type){
                    case BOLD:
                        messageModifierBuilder.append("§l");
                        break;
                    case ITALIC:
                        messageModifierBuilder.append("§o");
                        break;
                    case UNDERLINED:
                        messageModifierBuilder.append("§n");
                        break;
                }
            }
            this.messageModifier = messageModifierBuilder.toString();
        }
    }

    public PrefixData getPrefixData() {
        return prefixData;
    }

    public String getMessageModifier() {
        return messageModifier;
    }

    public String getDisplayedPrefix() {
        return displayedPrefix;
    }

    public void setDisplayedPrefix(String displayedPrefix) {
        this.displayedPrefix = displayedPrefix;
    }

    public void setMessageModifier(String messageModifier) {
        this.messageModifier = messageModifier;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
