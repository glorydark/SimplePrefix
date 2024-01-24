package glorydark.nukkit.provider;

import glorydark.nukkit.data.PlayerData;
import glorydark.nukkit.data.PrefixData;

/**
 * @author glorydark
 */
public interface PrefixProvider {

    PlayerData getPlayerPrefixData(String player);

    PrefixData getPrefixData(String identifier);

    boolean setDisplayedPrefix(String player, String identifier);


    /**
     * @param player     玩家名 - 字符串
     * @param identifier 称号名称 - 字符串
     * @param duration   持续时间 - long  -1为永久
     * @return 是否设置成功
     */
    boolean addOwnedPrefixes(String player, String identifier, long duration);
}
