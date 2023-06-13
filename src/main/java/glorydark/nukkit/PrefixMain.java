package glorydark.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMessageEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import glorydark.nukkit.data.PlayerData;
import glorydark.nukkit.data.PlayerPrefixData;
import glorydark.nukkit.data.PrefixData;
import glorydark.nukkit.event.PrefixChangeMessageEvent;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrefixMain extends PluginBase implements Listener {

    public static String path;

    public static HashMap<String, PlayerData> playerPrefixDataHashMap;

    public static HashMap<String, PrefixData> prefixDataHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        path = this.getDataFolder().getPath();
        this.saveDefaultConfig();
        this.getLogger().info("SimplePrefix 正在加载...");
        this.loadPrefix();
        this.getServer().getCommandMap().register("", new PrefixCommand("prefix"));
        this.getLogger().info("SimplePrefix 加载完成");
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event){
        Player player = event.getPlayer();
        File file = new File(path + "/" + player.getName() + ".yml");
        if(file.exists()){
            PlayerData playerData = new PlayerData(file);
            for(Map.Entry<String, PlayerPrefixData> entry : playerData.getOwnedPrefixes().entrySet()){
                if(!prefixDataHashMap.containsKey(entry.getKey())){
                    playerData.getOwnedPrefixes().remove(entry.getKey()); // 移除不存在的称号
                }
            }
            playerPrefixDataHashMap.put(player.getName(), playerData);
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        playerPrefixDataHashMap.remove(player.getName());
    }


    @EventHandler
    public void PlayerMessageEvent(PlayerMessageEvent event){
        Player player = event.getPlayer();
        PrefixChangeMessageEvent prefixChangeMessageEvent = new PrefixChangeMessageEvent(player, PrefixAPI.getPrefixData(player.getName()).getDisplayedPrefix());
        Server.getInstance().getPluginManager().callEvent(prefixChangeMessageEvent);
        if(!prefixChangeMessageEvent.isCancelled()){
            event.setMessage("["+prefixChangeMessageEvent.getPrefix()+"]"+event.getMessage());
        }
    }

    protected void loadPrefix(){
        Config config = new Config(path+"/config.yml", Config.YAML);
        if(config.exists("prefixes")){
            List<Map<String, Object>> prefixDataList = (List<Map<String, Object>>) config.get("prefixes");
            for(Map<String, Object> prefixDatum : prefixDataList){
                String identifier = (String) prefixDatum.get("identifier");
                prefixDataHashMap.put(identifier, new PrefixData(identifier, (String) prefixDatum.get("name"), (Double) prefixDatum.get("cost"), (Long) prefixDatum.get("duration")));
            }
        }
    }

}