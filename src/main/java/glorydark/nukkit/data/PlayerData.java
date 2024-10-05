package glorydark.nukkit.data;

import cn.nukkit.utils.Config;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixMain;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class PlayerData {

    protected PrefixData displayedPrefix;

    protected HashMap<String, PlayerPrefixData> ownedPrefixes = new HashMap<>();

    public PlayerData() {
        this.displayedPrefix = null;
    }

    public PlayerData(String player) {

    }

    public PrefixData getDisplayedPrefixData() {
        return displayedPrefix;
    }

    public String getDisplayedPrefixId() {
        return displayedPrefix == null ? "" : displayedPrefix.getIdentifier();
    }

    public String getDisplayedPrefixName() {
        return displayedPrefix == null ? PrefixMain.defaultPrefix : displayedPrefix.getName();
    }

    public HashMap<String, PlayerPrefixData> getOwnedPrefixes() {
        return ownedPrefixes;
    }

    public abstract void setDisplayedPrefix(String identifier, boolean save);
}
