package glorydark.nukkit;

import cn.nukkit.utils.Config;
import glorydark.nukkit.data.PlayerData;
import glorydark.nukkit.data.PlayerPrefixData;
import glorydark.nukkit.data.PrefixData;

public class PrefixAPI {

    public static PlayerData getPlayerPrefixData(String player){
        if(PrefixMain.playerPrefixDataHashMap.containsKey(player)){
            return PrefixMain.playerPrefixDataHashMap.get(player);
        }else{
            // 防止配置文件生成太多，只有有称号的才会保存。
            // 同时这个也方便其他系统来对接，临时发放称号。
            return new PlayerData();
        }
    }

    public static PrefixData getPrefixData(String identifier){
        return PrefixMain.prefixDataHashMap.get(identifier);
    }

    public static boolean setDisplayedPrefix(String player, String identifier){
        PlayerData data = getPlayerPrefixData(player);

        if(data.getOwnedPrefixes().containsKey(identifier)){
            if(data.getDisplayedPrefixData() == null || !data.getDisplayedPrefixData().getIdentifier().equals(identifier)){
                data.setDisplayedPrefix(identifier, true);
                return true;
            }
        }
        return false;
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
        PlayerData data = getPlayerPrefixData(player);
        PrefixData prefixData = PrefixAPI.getPrefixData(identifier);
        data.setConfig(new Config(PrefixMain.path+"/players/"+player+".yml", Config.YAML)); // 防止为空
        if(data.getDisplayedPrefix().equals("")) {
            data.setDisplayedPrefix(identifier, true);
        }
        if(prefixData == null){
            return false;
        }
        long expiredMillis;
        String expireDate = data.getConfig().get("prefixes."+identifier, "");
        if(expireDate.equals("permanent")){
            return false;
        }
        if(data.getOwnedPrefixes().containsKey(identifier)){
            expiredMillis = Long.parseLong(expireDate) + duration;
        }else{
            expiredMillis = System.currentTimeMillis() + duration;
        }
        if(duration == -1){
            data.getConfig().set("prefixes."+identifier, "permanent");
            data.getOwnedPrefixes().put(identifier, new PlayerPrefixData(prefixData.getIdentifier(), -1));
        }else{
            data.getConfig().set("prefixes."+identifier, expiredMillis);
            data.getOwnedPrefixes().put(identifier, new PlayerPrefixData(prefixData.getIdentifier(), expiredMillis));
        }
        data.getConfig().save();
        PrefixMain.playerPrefixDataHashMap.put(player, data);
        return true;
    }

}
