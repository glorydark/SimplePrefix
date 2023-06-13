package glorydark.nukkit.data;

import cn.nukkit.utils.Config;
import glorydark.nukkit.PrefixMain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PrefixData {

    protected Config config;

    protected String displayedPrefix;

    protected List<String> ownedPrefixes;

    public PrefixData(){
        this.config = null;
        this.displayedPrefix = "";
        this.ownedPrefixes = new ArrayList<>();
    }

    public PrefixData(String player){
        File file = new File(PrefixMain.path+"/"+player+".yml");
        if(!file.exists()) {
            this.config = new Config(file, Config.YAML);
        }
        this.fixConfig();
        this.displayedPrefix = config.getString("displayed_prefix", "");
        this.ownedPrefixes = new ArrayList<>(config.getStringList("prefixes"));
    }

    public PrefixData(File file){
        this.config = new Config(file, Config.YAML);
        this.fixConfig();
        this.displayedPrefix = config.getString("displayed_prefix", "");
        this.ownedPrefixes = new ArrayList<>(config.getStringList("prefixes"));
    }

    public String getDisplayedPrefix() {
        return displayedPrefix;
    }

    public List<String> getOwnedPrefixes() {
        return ownedPrefixes;
    }

    public void addOwnedPrefixes(String prefix, boolean save) {
        this.ownedPrefixes.add(prefix);
        if(config != null && save){
            this.config.set("prefixes", ownedPrefixes);
            this.config.save();
        }
    }

    public void setDisplayedPrefix(String prefix, boolean save){
        this.displayedPrefix = prefix;
        if(config != null && save){
            this.config.set("displayed_prefix", prefix);
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
