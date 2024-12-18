package glorydark.nukkit.data;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixMain;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class PlayerYamlData extends PlayerData {

    @Setter
    @Getter
    protected ConfigSection config;

    protected String playerName = "";

    public PlayerYamlData(String player) {
        this.playerName = player;
        File file = new File(PrefixMain.path + "/players/" + player + ".yml");
        if (file.exists()) {
            this.config = new Config(file, Config.YAML).getRootSection();
        } else {
            this.config = new ConfigSection();
        }
        this.fixConfig();
        this.displayedPrefix = PrefixAPI.getPrefixData(this.config.getString("displayed_prefix"));
        for (String identifier : this.config.getSection("prefixes").getKeys(false)) {
            this.ownedPrefixes.put(identifier, new PlayerPrefixData(identifier, this.config.getSection("prefixes").getLong(identifier)));
        }
    }

   /*
    public PlayerYamlData(File file) {
        this.config = new Config(file, Config.YAML).getRootSection();
        this.fixConfig();
        this.displayedPrefix = PrefixAPI.getPrefixData((config.getString("displayed_prefix")));
        for (String identifier : config.getSection("prefixes").getKeys(false)) {
            String expireMillis = config.get("prefixes." + identifier).toString();
            this.ownedPrefixes.put(identifier, new PlayerPrefixData(identifier, expireMillis.equals("permanent") ? -1 : Long.parseLong(expireMillis)));
        }
    }
    */

    public void saveConfig() {
        File file = new File(PrefixMain.path + "/players/" + this.playerName + ".yml");
        Config conf = new Config(file, Config.YAML);
        conf.setAll(this.config);
        conf.save();
    }

    public void deleteConfig() {
        File file = new File(PrefixMain.path + "/players/" + this.playerName + ".yml");
        file.delete();
    }

    public void setDisplayedPrefix(String identifier, boolean save) {
        this.displayedPrefix = PrefixAPI.getPrefixData(identifier);
        if (this.config != null && save) {
            this.config.set("displayed_prefix", identifier);
            this.saveConfig();
        }
    }

    protected void fixConfig() {
        if (this.config != null) {
            if (!this.config.exists("displayed_prefix")) {
                this.config.set("displayed_prefix", "null");
            }
            if (!this.config.exists("prefixes")) {
                this.config.set("prefixes", new LinkedHashMap<>());
            }
        }
    }

}
