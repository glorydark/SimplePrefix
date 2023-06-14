package glorydark.nukkit.forms;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.*;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixMain;
import glorydark.nukkit.PrefixUtils;
import glorydark.nukkit.data.MessageDecorationType;
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
    public void PlayerJoinEvent(PlayerJoinEvent event){
        Player player = event.getPlayer();
        File file = new File(PrefixMain.path + "/" + player.getName() + ".yml");
        if(file.exists()){
            PlayerData playerData = new PlayerData(file);
            for(Map.Entry<String, PlayerPrefixData> entry : playerData.getOwnedPrefixes().entrySet()){
                if(!PrefixMain.prefixDataHashMap.containsKey(entry.getKey())){
                    playerData.getOwnedPrefixes().remove(entry.getKey()); // 移除不存在的称号
                }
            }
            PrefixMain.playerPrefixDataHashMap.put(player.getName(), playerData);
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        PrefixMain.playerPrefixDataHashMap.remove(player.getName());
    }


    @EventHandler
    public void PlayerMessageEvent(PlayerChatEvent event){
        Player player = event.getPlayer();
        String identifier = PrefixAPI.getPlayerPrefixData(player.getName()).getDisplayedPrefix();
        PrefixModifyMessageEvent prefixModifyMessageEvent = new PrefixModifyMessageEvent(player, identifier);
        Server.getInstance().getPluginManager().callEvent(prefixModifyMessageEvent);
        if(!prefixModifyMessageEvent.isCancelled()){
            PrefixUtils.broadcastMessage(prefixModifyMessageEvent.getMessageModifier()+"["+ prefixModifyMessageEvent.getDisplayedPrefix()+"§r"+prefixModifyMessageEvent.getMessageModifier()+"§f] "+event.getPlayer().getName()+": "+event.getMessage());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerFormRespondedEvent(PlayerFormRespondedEvent event){
        Player p = event.getPlayer();
        FormWindow window = event.getWindow();
        if (p == null || window == null) {
            return;
        }
        FormType guiType = UI_CACHE.containsKey(p) ? UI_CACHE.get(p).get(event.getFormID()) : null;
        if(guiType == null){
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
        if(window.getResponse() == null){
            return;
        }
        switch (guiType){
            case SelectPrefix:
                int id = window.getResponse().getClickedButtonId() - 1;
                if(id < 0){
                    player.sendMessage("您还未选择一个称号");
                    return;
                }else{
                    PlayerData data = PrefixAPI.getPlayerPrefixData(player.getName());
                    Set<Map.Entry<String, PlayerPrefixData>> entrySet = data.getOwnedPrefixes().entrySet();
                    if(entrySet.size() >= id + 1){
                        Map.Entry<String, PlayerPrefixData> entry = (Map.Entry<String, PlayerPrefixData>) entrySet.toArray()[id];
                        String identifier = entry.getValue().getIdentifier();
                        if(PrefixAPI.setDisplayedPrefix(player.getName(), identifier)) {
                            player.sendMessage("成功设置当前称号为" +PrefixAPI.getPrefixData(identifier).getName());
                        }else{
                            player.sendMessage("该称号可能出现问题，请联系管理！");
                        }
                    }
                }
                break;
        }
    }

    private void formWindowCustomOnClick(Player player, FormWindowCustom window, FormType guiType) {

    }

    private void formWindowModalOnClick(Player player, FormWindowModal window, FormType guiType) {

    }

}
