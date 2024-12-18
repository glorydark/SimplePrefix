package glorydark.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import gameapi.GameAPI;
import glorydark.nukkit.data.PlayerPrefixData;
import glorydark.nukkit.data.PrefixData;
import glorydark.nukkit.forms.FormMain;
import org.checkerframework.checker.units.qual.Prefix;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

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
            File file = new File(PrefixMain.path + "/players/");
            switch (strings[0]) {
                case "reload":
                    PrefixMain.getPlugin().getProvider().loadPrefix();
                    PrefixMain.getPlugin().getProvider().reloadPlayerData();
                    commandSender.sendMessage(TextFormat.GREEN + "重载称号及玩家数据成功！");
                    break;
                case "give":
                    if (strings.length != 4) {
                        return false;
                    }
                    String player = strings[1];
                    String identifier = strings[2];
                    long duration = Long.parseLong(strings[3]);
                    if (player.equals("@a")) {
                        for (Player value : Server.getInstance().getOnlinePlayers().values()) {
                            Server.getInstance().dispatchCommand(commandSender, "prefix give \"" + value.getName() + "\" " + identifier + " " + duration);
                        }
                        return false;
                    }
                    if (!Server.getInstance().lookupName(player).isPresent()) {
                        commandSender.sendMessage(TextFormat.RED + "玩家不存在，玩家名：" + player);
                        return false;
                    }
                    if (PrefixAPI.addOwnedPrefix(player, identifier, duration)) {
                        PrefixData prefixData = PrefixMain.getPlugin().getProvider().getPrefixData(identifier);
                        commandSender.sendMessage(TextFormat.GREEN + "给予玩家 " + player + " 称号 " + prefixData.getName() + TextFormat.RESET + " * " + PrefixUtils.secToTime((int) (duration / 1000L)) + " 成功");
                        Player recipient = Server.getInstance().getPlayer(player);
                        if (recipient != null) {
                            recipient.sendMessage(TextFormat.GOLD + "恭喜你获得称号 " + prefixData.getName() + TextFormat.RESET + " * " + PrefixUtils.secToTime((int) (duration / 1000L)));
                        }
                    } else {
                        commandSender.sendMessage("给予失败！");
                    }
                    break;
                case "seeowner":
                    identifier = strings[1];
                    Map<String, Object> map = new LinkedHashMap<>();
                    for (File listFile : Objects.requireNonNull(file.listFiles())) {
                        String pn = listFile.getName().substring(0, listFile.getName().lastIndexOf("."));
                        PlayerPrefixData playerPrefixData = PrefixMain.getPlugin().getProvider().getPlayerPrefixData(pn).getOwnedPrefixes().getOrDefault(identifier, null);
                        if (playerPrefixData != null) {
                            String leftTime;
                            if (playerPrefixData.getExpireMillis() != -1L) {
                                long left = (playerPrefixData.getExpireMillis() - System.currentTimeMillis()) / 1000;
                                if (left <= 0) {
                                    PrefixMain.getPlugin().getProvider().removePrefix(pn, playerPrefixData.getIdentifier());
                                    continue;
                                }
                                leftTime = PrefixUtils.secToTime(left);
                            } else {
                                leftTime = "永久";
                            }
                            map.put(pn, leftTime);
                        }
                    }
                    PrefixMain.getPlugin().getLogger().info(TextFormat.YELLOW + "以下是拥有称号 " + TextFormat.GREEN + identifier + TextFormat.YELLOW + " 的玩家：");
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        PrefixMain.getPlugin().getLogger().info(entry.getKey() + ": " + entry.getValue());
                    }
                    break;
                case "clearemptyconf":
                    int count = 0;
                    for (File listFile : Objects.requireNonNull(file.listFiles())) {
                        Config config = new Config(listFile, Config.YAML);
                        if (config.getSection("prefixes").isEmpty()) {
                            listFile.delete();
                            count += 1;
                        }
                    }
                    commandSender.sendMessage("成功清理僵尸缓存 " + count + " 个！");
                    break;
                case "retrieve":
                    for (File listFile : Objects.requireNonNull(file.listFiles())) {
                        String pn = listFile.getName().substring(0, listFile.getName().lastIndexOf("."));
                        PrefixMain.getPlugin().getProvider().removePrefix(pn, strings[1]);
                    }
                    commandSender.sendMessage("成功收回所有称号：" + strings[1]);
                    break;
            }
        }
        return true;
    }
}
