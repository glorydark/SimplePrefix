package glorydark.nukkit.forms;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixMain;
import glorydark.nukkit.PrefixUtils;
import glorydark.nukkit.data.PlayerPrefixData;
import glorydark.nukkit.data.PrefixData;

import java.util.Map;
import java.util.Set;

public class FormMain {

    public static String noSelectedItemText = "- 未选择 -";

    public static String notFound = "未知项目";

    public static void showSelectPrefix(Player player){
        FormWindowSimple window = new FormWindowSimple("称号系统 - 设置展示称号", "请选择您要展示的称号！");
        window.addButton(new ElementButton(noSelectedItemText));
        Set<Map.Entry<String, PlayerPrefixData>> dataList = PrefixAPI.getPlayerPrefixData(player.getName()).getOwnedPrefixes().entrySet();
        if(dataList.size() > 0) {
            for (Map.Entry<String, PlayerPrefixData> data : dataList) {
                String identifier = data.getValue().getIdentifier();
                PrefixData prefixData = PrefixAPI.getPrefixData(identifier);
                if(prefixData == null){
                    window.addButton(new ElementButton(notFound));
                }else{
                    window.addButton(new ElementButton(prefixData.getName()+"\n剩余时间："+ PrefixUtils.secToTime((int) ((System.currentTimeMillis() - data.getValue().getExpireMillis()) / 1000))));
                }
            }
        }
        PrefixEventListener.showFormWindow(player, window, FormType.SelectPrefix);
    }

}