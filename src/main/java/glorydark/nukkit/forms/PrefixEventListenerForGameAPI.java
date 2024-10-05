package glorydark.nukkit.forms;

import cn.nukkit.Player;
import cn.nukkit.Server;
import gameapi.event.player.RoomPlayerChatEvent;
import gameapi.listener.base.annotations.GameEventHandler;
import gameapi.listener.base.interfaces.GameListener;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.event.PrefixModifyMessageEvent;

/**
 * @author glorydark
 */
public class PrefixEventListenerForGameAPI implements GameListener {

    @GameEventHandler
    public void RoomPlayerChatEvent(RoomPlayerChatEvent event) {
        Player player = event.getPlayer();
        String identifier = PrefixAPI.getPlayerPrefixData(player.getName()).getDisplayedPrefixName();
        PrefixModifyMessageEvent prefixModifyMessageEvent = new PrefixModifyMessageEvent(player, identifier);
        Server.getInstance().getPluginManager().callEvent(prefixModifyMessageEvent);
        if (!prefixModifyMessageEvent.isCancelled()) {
            event.getRoomChatData().setPrefix("[" + prefixModifyMessageEvent.getMessageModifier() + prefixModifyMessageEvent.getDisplayedPrefix() + "§r] ");
        }
    }
}
