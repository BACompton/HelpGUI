package d3ath5643.HelpGUI.runnables;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import d3ath5643.HelpGUI.Main;
import d3ath5643.HelpGUI.util.HelpMenu;
import d3ath5643.HelpGUI.util.MenuView;
import d3ath5643.HelpGUI.util.MenuView.MenuState;
import d3ath5643.HelpGUI.util.Util;

public class PermissionOverview extends BukkitRunnable{
    private Main plugin;
    private HumanEntity human;
    private String permission;
    private HelpMenu helpMenu;
    
    public PermissionOverview(Main plugin, HumanEntity human, String permission, HelpMenu helpMenu)
    {
        this.plugin = plugin;
        this.human = human;
        this.permission = permission;
        this.helpMenu = helpMenu;
    }
    
    @Override
    public void run() {
        Inventory inv;
        
        helpMenu.addView(new MenuView(MenuState.PERMISSION_OVERVIEW, permission));
        
        inv = Util.getInventory(plugin, helpMenu);
        Util.setPermissionOverview(plugin, inv, permission, 0);
        
        human.openInventory(inv);
        plugin.put(human.getUniqueId(), helpMenu);
        cancel();
    }

}
