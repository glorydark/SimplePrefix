package glorydark.nukkit.data;

import cn.nukkit.utils.Config;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixMain;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerData {

    protected Config config;

    protected PrefixData displayedPrefix;

    protected HashMap<String, PlayerPrefixData> ownedPrefixes;

    public PlayerData(){
        this.config = null; // 临时创建的时候，不创建config
        this.displayedPrefix = null;
        this.ownedPrefixes = new HashMap<>();
    }

    public PlayerData(String player){
        File file = new File(PrefixMain.path+"/"+player+".yml");
        if(!file.exists()) {
            this.config = new Config(file, Config.YAML);
        }
        this.fixConfig();
        this.displayedPrefix = PrefixAPI.getPrefixData((config.getString("displayed_prefix")));
        for(String identifier : config.getSection("prefixes").getKeys(false)){
            this.ownedPrefixes.put(identifier, new PlayerPrefixData(identifier, config.getLong("prefixes."+identifier)));
        }
    }

    public PlayerData(File file){
        this.config = new Config(file, Config.YAML);
        this.fixConfig();
        this.displayedPrefix = PrefixAPI.getPrefixData((config.getString("displayed_prefix")));
        for(String identifier : config.getSection("prefixes").getKeys(false)){
            this.ownedPrefixes.put(identifier, new PlayerPrefixData(identifier, config.getLong("prefixes."+identifier)));
        }
    }

    public PrefixData getDisplayedPrefixData() {
        return displayedPrefix;
    }

    public String getDisplayedPrefix() {
        return displayedPrefix == null? "萌新驾到": displayedPrefix.getName();
    }

    public HashMap<String, PlayerPrefixData> getOwnedPrefixes() {
        return ownedPrefixes;
    }

    public Config getConfig() {
        return config;
    }

    public void setDisplayedPrefix(String identifier, boolean save){
        this.displayedPrefix = PrefixAPI.getPrefixData(identifier);
        if(config != null && save){
            this.config.set("displayed_prefix", identifier);
            this.config.save();
        }
    }

    protected void fixConfig(){
        if(config != null) {
            if (!config.exists("displayed_prefix")) {
                config.set("displayed_prefix", "");
            }
            if (!config.exists("prefixes")) {
                config.set("prefixes", new ArrayList<>());
            }
        }
    }

}
