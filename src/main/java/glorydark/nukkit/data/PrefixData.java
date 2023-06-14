package glorydark.nukkit.data;

import cn.nukkit.Player;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixUtils;
import me.onebone.economyapi.EconomyAPI;

public class PrefixData {

    private String identifier;

    private String name;

    private double cost;

    private long duration;

    public PrefixData(String identifier, String name, double cost, long duration){
        this.identifier = identifier;
        this.name = name;
        this.cost = cost;
        this.duration = duration;
    }

    public double getCost() {
        return cost;
    }

    public long getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void buy(Player player){
        if(EconomyAPI.getInstance().myMoney(player) >= this.getCost()){
            if(PrefixAPI.addOwnedPrefixes(player.getName(), this.getName(), this.getDuration())) {
                EconomyAPI.getInstance().reduceMoney(player, this.getCost());
                long expireMillis = PrefixAPI.getPlayerPrefixData(player.getName()).getOwnedPrefixes().get(this.getIdentifier()).getExpireMillis();
                player.sendMessage("成功花费 ["+this.getCost()+"] "+EconomyAPI.getInstance().getName()+"购买 ["+this.getName()+"] " + (expireMillis == -1? "永久":PrefixUtils.secToTime((int) (expireMillis/1000))));
            }
        }
    }
}
