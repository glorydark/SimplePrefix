package glorydark.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import gameapi.listener.base.GameListenerRegistry;
import glorydark.nukkit.data.*;
import glorydark.nukkit.forms.PrefixEventListener;
import glorydark.nukkit.forms.PrefixEventListenerForGameAPI;
import glorydark.nukkit.provider.PrefixProvider;
import glorydark.nukkit.provider.PrefixYamlProvider;
import tip.utils.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PrefixMain extends PluginBase implements Listener {

    public static String defaultPrefix = "萌新驾到";
    public static String defaultRarity;

    public static String path;
    public static Map<String, PlayerData> playerPrefixDataHashMap = new LinkedHashMap<>();
    public static Map<String, PrefixData> prefixDataHashMap = new LinkedHashMap<>();
    public static Map<String, PrefixData> purchasablePrefixDataHashMap = new LinkedHashMap<>();
    public static Map<String, String> prefixRarityMap = new LinkedHashMap<>();
    protected static PrefixMain plugin;
    protected PrefixProvider provider;
    public static boolean gameapiEnabled;

    public static PrefixMain getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        provider = new PrefixYamlProvider();
        path = this.getDataFolder().getPath();
        gameapiEnabled = this.getServer().getPluginManager().getPlugin("GameAPI") != null;
        this.saveDefaultConfig();
        this.getLogger().info("SimplePrefix 正在加载...");
        this.getProvider().loadPrefix();
        this.getProvider().reloadPlayerData();
        this.getServer().getPluginManager().registerEvents(new PrefixEventListener(), this);
        if (gameapiEnabled) {
            this.getLogger().info(TextFormat.GREEN + "成功开启GameAPI适配！");
            GameListenerRegistry.registerEvents(GameListenerRegistry.KEY_GLOBAL_LISTENER, new PrefixEventListenerForGameAPI(), this);
        }
        this.getServer().getCommandMap().register("", new PrefixCommand("prefix"));
        Api.registerVariables("SimplePrefix", PrefixVariable.class);
        this.getServer().getScheduler().scheduleRepeatingTask(this, () -> {
            for (Player player : Server.getInstance().getOnlinePlayers().values()) {
                for (Map.Entry<String, PlayerPrefixData> entry : new ArrayList<>(PrefixAPI.getPlayerPrefixData(player.getName()).getOwnedPrefixes().entrySet())) {
                    PlayerPrefixData playerPrefixData = entry.getValue();
                    if (playerPrefixData.getExpireMillis() != -1 && System.currentTimeMillis() >= playerPrefixData.getExpireMillis()) {
                        PrefixAPI.removePrefix(player.getName(), entry.getKey());
                        player.sendMessage(TextFormat.RED + "您的称号 " + TextFormat.YELLOW + entry.getKey() + TextFormat.RED + " 已过期！");
                        PrefixMain.getPlugin().getLogger().info("玩家 " + player.getName() + " 的称号 " + entry.getKey() + " 已过期！");
                    }
                }
            }
        }, 20);
        this.getLogger().info("SimplePrefix 加载完成");
    }

    public PrefixProvider getProvider() {
        return provider;
    }
}