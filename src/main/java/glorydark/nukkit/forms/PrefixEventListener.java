package glorydark.nukkit.forms;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.window.FormWindow;
import gameapi.manager.RoomManager;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixMain;
import glorydark.nukkit.PrefixUtils;
import glorydark.nukkit.event.PrefixModifyMessageEvent;

import java.util.HashMap;

public class PrefixEventListener implements Listener {
    public static final HashMap<Player, HashMap<Integer, FormType>> UI_CACHE = new HashMap<>();

    public static void showFormWindow(Player player, FormWindow window, FormType guiType) {
        UI_CACHE.computeIfAbsent(player, i -> new HashMap<>()).put(player.showFormWindow(window), guiType);
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PrefixMain.getPlugin().getProvider().generatePlayerTempCache(player);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PrefixMain.playerPrefixDataHashMap.remove(player.getName());
    }


    @EventHandler
    public void PlayerMessageEvent(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!PrefixMain.gameapiEnabled || RoomManager.getRoom(player) == null) {
            String identifier = PrefixAPI.getPlayerPrefixData(player.getName()).getDisplayedPrefixName();
            PrefixModifyMessageEvent prefixModifyMessageEvent = new PrefixModifyMessageEvent(player, identifier);
            Server.getInstance().getPluginManager().callEvent(prefixModifyMessageEvent);
            if (!prefixModifyMessageEvent.isCancelled()) {
                PrefixUtils.broadcastMessage("[" + prefixModifyMessageEvent.getMessageModifier() + prefixModifyMessageEvent.getDisplayedPrefix() + "§r] " + event.getPlayer().getName() + ": " + event.getMessage());
                event.setCancelled(true);
            }
        }
    }
}
