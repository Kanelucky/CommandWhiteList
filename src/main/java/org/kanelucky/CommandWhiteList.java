package org.kanelucky;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import org.kanelucky.listener.OnCommand;

import java.util.List;

public class CommandWhiteList extends PluginBase {
    private OnCommand onCommandListener;
    @Override
    public void onLoad() {
        getLogger().info(TextFormat.WHITE + "Loading...");
    }
    @Override
    public void onEnable() {
        saveDefaultConfig();
        blockHelpIfNeeded();
        onCommandListener = new OnCommand(this);
        onCommandListener.loadConfig();
        getServer().getPluginManager().registerEvents(onCommandListener, this);
        getLogger().info(TextFormat.DARK_GREEN + "Enabled successfully!");
    }
    private void blockHelpIfNeeded() {
        List<String> cmds = getConfig().getStringList("commands");
        if (cmds == null || !cmds.contains("help")) return;
        Command help = getServer().getCommandMap().getCommand("help");
        if (help != null) {
            getServer().getCommandMap().unregister("help");
        }
        Command fakeHelp = new Command("help", "Blocked help command") {
            @Override
            public boolean execute(CommandSender sender, String label, String[] args) {
                sender.sendMessage(getConfig().getString(
                        "message",
                        "You do not have permission to use this command!"
                ));
                return true;
            }
        };
        getServer().getCommandMap().register("commandwhitelist", fakeHelp);
    }
    @Override
    public void onDisable() {
        getLogger().info(TextFormat.DARK_RED + "Disabled successfully!");
    }
}
