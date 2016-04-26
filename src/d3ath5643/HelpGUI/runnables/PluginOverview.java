package d3ath5643.HelpGUI.runnables;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import d3ath5643.HelpGUI.Main;
import d3ath5643.HelpGUI.util.HelpMenu;
import d3ath5643.HelpGUI.util.MenuView;
import d3ath5643.HelpGUI.util.MenuView.MenuState;
import d3ath5643.HelpGUI.util.Util;

public class PluginOverview extends BukkitRunnable{
    private Main plugin;
    private HumanEntity human;
    private HelpMenu helpMenu;
    private String pluginName;
    
    public PluginOverview(Main plugin, HumanEntity human, HelpMenu helpMenu, String pluginName)
    {
        this.plugin = plugin;
        this.human = human;
        this.helpMenu = helpMenu;
        this.pluginName = pluginName;
    }
    
    @Override
    public void run() {
        Inventory inv;
        
        helpMenu.addView(new MenuView(MenuState.PLUGIN_OVERVIEW, pluginName));
        
        inv = Util.getInventory(plugin, helpMenu);
        Util.setPluginOverview(plugin, inv, pluginName, helpMenu.getPluginCommands(pluginName),
                               helpMenu.getPluinPermissions(pluginName), 0);
        
        human.openInventory(inv);
        plugin.put(human.getUniqueId(), helpMenu);
        cancel();
    }

}
