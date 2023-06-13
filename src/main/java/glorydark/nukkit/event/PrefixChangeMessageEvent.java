package glorydark.nukkit.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

import java.util.List;

public class PrefixChangeMessageEvent extends PlayerEvent implements Cancellable {

    public static List<HandlerList> handlers;

    protected String prefix;

    public PrefixChangeMessageEvent(Player player, String prefix){
        this.player = player;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public static List<HandlerList> getHandlers() {
        return handlers;
    }

}
