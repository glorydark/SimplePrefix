package glorydark.nukkit.forms;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixMain;
import glorydark.nukkit.PrefixUtils;
import glorydark.nukkit.data.PlayerData;
import glorydark.nukkit.data.PlayerPrefixData;
import glorydark.nukkit.data.PrefixData;
import glorydark.nukkit.event.PrefixModifyMessageEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PrefixEventListener implements Listener {
    public static final HashMap<Player, HashMap<Integer, FormType>> UI_CACHE = new HashMap<>();

    public static void showFormWindow(Player player, FormWindow window, FormType guiType) {
        UI_CACHE.computeIfAbsent(player, i -> new HashMap<>()).put(player.showFormWindow(window), guiType);
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File file = new File(PrefixMain.path + "/players/" + player.getName() + ".yml");
        if (file.exists()) {
            PlayerData playerData = new PlayerData(file);
            for (Map.Entry<String, PlayerPrefixData> entry : playerData.getOwnedPrefixes().entrySet()) {
                if (!PrefixMain.prefixDataHashMap.containsKey(entry.getKey())) {
                    playerData.getOwnedPrefixes().remove(entry.getKey()); // 移除不存在的称号
                }
            }
            PrefixMain.playerPrefixDataHashMap.put(player.getName(), playerData);
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PrefixMain.playerPrefixDataHashMap.remove(player.getName());
    }


    @EventHandler
    public void PlayerMessageEvent(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String identifier = PrefixAPI.getPlayerPrefixData(player.getName()).getDisplayedPrefix();
        PrefixModifyMessageEvent prefixModifyMessageEvent = new PrefixModifyMessageEvent(player, identifier);
        Server.getInstance().getPluginManager().callEvent(prefixModifyMessageEvent);
        if (!prefixModifyMessageEvent.isCancelled()) {
            PrefixUtils.broadcastMessage(prefixModifyMessageEvent.getMessageModifier() + "[" + prefixModifyMessageEvent.getDisplayedPrefix() + "§r" + prefixModifyMessageEvent.getMessageModifier() + "§f] " + event.getPlayer().getName() + ": " + event.getMessage());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerFormRespondedEvent(PlayerFormRespondedEvent event) {
        Player p = event.getPlayer();
        FormWindow window = event.getWindow();
        if (p == null || window == null) {
            return;
        }
        FormType guiType = UI_CACHE.containsKey(p) ? UI_CACHE.get(p).get(event.getFormID()) : null;
        if (guiType == null) {
            return;
        }
        UI_CACHE.get(p).remove(event.getFormID());
        if (event.getResponse() == null) {
            return;
        }
        if (window instanceof FormWindowSimple) {
            this.formWindowSimpleOnClick(p, (FormWindowSimple) window, guiType);
        }
        if (window instanceof FormWindowCustom) {
            this.formWindowCustomOnClick(p, (FormWindowCustom) window, guiType);
        }
        if (window instanceof FormWindowModal) {
            this.formWindowModalOnClick(p, (FormWindowModal) window, guiType);
        }
    }

    private void formWindowSimpleOnClick(Player player, FormWindowSimple window, FormType guiType) {
        if (window.getResponse() == null) {
            return;
        }
        switch (guiType) {
            case SelectPrefix:
                if (window.getResponse().getClickedButton().getText().equals(FormMain.notFound)) {
                    player.sendMessage("§c该称号已被移除或已不存在！");
                    return;
                }
                int id = window.getResponse().getClickedButtonId() - 1;
                PlayerData data = PrefixAPI.getPlayerPrefixData(player.getName());
                if (id < 0) {
                    if (data.getDisplayedPrefixData() != null) {
                        data.setDisplayedPrefix("null", true);
                    }
                    player.sendMessage("§a成功设置当前称号为：" + PrefixMain.defaultPrefix);
                    PrefixMain.playerPrefixDataHashMap.put(player.getName(), data);
                    return;
                } else {
                    Set<Map.Entry<String, PlayerPrefixData>> entrySet = data.getOwnedPrefixes().entrySet();
                    if (entrySet.size() >= id + 1) {
                        Map.Entry<String, PlayerPrefixData> entry = (Map.Entry<String, PlayerPrefixData>) entrySet.toArray()[id];
                        String identifier = entry.getValue().getIdentifier();
                        if (PrefixAPI.setDisplayedPrefix(player.getName(), identifier)) {
                            player.sendMessage("§a成功设置当前称号为:" + PrefixAPI.getPrefixData(identifier).getName());
                        } else {
                            player.sendMessage("§c该称号可能出现问题，请联系管理！");
                        }
                    }
                }
                break;
            case BuyPrefix:
                id = window.getResponse().getClickedButtonId() - 1;
                if (id >= 0) {
                    Set<Map.Entry<String, PrefixData>> entrySet = PrefixMain.purchasablePrefixDataHashMap.entrySet();
                    if (entrySet.size() >= id + 1) {
                        Map.Entry<String, PrefixData> entry = (Map.Entry<String, PrefixData>) entrySet.toArray()[id];
                        entry.getValue().buy(player);
                    }
                } else {
                    FormMain.showPrefixMain(player);
                }
                break;
            case PrefixMain:
                switch (window.getResponse().getClickedButtonId()) {
                    case 0:
                        FormMain.showBuyPrefix(player);
                        break;
                    case 1:
                        FormMain.showSelectPrefix(player);
                        break;
                }
                break;
        }
    }

    private void formWindowCustomOnClick(Player player, FormWindowCustom window, FormType guiType) {

    }

    private void formWindowModalOnClick(Player player, FormWindowModal window, FormType guiType) {

    }

}
