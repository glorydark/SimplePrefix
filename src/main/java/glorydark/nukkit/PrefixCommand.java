package glorydark.nukkit;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class PrefixCommand extends Command {
    public PrefixCommand(String command) {
        super(command);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(commandSender.isPlayer()){
            if(strings.length == 0){
                return false;
            }
            Player player = commandSender.asPlayer();
            switch (strings[0]){
                case "buy":
                    if(strings.length != 2){
                        commandSender.sendMessage("§c使用方法: /prefix buy 称号标识符");
                    }
                    if(PrefixMain.prefixDataHashMap.containsKey(strings[1])){
                        PrefixAPI.getPrefixData(strings[1]).buy(player);
                    }else{
                        commandSender.sendMessage("§c* 不存在该称号！");
                    }
                    break;
            }
        }
        return true;
    }
}
