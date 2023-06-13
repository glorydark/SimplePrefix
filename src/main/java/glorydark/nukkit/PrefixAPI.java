package glorydark.nukkit;

import glorydark.nukkit.data.PlayerPrefixData;

public class PrefixAPI {

    public static PlayerPrefixData getPrefixData(String player){
        if(PrefixMain.prefixDataHashMap.containsKey(player)){
            return PrefixMain.prefixDataHashMap.get(player);
        }else{
            // 防止配置文件生成太多，只有有称号的才会保存。
            // 同时这个也方便其他系统来对接，临时发放称号。
            return new PlayerPrefixData();
        }
    }


    /**
     *
     * @param player 玩家名 - 字符串
     * @param prefix 称号名称 - 字符串
     * @param duration 持续时间 - long
     *
     * @return 是否设置成功
     */
    public static boolean addOwnedPrefixes(String player, String prefix, double duration) {
        PlayerPrefixData data = getPrefixData(player);
        if(data.getDisplayedPrefix().equals("")) {
            data.setDisplayedPrefix(prefix, true);
        }
        data.getOwnedPrefixes().add(prefix);
        if(data.getConfig() != null){
            if(data.getOwnedPrefixes().contains(prefix)){
                String expireDate = data.getConfig().get("prefixes"+prefix, "");
                if(expireDate.equals("permanent")){
                    return false;
                }
                data.getConfig().set("prefixes."+prefix, duration == -1? "permanent" :  + duration);
            }else{
                data.getConfig().set("prefixes."+prefix, duration == -1? "permanent" : System.currentTimeMillis() + duration);
            }
            data.getConfig().save();
        }
        return true;
    }

}
