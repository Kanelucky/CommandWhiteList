package org.kanelucky.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.utils.TextFormat;
import org.kanelucky.CommandWhiteList;

import java.util.ArrayList;
import java.util.List;

public class OnCommand implements Listener {

    private CommandWhiteList plugin;
    private List<String> blockedcommands = new ArrayList<>();
    private String message;
    private boolean opByPass;

    public OnCommand(CommandWhiteList plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        List<String> list = plugin.getConfig().getStringList("commands");
        if (list != null) {
            this.blockedcommands = list;
        }

        this.message = TextFormat.colorize(
                plugin.getConfig().getString(
                        "message",
                        "You do not have permission to use this command!"
                )
        );

        this.opByPass = plugin.getConfig().getBoolean("opByPass", true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {

        String msg = event.getMessage();
        if (msg.length() <= 1) return;

        boolean isOp = event.getPlayer().isOp();

        if (event.getPlayer().hasPermission("commandwhitelist.bypass")) {

            if (!(isOp && !opByPass)) {
                return;
            }
        }

        String cmd = msg.substring(1).split(" ")[0].toLowerCase();

        if (blockedcommands.contains(cmd)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(message);
        }
    }
}