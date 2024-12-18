package glorydark.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PrefixUtils {

    public static void main(String[] args) {
        System.out.println(secToTime(2590650216L / 1000));
    }

    public static String secToTime(long seconds) {
        long day = seconds / 86400;
        long hour = seconds / 3600;
        long minute = (seconds - hour * 3600) / 60;
        long second = seconds - hour * 3600 - minute * 60;
        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append("天");
            hour -= 24 * day;
        }

        if (hour > 0) {
            sb.append(hour).append("小时");
            minute -= hour * 60;
        }

        if (minute > 0) {
            sb.append(minute).append("分钟");
            second -= minute * 60;
        }

        if (second > 0) {
            sb.append(second).append("秒");
        }

        return sb.toString();
    }

    public static String getDate(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return format.format(date);
    }

    public static void broadcastMessage(String message) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            player.sendMessage(message);
        }
        PrefixMain.getPlugin().getLogger().info(message);
    }

}
