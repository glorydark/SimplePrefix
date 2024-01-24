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

    protected HashMap<String, PlayerPrefixData> ownedPrefixes = new HashMap<>();

    public PlayerData() {
        this.config = null; // 临时创建的时候，不创建config
        this.displayedPrefix = null;
    }

    public PlayerData(String player) {
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

    public PlayerData(File file) {
        this.config = new Config(file, Config.YAML);
        this.fixConfig();
        this.displayedPrefix = PrefixAPI.getPrefixData((config.getString("displayed_prefix")));
        for (String identifier : config.getSection("prefixes").getKeys(false)) {
            String expireMillis = config.get("prefixes." + identifier).toString();
            this.ownedPrefixes.put(identifier, new PlayerPrefixData(identifier, expireMillis.equals("permanent") ? -1 : Long.parseLong(expireMillis)));
        }
    }

    public PrefixData getDisplayedPrefixData() {
        return displayedPrefix;
    }

    public String getDisplayedPrefix() {
        return displayedPrefix == null ? PrefixMain.defaultPrefix : displayedPrefix.getName();
    }

    public HashMap<String, PlayerPrefixData> getOwnedPrefixes() {
        return ownedPrefixes;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setDisplayedPrefix(String identifier, boolean save) {
        this.displayedPrefix = PrefixAPI.getPrefixData(identifier);
        if (config != null && save) {
            this.config.set("displayed_prefix", identifier);
            this.config.save();
        }
    }

    protected void fixConfig() {
        if (config != null) {
            if (!config.exists("displayed_prefix")) {
                config.set("displayed_prefix", "null");
            }
            if (!config.exists("prefixes")) {
                config.set("prefixes", new ArrayList<>());
            }
        }
    }

}
