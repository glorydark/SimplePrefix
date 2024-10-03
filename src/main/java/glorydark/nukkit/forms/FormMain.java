package glorydark.nukkit.forms;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixMain;
import glorydark.nukkit.PrefixUtils;
import glorydark.nukkit.data.PlayerPrefixData;
import glorydark.nukkit.data.PrefixData;

import java.util.Map;
import java.util.Set;

public class FormMain {

    public static String defaultPrefix = "默认称号";

    public static String notFound = "未知项目";

    public static String returnString = "返回";

    public static void showPrefixMain(Player player) {
        FormWindowSimple window = new FormWindowSimple("称号系统", "选择您需要的功能吧！");
        window.addButton(new ElementButton("购买称号"));
        window.addButton(new ElementButton("我的称号"));
        PrefixEventListener.showFormWindow(player, window, FormType.PrefixMain);
    }

    public static void showSelectPrefix(Player player) {
        FormWindowSimple window = new FormWindowSimple("称号系统 - 设置展示称号", "请选择您要展示的称号！");
        window.addButton(new ElementButton(defaultPrefix));
        Set<Map.Entry<String, PlayerPrefixData>> dataList = PrefixAPI.getPlayerPrefixData(player.getName()).getOwnedPrefixes().entrySet();
        if (!dataList.isEmpty()) {
            for (Map.Entry<String, PlayerPrefixData> data : dataList) {
                String identifier = data.getValue().getIdentifier();
                PrefixData prefixData = PrefixAPI.getPrefixData(identifier);
                if (prefixData == null) {
                    window.addButton(new ElementButton(notFound));
                } else {
                    window.addButton(new ElementButton(prefixData.getName() + TextFormat.RESET + "\n剩余时间：" + PrefixUtils.secToTime((int) (data.getValue().getExpireMillis() - System.currentTimeMillis()) / 1000)));
                }
            }
        }
        PrefixEventListener.showFormWindow(player, window, FormType.SelectPrefix);
    }

    public static void showBuyPrefix(Player player) {
        FormWindowSimple window = new FormWindowSimple("称号系统 - 购买称号", "请选择您要购买的称号！");
        window.addButton(new ElementButton(returnString));
        Set<Map.Entry<String, PrefixData>> dataList = PrefixMain.purchasablePrefixDataHashMap.entrySet();
        if (!dataList.isEmpty()) {
            for (Map.Entry<String, PrefixData> data : dataList) {
                PrefixData prefixData = data.getValue();
                if (prefixData == null) {
                    window.addButton(new ElementButton(notFound));
                } else {
                    long duration = prefixData.getDuration();
                    window.addButton(new ElementButton(prefixData.getName() + "\n价格：" + prefixData.getCost() + "金币 | 期限：" + (duration == -1 ? "永久" : PrefixUtils.secToTime((int) (duration / 1000)))));
                }
            }
        }
        PrefixEventListener.showFormWindow(player, window, FormType.BuyPrefix);
    }

}