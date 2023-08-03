package glorydark.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import glorydark.nukkit.forms.FormMain;

public class PrefixCommand extends Command {
    public PrefixCommand(String command) {
        super(command);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(commandSender.isPlayer()){
            if(strings.length == 0){
               FormMain.showPrefixMain(commandSender.asPlayer());
               return true;
            }
            Player player = (Player) commandSender;
            switch (strings[0]){
                case "buy":
                    if(strings.length != 2){
                        FormMain.showBuyPrefix(player);
                    }else {
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
        }else{
            switch (strings[0]){
                case "give":
                    if(strings.length != 4){
                        return false;
                    }
                    String player = strings[1];
                    String identifier = strings[2];
                    long duration = Long.parseLong(strings[3]);
                    if(PrefixAPI.addOwnedPrefixes(player, identifier, duration)){
                        commandSender.sendMessage("给予成功！");
                    }else{
                        commandSender.sendMessage("给予失败！");
                    }
                    break;
            }
        }
        return true;
    }
}
