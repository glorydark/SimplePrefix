package glorydark.nukkit;

import glorydark.nukkit.data.PlayerData;
import glorydark.nukkit.data.PlayerPrefixData;
import glorydark.nukkit.data.PrefixData;

public class PrefixAPI {

    public static PlayerData getPrefixData(String player){
        if(PrefixMain.playerPrefixDataHashMap.containsKey(player)){
            return PrefixMain.playerPrefixDataHashMap.get(player);
        }else{
            // 防止配置文件生成太多，只有有称号的才会保存。
            // 同时这个也方便其他系统来对接，临时发放称号。
            return new PlayerData();
        }
    }


    /**
     *
     * @param player 玩家名 - 字符串
     * @param identifier 称号名称 - 字符串
     * @param duration 持续时间 - long  -1为永久
     *
     * @return 是否设置成功
     */
    public static boolean addOwnedPrefixes(String player, String identifier, long duration) {
        PlayerData data = getPrefixData(player);
        PrefixData prefixData = PrefixMain.prefixDataHashMap.get(identifier);
        if(data.getDisplayedPrefix().equals("")) {
            data.setDisplayedPrefix(identifier, true);
        }
        if(prefixData == null){
            return false;
        }
        if(data.getConfig() != null){
            long expiredMillis;
            if(data.getOwnedPrefixes().containsKey(identifier)){
                String expireDate = data.getConfig().get("prefixes."+identifier, "");
                if(expireDate.equals("permanent")){
                    return false;
                }
                expiredMillis = Long.parseLong(expireDate) + duration;
            }else{
                expiredMillis = System.currentTimeMillis() + duration;
            }
            data.getConfig().set("prefixes."+identifier, duration == -1? "permanent" :  expiredMillis);
            data.getOwnedPrefixes().put(identifier, new PlayerPrefixData(prefixData.getIdentifier(), expiredMillis));
            data.getConfig().save();
        }
        PrefixMain.playerPrefixDataHashMap.put(player, data);
        return true;
    }

}
