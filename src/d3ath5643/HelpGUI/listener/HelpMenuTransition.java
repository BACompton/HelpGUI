package d3ath5643.HelpGUI.listener;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import d3ath5643.HelpGUI.Main;
import d3ath5643.HelpGUI.runnables.CommandOverview;
import d3ath5643.HelpGUI.runnables.HelpMenuPageChange;
import d3ath5643.HelpGUI.runnables.PermissionOverview;
import d3ath5643.HelpGUI.runnables.PluginOverview;
import d3ath5643.HelpGUI.runnables.PreviousHelpMenu;
import d3ath5643.HelpGUI.util.HelpMenu;
import d3ath5643.HelpGUI.util.MenuView;
import d3ath5643.HelpGUI.util.MenuView.MenuState;
import d3ath5643.HelpGUI.util.Representation;
import d3ath5643.HelpGUI.util.Util;

public class HelpMenuTransition implements Listener{
    private Main plugin;
    
    public HelpMenuTransition(Main plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onHelpMenuClose(InventoryCloseEvent e)
    {
        String helpMenuName = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("helpMenu.name"));
        
        if(e.getInventory().getName().startsWith(helpMenuName))
            plugin.remove(e.getPlayer().getUniqueId());
    }
    
    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true)
    public void onMenuTransition(InventoryClickEvent e)
    {
        String helpMenuName = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("helpMenu.name"));
        
        if(e.getInventory().getName().startsWith(helpMenuName)
                && e.getRawSlot() < e.getInventory().getSize())
        {
            String itemName;
            HelpMenu helpMenu = plugin.get(e.getWhoClicked().getUniqueId());
            MenuView view = helpMenu.getCurrView();
            Material clickedMat;
            byte clickedData;
            
            if(e.getCurrentItem() != null)
                itemName = e.getCurrentItem().getItemMeta().getDisplayName();
            else
                return;
            
            clickedMat = e.getCurrentItem().getType();
            clickedData = e.getCurrentItem().getData().getData();
            
            e.setCancelled(true);
            if(itemName.equals(Util.getItemName(plugin, Representation.PREVIOUS_MENU)))
                new PreviousHelpMenu(plugin, e.getWhoClicked()).runTaskLater(plugin, 1);
            else if(itemName.equals(Util.getItemName(plugin, Representation.PREVIOUS_PAGE)))
                new HelpMenuPageChange(plugin, e.getWhoClicked(), plugin.get(e.getWhoClicked().getUniqueId()), 
                        view.getPageNumber()-1).runTaskLater(plugin, 1);
            else if(itemName.equals(Util.getItemName(plugin, Representation.NEXT_PAGE)))
                new HelpMenuPageChange(plugin, e.getWhoClicked(), plugin.get(e.getWhoClicked().getUniqueId()),
                        view.getPageNumber()+1).runTaskLater(plugin, 1);
            else
            {
                Collection<Command> cm = Util.getCommandMap(plugin.getServer());
                String noColorCode = ChatColor.stripColor(itemName);
                ItemStack permission = Util.getItemStack(plugin, Representation.PERMISSION_OVERVIEW),
                          command = Util.getItemStack(plugin, Representation.COMMAND_OVERVIEW),
                          pluginItem = Util.getItemStack(plugin, Representation.PLUGIN_OVERVIEW),
                          child = Util.getItemStack(plugin, Representation.PERMISSION_CHILD);
                
                for(String pluginName: helpMenu.getPlugins())
                {
                    if(view.getState() == MenuState.PLUGIN_OVERVIEW)
                        break;
                    if(pluginName.equals(noColorCode) 
                            && pluginItem.getType() == clickedMat && pluginItem.getData().getData() == clickedData)
                    {
                        new PluginOverview(plugin, e.getWhoClicked(), plugin.get(e.getWhoClicked().getUniqueId()), pluginName).runTaskLater(plugin, 1);
                        return;
                    }
                }
                for(Command cmd: cm)
                {
                    if(view.getState() == MenuState.COMMAND_OVERVIEW)
                        break;
                    if(cmd.getName().equals(noColorCode) 
                            && command.getType() == clickedMat && command.getData().getData() == clickedData)
                    {
                        new CommandOverview(plugin, e.getWhoClicked(), noColorCode, helpMenu).runTaskLater(plugin, 1);
                        return;
                    }
                }
                if((view.getState() != MenuState.PERMISSION_OVERVIEW
                        && permission.getType() == clickedMat && permission.getData().getData() == clickedData) ||
                        (view.getState() == MenuState.PERMISSION_OVERVIEW
                        && child.getType() == clickedMat && child.getData().getData() == clickedData))
                {
                    if(plugin.getServer().getPluginManager().getPermission(noColorCode) != null)
                        new PermissionOverview(plugin, e.getWhoClicked(), noColorCode, plugin.get(e.getWhoClicked().getUniqueId())).runTaskLater(plugin, 1);
                    else
                        e.getWhoClicked().sendMessage(ChatColor.RED + "Unknow Permission! " + itemName + ChatColor.RED
                                + " is either not registered or is missing its respective plugin."); 
                }
            }
        }
            
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onMenuDrag(InventoryDragEvent e)
    {
        String helpMenuName = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("helpMenu.name"));
        
        if(e.getInventory().getName().startsWith(helpMenuName))
            for(int slot: e.getRawSlots())
                if(slot < e.getInventory().getSize())
                    e.setCancelled(true);
    }
}
