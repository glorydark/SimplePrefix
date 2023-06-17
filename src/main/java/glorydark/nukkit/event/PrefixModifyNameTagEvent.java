package glorydark.nukkit.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.data.MessageDecorationType;
import glorydark.nukkit.data.PrefixData;

public class PrefixModifyNameTagEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected PrefixData prefixData;

    protected String displayedPrefix;

    public PrefixModifyNameTagEvent(Player player, String prefixIdentifier){
        this.player = player;
        this.prefixData = PrefixAPI.getPrefixData(prefixIdentifier);
        this.displayedPrefix = PrefixAPI.getPlayerPrefixData(player.getName()).getDisplayedPrefix();
    }

    public PrefixData getPrefixData() {
        return prefixData;
    }

    public String getDisplayedPrefix() {
        return displayedPrefix;
    }

    public void setDisplayedPrefix(String displayedPrefix) {
        this.displayedPrefix = displayedPrefix;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
