package glorydark.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import glorydark.nukkit.event.PrefixModifyNameTagEvent;
import tip.utils.variables.BaseVariable;

public class PrefixVariable extends BaseVariable {

    public PrefixVariable(Player player) {
        super(player);
    }

    @Override
    public void strReplace() {
        String identifier = PrefixAPI.getPlayerPrefixData(player.getName()).getDisplayedPrefix();
        PrefixModifyNameTagEvent event = new PrefixModifyNameTagEvent(player, identifier);
        Server.getInstance().getPluginManager().callEvent(event);
        if(!event.isCancelled()){
            this.addStrReplaceString("{prefix}", event.getDisplayedPrefix());
            event.setCancelled(true);
        }
    }

}
