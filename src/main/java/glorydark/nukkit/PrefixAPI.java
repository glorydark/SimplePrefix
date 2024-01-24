package glorydark.nukkit;

import glorydark.nukkit.data.PlayerData;
import glorydark.nukkit.data.PrefixData;

/**
 * @author glorydark
 */
public class PrefixAPI {

    public static PlayerData getPlayerPrefixData(String player) {
        return PrefixMain.getPlugin().getProvider().getPlayerPrefixData(player);
    }

    public static PrefixData getPrefixData(String identifier) {
        return PrefixMain.getPlugin().getProvider().getPrefixData(identifier);
    }

    public static boolean setDisplayedPrefix(String player, String identifier) {
        return PrefixMain.getPlugin().getProvider().setDisplayedPrefix(player, identifier);
    }


    /**
     * @param player     玩家名 - 字符串
     * @param identifier 称号名称 - 字符串
     * @param duration   持续时间 - long  -1为永久
     * @return 是否设置成功
     */
    public static boolean addOwnedPrefixes(String player, String identifier, long duration) {
        return PrefixMain.getPlugin().getProvider().addOwnedPrefixes(player, identifier, duration);
    }
}
