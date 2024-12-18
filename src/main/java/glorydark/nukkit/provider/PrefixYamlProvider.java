package glorydark.nukkit.provider;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import glorydark.nukkit.PrefixMain;
import glorydark.nukkit.data.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PrefixYamlProvider implements PrefixProvider {

    public PrefixYamlProvider() {

    }

    public PlayerData getPlayerPrefixData(String player) {
        if (PrefixMain.playerPrefixDataHashMap.containsKey(player)) {
            return PrefixMain.playerPrefixDataHashMap.get(player);
        } else {
            // 防止配置文件生成太多，只有有称号的才会保存。
            // 同时这个也方便其他系统来对接，临时发放称号。
            return new PlayerYamlData(player);
        }
    }

    public PrefixData getPrefixData(String identifier) {
        return PrefixMain.prefixDataHashMap.get(identifier);
    }

    public boolean setDisplayedPrefix(String player, String identifier) {
        PlayerData data = getPlayerPrefixData(player);

        if (data.getOwnedPrefixes().containsKey(identifier)) {
            data.setDisplayedPrefix(identifier, true);
            return true;
        }
        return false;
    }


    /**
     * @param player     玩家名 - 字符串
     * @param identifier 称号名称 - 字符串
     * @param duration   持续时间 - long  -1为永久
     * @return 是否设置成功
     */
    public boolean addPrefix(String player, String identifier, long duration) {
        PlayerYamlData data = (PlayerYamlData) getPlayerPrefixData(player);
        PrefixData prefixData = getPrefixData(identifier);
        // data.setConfig(new Config(PrefixMain.path + "/players/" + player + ".yml", Config.YAML)); // 防止为空
        if (data.getDisplayedPrefixName().isEmpty()) {
            data.setDisplayedPrefix(identifier, true);
        }
        if (prefixData == null) {
            return false;
        }
        long expiredMillis;
        String expireDate = String.valueOf(data.getConfig().get("prefixes." + identifier));
        if (expireDate.equals("permanent")) {
            return false;
        }
        if (data.getOwnedPrefixes().containsKey(identifier)) {
            expiredMillis = Long.parseLong(expireDate) + duration;
        } else {
            expiredMillis = System.currentTimeMillis() + duration;
        }
        if (duration == -1) {
            data.getConfig().set("prefixes." + identifier, -1L);
            data.getOwnedPrefixes().put(identifier, new PlayerPrefixData(prefixData.getIdentifier(), -1L));
        } else {
            data.getConfig().set("prefixes." + identifier, expiredMillis);
            data.getOwnedPrefixes().put(identifier, new PlayerPrefixData(prefixData.getIdentifier(), expiredMillis));
        }
        data.saveConfig();
        PrefixMain.playerPrefixDataHashMap.put(player, data);
        return true;
    }

    @Override
    public void removePrefix(String player, String identifier) {
        PlayerYamlData data = (PlayerYamlData) getPlayerPrefixData(player);
        if (data.getDisplayedPrefixName().equals(getPrefixData(identifier).getName())) {
            data.setDisplayedPrefix("", true);
        }
        data.getOwnedPrefixes().remove(identifier);
        ConfigSection section = data.getConfig().getSection("prefixes");
        section.remove(identifier);
        if (!section.isEmpty()) {
            data.getConfig().set("prefixes", section);
            data.saveConfig();
        } else {
            data.deleteConfig();
        }
    }

    @Override
    public void reloadPlayerData() {
        PrefixMain.playerPrefixDataHashMap.clear();
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            generatePlayerTempCache(player);
        }
    }

    @Override
    public void loadPrefix() {
        PrefixMain.prefixDataHashMap.clear();
        PrefixMain.purchasablePrefixDataHashMap.clear();
        PrefixMain.prefixRarityMap.clear();
        Config config = new Config(PrefixMain.path + "/config.yml", Config.YAML);
        PrefixMain.defaultRarity = "普通";
        if (config.exists("prefixes")) {
            List<Map<String, Object>> prefixDataList = (List<Map<String, Object>>) config.get("prefixes");
            for (Map<String, Object> prefixDatum : prefixDataList) {
                String identifier = (String) prefixDatum.get("identifier");
                PrefixData data = new PrefixData(identifier, (String) prefixDatum.get("name"), (String) prefixDatum.getOrDefault("rarity", ""), (Double) prefixDatum.getOrDefault("price", -1), prefixDatum.getOrDefault("duration", 0).toString().equals("permanent") ? -1 : Long.parseLong(prefixDatum.get("duration").toString()));
                PrefixMain.prefixDataHashMap.put(identifier, data);
                if (data.getCost() >= 0) {
                    PrefixMain.purchasablePrefixDataHashMap.put(identifier, data);
                }
            }
        }
        PrefixMain.defaultRarity = config.getString("default_rarity", "");
        if (config.exists("rarity")) {
            Map<String, Object> rarityMap = config.get("rarity", new LinkedHashMap<>());
            for (Map.Entry<String, Object> entry : rarityMap.entrySet()) {
                PrefixMain.prefixRarityMap.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
    }

    @Override
    public void generatePlayerTempCache(Player player) {
        PlayerData playerData = new PlayerYamlData(player.getName());
        PrefixMain.playerPrefixDataHashMap.put(player.getName(), playerData);
        for (Map.Entry<String, PlayerPrefixData> entry : playerData.getOwnedPrefixes().entrySet()) {
            if (!PrefixMain.prefixDataHashMap.containsKey(entry.getKey())) {
                playerData.getOwnedPrefixes().remove(entry.getKey()); // 移除不存在的称号
            }
        }
    }
}
