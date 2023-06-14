package glorydark.nukkit.data;

import cn.nukkit.Player;
import glorydark.nukkit.PrefixAPI;
import glorydark.nukkit.PrefixUtils;
import me.onebone.economyapi.EconomyAPI;

import java.util.List;

public class PrefixData {

    private String identifier;

    private String name;

    private double cost;

    private long duration;

    private final List<MessageDecorationType> messageDecorationTypes;


    public PrefixData(String identifier, String name, double cost, long duration, List<MessageDecorationType> messageDecorationTypes){
        this.identifier = identifier;
        this.name = name;
        this.cost = cost;
        this.duration = duration;
        this.messageDecorationTypes = messageDecorationTypes;
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

    public List<MessageDecorationType> getMessageDecorationTypes() {
        return messageDecorationTypes;
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
        double ownMoney = EconomyAPI.getInstance().myMoney(player);
        if(ownMoney >= this.getCost()){
            if(PrefixAPI.addOwnedPrefixes(player.getName(), this.getName(), this.getDuration())) {
                EconomyAPI.getInstance().reduceMoney(player, this.getCost());
                long expireMillis = PrefixAPI.getPlayerPrefixData(player.getName()).getOwnedPrefixes().get(this.getIdentifier()).getExpireMillis();
                player.sendMessage("成功花费 ["+this.getCost()+"] "+EconomyAPI.getInstance().getName()+"购买 ["+this.getName()+"] " + (expireMillis == -1? "永久":PrefixUtils.secToTime((int) (expireMillis/1000))));
            }else{
                player.sendMessage("称号购买出现问题，购买称号标识符："+this.getIdentifier()+"！");
            }
        }else{
            player.sendMessage("您的金币不足，还差"+EconomyAPI.getInstance().getName()+"*"+(cost - ownMoney));
        }
    }
}