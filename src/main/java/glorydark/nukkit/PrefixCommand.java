package glorydark.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import glorydark.nukkit.data.PrefixData;
import glorydark.nukkit.forms.FormMain;
import org.checkerframework.checker.units.qual.Prefix;

public class PrefixCommand extends Command {
    public PrefixCommand(String command) {
        super(command);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender.isPlayer()) {
            if (strings.length == 0) {
                FormMain.showPrefixMain((Player) commandSender);
                return true;
            }
            Player player = (Player) commandSender;
            switch (strings[0]) {
                case "buy":
                    if (strings.length != 2) {
                        FormMain.showBuyPrefix(player);
                    } else {
                        if (PrefixMain.purchasablePrefixDataHashMap.containsKey(strings[1])) {
                            PrefixAPI.getPrefixData(strings[1]).buy(player);
                        } else {
                            commandSender.sendMessage("§c* 该称号不可购买或不存在！");
                        }
                    }
                    break;
                case "wear":
                    FormMain.showSelectPrefix(player);
                    break;
            }
        } else {
            switch (strings[0]) {
                case "reload":
                    PrefixMain.getPlugin().loadPrefix();
                    PrefixMain.getPlugin().reloadPlayerData();
                    commandSender.sendMessage(TextFormat.GREEN + "重载称号及玩家数据成功！");
                    break;
                case "give":
                    if (strings.length != 4) {
                        return false;
                    }
                    String player = strings[1];
                    String identifier = strings[2];
                    long duration = Long.parseLong(strings[3]);
                    if (PrefixAPI.addOwnedPrefix(player, identifier, duration)) {
                        PrefixData prefixData = PrefixMain.getPlugin().getProvider().getPrefixData(identifier);
                        commandSender.sendMessage(TextFormat.GREEN + "给予玩家称号 " + prefixData.getName() + TextFormat.RESET + " * " + PrefixUtils.secToTime((int) (duration / 1000L)) + " 成功");
                        Player recipient = Server.getInstance().getPlayer(player);
                        if (recipient != null) {
                            recipient.sendMessage(TextFormat.GOLD + "恭喜你获得称号 " + prefixData.getName() + TextFormat.RESET + " * " + PrefixUtils.secToTime((int) (duration / 1000L)));
                        }
                    } else {
                        commandSender.sendMessage("给予失败！");
                    }
                    break;
            }
        }
        return true;
    }
}
