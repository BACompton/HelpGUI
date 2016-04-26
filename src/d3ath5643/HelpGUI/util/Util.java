package d3ath5643.HelpGUI.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;

import d3ath5643.HelpGUI.Main;
import d3ath5643.HelpGUI.util.MenuView.MenuState;

public class Util {
    public static final int CHEST_ROW_SIZE = 9;
    public static final int MIN_ROWS = 5;
    public static final int MAX_INV_LENGTH = 32;
    
    @SuppressWarnings("deprecation")
    public static ItemStack getItemStack(Main plugin, Representation rep) {
        Material mat = Material.getMaterial(plugin.getConfig().getString(rep.getPath() + ConfigPath.REPRESENTATION_MATERIAL.getPath()));
        int data = plugin.getConfig().getInt(rep.getPath() + ConfigPath.REPRESENTATION_DATA.getPath());
        
        if(mat == null || mat == Material.AIR || data > Byte.MAX_VALUE || data < Byte.MIN_VALUE)
        {
            return rep.getDefaultItemStack();
        }
        return new ItemStack(mat, 1, (short)1, (byte)data);
    }

    public static void setLore(Main plugin, ItemMeta meta, Representation rep) {
        String lore = rep.getDefaultLore();
        
        if(plugin.getConfig().isString(rep.getPath() + ConfigPath.REPRESENTATION_LORE.getPath()))
            lore = plugin.getConfig().getString(rep.getPath() + ConfigPath.REPRESENTATION_LORE.getPath());
        
        setLore(plugin, meta, lore, rep);
    }

    public static void setLore(Main plugin, ItemMeta meta, String lore, Representation rep) {
        int lineLength = rep.getDefaultLineLength();
        List<String> loreLines = new ArrayList<String>();
        String tempLine = "",
               lineColor = rep.getDefaultLineColor() + "";
        
        if(plugin.getConfig().isInt(rep.getPath() + ConfigPath.REPRESENTATION_LORE_LINE_LENGTH.getPath()))
            lineLength = plugin.getConfig().getInt(rep.getPath() + ConfigPath.REPRESENTATION_LORE_LINE_LENGTH.getPath());
        
        if(plugin.getConfig().isString(rep.getPath() + ConfigPath.REPRESENTATION_LORE_COLOR_CODE.getPath()))
            lineColor = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(rep.getPath() + ConfigPath.REPRESENTATION_LORE_COLOR_CODE.getPath()));
        
        if(lore == null || lore.isEmpty())
            lore = rep.getDefaultLore();
        
        for(String word: lore.split(" "))
        {
            word = ChatColor.translateAlternateColorCodes('&', word);
            
            if(ChatColor.stripColor(tempLine + word + " ").length() > lineLength)
            {
                lore = lore.substring(tempLine.length()-1);
                loreLines.add(lineColor + tempLine);
                tempLine = "";
            }
            tempLine += word + " ";
        }
        if(!tempLine.isEmpty())
            loreLines.add(lineColor + tempLine);
        
        if(rep.isOverview())
        {
            String overview = "Click to go to detailed view.";
            
            if(plugin.getConfig().isString(rep.getPath() + ConfigPath.REPRESENTATION_OVERVIEW.getPath()))
                overview = plugin.getConfig().getString(rep.getPath() + ConfigPath.REPRESENTATION_OVERVIEW.getPath());
            overview = overview.replaceAll("%name%", meta.getDisplayName() + lineColor);
            
            loreLines.add("");
            loreLines.add(lineColor + overview);
        }
        
        meta.setLore(loreLines);    
    }

    public static void setName(Main plugin, ItemMeta meta, Representation rep)
    {
        String name = getItemName(plugin, rep);
        
        meta.setDisplayName(name);
    }
    
    public static void setName(Main plugin, ItemMeta meta, String name, Representation rep)
    {
        String display = getItemName(plugin, rep, name);
        
        meta.setDisplayName(display);
    }
    
    public static String getItemName(Main plugin, Representation rep)
    {
        String name = rep.getDefaultName();
        
        if(plugin.getConfig().isString(rep.getPath() + ConfigPath.REPRESENTATION_NAME.getPath()))
            name = plugin.getConfig().getString(rep.getPath() + ConfigPath.REPRESENTATION_NAME.getPath());
        
        return getItemName(plugin, rep, name);
    }
    
    public static String getItemName(Main plugin, Representation rep, String name)
    {
        String nameColor = rep.getDefaultNameColor() + "";
        
        if(plugin.getConfig().isString(rep.getPath() + ConfigPath.REPRESENTATION_NAME_COLOR_CODE.getPath()))
            nameColor = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(rep.getPath() + ConfigPath.REPRESENTATION_NAME_COLOR_CODE.getPath()));
        
        return nameColor + ChatColor.translateAlternateColorCodes('&', name);
    }

    public static Collection<Command> getCommandMap(Server server) {
        for(Field f: server.getClass().getDeclaredFields())
            if(SimpleCommandMap.class.isAssignableFrom(f.getType()))
            {
                f.setAccessible(true);
                try
                {
                    SimpleCommandMap cm = (SimpleCommandMap)f.get(server);
                    if(cm != null)
                        return cm.getCommands();
                }
                catch(IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch(IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        return null;
    }

    public static void setResultsPage(Main plugin, Inventory inv, List<ItemStack> results, int pageNumber) {
        boolean includeNext = false, includePrev = false;
        int rows = plugin.getConfig().getInt(ConfigPath.HELP_MENU_ROWS.getPath()),
            fillableRows = rows - 1;
        
        if((fillableRows)*CHEST_ROW_SIZE *(1 + pageNumber) < results.size())
            includeNext = true;
        if(pageNumber > 0)
            includePrev = true;
        
        inv.clear();
        addBottom(plugin, inv, includePrev, includeNext);
        while(inv.firstEmpty() > -1 && inv.firstEmpty() < results.size()-pageNumber*CHEST_ROW_SIZE*(fillableRows))
            inv.setItem(inv.firstEmpty(), results.get(inv.firstEmpty()+pageNumber*CHEST_ROW_SIZE*(fillableRows)));
        addFiller(plugin,inv);
    }
    
    public static void setPluginOverview(Main plugin, Inventory inv, String pluginName, List<ItemStack> commands, 
                                         List<ItemStack> permissions, int pageNumber)
    {
        if(commands == null)
            commands = new ArrayList<ItemStack>();
        if(permissions == null)
            permissions = new ArrayList<ItemStack>();
        
        ItemStack name = getItemStack(plugin, Representation.PLUGIN_NAME),
                  description = getItemStack(plugin, Representation.PLUGIN_DESCRIPTION);
        ItemMeta nameMeta = name.getItemMeta(),
                 descriptionMeta = description.getItemMeta();
        int rows = plugin.getConfig().getInt(ConfigPath.HELP_MENU_ROWS.getPath()),
            fillableRows = rows - 3,
            numberShown = pageNumber*CHEST_ROW_SIZE*(fillableRows),
            commandStart = commands.size() > numberShown ? numberShown : commands.size(),
            permissionStart = commandStart == commands.size() ? numberShown-commands.size() : 0;
        boolean next = false, previous = false;
        
        Util.setName(plugin, nameMeta, pluginName, Representation.PLUGIN_NAME);
        Util.setName(plugin, descriptionMeta, Representation.PLUGIN_DESCRIPTION);
        
        Util.setLore(plugin, nameMeta, Representation.PLUGIN_NAME);
        Util.setLore(plugin, descriptionMeta, getPluginDescription(plugin, pluginName), 
                     Representation.PLUGIN_DESCRIPTION);
        
        name.setItemMeta(nameMeta);
        description.setItemMeta(descriptionMeta);
        
        
        if(commands.size()+permissions.size() > CHEST_ROW_SIZE*(fillableRows)*(1+pageNumber))
            next = true;
        if(pageNumber > 0)
            previous = true;
        
        inv.clear();
        addBottom(plugin, inv,previous, next);
        
        for(int i = 0; i < CHEST_ROW_SIZE; i++)
            inv.setItem(inv.firstEmpty(), name);
        for(int i = 0; i < CHEST_ROW_SIZE; i++)
            inv.setItem(inv.firstEmpty(), description);
        while(inv.firstEmpty() > -1 && commandStart < commands.size())
            inv.setItem(inv.firstEmpty(), commands.get(commandStart++));
        while(inv.firstEmpty() > -1 && permissionStart < permissions.size())
            inv.setItem(inv.firstEmpty(), permissions.get(permissionStart++));
        
        addFiller(plugin, inv);
    }
    
    public static void setCommandOverview(Main plugin, Inventory inv, String commandName, int pageNumber)
    {
        ItemStack name = getItemStack(plugin, Representation.COMMAND_NAME),
                  description = getItemStack(plugin, Representation.COMMAND_DESCRIPTION),
                  permission = getItemStack(plugin, Representation.PERMISSION_OVERVIEW);
        ItemMeta nameMeta = name.getItemMeta(),
                 descriptionMeta = description.getItemMeta(),
                 permissionMeta = permission.getItemMeta();
        String permissionName = getCommandPermission(plugin, commandName);
        boolean next = false, prev = false;
        int rows = plugin.getConfig().getInt(ConfigPath.HELP_MENU_ROWS.getPath()),
            fillableRows = rows-4,
            filledRows = 3;
        List<String> aliases = new ArrayList<String>();
        
        Util.setName(plugin, nameMeta, commandName, Representation.COMMAND_NAME);
        Util.setName(plugin, descriptionMeta, Representation.COMMAND_DESCRIPTION);
        Util.setName(plugin, permissionMeta, permissionName, Representation.PERMISSION_OVERVIEW);
        
        Util.setLore(plugin, nameMeta, getCommandUsage(plugin, commandName), Representation.COMMAND_NAME);
        Util.setLore(plugin, descriptionMeta, getCommandDescription(plugin, commandName), Representation.COMMAND_DESCRIPTION);
        if(!permissionName.equals(plugin.getConfig().getString(Representation.PERMISSION_OVERVIEW.getPath() + ConfigPath.REPRESENTATION_NAME.getPath())))
            Util.setLore(plugin, permissionMeta, getPermissionDescription(plugin, permissionName), Representation.PERMISSION_OVERVIEW);
        else
            Util.setLore(plugin, permissionMeta, Representation.PERMISSION_NAME);
        
        name.setItemMeta(nameMeta);
        description.setItemMeta(descriptionMeta);
        permission.setItemMeta(permissionMeta);
        
        if(pageNumber > 0)
            prev = true;
        for(Command cmd: getCommandMap(plugin.getServer()))
            if(cmd.getName().equals(commandName))
            {
                aliases = cmd.getAliases();
                
                if(aliases.size() > CHEST_ROW_SIZE*(fillableRows)*(pageNumber+1))
                    next = true;
                break;
            }
        
        inv.clear();
        addBottom(plugin,inv, prev, next);
        
        for(int i = 0; i < CHEST_ROW_SIZE; i++)
            inv.setItem(inv.firstEmpty(), name);
        for(int i = 0; i < CHEST_ROW_SIZE; i++)
            inv.setItem(inv.firstEmpty(), description);
        for(int i = 0; i < CHEST_ROW_SIZE; i++)
            inv.setItem(inv.firstEmpty(), permission);
        while(inv.firstEmpty() > -1 && inv.firstEmpty()-filledRows*CHEST_ROW_SIZE < aliases.size()-pageNumber*(fillableRows)*CHEST_ROW_SIZE)
        {
            ItemStack alias = getItemStack(plugin, Representation.COMMAND_ALIAS);
            ItemMeta aliasMeta = alias.getItemMeta();
            
            setName(plugin, aliasMeta, aliases.get(pageNumber*CHEST_ROW_SIZE*(fillableRows)+inv.firstEmpty()-filledRows*CHEST_ROW_SIZE), Representation.COMMAND_ALIAS);
            setLore(plugin, aliasMeta, Representation.COMMAND_ALIAS);
            alias.setItemMeta(aliasMeta);
            
            inv.setItem(inv.firstEmpty(), alias);
        }
        addFiller(plugin, inv);
    }
    
    public static void setPermissionOverview(Main plugin, Inventory inv, String permissionName, int pageNumber)
    {
        ItemStack name = getItemStack(plugin, Representation.PERMISSION_NAME),
                  description = getItemStack(plugin, Representation.PERMISSION_DESCRIPTION);
        ItemMeta nameMeta = name.getItemMeta(),
                 descriptionMeta = description.getItemMeta();
        boolean next = false, prev = false;
        Permission permission = plugin.getServer().getPluginManager().getPermission(permissionName);
        int rows = plugin.getConfig().getInt(ConfigPath.HELP_MENU_ROWS.getPath()),
            fillableRows = rows-3, filledRows = 2;
        
        setName(plugin, nameMeta, permissionName, Representation.PERMISSION_NAME);
        setName(plugin, descriptionMeta, Representation.PERMISSION_DESCRIPTION);
        
        setLore(plugin, nameMeta, Representation.PERMISSION_NAME);
        setLore(plugin, descriptionMeta, getPermissionDescription(plugin, permissionName), Representation.PERMISSION_DESCRIPTION);
        
        name.setItemMeta(nameMeta);
        description.setItemMeta(descriptionMeta);
        
        if(pageNumber > 0)
            prev = true;
        if(permission != null && permission.getChildren().size() > (fillableRows)*CHEST_ROW_SIZE*(pageNumber+1))
            next = true;
        
        inv.clear();
        addBottom(plugin, inv, prev, next);
        
        for(int i = 0; i < CHEST_ROW_SIZE; i++)
            inv.setItem(inv.firstEmpty(), name);
        for(int i = 0; i < CHEST_ROW_SIZE; i++)
            inv.setItem(inv.firstEmpty(), description);
        while(permission != null && inv.firstEmpty() > -1 && 
                inv.firstEmpty()-filledRows*CHEST_ROW_SIZE < permission.getChildren().size()-pageNumber*CHEST_ROW_SIZE*(fillableRows))
        {
            ItemStack child = getItemStack(plugin, Representation.PERMISSION_CHILD);
            ItemMeta childMeta = child.getItemMeta();
            int childNumber = inv.firstEmpty()-filledRows*CHEST_ROW_SIZE + pageNumber*CHEST_ROW_SIZE*(fillableRows);
            String childName = "";
            
            for(String key: permission.getChildren().keySet())
            {
                if(childNumber < 1)
                {
                    childName = key;
                    break;
                }
                childNumber--;
            }
            
            setName(plugin, childMeta, childName, Representation.PERMISSION_CHILD);
            setLore(plugin, childMeta, getPermissionDescription(plugin, childName), Representation.PERMISSION_CHILD);
            child.setItemMeta(childMeta);
            
            inv.setItem(inv.firstEmpty(), child);
        }
        
        addFiller(plugin, inv);
    }
    
    public static Inventory getInventory(Main plugin, HelpMenu helpMenu)
    {
        int rows = plugin.getConfig().getInt(ConfigPath.HELP_MENU_ROWS.getPath()),
            currPage = helpMenu.getCurrView().getPageNumber()+1,
            pages = 1;
        MenuState currView = helpMenu.getCurrView().getState();
        String invName = plugin.getConfig().getString(ConfigPath.HELP_MENU_NAME.getPath()),
               details = helpMenu.getCurrView().getDetails(),
               pageDetails= "";
        
        if(currView == MenuState.RESULTS)
            pages = (int) Math.ceil(helpMenu.getResults().size() / (double)(Util.CHEST_ROW_SIZE*(rows-1)));
        else if(currView == MenuState.COMMAND_OVERVIEW)
        {
            int aliases = 1;
            
            for(Command cmd: getCommandMap(plugin.getServer()))
                if(cmd.getName().equals(details) && !cmd.getAliases().isEmpty())
                {
                    aliases = cmd.getAliases().size();
                    break;
                }
            
            pages = (int) Math.ceil(aliases / (double)(Util.CHEST_ROW_SIZE*(rows-4)));
        }
        else if(currView == MenuState.PLUGIN_OVERVIEW)
        {
            int total = 0;
            
            if(helpMenu.getPluginCommands(details) != null 
                    && !helpMenu.getPluginCommands(details).isEmpty())
                total += helpMenu.getPluginCommands(details).size();
            if(helpMenu.getPluinPermissions(details) != null 
                    && !helpMenu.getPluinPermissions(details).isEmpty())
                total += helpMenu.getPluinPermissions(details).size();
            if(total == 0)
                total = 1;
                
            pages = (int) Math.ceil(total / (double)(Util.CHEST_ROW_SIZE*(rows-3)));
        }
        else if(currView == MenuState.PERMISSION_OVERVIEW)
        {
            int children = 1;
            
            if(plugin.getServer().getPluginManager().getPermission(details) != null 
                    && !plugin.getServer().getPluginManager().getPermission(details).getChildren().isEmpty())
                children = plugin.getServer().getPluginManager().getPermission(details).getChildren().size();
            
            pages = (int) Math.ceil(children / (double)(Util.CHEST_ROW_SIZE*(rows-3)));
        }
        
        if(currPage > pages)
            currPage = pages;
        
        pageDetails = " " + currPage + "/" + pages;
        
        if(!details.isEmpty())
            invName += " for " + details;
        if(invName.length() + pageDetails.length() > MAX_INV_LENGTH)
            invName = invName.substring(0, MAX_INV_LENGTH-pageDetails.length()-"...".length()) + "...";
        invName += pageDetails;
        
        return plugin.getServer().createInventory(null, rows*CHEST_ROW_SIZE, ChatColor.translateAlternateColorCodes('&', invName));
    }
    
    private static String getPermissionDescription(Main plugin, String name)
    {
        if(plugin.getServer().getPluginManager().getPermission(name) != null
                && !plugin.getServer().getPluginManager().getPermission(name).getDescription().isEmpty())
            return plugin.getServer().getPluginManager().getPermission(name).getDescription();
        return Representation.PERMISSION_DESCRIPTION.getDefaultLore();
    }
    
    private static String getCommandDescription(Main plugin, String name)
    {
        Collection<Command> commands = getCommandMap(plugin.getServer());
        
        if(commands != null)
            for(Command cmd: commands)
                if(cmd.getName().equals(name))
                    return cmd.getDescription();
        return Representation.COMMAND_DESCRIPTION.getDefaultLore();
    }
    
    private static String getCommandUsage(Main plugin, String name)
    {
        Collection<Command> commands = getCommandMap(plugin.getServer());
        
        if(commands != null)
            for(Command cmd: commands)
                if(cmd.getName().equals(name))
                    return cmd.getUsage();
        return Representation.COMMAND_NAME.getDefaultLore();
    }
    
    private static String getCommandPermission(Main plugin, String name)
    {
        Collection<Command> commands = getCommandMap(plugin.getServer());
        
        if(commands != null)
            for(Command cmd: commands)
                if(cmd.getName().equals(name))
                {
                    if(cmd.getPermission() != null && !cmd.getPermission().isEmpty())
                        return cmd.getPermission();
                    break;
                }
        return plugin.getConfig().getString(Representation.PERMISSION_OVERVIEW.getPath() + ConfigPath.REPRESENTATION_NAME.getPath());
    }
    
    private static String getPluginDescription(Main plugin, String name)
    {
        if(plugin.getServer().getPluginManager().getPlugin(name) != null)
            return plugin.getServer().getPluginManager().getPlugin(name).getDescription().getDescription();
        if(name.equals(plugin.getConfig().getString(ConfigPath.BUKKIT_DESCRIPTION.getPath())))
            return plugin.getConfig().getString(ConfigPath.BUKKIT_DESCRIPTION.getPath());
        if(name.equals(plugin.getConfig().getString(ConfigPath.MINECRAFT_DESCRIPTION.getPath())))
            return plugin.getConfig().getString(ConfigPath.MINECRAFT_DESCRIPTION.getPath());
        return plugin.getConfig().getString(ConfigPath.UNKNOWN_DESCRIPTION.getPath());
    }
    
    private static void addBottom(Main plugin, Inventory inv, boolean prev, boolean next)
    {
        ItemStack prevMenu = getItemStack(plugin, Representation.PREVIOUS_MENU),
                  prevPage = getItemStack(plugin, Representation.PREVIOUS_PAGE),
                  nextPage = getItemStack(plugin, Representation.NEXT_PAGE),
                  filler = getItemStack(plugin, Representation.FILLER_MENU);
        ItemMeta prevMenuMeta = prevMenu.getItemMeta(),
                 prevPageMeta = prevPage.getItemMeta(),
                 nextPageMeta = nextPage.getItemMeta(),
                 fillerMeta = filler.getItemMeta();
        int helpMenuRows = plugin.getConfig().getInt(ConfigPath.HELP_MENU_ROWS.getPath()),
            currSlot = (helpMenuRows-1)*CHEST_ROW_SIZE;
        
        Util.setName(plugin, prevMenuMeta, Representation.PREVIOUS_MENU);
        Util.setName(plugin, prevPageMeta, Representation.PREVIOUS_PAGE);
        Util.setName(plugin, nextPageMeta, Representation.NEXT_PAGE);
        Util.setName(plugin, fillerMeta, Representation.FILLER_MENU);
        
        Util.setLore(plugin, prevMenuMeta, Representation.PREVIOUS_MENU);
        Util.setLore(plugin, prevPageMeta, Representation.PREVIOUS_PAGE);
        Util.setLore(plugin, nextPageMeta, Representation.NEXT_PAGE);
        Util.setLore(plugin, fillerMeta, Representation.FILLER_MENU);
        
        prevMenu.setItemMeta(prevMenuMeta);
        prevPage.setItemMeta(prevPageMeta);
        nextPage.setItemMeta(nextPageMeta);
        filler.setItemMeta(fillerMeta);
        
        if(prev)
            inv.setItem(currSlot++, prevPage);
        for(; currSlot < (helpMenuRows-1)*CHEST_ROW_SIZE + CHEST_ROW_SIZE/2; currSlot++)
            inv.setItem(currSlot, filler);
        inv.setItem(currSlot++, prevMenu);
        for(; currSlot < helpMenuRows*CHEST_ROW_SIZE-1; currSlot++)
            inv.setItem(currSlot, filler);
        if(next)
            inv.setItem(currSlot, nextPage);
        else
            inv.setItem(currSlot, filler);
    }
    
    private static void addFiller(Main plugin, Inventory inv)
    {
        ItemStack filler = getItemStack(plugin, Representation.FILLER);
        ItemMeta fillerMeta = filler.getItemMeta();
        
        Util.setName(plugin, fillerMeta, Representation.FILLER);
        Util.setLore(plugin, fillerMeta, Representation.FILLER);
        filler.setItemMeta(fillerMeta);
        
        while(inv.firstEmpty() > -1)
            inv.setItem(inv.firstEmpty(), filler);
    }
}
