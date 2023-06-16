package glorydark.nukkit;

import cn.nukkit.Player;
import tip.utils.variables.BaseVariable;

public class PrefixVariable extends BaseVariable {

    public PrefixVariable(Player player) {
        super(player);
    }

    @Override
    public void strReplace() {
        this.addStrReplaceString("{prefix}", PrefixAPI.getPlayerPrefixData(player.getName()).getDisplayedPrefix());
    }

}
