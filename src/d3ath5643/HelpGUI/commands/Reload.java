package d3ath5643.HelpGUI.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import d3ath5643.HelpGUI.Main;

public class Reload implements CommandExecutor{
    private Main plugin;
    
    public Reload(Main plugin)
    {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0)
        {
            plugin.createConfigs();
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + plugin.getName() + " reload successful!");
            return true;
        }
        return false;
    }

}
