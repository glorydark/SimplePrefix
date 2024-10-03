package glorydark.nukkit.data;

import cn.nukkit.Player;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixUtils;
import me.onebone.economyapi.EconomyAPI;

import java.util.List;

public class PrefixData {

    private final List<MessageDecorationType> messageDecorationTypes;
    private String identifier;
    private String name;
    private double cost; // -1就是不售卖
    private long duration;


    public PrefixData(String identifier, String name, double cost, long duration, List<MessageDecorationType> messageDecorationTypes) {
        this.identifier = identifier;
        this.name = name;
        this.cost = cost;
        this.duration = duration;
        this.messageDecorationTypes = messageDecorationTypes;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<MessageDecorationType> getMessageDecorationTypes() {
        return messageDecorationTypes;
    }

    public void buy(Player player) {
        if (cost < 0) {
            player.sendMessage("该称号不可购买！");
            return;
        }
        double ownMoney = EconomyAPI.getInstance().myMoney(player);
        if (ownMoney >= this.getCost()) {
            if (PrefixAPI.addOwnedPrefix(player.getName(), this.getIdentifier(), this.getDuration())) {
                EconomyAPI.getInstance().reduceMoney(player, this.getCost());
                long expireMillis = PrefixAPI.getPlayerPrefixData(player.getName()).getOwnedPrefixes().get(this.getIdentifier()).getExpireMillis();
                player.sendMessage("§a成功花费 [" + this.getCost() + "] 金币购买 [" + this.getName() + "] " + (expireMillis == -1 ? "永久" : PrefixUtils.secToTime((int) (expireMillis / 1000))));
            } else {
                player.sendMessage("§c您已经永久拥有这个称号了！");
            }
        } else {
            player.sendMessage("§c您的金币不足，还差" + EconomyAPI.getInstance().getName() + "*" + (cost - ownMoney));
        }
    }
}
