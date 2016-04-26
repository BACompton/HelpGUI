package d3ath5643.HelpGUI.runnables;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import d3ath5643.HelpGUI.Main;
import d3ath5643.HelpGUI.util.HelpMenu;
import d3ath5643.HelpGUI.util.MenuView;
import d3ath5643.HelpGUI.util.MenuView.MenuState;
import d3ath5643.HelpGUI.util.Util;

public class HelpMenuPageChange extends BukkitRunnable{
    private Main plugin;
    private HumanEntity human;
    private HelpMenu helpMenu;
    private int pageNumber;
    
    public HelpMenuPageChange(Main plugin, HumanEntity human, HelpMenu helpMenu, int pageNumber)
    {
        this.plugin = plugin;
        this.human = human;
        this.helpMenu = helpMenu;
        this.pageNumber = pageNumber;
    }
    
    @Override
    public void run() {
        MenuView curr = helpMenu.getCurrView();
        Inventory inv = null;
        
        if(helpMenu.hasPreviousMenu()
                && helpMenu.getLastView().getState() == curr.getState()
                && curr.getPageNumber() > pageNumber)
        {
            helpMenu.removeCurrView();
        }
        else
            helpMenu.addView(new MenuView(curr.getState(),
                                     curr.getDetails(),
                                     pageNumber));
        
        inv = Util.getInventory(plugin, helpMenu);
        
        if(curr.getState() == MenuState.RESULTS)
            Util.setResultsPage(plugin, inv, helpMenu.getResults(), pageNumber);
        else if(curr.getState() == MenuState.PLUGIN_OVERVIEW)
            Util.setPluginOverview(plugin, inv, curr.getDetails(), 
                                   helpMenu.getPluginCommands(curr.getDetails()), 
                                   helpMenu.getPluinPermissions(curr.getDetails()), 
                                   pageNumber);
        else if(curr.getState() == MenuState.COMMAND_OVERVIEW)
            Util.setCommandOverview(plugin, inv, curr.getDetails(), pageNumber);
        else if(curr.getState() == MenuState.PERMISSION_OVERVIEW)
            Util.setPermissionOverview(plugin, inv, curr.getDetails(), pageNumber);
        
        human.openInventory(inv);
        plugin.put(human.getUniqueId(), helpMenu);
        cancel();
    }

}
