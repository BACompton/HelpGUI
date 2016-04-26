package d3ath5643.HelpGUI.runnables;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import d3ath5643.HelpGUI.Main;
import d3ath5643.HelpGUI.util.HelpMenu;
import d3ath5643.HelpGUI.util.MenuView;
import d3ath5643.HelpGUI.util.MenuView.MenuState;
import d3ath5643.HelpGUI.util.Util;

public class CommandOverview extends BukkitRunnable{
    private Main plugin;
    private HumanEntity human;
    private String command;
    private HelpMenu helpMenu;
    
    public CommandOverview(Main plugin, HumanEntity human, String command, HelpMenu helpMenu)
    {
        this.plugin = plugin;
        this.human = human;
        this.command = command;
        this.helpMenu = helpMenu;
    }
    
    @Override
    public void run() {
        Inventory inv;
        
        helpMenu.addView(new MenuView(MenuState.COMMAND_OVERVIEW, command));
        
        inv = Util.getInventory(plugin, helpMenu);
        Util.setCommandOverview(plugin, inv, command, helpMenu.getCurrView().getPageNumber());
        
        human.openInventory(inv);
        plugin.put(human.getUniqueId(), helpMenu);
        cancel();
    }

}
