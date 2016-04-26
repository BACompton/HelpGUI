package d3ath5643.HelpGUI.runnables;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import d3ath5643.HelpGUI.Main;
import d3ath5643.HelpGUI.util.HelpMenu;
import d3ath5643.HelpGUI.util.MenuView;
import d3ath5643.HelpGUI.util.Util;

public class PreviousHelpMenu extends BukkitRunnable{
    private HumanEntity human;
    private Main plugin;
    
    public PreviousHelpMenu(Main plugin, HumanEntity human)
    {
        this.plugin = plugin;
        this.human = human;
    }
    
    @Override
    public void run() {
        if(plugin.get(human.getUniqueId()) != null)
        {
            HelpMenu helpMenu = plugin.get(human.getUniqueId());
            
            if(helpMenu.hasPreviousMenu())
            {
                MenuView view = helpMenu.getLastView();
                Inventory inv;
                
                helpMenu.removeCurrView();
                inv = Util.getInventory(plugin, helpMenu);
                
                switch(view.getState())
                {
                    case RESULTS:
                        Util.setResultsPage(plugin, inv, helpMenu.getResults(), view.getPageNumber());
                        break;
                    case PLUGIN_OVERVIEW:
                        Util.setPluginOverview(plugin, inv, view.getDetails(), helpMenu.getPluginCommands(view.getDetails()), 
                                               helpMenu.getPluinPermissions(view.getDetails()), view.getPageNumber());
                        break;
                    case COMMAND_OVERVIEW:
                        Util.setCommandOverview(plugin, inv, view.getDetails(), view.getPageNumber());
                    case PERMISSION_OVERVIEW:
                        Util.setPermissionOverview(plugin, inv, view.getDetails(), view.getPageNumber());
                        break;
                }
                human.openInventory(inv);
                plugin.put(human.getUniqueId(), helpMenu);
            }
            else
            {
                human.closeInventory();
                plugin.remove(human.getUniqueId());
            }
        }
        cancel();
    }

}
