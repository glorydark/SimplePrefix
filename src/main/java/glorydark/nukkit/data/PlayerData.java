package glorydark.nukkit.data;

import glorydark.nukkit.PrefixMain;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class PlayerData {

    protected PrefixData displayedPrefix;

    protected Map<String, PlayerPrefixData> ownedPrefixes = new LinkedHashMap<>();

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

    public Map<String, PlayerPrefixData> getOwnedPrefixes() {
        return ownedPrefixes;
    }

    public abstract void setDisplayedPrefix(String identifier, boolean save);
}
