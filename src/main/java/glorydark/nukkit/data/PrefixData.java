package glorydark.nukkit.data;

import cn.nukkit.Player;
import glorydark.nukkit.PrefixAPI;
import me.onebone.economyapi.EconomyAPI;

import java.util.Objects;

public class PrefixData {

    private String name;

    private double cost;

    private long duration;

    public PrefixData(String name, double cost, long duration){
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

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void buy(Player player){
        if(EconomyAPI.getInstance().myMoney(player) >= cost){
            if(PrefixAPI.addOwnedPrefixes(player.getName(), this.getName(), this.getDuration())) {
                EconomyAPI.getInstance().reduceMoney(player, cost);
            }
        }
    }
}
