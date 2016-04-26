package d3ath5643.HelpGUI.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import d3ath5643.HelpGUI.Main;
import d3ath5643.HelpGUI.util.ConfigPath;
import d3ath5643.HelpGUI.util.HelpMenu;
import d3ath5643.HelpGUI.util.MenuView;
import d3ath5643.HelpGUI.util.Representation;
import d3ath5643.HelpGUI.util.Util;

@SuppressWarnings("deprecation")
public class HelpGui implements CommandExecutor{
    private class ComparableItemStack implements Comparable<ComparableItemStack>
    {
        private ItemStack itemStack;
        
        public ComparableItemStack(ItemStack itemStack)
        {
            this.itemStack = itemStack;
        }
        
        public ItemStack getItemStack()
        {
            return itemStack;
        }

        @Override
        public int compareTo(ComparableItemStack cis) {
            return itemStack.getItemMeta().getDisplayName().compareToIgnoreCase(cis.getItemStack().getItemMeta().getDisplayName());
        }
    }
    
    private Main plugin;
    
    public HelpGui(Main plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player)
        {
            String search = "";
            
            if(args.length > 0)
            {
                for(String s: args)
                    search += s + " ";
                search = search.substring(0, search.length() - 1);
            }
            Player p = (Player) sender;
            HelpMenu helpMenu = getHelpMenu(p, search);
            Inventory inv; 
            
            helpMenu.addView(new MenuView(MenuView.MenuState.RESULTS, search));
            plugin.put(p.getUniqueId(), helpMenu);
            
            inv = Util.getInventory(plugin, helpMenu);

            Util.setResultsPage(plugin, inv, helpMenu.getResults(), helpMenu.getCurrView().getPageNumber());
            p.openInventory(inv);
            
            return true;
        }
        else
        {
            String msg = plugin.getConfig().getString(ConfigPath.HELP_MENU_NON_PLAYER_MSG.getPath());
            
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            sender.sendMessage(msg);
            return true;
        }
    }
    
    //TODO: add & and | to search function
    //TODO: add support user defined cmd and permissions
    private HelpMenu getHelpMenu(Player p, String search)
    {
        HelpMenu helpMenu = new HelpMenu();
        Map<String, List<ItemStack>> pluginCommands = new HashMap<String, List<ItemStack>>(),
                                   pluginPermissions = new HashMap<String, List<ItemStack>>();
        List<ItemStack> allResults = new ArrayList<ItemStack>(),
                             pluginResults = new ArrayList<ItemStack>(),
                             commandResults = new ArrayList<ItemStack>(),
                             permissionResults = new ArrayList<ItemStack>();
        ArrayList<Command> allowedCommands = getAllowedCommands(p);
        
        for(Command cmd: allowedCommands)
        {
            boolean add = false;
            
            for(String alias: cmd.getAliases())
                if(alias.matches(".*" + search + ".*"))
                    add = true;
            if(cmd.getName().matches(".*" + search + ".*") || cmd.getDescription().matches(".*" + search + ".*"))
                add = true;
            
            if(add)
            {
                ItemStack commandOverview = Util.getItemStack(plugin, Representation.COMMAND_OVERVIEW);
                ItemMeta commandMeta = commandOverview.getItemMeta();
                String pluginName = plugin.getConfig().getString(ConfigPath.UNKNOWN_NAME.getPath());
                
                Util.setName(plugin, commandMeta, cmd.getName(), Representation.COMMAND_OVERVIEW);
                Util.setLore(plugin, commandMeta, cmd.getDescription(), Representation.COMMAND_OVERVIEW);
                commandOverview.setItemMeta(commandMeta);
                
                if(cmd instanceof PluginCommand)
                    pluginName = ((PluginCommand)cmd).getPlugin().getName();
                else if(cmd instanceof BukkitCommand)
                    pluginName = plugin.getConfig().getString(ConfigPath.BUKKIT_NAME.getPath());
                else if(cmd instanceof VanillaCommand)
                    pluginName = plugin.getConfig().getString(ConfigPath.MINECRAFT_NAME.getPath());
                else if(cmd.getPermission() != null && !cmd.getPermission().isEmpty())
                    pluginName = getPluginPermisssion(cmd.getPermission());
                    
                if(!pluginCommands.containsKey(pluginName))
                    pluginCommands.put(pluginName, new ArrayList<ItemStack>());
                pluginCommands.get(pluginName).add(commandOverview);
                commandResults.add(commandOverview);
            }    
        }
        
        for(PermissionAttachmentInfo pai: p.getEffectivePermissions())
        {
            boolean add = false;
            
            if(pai.getPermission().matches(".*" + search + ".*"))
                add = true;
            if(p.getServer().getPluginManager().getPermission(pai.getPermission()) != null)
            {
                Permission perm = p.getServer().getPluginManager().getPermission(pai.getPermission());
                
                if(perm.getDescription().matches(".*" + search + ".*"))
                    add = true;
                for(String permName: perm.getChildren().keySet())
                {
                    Permission child = p.getServer().getPluginManager().getPermission(permName);
                    
                    if(child != null 
                            && (child.getName().matches(".*" + search + ".*") || child.getDescription().matches(".*" + search + ".*")))
                    {
                        add = true;
                        break;
                    }
                }
            }
            
            if(add)
            {
                ItemStack permissionOverview = Util.getItemStack(plugin, Representation.PERMISSION_OVERVIEW);
                ItemMeta permissionMeta = permissionOverview.getItemMeta();
                String pluginName = getPluginPermisssion(pai.getPermission());
                
                Util.setName(plugin, permissionMeta, pai.getPermission(), Representation.PERMISSION_OVERVIEW);
                if(p.getServer().getPluginManager().getPermission(pai.getPermission()) != null)
                    Util.setLore(plugin, permissionMeta, p.getServer().getPluginManager().getPermission(pai.getPermission()).getDescription(), Representation.PERMISSION_OVERVIEW);
                else
                    Util.setLore(plugin, permissionMeta, Representation.PERMISSION_OVERVIEW);
                permissionOverview.setItemMeta(permissionMeta);
                
                if(!pluginPermissions.containsKey(pluginName))
                    pluginPermissions.put(pluginName, new ArrayList<ItemStack>());
                pluginPermissions.get(pluginName).add(permissionOverview);
                permissionResults.add(permissionOverview);
            }
        }
        
        for(Entry<String, List<ItemStack>> entry: pluginCommands.entrySet())
            entry.setValue(sort(entry.getValue()));
        for(Entry<String, List<ItemStack>> entry: pluginPermissions.entrySet())
            entry.setValue(sort(entry.getValue()));
        
        helpMenu.setPluginCommands(pluginCommands);
        helpMenu.setPluginPermissions(pluginPermissions);
        
        for(String pluginName: helpMenu.getPlugins())
        {
            ItemStack pluginOverview = Util.getItemStack(plugin, Representation.PLUGIN_OVERVIEW);
            ItemMeta pluginMeta = pluginOverview.getItemMeta();
            String name = plugin.getConfig().getString(ConfigPath.UNKNOWN_NAME.getPath()),
                   description = plugin.getConfig().getString(ConfigPath.UNKNOWN_DESCRIPTION.getPath());
            
            if(p.getServer().getPluginManager().isPluginEnabled(pluginName))
            {
                Plugin plug = plugin.getServer().getPluginManager().getPlugin(pluginName);
                name = plug.getName();
                description = plug.getDescription().getDescription();
            }
            else if(pluginName.equalsIgnoreCase(plugin.getConfig().getString(ConfigPath.BUKKIT_NAME.getPath())))
            {
                name = plugin.getConfig().getString(ConfigPath.BUKKIT_NAME.getPath());
                description = plugin.getConfig().getString(ConfigPath.BUKKIT_DESCRIPTION.getPath());
            }
            else if(pluginName.equalsIgnoreCase(plugin.getConfig().getString(ConfigPath.MINECRAFT_NAME.getPath())))
            {
                name = plugin.getConfig().getString(ConfigPath.MINECRAFT_NAME.getPath());
                description = plugin.getConfig().getString(ConfigPath.MINECRAFT_DESCRIPTION.getPath());
            }
            
            Util.setName(plugin, pluginMeta, name, Representation.PLUGIN_OVERVIEW);
            Util.setLore(plugin, pluginMeta, description, Representation.PLUGIN_OVERVIEW);
            pluginOverview.setItemMeta(pluginMeta);
            
            pluginResults.add(pluginOverview);
        }

        pluginResults = sort(pluginResults);
        commandResults = sort(commandResults);
        permissionResults = sort(permissionResults);
        
        allResults.addAll(pluginResults);
        allResults.addAll(commandResults);
        allResults.addAll(permissionResults);
        helpMenu.setResults(allResults);
        
        return helpMenu;
    }
    
    private ArrayList<Command> getAllowedCommands(Player p)
    {
        ArrayList<Command> commands = new ArrayList<Command>();
        Collection<Command> serverCommands = Util.getCommandMap(p.getServer());
        
        if(serverCommands != null)
            for(Command cmd: serverCommands)
                if(cmd.getPermission() == null || cmd.getPermission().isEmpty()
                        || p.hasPermission(cmd.getPermission()) || cmd.testPermissionSilent(p))
                    if(!commands.contains(cmd))
                        commands.add(cmd);
        
        return commands;
    }
    
    private List<ItemStack> sort(List<ItemStack> list)
    {
        List<ItemStack> sortedList = new ArrayList<ItemStack>();
        List<ComparableItemStack> compareList = new ArrayList<ComparableItemStack>();
        
        for(ItemStack item: list)
            compareList.add(new ComparableItemStack(item));
        
        compareList.sort(null);
        
        for(ComparableItemStack cis: compareList)
            sortedList.add(cis.itemStack);
        
        return sortedList;
    }
    
    private String getPluginPermisssion(String permission)
    {
        String pluginName = plugin.getConfig().getString(ConfigPath.UNKNOWN_NAME.getPath()),
                permissionBeginging = permission.split("\\.")[0];
         boolean found = false;
         
         /*if permissionBeggining matches pluign name or an alias*/
         for(Plugin plug: plugin.getServer().getPluginManager().getPlugins())
             if(plug.getName().equalsIgnoreCase(permissionBeginging) ||
                     (plugin.getConfig().isList(ConfigPath.BASE.getPath() + "." + plug.getName() + ConfigPath.PLUGIN_ALIAS.getPath()) &&
                      plugin.getConfig().getStringList(ConfigPath.BASE.getPath() + "." + plug.getName() + ConfigPath.PLUGIN_ALIAS.getPath()).contains(permissionBeginging)))
             {
                 pluginName = plug.getName();
                 break;
             }
         if(!found && permissionBeginging.equalsIgnoreCase(plugin.getConfig().getString(ConfigPath.BUKKIT_NAME.getPath())) ||
                 (plugin.getConfig().isList(ConfigPath.BUKKIT_ALIAS.getPath()) &&
                  plugin.getConfig().getStringList(ConfigPath.BUKKIT_ALIAS.getPath()).contains(permissionBeginging)))
             pluginName = plugin.getConfig().getString(ConfigPath.BUKKIT_NAME.getPath());
         if(!found && permissionBeginging.equalsIgnoreCase(plugin.getConfig().getString(ConfigPath.MINECRAFT_NAME.getPath()))||
                 (plugin.getConfig().isList(ConfigPath.MINECRAFT_ALIAS.getPath()) &&
                         plugin.getConfig().getStringList(ConfigPath.MINECRAFT_ALIAS.getPath()).contains(permissionBeginging)))
             pluginName = plugin.getConfig().getString(ConfigPath.MINECRAFT_NAME.getPath());
         
         return pluginName;
    }
}
