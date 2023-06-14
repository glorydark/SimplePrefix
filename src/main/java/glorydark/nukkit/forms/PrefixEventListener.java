package glorydark.nukkit.forms;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.data.PlayerData;
import glorydark.nukkit.data.PlayerPrefixData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PrefixEventListener implements Listener {
    public static final HashMap<Player, HashMap<Integer, FormType>> UI_CACHE = new HashMap<>();
    
    public static void showFormWindow(Player player, FormWindow window, FormType guiType) {
        UI_CACHE.computeIfAbsent(player, i -> new HashMap<>()).put(player.showFormWindow(window), guiType);
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
