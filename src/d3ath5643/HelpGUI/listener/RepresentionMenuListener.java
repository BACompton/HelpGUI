package d3ath5643.HelpGUI.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import d3ath5643.HelpGUI.Main;
import d3ath5643.HelpGUI.util.ConfigPath;
import d3ath5643.HelpGUI.util.Representation;
import d3ath5643.HelpGUI.util.Util;

public class RepresentionMenuListener implements Listener{
    private Main plugin;
    
    public RepresentionMenuListener(Main plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled = true)
    public void itemClickEvent(InventoryClickEvent e)
    {   
        String repInvName = plugin.getConfig().getString(ConfigPath.REPRESENTATION_MENU_NAME.getPath());
        
        if(e.getInventory().getName().startsWith(ChatColor.translateAlternateColorCodes('&', repInvName))
                && e.getRawSlot() < (int) Math.ceil(Representation.values().length / (double)Util.CHEST_ROW_SIZE)*Util.CHEST_ROW_SIZE)
            e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void itemDragEvent(InventoryDragEvent e)
    {
        String repInvName = plugin.getConfig().getString(ConfigPath.REPRESENTATION_MENU_NAME.getPath());
        
        if(e.getInventory().getName().startsWith(ChatColor.translateAlternateColorCodes('&', repInvName)))
        {
            for(Integer slot: e.getRawSlots())
                if(slot < (int) Math.ceil(Representation.values().length / (double)Util.CHEST_ROW_SIZE)*Util.CHEST_ROW_SIZE)
                {
                    e.setCancelled(true);
                    return;
                }
        }
    }
}
