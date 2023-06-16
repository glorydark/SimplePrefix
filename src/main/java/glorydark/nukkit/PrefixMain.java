package glorydark.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import glorydark.nukkit.data.MessageDecorationType;
import glorydark.nukkit.data.PlayerData;
import glorydark.nukkit.data.PrefixData;
import glorydark.nukkit.forms.PrefixEventListener;
import tip.utils.Api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrefixMain extends PluginBase implements Listener {

    public static String defaultPrefix = "萌新驾到";

    public static String path;

    public static HashMap<String, PlayerData> playerPrefixDataHashMap = new HashMap<>();

    public static HashMap<String, PrefixData> prefixDataHashMap = new HashMap<>();

    public static HashMap<String, PrefixData> purchasablePrefixDataHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        path = this.getDataFolder().getPath();
        this.saveDefaultConfig();
        this.getLogger().info("SimplePrefix 正在加载...");
        this.loadPrefix();
        this.reloadPlayerData();
        this.getServer().getPluginManager().registerEvents(new PrefixEventListener(), this);
        this.getServer().getCommandMap().register("", new PrefixCommand("prefix"));
        Api.registerVariables("SimplePrefix", PrefixVariable.class);
        this.getLogger().info("SimplePrefix 加载完成");
    }

    protected void loadPrefix(){
        Config config = new Config(path+"/config.yml", Config.YAML);
        if(config.exists("prefixes")){
            List<Map<String, Object>> prefixDataList = (List<Map<String, Object>>) config.get("prefixes");
            for(Map<String, Object> prefixDatum : prefixDataList){
                String identifier = (String) prefixDatum.get("identifier");
                List<MessageDecorationType> typeStringList = new ArrayList<>();
                for(String s: new ArrayList<>(config.getStringList("decoration_types"))){
                    typeStringList.add(MessageDecorationType.valueOf(s));
                }
                PrefixData data = new PrefixData(identifier, (String) prefixDatum.get("name"), (Double) prefixDatum.getOrDefault("price", -1), prefixDatum.getOrDefault("duration", 0).toString().equals("permanent")? -1: Long.parseLong(prefixDatum.get("duration").toString()), typeStringList);
                prefixDataHashMap.put(identifier, data);
                if(data.getCost() >= 0){
                    purchasablePrefixDataHashMap.put(identifier, data);
                }
            }
        }
    }

    protected void reloadPlayerData(){
        PrefixMain.playerPrefixDataHashMap.clear();
        for(Player player : Server.getInstance().getOnlinePlayers().values()){
            File file = new File(PrefixMain.path + "/players/" + player.getName() + ".yml");
            if(file.exists()){
                PlayerData playerData = new PlayerData(file);
                PrefixMain.playerPrefixDataHashMap.put(player.getName(), playerData);
            }
        }
    }

}