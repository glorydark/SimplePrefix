package glorydark.nukkit.data;

import cn.nukkit.utils.Config;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixMain;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerYamlData extends PlayerData {

    protected Config config;

    public PlayerYamlData() {
        this.config = null; // 临时创建的时候，不创建config
        this.displayedPrefix = null;
    }

    public PlayerYamlData(String player) {
        File file = new File(PrefixMain.path + "/players/" + player + ".yml");
        if (!file.exists()) {
            this.config = new Config(file, Config.YAML);
        }
        this.fixConfig();
        this.displayedPrefix = PrefixAPI.getPrefixData((config.getString("displayed_prefix")));
        for (String identifier : config.getSection("prefixes").getKeys(false)) {
            this.ownedPrefixes.put(identifier, new PlayerPrefixData(identifier, config.getLong("prefixes." + identifier)));
        }
    }

    public PlayerYamlData(File file) {
        this.config = new Config(file, Config.YAML);
        this.fixConfig();
        this.displayedPrefix = PrefixAPI.getPrefixData((config.getString("displayed_prefix")));
        for (String identifier : config.getSection("prefixes").getKeys(false)) {
            String expireMillis = config.get("prefixes." + identifier).toString();
            this.ownedPrefixes.put(identifier, new PlayerPrefixData(identifier, expireMillis.equals("permanent") ? -1 : Long.parseLong(expireMillis)));
        }
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setDisplayedPrefix(String identifier, boolean save) {
        this.displayedPrefix = PrefixAPI.getPrefixData(identifier);
        if (this.config != null && save) {
            this.config.set("displayed_prefix", identifier);
            this.config.save();
        }
    }

    protected void fixConfig() {
        if (this.config != null) {
            if (!this.config.exists("displayed_prefix")) {
                this.config.set("displayed_prefix", "null");
            }
            if (!this.config.exists("prefixes")) {
                this.config.set("prefixes", new ArrayList<>());
            }
        }
    }

}
