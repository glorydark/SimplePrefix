package glorydark.nukkit.forms;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.utils.TextFormat;
import gameapi.form.AdvancedFormWindowSimple;
import gameapi.form.element.ResponsiveElementButton;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixMain;
import glorydark.nukkit.PrefixUtils;
import glorydark.nukkit.data.PlayerData;
import glorydark.nukkit.data.PlayerPrefixData;
import glorydark.nukkit.data.PrefixData;

import java.util.Map;
import java.util.Set;

public class FormMain {

    public static String defaultPrefix = "默认称号";

    public static String notFound = "未知项目";

    public static String returnString = "返回";

    public static void showPrefixMain(Player player) {
        AdvancedFormWindowSimple window = new AdvancedFormWindowSimple("称号系统", "选择您需要的功能吧！");
        window.addButton(
                new ResponsiveElementButton("购买称号")
                        .onRespond(FormMain::showBuyPrefix)
        );
        window.addButton(
                new ResponsiveElementButton("我的称号")
                        .onRespond(FormMain::showSelectPrefix)
        );
        window.showToPlayer(player);
    }

    public static void showSelectPrefix(Player player) {
        AdvancedFormWindowSimple window = new AdvancedFormWindowSimple("称号系统 - 设置展示称号", "请选择您要展示的称号！");
        window.addButton(
                new ResponsiveElementButton(defaultPrefix)
                        .onRespond(player1 -> {
                            PlayerData data = PrefixAPI.getPlayerPrefixData(player1.getName());
                            if (data.getDisplayedPrefixData() != null) {
                                data.setDisplayedPrefix("null", true);
                            }
                            player1.sendMessage("§a成功设置当前称号为：" + PrefixMain.defaultPrefix);
                            PrefixMain.playerPrefixDataHashMap.put(player1.getName(), data);
                        })
        );
        Set<Map.Entry<String, PlayerPrefixData>> dataList = PrefixAPI.getPlayerPrefixData(player.getName()).getOwnedPrefixes().entrySet();
        if (!dataList.isEmpty()) {
            long currentMillis = System.currentTimeMillis();
            for (Map.Entry<String, PlayerPrefixData> data : dataList) {
                String identifier = data.getValue().getIdentifier();
                PrefixData prefixData = PrefixAPI.getPrefixData(identifier);
                if (prefixData == null) {
                    window.addButton(
                            new ResponsiveElementButton(notFound)
                                    .onRespond(player1 -> player1.sendMessage("§c该称号已被移除或已不存在！"))
                    );
                } else {
                    long expireMillis = data.getValue().getExpireMillis();
                    String leftTime;
                    if (data.getValue().getExpireMillis() != -1L) {
                        leftTime = PrefixUtils.secToTime((expireMillis - currentMillis) / 1000);
                    } else {
                        leftTime = "永久";
                    }
                    String prefix = prefixData.getRarity().isEmpty()?
                            PrefixMain.prefixRarityMap.getOrDefault(PrefixMain.defaultRarity, "")
                            : PrefixMain.prefixRarityMap.getOrDefault(prefixData.getRarity(), PrefixMain.prefixRarityMap.getOrDefault(PrefixMain.defaultRarity, ""));
                    window.addButton(
                            new ResponsiveElementButton((prefix.isEmpty()? "" : TextFormat.RESET + "[" + prefix + TextFormat.RESET + "] ") + prefixData.getName() + TextFormat.RESET + "\n剩余时间：" + leftTime)
                                    .onRespond(player1 -> {
                                        if (PrefixAPI.setDisplayedPrefix(player1.getName(), identifier)) {
                                            player1.sendMessage("§a成功设置当前称号为: " + PrefixAPI.getPrefixData(identifier).getName());
                                        } else {
                                            player1.sendMessage("§c该称号可能出现问题，请联系管理！");
                                        }
                                    })
                    );
                }
            }
        }
        window.showToPlayer(player);
    }

    public static void showBuyPrefix(Player player) {
        AdvancedFormWindowSimple window = new AdvancedFormWindowSimple("称号系统 - 购买称号", "请选择您要购买的称号！");
        window.addButton(
                new ResponsiveElementButton(returnString)
                        .onRespond(FormMain::showPrefixMain)
        );
        Set<Map.Entry<String, PrefixData>> dataList = PrefixMain.purchasablePrefixDataHashMap.entrySet();
        if (!dataList.isEmpty()) {
            for (Map.Entry<String, PrefixData> data : dataList) {
                PrefixData prefixData = data.getValue();
                if (prefixData == null) {
                    window.addButton(new ElementButton(notFound));
                } else {
                    long duration = prefixData.getDuration();
                    window.addButton(
                            new ResponsiveElementButton(prefixData.getName() + "\n价格：" + prefixData.getCost() + "金币 | 期限：" + (duration == -1 ? "永久" : PrefixUtils.secToTime((int) (duration / 1000))))
                                    .onRespond(prefixData::buy)
                    );
                }
            }
        }
        window.showToPlayer(player);
    }
}