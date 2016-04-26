package d3ath5643.HelpGUI.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import d3ath5643.HelpGUI.Main;
import d3ath5643.HelpGUI.util.ConfigPath;
import d3ath5643.HelpGUI.util.Representation;
import d3ath5643.HelpGUI.util.Util;
import net.md_5.bungee.api.ChatColor;

public class RepresentationCommand implements CommandExecutor {
    private Main plugin;
    
    public RepresentationCommand(Main plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0)
        {
            if(sender instanceof Player)
            {
                int rows = (int) Math.ceil(Representation.values().length / (double)Util.CHEST_ROW_SIZE);
                String invName = plugin.getConfig().getString(ConfigPath.REPRESENTATION_MENU_NAME.getPath());
                Inventory inv = plugin.getServer().createInventory(null, rows*Util.CHEST_ROW_SIZE, ChatColor.translateAlternateColorCodes('&', invName));
                
                addExampleRepresentation(inv);
                ((Player)sender).openInventory(inv);
            }
            else
            {
                String msg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(ConfigPath.REPRESENTATION_MENU_NON_PLAYER_MSG.getPath()));
                sender.sendMessage(msg);
            }
            
            return true;
        }
        return false;
    }

    private void addExampleRepresentation(Inventory inv)
    {
        ItemStack filler = Util.getItemStack(plugin, Representation.FILLER);
        ItemMeta fillerMeta = filler.getItemMeta();
        
        for(Representation rep: Representation.values())
        {
            ItemStack item = Util.getItemStack(plugin, rep);
            ItemMeta itemMeta = item.getItemMeta();
            
            Util.setName(plugin, itemMeta, rep.getType(), rep);
            
            Util.setLore(plugin, itemMeta, rep.getUse(), rep);
            if(rep.isOverview())
            {
                List<String> lore = itemMeta.getLore();
                
                for(int i = 0; i < 2; i++)
                    lore.remove(lore.size()-1);
                
                itemMeta.setLore(lore);
            }
            
            item.setItemMeta(itemMeta);
            
            if(inv.firstEmpty() > -1)
                inv.setItem(inv.firstEmpty(), item);
        }
        
        Util.setName(plugin, fillerMeta, Representation.FILLER);
        Util.setLore(plugin, fillerMeta, Representation.FILLER);
        filler.setItemMeta(fillerMeta);
        
        while(inv.firstEmpty() > -1)
            inv.setItem(inv.firstEmpty(), filler);
            
    }
}
